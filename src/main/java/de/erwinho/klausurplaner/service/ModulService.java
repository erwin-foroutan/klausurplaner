package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.repository.ModulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ModulService {
    private final ModulRepository modulRepository;

    public List<Modul> alleModule() {
        return modulRepository.findAll();
    }

    public Modul erstellen(Modul modul) {
        return modulRepository.save(modul);
    }
}
