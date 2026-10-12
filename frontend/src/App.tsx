import { useCallback, useEffect, useState } from 'react'
import type { SemesterUebersicht } from './types'
import Fortschritt from './Fortschritt'
import ModulKarte from './ModulKarte'

function App() {
    const [daten, setDaten] = useState<SemesterUebersicht | null>(null)
    const [fehler, setFehler] = useState<string | null>(null)

    const laden = useCallback(() => {
        fetch('/semester/aktuell')
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

    if (fehler) return <p className="p-8 text-red-600">{fehler}</p>
    if (!daten) return <p className="p-8">Lädt …</p>

    const aktuelle = daten.module.filter((m) => m.semester === daten.aktuellesSemester)
    const nachholen = daten.module.filter((m) => m.semester < daten.aktuellesSemester)

    return (
        <div className="min-h-screen bg-gray-50">
            <main className="mx-auto max-w-5xl p-6 sm:p-8">
                <header className="rounded-xl bg-white p-6 shadow-sm">
                    <h1 className="text-3xl font-bold">Klausurplaner</h1>
                    <p className="mt-1 text-gray-600">
                        Angewandte Informatik · {daten.aktuellesSemester}. Semester
                    </p>
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
            </main>
        </div>
    )
}

export default App