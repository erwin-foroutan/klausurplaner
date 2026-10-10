package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Lerneinheit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LerneinheitRepository extends JpaRepository<Lerneinheit, Long> {

    List<Lerneinheit> findByModulIdOrderByDatumDesc(Long modulId);

    List<Lerneinheit> findByModulIdAndDatumBetween(Long modulId, LocalDate von, LocalDate bis);
}