package com.ejemplo.act4_api_jwt.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String titulo;

    // Relación 1 a muchos: un anime tiene muchas figuras
    @OneToMany(mappedBy = "anime", fetch = FetchType.LAZY)
    private List<Figura> figuras = new ArrayList<>();

    public Anime() {}

    public Anime(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<Figura> getFiguras() { return figuras; }
    public void setFiguras(List<Figura> figuras) { this.figuras = figuras; }
}
