package com.goose.projetfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmpruntResponse {
    @NotNull(message = "L'id du livre est obligatoire")
    @Positive(message = "l'ID du l'ivre doit etre positive")
    private Long livreId;
    @NotBlank(message = "L'emprunteur est obligatoire")
    @Size(max = 100, message = "L'empreteur ne doit pas depasser les 100 caracteres")
    private String emprunteur;
    @NotBlank(message = "L'email de l'emprunteur doit etre present")
    @Size(max = 100, message = "L'email de l'emprunteur ne doit pas depasser les 100 caracteres")
    private String emailEmprunteur;
}
