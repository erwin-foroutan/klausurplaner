package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Studienprofil;
import de.erwinho.klausurplaner.repository.StudienprofilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudienprofilService {

    private final StudienprofilRepository studienprofilRepository;

    public Studienprofil speichern(int startJahr) {
        Studienprofil profil = studienprofilRepository.findAll().stream()
                .findFirst()
                .orElseGet(Studienprofil::new);
        profil.setStartJahr(startJahr);
        return studienprofilRepository.save(profil);
    }

    public Studienprofil laden() {
        return studienprofilRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Noch kein Studienbeginn gespeichert"));
    }

    public int aktuellesSemester() {
        return berechneSemester(laden().getStartJahr(), LocalDate.now());
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