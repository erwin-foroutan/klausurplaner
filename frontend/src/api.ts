async function senden(url: string, methode: string, body: unknown) {
    const res = await fetch(url, {
        method: methode,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
    })
    if (!res.ok) {
        const daten = await res.json().catch(() => ({}))
        const meldung = daten.fehler || Object.values(daten).join(', ') || 'Fehler ' + res.status
        throw new Error(meldung)
    }
    return res.json()
}

export function anmelden(modulId: number, datum: string) {
    return senden('/pruefungen', 'POST', { modulId, datum })
}

export function ergebnisEintragen(anmeldungId: number, status: string, note: number | null) {
    return senden('/pruefungen/' + anmeldungId + '/ergebnis', 'PUT', { status, note })
}