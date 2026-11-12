package com.ejemplo.act4_api_jwt.repository;

import com.ejemplo.act4_api_jwt.entity.Figura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiguraRepository extends JpaRepository<Figura, Long> {
}
