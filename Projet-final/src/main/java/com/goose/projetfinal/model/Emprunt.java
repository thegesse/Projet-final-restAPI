package com.goose.projetfinal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long livre_id;

    @Column(nullable = false)
    private String emprunteur;

    @Column(nullable = false)
    private String email_emprunteur;

    @Column(nullable = false)
    LocalDateTime date_emprunt;

    @Column(nullable = false)
    LocalDateTime date_retour_prevue;

    @Column
    LocalDateTime date_retour_effective;

    @Column(nullable = false)
    private Boolean rendu;
}
