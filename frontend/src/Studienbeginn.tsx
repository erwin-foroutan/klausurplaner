import { useEffect, useState } from 'react'
import { setzeStudienbeginn } from './api'

function spaetestesJahr() {
    const heute = new Date()
    return heute.getMonth() >= 9 ? heute.getFullYear() : heute.getFullYear() - 1
}

export default function Studienbeginn({ onFertig }: { onFertig: () => void }) {
    const [startJahr, setStartJahr] = useState<number | null>(null)
    const [fehler, setFehler] = useState<string | null>(null)

    useEffect(() => {
        fetch('/studienprofil')
            .then((res) => (res.ok ? res.json() : null))
            .then((profil) => {
                if (profil) setStartJahr(profil.startJahr)
            })
    }, [])

    const jahre = Array.from({ length: 10 }, (_, i) => spaetestesJahr() - i)

    async function aendern(jahr: number) {
        setFehler(null)
        try {
            await setzeStudienbeginn(jahr)
            setStartJahr(jahr)
            onFertig()
        } catch (e) {
            setFehler((e as Error).message)
        }
    }

    return (
        <div className="flex flex-wrap items-center gap-2 text-sm">
            <label htmlFor="studienbeginn" className="text-gray-600">
                Studienbeginn:
            </label>
            <select
                id="studienbeginn"
                value={startJahr ?? ''}
                onChange={(e) => aendern(Number(e.target.value))}
                className="rounded border bg-white px-2 py-1"
            >
                {startJahr === null && <option value="">Bitte wählen</option>}
                {jahre.map((j) => (
                    <option key={j} value={j}>
                        WiSe {j}/{String(j + 1).slice(2)}
                    </option>
                ))}
            </select>
            {fehler && <span className="text-red-600">{fehler}</span>}
        </div>
    )
}