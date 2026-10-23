package com.goose.projetfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LivreRequest {
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    private String titre;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Size(max = 20, message = "L'ISBN ne doit pas dépasser 20 caractères")
    private String isbn;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private Double prix;

    @Positive(message = "L'année de publication doit être positive")
    private Integer anneePublication;

    @NotNull(message = "Le stock est obligatoire")
    @PositiveOrZero(message = "Le stock ne peut pas être négatif")
    private Integer stock;

    @NotNull(message = "L'identifiant de l'auteur est obligatoire")
    private Long auteurId;
}
