package de.erwinho.klausurplaner.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LerneinheitRequest(
        @NotNull(message = "modulId fehlt") Long modulId,
        LocalDate datum,
        @Min(value = 1, message = "dauerMinuten muss mindestens 1 sein")
        @Max(value = 1440, message = "dauerMinuten darf höchstens 1440 (24 Std.) sein") int dauerMinuten,
        @Size(max = 255, message = "thema darf höchstens 255 Zeichen lang sein") String thema,
        @Size(max = 255, message = "notiz darf höchstens 255 Zeichen lang sein") String notiz
) {
}