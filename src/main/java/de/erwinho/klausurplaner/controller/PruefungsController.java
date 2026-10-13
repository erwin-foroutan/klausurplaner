package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.AnmeldungRequest;
import de.erwinho.klausurplaner.dto.AnmeldungResponse;
import de.erwinho.klausurplaner.dto.ErgebnisRequest;
import de.erwinho.klausurplaner.dto.NachtragRequest;
import de.erwinho.klausurplaner.service.PruefungsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pruefungen")
@RequiredArgsConstructor
public class PruefungsController {

    private final PruefungsService pruefungsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnmeldungResponse anmelden(@AuthenticationPrincipal Jwt jwt,
                                      @Valid @RequestBody AnmeldungRequest request) {
        return AnmeldungResponse.von(
                pruefungsService.anmelden(BenutzerId.aus(jwt), request.modulId(), request.datum()));
    }

    @PutMapping("/{id}/ergebnis")
    public AnmeldungResponse ergebnisEintragen(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long id,
                                               @Valid @RequestBody ErgebnisRequest request) {
        return AnmeldungResponse.von(
                pruefungsService.ergebnisEintragen(BenutzerId.aus(jwt), id, request.status(), request.note()));
    }

    @PostMapping("/nachtragen")
    @ResponseStatus(HttpStatus.CREATED)
    public AnmeldungResponse nachtragen(@AuthenticationPrincipal Jwt jwt,
                                        @Valid @RequestBody NachtragRequest request) {
        return AnmeldungResponse.von(pruefungsService.nachtragen(
                BenutzerId.aus(jwt), request.modulId(), request.datum(), request.status(), request.note()));
    }

    @DeleteMapping("/modul/{modulId}/letzte")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void letzteRueckgaengig(@AuthenticationPrincipal Jwt jwt, @PathVariable Long modulId) {
        pruefungsService.letzteRueckgaengig(BenutzerId.aus(jwt), modulId);
    }
}