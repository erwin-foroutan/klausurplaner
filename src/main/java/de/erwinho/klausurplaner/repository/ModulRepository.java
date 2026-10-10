package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Modul;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModulRepository extends JpaRepository<Modul, Long> {
}
