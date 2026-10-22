package com.goose.projetfinal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "emprunts")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long livreId;

    @Column(nullable = false)
    private String emprunteur;

    @Column(nullable = false)
    private String emailEmprunteur;

    @Column(nullable = false)
    LocalDate dateEmprunt;

    @Column(nullable = false)
    LocalDate dateRetourPrevue;

    LocalDate dateRetourEffective;

    @Column(nullable = false)
    private Boolean rendu;
}
