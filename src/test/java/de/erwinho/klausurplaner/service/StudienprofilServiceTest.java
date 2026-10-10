package de.erwinho.klausurplaner.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudienprofilServiceTest {

    @Test
    void oktoberImStartjahrIstErstesSemester() {
        assertEquals(1, StudienprofilService.berechneSemester(2025, LocalDate.of(2025, 10, 1)));
    }

    @Test
    void letzterMaerzIstNochErstesSemester() {
        assertEquals(1, StudienprofilService.berechneSemester(2025, LocalDate.of(2026, 3, 31)));
    }

    @Test
    void ersterAprilIstZweitesSemester() {
        assertEquals(2, StudienprofilService.berechneSemester(2025, LocalDate.of(2026, 4, 1)));
    }

    @Test
    void oktoberImFolgejahrIstDrittesSemester() {
        assertEquals(3, StudienprofilService.berechneSemester(2025, LocalDate.of(2026, 10, 10)));
    }

    @Test
    void februarImDrittenSemesterIstNochDrittesSemester() {
        assertEquals(3, StudienprofilService.berechneSemester(2025, LocalDate.of(2027, 2, 15)));
    }

    @Test
    void oktober2028IstSiebtesSemester() {
        assertEquals(7, StudienprofilService.berechneSemester(2025, LocalDate.of(2028, 10, 1)));
    }
}