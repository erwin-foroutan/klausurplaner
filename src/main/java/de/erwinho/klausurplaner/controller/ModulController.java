package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.service.ModulService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
public class ModulController {
    private final ModulService modulService;

    @GetMapping
    public List<Modul> alleModule() {
        return modulService.alleModule();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Modul erstellen(@RequestBody Modul modul) {
        return modulService.erstellen(modul);
    }
}
