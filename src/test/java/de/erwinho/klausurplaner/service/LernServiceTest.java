package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Lerneinheit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LernServiceTest {

    private List<Lerneinheit> einheiten(int... minuten) {
        return Arrays.stream(minuten).mapToObj(m -> {
            Lerneinheit e = new Lerneinheit();
            e.setDauerMinuten(m);
            return e;
        }).toList();
    }

    @Test
    void minutenWerdenSummiert() {
        assertEquals(150, LernService.summeMinuten(einheiten(30, 45, 75)));
    }

    @Test
    void ohneEinheitenSindNullMinuten() {
        assertEquals(0, LernService.summeMinuten(einheiten()));
    }

    @Test
    void halbesZielIstFuenfzigProzent() {
        assertEquals(50, LernService.fortschrittProzent(300, 10));
    }

    @Test
    void fortschrittWirdBeiHundertGekappt() {
        assertEquals(100, LernService.fortschrittProzent(900, 10));
    }

    @Test
    void nachkommastellenWerdenAbgeschnitten() {
        assertEquals(98, LernService.fortschrittProzent(59, 1));
    }

    @Test
    void deadlineVorStartIstNichtErlaubt() {
        assertThrows(IllegalArgumentException.class, () -> LernService.pruefeLernziel(
                10, LocalDate.of(2027, 2, 1), LocalDate.of(2027, 1, 1)));
    }

    @Test
    void nullZielstundenSindNichtErlaubt() {
        assertThrows(IllegalArgumentException.class, () -> LernService.pruefeLernziel(
                0, LocalDate.of(2027, 1, 1), LocalDate.of(2027, 2, 1)));
    }

    @Test
    void startUndDeadlineAmSelbenTagSindErlaubt() {
        assertDoesNotThrow(() -> LernService.pruefeLernziel(
                2, LocalDate.of(2027, 1, 1), LocalDate.of(2027, 1, 1)));
    }
}