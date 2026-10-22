package com.goose.projetfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LivreRequest {
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne doit pas dèpasser les 100 caractères")
    private String titre;
    @NotBlank(message = "L'ISBN est obligatoire")
    @Size(max = 50, message = "L'isbn ne doit pas depasser les 50 caractères")
    private String isbn;
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "le prix doit etre positive")
    private double prix;
    @Positive(message = "La date doit etre positive")
    private Integer anneePublication;
    @NotNull(message = "Le stock est obligatoire")
    @Positive(message = "Le stock doit etre positive")
    private Integer stock;
    @NotNull(message = "L'auteur id est obligatoire")
    private  Long  auteurId;
}
