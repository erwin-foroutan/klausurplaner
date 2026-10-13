package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Studienprofil;
import de.erwinho.klausurplaner.repository.BenutzerRepository;
import de.erwinho.klausurplaner.repository.StudienprofilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudienprofilService {

    private final StudienprofilRepository studienprofilRepository;
    private final BenutzerRepository benutzerRepository;

    public Studienprofil speichern(Long benutzerId, int startJahr) {
        LocalDate heute = LocalDate.now();
        int spaetestesJahr = heute.getMonthValue() >= 10 ? heute.getYear() : heute.getYear() - 1;
        if (startJahr > spaetestesJahr) {
            throw new IllegalArgumentException("Das Wintersemester " + startJahr + " hat noch nicht begonnen");
        }
        Studienprofil profil = studienprofilRepository.findByBenutzerId(benutzerId)
                .orElseGet(() -> {
                    Studienprofil neu = new Studienprofil();
                    neu.setBenutzer(benutzerRepository.getReferenceById(benutzerId));
                    return neu;
                });
        profil.setStartJahr(startJahr);
        return studienprofilRepository.save(profil);
    }

    public Studienprofil laden(Long benutzerId) {
        return studienprofilRepository.findByBenutzerId(benutzerId)
                .orElseThrow(() -> new IllegalStateException("Noch kein Studienbeginn gespeichert"));
    }

    public int aktuellesSemester(Long benutzerId) {
        return berechneSemester(laden(benutzerId).getStartJahr(), LocalDate.now());
    }

    static int berechneSemester(int startJahr, LocalDate datum) {
        int jahr = datum.getYear();
        int monat = datum.getMonthValue();

        if (monat >= 10) {
            return (jahr - startJahr) * 2 + 1;
        } else if (monat >= 4) {
            return (jahr - startJahr) * 2;
        } else {
            return (jahr - 1 - startJahr) * 2 + 1;
        }
    }
}