package com.goose.projetfinal.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "titre", "isbn", "prix", "anneePublication", "stock", "auteurId"})
public class LivreResponse {
    private final Long id;
    private final String titre;
    private final String isbn;
    private final Double prix;
    private final Integer anneePublication;
    private final Integer stock;
    private final Long  auteurId;
}
