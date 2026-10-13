import { useCallback, useEffect, useState } from 'react'
import type { ModulStatus, ModulUebersicht } from './types'
import RueckgaengigButton from './RueckgaengigButton'
import { authFetch } from './auth'

const statusFarbe: Record<ModulStatus, string> = {
    OFFEN: 'bg-white border-gray-200',
    ANGEMELDET: 'bg-blue-50 border-blue-300',
    BESTANDEN: 'bg-green-50 border-green-400',
    ENDGUELTIG_NICHT_BESTANDEN: 'bg-red-50 border-red-400',
}

const semester = [1, 2, 3, 4, 5, 6, 7]

export default function Verlauf() {
    const [alle, setAlle] = useState<ModulUebersicht[] | null>(null)
    const [fehler, setFehler] = useState<string | null>(null)

    const laden = useCallback(() => {
        authFetch('/semester/verlauf')
            .then((res) => {
                if (!res.ok) throw new Error('Fehler ' + res.status)
                return res.json()
            })
            .then(setAlle)
            .catch((e) => setFehler(e.message))
    }, [])

    useEffect(() => {
        laden()
    }, [laden])

    if (fehler) return <p className="text-red-600">{fehler}</p>
    if (!alle) return <p>Lädt …</p>

    return (
        <>
            <h1 className="text-2xl font-bold">Studienverlauf</h1>
            <div className="mt-2 flex flex-wrap gap-2 text-xs text-gray-600">
                <span className="rounded border border-green-400 bg-green-50 px-2 py-0.5">Bestanden</span>
                <span className="rounded border border-blue-300 bg-blue-50 px-2 py-0.5">Angemeldet</span>
                <span className="rounded border border-gray-200 bg-white px-2 py-0.5">Offen</span>
            </div>

            <div className="mt-6 overflow-x-auto pb-2">
                <div className="grid min-w-[900px] grid-cols-7 gap-2">
                    {semester.map((s) => {
                        const liste = alle.filter((m) => m.semester === s)
                        const gesamt = liste.reduce((summe, m) => summe + m.ects, 0)
                        const erreicht = liste
                            .filter((m) => m.status === 'BESTANDEN')
                            .reduce((summe, m) => summe + m.ects, 0)

                        return (
                            <div key={s} className="min-w-0">
                                <div className="mb-2 rounded-lg bg-white px-2.5 py-2 shadow-sm">
                                    <h2 className="text-sm font-semibold">{s}. Semester</h2>
                                    <div className="mt-1.5 h-1.5 rounded-full bg-gray-200">
                                        <div
                                            className="h-1.5 rounded-full bg-green-500"
                                            style={{ width: (erreicht / gesamt) * 100 + '%' }}
                                        />
                                    </div>
                                    <p className="mt-1 text-xs text-gray-500">
                                        {erreicht} / {gesamt} CP
                                    </p>
                                </div>

                                <div className="grid gap-2" style={{ gridTemplateRows: 'repeat(6, 4.5rem)' }}>
                                    {liste.map((m) => (
                                        <div
                                            key={m.modulId}
                                            className={
                                                'flex min-w-0 flex-col justify-between overflow-hidden rounded-lg border p-2 ' +
                                                statusFarbe[m.status]
                                            }
                                            style={{ gridRow: 'span ' + m.ects / 5 }}
                                            title={m.name}
                                        >
                                            <p className="line-clamp-2 break-words hyphens-auto text-xs leading-tight font-medium">
                                                {m.name}
                                            </p>
                                            <div className="flex items-center justify-between text-[11px] text-gray-500">
                        <span>
                          {m.ects} CP
                            {m.note !== null && ' · ' + m.note.toFixed(1).replace('.', ',')}
                        </span>
                                                <span className="flex items-center gap-1.5">
                          {m.status === 'BESTANDEN' && (
                              <span className="font-bold text-green-600">✓</span>
                          )}
                                                    {(m.status !== 'OFFEN' || m.fehlversuche > 0) && (
                                                        <RueckgaengigButton modulId={m.modulId} onFertig={laden} kompakt />
                                                    )}
                        </span>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )
                    })}
                </div>
            </div>
        </>
    )
}