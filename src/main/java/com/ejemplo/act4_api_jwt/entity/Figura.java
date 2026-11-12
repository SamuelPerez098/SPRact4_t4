package com.ejemplo.act4_api_jwt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "figuras")
public class Figura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String personaje;

    private Double precio;

    // Relación muchos a 1: muchas figuras pertenecen a un anime (llave foránea anime_id)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    public Figura() {}

    public Figura(String personaje, Double precio, Anime anime) {
        this.personaje = personaje;
        this.precio = precio;
        this.anime = anime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPersonaje() { return personaje; }
    public void setPersonaje(String personaje) { this.personaje = personaje; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Anime getAnime() { return anime; }
    public void setAnime(Anime anime) { this.anime = anime; }
}
