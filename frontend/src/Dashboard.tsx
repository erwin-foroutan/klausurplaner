import { useCallback, useEffect, useState } from 'react'
import type { SemesterUebersicht } from './types'
import Fortschritt from './Fortschritt'
import ModulKarte from './ModulKarte'
import Studienbeginn from './Studienbeginn'
import { authFetch } from './auth'

export default function Dashboard() {
    const [daten, setDaten] = useState<SemesterUebersicht | null>(null)
    const [fehler, setFehler] = useState<string | null>(null)

    const laden = useCallback(() => {
        authFetch('/semester/aktuell')
            .then((res) => {
                if (!res.ok) throw new Error('Fehler ' + res.status)
                return res.json()
            })
            .then(setDaten)
            .catch((e) => setFehler(e.message))
    }, [])

    useEffect(() => {
        laden()
    }, [laden])

    function neuLaden() {
        setFehler(null)
        laden()
    }

    if (fehler) {
        return (
            <div className="rounded-xl bg-white p-6 shadow-sm">
                <h1 className="text-2xl font-bold">Willkommen</h1>
                <p className="mt-1 text-gray-600">Wähle deinen Studienbeginn, um loszulegen.</p>
                <div className="mt-4">
                    <Studienbeginn onFertig={neuLaden} />
                </div>
            </div>
        )
    }
    if (!daten) return <p>Lädt …</p>

    const aktuelle = daten.module.filter((m) => m.semester === daten.aktuellesSemester)
    const nachholen = daten.module.filter((m) => m.semester < daten.aktuellesSemester)

    return (
        <>
            <header className="rounded-xl bg-white p-6 shadow-sm">
                <div className="flex flex-wrap items-start justify-between gap-3">
                    <div>
                        <h1 className="text-2xl font-bold">Mein Studium</h1>
                        <p className="mt-1 text-gray-600">
                            Angewandte Informatik · {daten.aktuellesSemester}. Semester
                        </p>
                    </div>
                    <Studienbeginn onFertig={neuLaden} />
                </div>
                <Fortschritt ects={daten.erreichteEcts} />
            </header>

            <section className="mt-8">
                <h2 className="text-xl font-semibold">{daten.aktuellesSemester}. Semester</h2>
                <div className="mt-3 grid gap-3 sm:grid-cols-2">
                    {aktuelle.map((m) => (
                        <ModulKarte key={m.modulId} modul={m} onFertig={laden} />
                    ))}
                </div>
            </section>

            {nachholen.length > 0 && (
                <section className="mt-8">
                    <h2 className="text-xl font-semibold">
                        Offen aus früheren Semestern
                        <span className="ml-2 rounded-full bg-amber-100 px-2 py-0.5 text-sm text-amber-800">
              {nachholen.length}
            </span>
                    </h2>
                    <div className="mt-3 grid gap-3 sm:grid-cols-2">
                        {nachholen.map((m) => (
                            <ModulKarte key={m.modulId} modul={m} onFertig={laden} />
                        ))}
                    </div>
                </section>
            )}
        </>
    )
}