package de.erwinho.klausurplaner.controller;
import de.erwinho.klausurplaner.dto.SemesterUebersicht;
import de.erwinho.klausurplaner.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semester")
@RequiredArgsConstructor

public class SemesterController {
    private final SemesterService semesterService;
    @GetMapping("/aktuell")
    public SemesterUebersicht aktuell() {
        return semesterService.aktuellesSemester();
    }
}
