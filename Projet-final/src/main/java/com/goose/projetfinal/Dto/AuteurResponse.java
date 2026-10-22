package com.goose.projetfinal.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "nom", "prenom", "nationalite"})
public class AuteurResponse {
    private final Long id;
    private final String nom;
    private final String prenom;
    private final String nationalite;
}
