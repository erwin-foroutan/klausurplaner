import type { ModulStatus, ModulUebersicht } from './types'
import ModulAktionen from './ModulAktionen'
import LernBereich from './LernBereich'
const statusStil: Record<ModulStatus, { text: string; klasse: string }> = {
    OFFEN: { text: 'Offen', klasse: 'bg-gray-100 text-gray-700' },
    ANGEMELDET: { text: 'Angemeldet', klasse: 'bg-blue-100 text-blue-700' },
    BESTANDEN: { text: 'Bestanden', klasse: 'bg-green-100 text-green-700' },
    ENDGUELTIG_NICHT_BESTANDEN: { text: 'Endgültig nicht bestanden', klasse: 'bg-red-100 text-red-700' },
}

const kategorieFarbe: Record<string, string> = {
    INFORMATIK_GRUNDLAGEN: 'border-l-blue-500',
    INFORMATIK_ANWENDUNGEN: 'border-l-emerald-500',
    MATHEMATIK: 'border-l-amber-500',
    FACHUEBERGREIFEND: 'border-l-purple-500',
}

export default function ModulKarte({ modul, onFertig }: { modul: ModulUebersicht; onFertig: () => void }) {    const status = statusStil[modul.status]
    const farbe = kategorieFarbe[modul.kategorie] ?? 'border-l-gray-300'

    return (
        <div className={'rounded-lg border border-l-4 bg-white p-4 shadow-sm ' + farbe}>
            <div className="flex items-start justify-between gap-2">
                <h3 className="font-semibold">{modul.name}</h3>
                <span className={'shrink-0 rounded-full px-2 py-0.5 text-xs font-medium ' + status.klasse}>
          {status.text}
        </span>
            </div>

            <p className="mt-1 text-sm text-gray-500">
                {modul.semester}. Semester · {modul.ects} CP
                {modul.note !== null && ' · Note ' + modul.note.toFixed(1)}
            </p>

            {modul.fehlversuche > 0 && modul.status !== 'BESTANDEN' && (
                <p className="mt-2 text-sm text-gray-600">
                    {modul.fehlversuche} von 3 Versuchen verbraucht
                </p>
            )}

            {modul.letzterVersuch && (
                <p className="mt-2 rounded bg-red-50 px-2 py-1 text-sm font-medium text-red-700">
                    ⚠️ Letzter Versuch – bei Nichtbestehen Exmatrikulation
                </p>
            )}
            <ModulAktionen modul={modul} onFertig={onFertig} />
            {modul.status !== 'BESTANDEN' && modul.status !== 'ENDGUELTIG_NICHT_BESTANDEN' && (
                <LernBereich modulId={modul.modulId} />
            )}
        </div>
    )
}