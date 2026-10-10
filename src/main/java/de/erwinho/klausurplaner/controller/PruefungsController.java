package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.AnmeldungRequest;
import de.erwinho.klausurplaner.dto.AnmeldungResponse;
import de.erwinho.klausurplaner.dto.ErgebnisRequest;
import de.erwinho.klausurplaner.service.PruefungsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pruefungen")
@RequiredArgsConstructor
public class PruefungsController {

    private final PruefungsService pruefungsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnmeldungResponse anmelden(@RequestBody AnmeldungRequest request) {
        return AnmeldungResponse.von(
                pruefungsService.anmelden(request.modulId(), request.datum()));
    }

    @PutMapping("/{id}/ergebnis")
    public AnmeldungResponse ergebnisEintragen(@PathVariable Long id,
                                               @RequestBody ErgebnisRequest request) {
        return AnmeldungResponse.von(
                pruefungsService.ergebnisEintragen(id, request.status(), request.note()));
    }
}