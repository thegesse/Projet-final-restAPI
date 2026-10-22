package com.goose.projetfinal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "livres")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private double prix;

    @Column
    private int annee_publication;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private Long auteur_id;
}
