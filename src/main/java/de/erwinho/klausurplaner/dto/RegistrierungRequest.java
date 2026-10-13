package de.erwinho.klausurplaner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrierungRequest(
        @NotBlank(message = "E-Mail fehlt")
        @Email(message = "Keine gültige E-Mail-Adresse") String email,
        @NotBlank(message = "Passwort fehlt")
        @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein") String passwort
) {
}