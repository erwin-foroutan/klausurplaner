package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PruefungsanmeldungRepository extends JpaRepository<Pruefungsanmeldung, Long> {

    List<Pruefungsanmeldung> findByModulIdAndBenutzerId(Long modulId, Long benutzerId);

    Optional<Pruefungsanmeldung> findByIdAndBenutzerId(Long id, Long benutzerId);

    @Query("SELECT a FROM Pruefungsanmeldung a JOIN FETCH a.modul WHERE a.benutzer.id = :benutzerId")
    List<Pruefungsanmeldung> findAllMitModul(@Param("benutzerId") Long benutzerId);
}