export type ModulStatus = 'OFFEN' | 'ANGEMELDET' | 'BESTANDEN' | 'ENDGUELTIG_NICHT_BESTANDEN'

export interface ModulUebersicht {
    modulId: number
    name: string
    semester: number
    ects: number
    kategorie: string
    status:ModulStatus
    fehlversuche:number
    letzterVersuch:boolean
    note:number | null
}

export interface SemesterUebersicht {
    aktuellesSemester: number
    erreichteEcts: number
    module: ModulUebersicht[]
}