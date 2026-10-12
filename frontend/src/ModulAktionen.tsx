import { useState, type FormEvent } from 'react'
import type { ModulUebersicht } from './types'
import { anmelden, ergebnisEintragen } from './api'

type Props = { modul: ModulUebersicht; onFertig: () => void }

export default function ModulAktionen({ modul, onFertig }: Props) {
    const [offen, setOffen] = useState(false)
    const [datum, setDatum] = useState('')
    const [ergebnis, setErgebnis] = useState('BESTANDEN')
    const [note, setNote] = useState('')
    const [fehler, setFehler] = useState<string | null>(null)
    const [laedt, setLaedt] = useState(false)

    if (modul.status === 'BESTANDEN' || modul.status === 'ENDGUELTIG_NICHT_BESTANDEN') {
        return null
    }

    const istAngemeldet = modul.status === 'ANGEMELDET'

    async function absenden(e: FormEvent) {
        e.preventDefault()
        setFehler(null)
        setLaedt(true)
        try {
            if (istAngemeldet && modul.offeneAnmeldungId !== null) {
                const wert = ergebnis === 'BESTANDEN' ? Number(note.replace(',', '.')) : null
                await ergebnisEintragen(modul.offeneAnmeldungId, ergebnis, wert)
            } else {
                await anmelden(modul.modulId, datum)
            }
            setOffen(false)
            setNote('')
            onFertig()
        } catch (err) {
            setFehler((err as Error).message)
        } finally {
            setLaedt(false)
        }
    }

    if (!offen) {
        return (
            <button
                onClick={() => setOffen(true)}
                className="mt-3 text-sm font-medium text-blue-600 hover:underline"
            >
                {istAngemeldet ? 'Ergebnis eintragen' : 'Zur Prüfung anmelden'}
            </button>
        )
    }

    return (
        <form onSubmit={absenden} className="mt-3 space-y-2 rounded-md bg-gray-50 p-3">
            {istAngemeldet ? (
                <>
                    <select
                        value={ergebnis}
                        onChange={(e) => setErgebnis(e.target.value)}
                        className="w-full rounded border bg-white px-2 py-1 text-sm"
                    >
                        <option value="BESTANDEN">Bestanden</option>
                        <option value="NICHT_BESTANDEN">Nicht bestanden</option>
                        <option value="NICHT_ANGETRETEN">Nicht angetreten</option>
                    </select>
                    {ergebnis === 'BESTANDEN' && (
                        <input
                            value={note}
                            onChange={(e) => setNote(e.target.value)}
                            placeholder="Note, z. B. 2,3"
                            required
                            className="w-full rounded border bg-white px-2 py-1 text-sm"
                        />
                    )}
                </>
            ) : (
                <input
                    type="date"
                    value={datum}
                    onChange={(e) => setDatum(e.target.value)}
                    required
                    className="w-full rounded border bg-white px-2 py-1 text-sm"
                />
            )}

            {fehler && <p className="text-sm text-red-600">{fehler}</p>}

            <div className="flex gap-2">
                <button
                    type="submit"
                    disabled={laedt}
                    className="rounded bg-blue-600 px-3 py-1 text-sm text-white hover:bg-blue-700 disabled:opacity-50"
                >
                    Speichern
                </button>
                <button
                    type="button"
                    onClick={() => {
                        setOffen(false)
                        setFehler(null)
                    }}
                    className="rounded px-3 py-1 text-sm text-gray-600 hover:bg-gray-200"
                >
                    Abbrechen
                </button>
            </div>
        </form>
    )
}