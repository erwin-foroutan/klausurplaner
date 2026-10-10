package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.LerneinheitRequest;
import de.erwinho.klausurplaner.dto.LerneinheitResponse;
import de.erwinho.klausurplaner.dto.LernzielRequest;
import de.erwinho.klausurplaner.dto.LernzielResponse;
import de.erwinho.klausurplaner.service.LernService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LernController {

    private final LernService lernService;

    @PostMapping("/lerneinheiten")
    @ResponseStatus(HttpStatus.CREATED)
    public LerneinheitResponse lerneinheitErfassen(@Valid @RequestBody LerneinheitRequest r) {
        return LerneinheitResponse.von(lernService.lerneinheitErfassen(
                r.modulId(), r.datum(), r.dauerMinuten(), r.thema(), r.notiz()));
    }

    @GetMapping("/module/{modulId}/lerneinheiten")
    public List<LerneinheitResponse> lerneinheiten(@PathVariable Long modulId) {
        return lernService.lerneinheiten(modulId).stream()
                .map(LerneinheitResponse::von)
                .toList();
    }

    @PostMapping("/lernziele")
    @ResponseStatus(HttpStatus.CREATED)
    public LernzielResponse lernzielAnlegen(@Valid @RequestBody LernzielRequest r) {
        return lernService.alsResponse(lernService.lernzielAnlegen(
                r.modulId(), r.zielStunden(), r.startDatum(), r.deadline(), r.beschreibung()));
    }

    @GetMapping("/module/{modulId}/lernziele")
    public List<LernzielResponse> lernziele(@PathVariable Long modulId) {
        return lernService.lernziele(modulId).stream()
                .map(lernService::alsResponse)
                .toList();
    }
}