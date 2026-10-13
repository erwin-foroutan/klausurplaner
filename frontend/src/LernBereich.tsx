import { useCallback, useEffect, useState, type FormEvent } from 'react'
import type { Lerneinheit, Lernziel } from './types'
import { lerneinheitErfassen, lernzielAnlegen } from './api'

function stunden(minuten: number) {
    return (minuten / 60).toFixed(1).replace('.', ',')
}

type Modus = 'zu' | 'lernzeit' | 'ziel'

export default function LernBereich({ modulId }: { modulId: number }) {
    const [ziel, setZiel] = useState<Lernziel | null>(null)
    const [gesamtMinuten, setGesamtMinuten] = useState(0)
    const [modus, setModus] = useState<Modus>('zu')
    const [minuten, setMinuten] = useState('')
    const [thema, setThema] = useState('')
    const [zielStunden, setZielStunden] = useState('')
    const [deadline, setDeadline] = useState('')
    const [fehler, setFehler] = useState<string | null>(null)

    const laden = useCallback(() => {
        Promise.all([
            fetch('/module/' + modulId + '/lernziele').then((r) => r.json()),
            fetch('/module/' + modulId + '/lerneinheiten').then((r) => r.json()),
        ]).then(([ziele, einheiten]: [Lernziel[], Lerneinheit[]]) => {
            setZiel(ziele.length > 0 ? ziele[ziele.length - 1] : null)
            setGesamtMinuten(einheiten.reduce((summe, e) => summe + e.dauerMinuten, 0))
        })
    }, [modulId])

    useEffect(() => {
        laden()
    }, [laden])

    function schliessen() {
        setModus('zu')
        setFehler(null)
        setMinuten('')
        setThema('')
        setZielStunden('')
        setDeadline('')
    }

    async function absenden(e: FormEvent) {
        e.preventDefault()
        setFehler(null)
        try {
            if (modus === 'lernzeit') {
                await lerneinheitErfassen(modulId, Number(minuten), thema)
            } else {
                await lernzielAnlegen(modulId, Number(zielStunden), deadline)
            }
            schliessen()
            laden()
        } catch (err) {
            setFehler((err as Error).message)
        }
    }

    const feld = 'w-full rounded border bg-white px-2 py-1 text-sm'

    return (
        <div className="mt-3 border-t border-gray-100 pt-3">
            {ziel ? (
                <div>
                    <div className="flex justify-between text-xs text-gray-600">
            <span>
              Lernziel: {stunden(ziel.gelernteMinuten)} / {ziel.zielStunden} Std.
            </span>
                        <span>
              {ziel.verbleibendeTage > 0 ? 'noch ' + ziel.verbleibendeTage + ' Tage' : 'Deadline erreicht'}
            </span>
                    </div>
                    <div className="mt-1 h-2 rounded-full bg-gray-200">
                        <div
                            className="h-2 rounded-full bg-indigo-500"
                            style={{ width: ziel.fortschrittProzent + '%' }}
                        />
                    </div>
                </div>
            ) : (
                <p className="text-xs text-gray-600">Gelernt: {stunden(gesamtMinuten)} Std.</p>
            )}

            {modus === 'zu' ? (
                <div className="mt-2 flex gap-4 text-sm">
                    <button
                        onClick={() => setModus('lernzeit')}
                        className="font-medium text-indigo-600 hover:underline"
                    >
                        + Lernzeit
                    </button>
                    <button onClick={() => setModus('ziel')} className="text-gray-500 hover:underline">
                        {ziel ? 'Neues Lernziel' : 'Lernziel setzen'}
                    </button>
                </div>
            ) : (
                <form onSubmit={absenden} className="mt-2 space-y-2 rounded-md bg-gray-50 p-3">
                    {modus === 'lernzeit' ? (
                        <>
                            <input
                                type="number"
                                min="1"
                                value={minuten}
                                onChange={(e) => setMinuten(e.target.value)}
                                placeholder="Minuten, z. B. 90"
                                required
                                className={feld}
                            />
                            <input
                                value={thema}
                                onChange={(e) => setThema(e.target.value)}
                                placeholder="Thema (optional), z. B. AVL-Bäume"
                                className={feld}
                            />
                        </>
                    ) : (
                        <>
                            <input
                                type="number"
                                min="1"
                                value={zielStunden}
                                onChange={(e) => setZielStunden(e.target.value)}
                                placeholder="Ziel in Stunden, z. B. 20"
                                required
                                className={feld}
                            />
                            <label className="block text-xs text-gray-600">
                                Deadline (z. B. Klausurdatum)
                                <input
                                    type="date"
                                    value={deadline}
                                    onChange={(e) => setDeadline(e.target.value)}
                                    required
                                    className={'mt-1 ' + feld}
                                />
                            </label>
                        </>
                    )}

                    {fehler && <p className="text-sm text-red-600">{fehler}</p>}

                    <div className="flex gap-2">
                        <button
                            type="submit"
                            className="rounded bg-indigo-600 px-3 py-1 text-sm text-white hover:bg-indigo-700"
                        >
                            Speichern
                        </button>
                        <button
                            type="button"
                            onClick={schliessen}
                            className="rounded px-3 py-1 text-sm text-gray-600 hover:bg-gray-200"
                        >
                            Abbrechen
                        </button>
                    </div>
                </form>
            )}
        </div>
    )
}