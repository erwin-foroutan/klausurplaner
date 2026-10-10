package de.erwinho.klausurplaner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pruefungsanmeldung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Modul modul;

    private LocalDate datum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PruefungsStatus status;

    private Double note;
}