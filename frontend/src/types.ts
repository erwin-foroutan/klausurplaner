export type ModulStatus = 'OFFEN' | 'ANGEMELDET' | 'BESTANDEN' | 'ENDGUELTIG_NICHT_BESTANDEN'

export interface ModulUebersicht {
    modulId: number
    name: string
    semester: number
    ects: number
    kategorie: string
    status: ModulStatus
    fehlversuche: number
    letzterVersuch: boolean
    note: number | null
    offeneAnmeldungId: number | null
}

export interface SemesterUebersicht {
    aktuellesSemester: number
    erreichteEcts: number
    module: ModulUebersicht[]
}

export interface Lernziel {
    id: number
    modulId: number
    modulName: string
    zielStunden: number
    startDatum: string
    deadline: string
    beschreibung: string | null
    gelernteMinuten: number
    fortschrittProzent: number
    verbleibendeTage: number
}

export interface Lerneinheit {
    id: number
    modulId: number
    modulName: string
    datum: string
    dauerMinuten: number
    thema: string | null
    notiz: string | null
}