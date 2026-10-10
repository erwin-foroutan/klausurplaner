package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.ModulStatus;
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
    // ---------- Ergebnis eintragen ----------

    private Pruefungsanmeldung offeneAnmeldung() {
        Pruefungsanmeldung a = new Pruefungsanmeldung();
        a.setStatus(ANGEMELDET);
        return a;
    }

    @Test
    void bestandenMitGueltigerNoteWirdGespeichert() {
        var a = offeneAnmeldung();
        PruefungsService.setzeErgebnis(a, BESTANDEN, 2.3);
        assertEquals(BESTANDEN, a.getStatus());
        assertEquals(2.3, a.getNote());
    }

    @Test
    void bestandenOhneNoteIstNichtErlaubt() {
        assertThrows(IllegalArgumentException.class,
                () -> PruefungsService.setzeErgebnis(offeneAnmeldung(), BESTANDEN, null));
    }

    @Test
    void noteSchlechterAlsVierBeiBestandenIstNichtErlaubt() {
        assertThrows(IllegalArgumentException.class,
                () -> PruefungsService.setzeErgebnis(offeneAnmeldung(), BESTANDEN, 4.3));
    }

    @Test
    void nichtBestandenBekommtAutomatischFuenf() {
        var a = offeneAnmeldung();
        PruefungsService.setzeErgebnis(a, NICHT_BESTANDEN, null);
        assertEquals(5.0, a.getNote());
    }

    @Test
    void nichtAngetretenHatKeineNote() {
        var a = offeneAnmeldung();
        PruefungsService.setzeErgebnis(a, NICHT_ANGETRETEN, 1.0);
        assertNull(a.getNote());
    }

    @Test
    void ergebnisKannNurEinmalEingetragenWerden() {
        var a = offeneAnmeldung();
        PruefungsService.setzeErgebnis(a, NICHT_BESTANDEN, null);
        assertThrows(IllegalStateException.class,
                () -> PruefungsService.setzeErgebnis(a, BESTANDEN, 1.7));
    }
    // ---------- Modulstatus ----------

    @Test
    void ohneAnmeldungIstModulOffen() {
        assertEquals(ModulStatus.OFFEN, PruefungsService.bestimmeStatus(anmeldungen()));
    }

    @Test
    void nachNichtAntretenIstModulWiederOffen() {
        assertEquals(ModulStatus.OFFEN, PruefungsService.bestimmeStatus(anmeldungen(NICHT_ANGETRETEN)));
    }

    @Test
    void offeneAnmeldungErgibtAngemeldet() {
        assertEquals(ModulStatus.ANGEMELDET,
                PruefungsService.bestimmeStatus(anmeldungen(NICHT_BESTANDEN, ANGEMELDET)));
    }

    @Test
    void bestandenNachFehlversuchenErgibtBestanden() {
        assertEquals(ModulStatus.BESTANDEN,
                PruefungsService.bestimmeStatus(anmeldungen(NICHT_BESTANDEN, NICHT_BESTANDEN, BESTANDEN)));
    }

    @Test
    void dreiFehlversucheErgebenEndgueltigNichtBestanden() {
        assertEquals(ModulStatus.ENDGUELTIG_NICHT_BESTANDEN,
                PruefungsService.bestimmeStatus(anmeldungen(NICHT_BESTANDEN, NICHT_BESTANDEN, NICHT_BESTANDEN)));
    }
}