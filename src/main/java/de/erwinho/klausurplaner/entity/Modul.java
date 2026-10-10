package de.erwinho.klausurplaner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Modul {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String kuerzel;

    private int ects;

    private int semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kategorie kategorie;

    public Modul(String name, int ects, int semester, Kategorie kategorie) {
        this.name = name;
        this.ects = ects;
        this.semester = semester;
        this.kategorie = kategorie;
    }
}