package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PruefungsanmeldungRepository extends JpaRepository<Pruefungsanmeldung, Long> {

    List<Pruefungsanmeldung> findByModulId(Long modulId);

    @Query("SELECT a FROM Pruefungsanmeldung a JOIN FETCH a.modul")
    List<Pruefungsanmeldung> findAllMitModul();
}