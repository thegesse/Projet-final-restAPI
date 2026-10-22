package com.goose.projetfinal.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuteurRequest {
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 100, message = "Le nom ne doit pas dèpasser les 100 caractères.")
    private String nom;
    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères.")
    private String prenom;
    @Size(max = 50, message = "La nationalité ne doit pas dépasser 50 caractères")
    private String nationalite;
}
