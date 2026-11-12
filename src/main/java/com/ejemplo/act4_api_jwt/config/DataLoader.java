package com.ejemplo.act4_api_jwt.config;

import com.ejemplo.act4_api_jwt.entity.Anime;
import com.ejemplo.act4_api_jwt.entity.Figura;
import com.ejemplo.act4_api_jwt.repository.AnimeRepository;
import com.ejemplo.act4_api_jwt.repository.FiguraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Carga datos de ejemplo solo si la base está vacía (no duplica nada al reiniciar). */
@Component
public class DataLoader implements CommandLineRunner {

    private final AnimeRepository animeRepository;
    private final FiguraRepository figuraRepository;

    public DataLoader(AnimeRepository animeRepository, FiguraRepository figuraRepository) {
        this.animeRepository = animeRepository;
        this.figuraRepository = figuraRepository;
    }

    @Override
    public void run(String... args) {
        if (animeRepository.count() > 0) {
            return;
        }
        Anime dbz = animeRepository.save(new Anime("Dragon Ball Z"));
        Anime naruto = animeRepository.save(new Anime("Naruto"));
        Anime onePiece = animeRepository.save(new Anime("One Piece"));

        figuraRepository.saveAll(List.of(
                new Figura("Goku Super Saiyan", 1500.50, dbz),
                new Figura("Vegeta", 1300.00, dbz),
                new Figura("Gohan", 1100.00, dbz),
                new Figura("Piccolo", 950.00, dbz),
                new Figura("Naruto Modo Sabio", 1850.00, naruto),
                new Figura("Sasuke Uchiha", 1700.00, naruto),
                new Figura("Kakashi Hatake", 1600.00, naruto),
                new Figura("Itachi Uchiha", 2100.00, naruto),
                new Figura("Monkey D. Luffy", 1200.00, onePiece),
                new Figura("Roronoa Zoro", 1400.00, onePiece),
                new Figura("Nami", 980.00, onePiece),
                new Figura("Sanji", 1250.00, onePiece)));
    }
}
