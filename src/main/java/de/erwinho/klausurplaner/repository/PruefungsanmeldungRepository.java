package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PruefungsanmeldungRepository extends JpaRepository<Pruefungsanmeldung, Long> {

    List<Pruefungsanmeldung> findByModulId(Long modulId);
}