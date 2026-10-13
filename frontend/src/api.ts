async function senden(url: string, methode: string, body?: unknown) {
    const res = await fetch(url, {
        method: methode,
        headers: { 'Content-Type': 'application/json' },
        body: body === undefined ? undefined : JSON.stringify(body),
    })
    if (!res.ok) {
        const daten = await res.json().catch(() => ({}))
        const meldung = daten.fehler || Object.values(daten).join(', ') || 'Fehler ' + res.status
        throw new Error(meldung)
    }
    if (res.status === 204) return null
    return res.json()
}

export function anmelden(modulId: number, datum: string) {
    return senden('/pruefungen', 'POST', { modulId, datum })
}

export function ergebnisEintragen(anmeldungId: number, status: string, note: number | null) {
    return senden('/pruefungen/' + anmeldungId + '/ergebnis', 'PUT', { status, note })
}

export function rueckgaengig(modulId: number) {
    return senden('/pruefungen/modul/' + modulId + '/letzte', 'DELETE')
}

export function setzeStudienbeginn(startJahr: number) {
    return senden('/studienprofil', 'PUT', { startJahr })
}

function semesterBeginn() {
    const heute = new Date()
    const jahr = heute.getFullYear()
    const monat = heute.getMonth() + 1
    if (monat >= 10) return jahr + '-10-01'
    if (monat >= 4) return jahr + '-04-01'
    return jahr - 1 + '-10-01'
}

export function lerneinheitErfassen(modulId: number, dauerMinuten: number, thema: string) {
    return senden('/lerneinheiten', 'POST', { modulId, dauerMinuten, thema: thema || null })
}

export function lernzielAnlegen(modulId: number, zielStunden: number, deadline: string) {
    return senden('/lernziele', 'POST', {
        modulId,
        zielStunden,
        startDatum: semesterBeginn(),
        deadline,
    })
}