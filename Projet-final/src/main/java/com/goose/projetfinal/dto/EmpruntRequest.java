package com.goose.projetfinal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class EmpruntRequest {
    @NotNull(message = "L'identifiant du livre est obligatoire")
    private Long livreId;

    @NotBlank(message = "Le nom de l'emprunteur est obligatoire")
    @Size(max = 100, message = "Le nom de l'emprunteur ne doit pas dépasser 100 caractères")
    private String emprunteur;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String emailEmprunteur;
}
