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
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private Double prix;

    @Column
    private Integer anneePublication;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Long auteurId;
}
