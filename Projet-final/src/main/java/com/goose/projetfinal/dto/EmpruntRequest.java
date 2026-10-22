package com.goose.projetfinal.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class EmpruntRequest {
    @NotNull(message = "L'id du livre est obligatoire")
    private Long livreId;
    @NotBlank(message = "L'emprunteur est obligatoire")
    @Size(max = 100, message = "L'empreteur ne doit pas depasser les 100 caracteres")
    private String emprunteur;
    @NotBlank(message = "L'email de l'emprunteur doit etre present")
    @Size(max = 100, message = "L'email de l'emprunteur ne doit pas depasser les 100 caracteres")
    @Email
    private String emailEmprunteur;
}
