package com.goose.projetfinal.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "livreId", "emprunteur", "dateEmprunteur", "dateRetourPrevue", "dateRetourEffective", "rendu", "enRetard"})
public class EmpruntRequest {
    private final Long id;
    private final Long livreId;
    private final String emprunteur;
    private final LocalDate dateEmprunteur;
    private final LocalDate dateRetourPrevue;
    private final LocalDate dateRetourEffective;
    private final Boolean rendu;
    private final Boolean enRetard;
}
