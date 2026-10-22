package com.goose.projetfinal.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "livreId", "emprunteur", "dateEmprunt", "dateRetourPrevue", "dateRetourEffective", "rendu", "enRetard"})
public class EmpruntResponse {
    private final Long id;
    private final Long livreId;
    private final String emprunteur;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private final LocalDate dateRetourEffective;
    private final Boolean rendu;
    private final Boolean enRetard;
}
