package de.erwinho.klausurplaner.dto;
import java.time.LocalDate;

public record LerneinheitRequest(Long modulId, LocalDate datum, int dauerMinuten, String thema, String notiz) {
}
