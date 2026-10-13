import { useState, type FormEvent } from 'react'
import { speichereLogin } from './auth'

type Modus = 'login' | 'registrieren'

export default function Login({ onLogin }: { onLogin: () => void }) {
    const [modus, setModus] = useState<Modus>('login')
    const [email, setEmail] = useState('')
    const [passwort, setPasswort] = useState('')
    const [fehler, setFehler] = useState<string | null>(null)
    const [laedt, setLaedt] = useState(false)

    const istLogin = modus === 'login'

    async function absenden(e: FormEvent) {
        e.preventDefault()
        setFehler(null)
        setLaedt(true)
        try {
            const res = await fetch('/auth/' + modus, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, passwort }),
            })
            const daten = await res.json().catch(() => ({}))
            if (!res.ok) {
                throw new Error(daten.fehler || Object.values(daten).join(', ') || 'Fehler ' + res.status)
            }
            speichereLogin(daten.token, daten.email)
            onLogin()
        } catch (err) {
            setFehler((err as Error).message)
        } finally {
            setLaedt(false)
        }
    }

    function wechseln() {
        setModus(istLogin ? 'registrieren' : 'login')
        setFehler(null)
    }

    const feld = 'mt-1 w-full rounded-md border px-3 py-2 focus:border-gray-900 focus:outline-none'

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-50 p-4">
            <div className="w-full max-w-sm rounded-xl bg-white p-8 shadow-sm">
                <h1 className="text-2xl font-bold">Klausurplaner</h1>
                <p className="mt-1 text-gray-600">{istLogin ? 'Melde dich an' : 'Erstelle ein Konto'}</p>

                <form onSubmit={absenden} className="mt-6 space-y-4">
                    <label className="block text-sm font-medium">
                        E-Mail
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            autoComplete="email"
                            className={feld}
                        />
                    </label>
                    <label className="block text-sm font-medium">
                        Passwort
                        <input
                            type="password"
                            value={passwort}
                            onChange={(e) => setPasswort(e.target.value)}
                            required
                            minLength={istLogin ? undefined : 8}
                            autoComplete={istLogin ? 'current-password' : 'new-password'}
                            className={feld}
                        />
                    </label>

                    {fehler && <p className="text-sm text-red-600">{fehler}</p>}

                    <button
                        type="submit"
                        disabled={laedt}
                        className="w-full rounded-md bg-gray-900 py-2 font-medium text-white hover:bg-gray-700 disabled:opacity-50"
                    >
                        {istLogin ? 'Anmelden' : 'Registrieren'}
                    </button>
                </form>

                <p className="mt-4 text-center text-sm text-gray-600">
                    {istLogin ? 'Noch kein Konto? ' : 'Schon registriert? '}
                    <button onClick={wechseln} className="font-medium text-gray-900 hover:underline">
                        {istLogin ? 'Registrieren' : 'Anmelden'}
                    </button>
                </p>
            </div>
        </div>
    )
}