package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import de.erwinho.klausurplaner.entity.PruefungsStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static de.erwinho.klausurplaner.entity.PruefungsStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class PruefungsServiceTest {

    private List<Pruefungsanmeldung> anmeldungen(PruefungsStatus... status) {
        return Arrays.stream(status).map(s -> {
            Pruefungsanmeldung a = new Pruefungsanmeldung();
            a.setStatus(s);
            return a;
        }).toList();
    }

    @Test
    void nichtAngetretenZaehltNichtAlsFehlversuch() {
        var liste = anmeldungen(NICHT_ANGETRETEN, NICHT_ANGETRETEN, NICHT_BESTANDEN);
        assertEquals(1, PruefungsService.fehlversuche(liste));
    }

    @Test
    void ohneAnmeldungenIstAnmeldungErlaubt() {
        assertDoesNotThrow(() -> PruefungsService.pruefeAnmeldungErlaubt(anmeldungen()));
    }

    @Test
    void nachZweiFehlversuchenIstLetzterVersuch() {
        var liste = anmeldungen(NICHT_BESTANDEN, NICHT_ANGETRETEN, NICHT_BESTANDEN);
        assertTrue(PruefungsService.istLetzterVersuch(liste));
        assertFalse(PruefungsService.istEndgueltigNichtBestanden(liste));
    }

    @Test
    void nachDreiFehlversuchenEndgueltigNichtBestanden() {
        var liste = anmeldungen(NICHT_BESTANDEN, NICHT_BESTANDEN, NICHT_BESTANDEN);
        assertTrue(PruefungsService.istEndgueltigNichtBestanden(liste));
        assertThrows(IllegalStateException.class,
                () -> PruefungsService.pruefeAnmeldungErlaubt(liste));
    }

    @Test
    void bestandenesModulKannNichtErneutAngemeldetWerden() {
        var liste = anmeldungen(NICHT_BESTANDEN, BESTANDEN);
        assertThrows(IllegalStateException.class,
                () -> PruefungsService.pruefeAnmeldungErlaubt(liste));
    }

    @Test
    void zweiOffeneAnmeldungenGleichzeitigSindNichtErlaubt() {
        var liste = anmeldungen(ANGEMELDET);
        assertThrows(IllegalStateException.class,
                () -> PruefungsService.pruefeAnmeldungErlaubt(liste));
    }

    @Test
    void nachNichtAntretenIstNeueAnmeldungErlaubt() {
        var liste = anmeldungen(NICHT_ANGETRETEN);
        assertDoesNotThrow(() -> PruefungsService.pruefeAnmeldungErlaubt(liste));
    }

    @Test
    void bestandenNachZweiFehlversuchenIstKeinLetzterVersuchMehr() {
        var liste = anmeldungen(NICHT_BESTANDEN, NICHT_BESTANDEN, BESTANDEN);
        assertFalse(PruefungsService.istLetzterVersuch(liste));
        assertFalse(PruefungsService.istEndgueltigNichtBestanden(liste));
    }
}