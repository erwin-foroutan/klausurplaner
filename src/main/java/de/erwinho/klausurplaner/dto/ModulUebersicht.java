package de.erwinho.klausurplaner.dto;
import de.erwinho.klausurplaner.entity.Kategorie;
import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.entity.ModulStatus;

public record ModulUebersicht(
        Long modulId,
        String name,
        int semester,
        int ects,
        Kategorie kategorie,
        ModulStatus status,
        long fehlversuche,
        boolean letzterVersuch,
        Double note
) {
}
