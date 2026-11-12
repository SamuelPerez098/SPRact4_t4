package com.ejemplo.act4_api_jwt.exception;

import com.ejemplo.act4_api_jwt.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte todas las excepciones en respuestas JSON con el mismo formato (ApiError)
 * y el código HTTP apropiado. Extiende ResponseEntityExceptionHandler para que los
 * errores estándar de Spring MVC (404, 405, 415...) también salgan con este formato.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---- 400: validación de DTOs (@Valid) ----
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        ApiError body = ApiError.of(HttpStatus.BAD_REQUEST,
                "Hay errores de validación en los datos enviados", rutaDe(request), errores);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    // ---- 400: JSON mal formado o con tipos incorrectos ----
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ApiError body = ApiError.of(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o tiene tipos de dato incorrectos",
                rutaDe(request), null);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    // ---- Resto de errores estándar de Spring MVC (404, 405, 415, parámetros inválidos...) ----
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        String detalle = status.getReasonPhrase();
        if (ex instanceof TypeMismatchException tm) {
            detalle = "Valor inválido para un parámetro: " + tm.getValue();
        } else if (ex instanceof org.springframework.web.ErrorResponse er && er.getBody().getDetail() != null) {
            detalle = er.getBody().getDetail();
        }
        ApiError cuerpo = ApiError.of(status, detalle, rutaDe(request), null);
        return new ResponseEntity<>(cuerpo, headers, statusCode);
    }

    // ---- Excepciones propias ----
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ApiError> solicitudInvalida(SolicitudInvalidaException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ApiError> conflicto(ConflictoException ex, HttpServletRequest req) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    // ---- 401: credenciales incorrectas en el login ----
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> credencialesInvalidas(AuthenticationException ex, HttpServletRequest req) {
        return construir(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", req);
    }

    // ---- 409: restricciones de la base de datos (únicos, llaves foráneas) ----
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridad(DataIntegrityViolationException ex, HttpServletRequest req) {
        return construir(HttpStatus.CONFLICT,
                "La operación viola una restricción de la base de datos (dato duplicado o referenciado)", req);
    }

    // ---- 500: cualquier error inesperado (sin exponer detalles internos) ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Error inesperado en {}", req.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req);
    }

    private ResponseEntity<ApiError> construir(HttpStatus status, String mensaje, HttpServletRequest req) {
        return ResponseEntity.status(status).body(ApiError.of(status, mensaje, req.getRequestURI(), null));
    }

    private String rutaDe(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
