import { rueckgaengig } from './api'

type Props = { modulId: number; onFertig: () => void; kompakt?: boolean }

export default function RueckgaengigButton({ modulId, onFertig, kompakt = false }: Props) {
    async function klick() {
        if (!window.confirm('Letzten Eintrag für dieses Modul rückgängig machen?')) return
        try {
            await rueckgaengig(modulId)
            onFertig()
        } catch (e) {
            window.alert((e as Error).message)
        }
    }

    return (
        <button
            type="button"
            onClick={klick}
            title="Letzten Eintrag rückgängig machen"
            className={
                kompakt
                    ? 'text-base leading-none text-gray-400 hover:text-red-600'
                    : 'text-sm font-medium text-gray-500 hover:text-red-600 hover:underline'
            }
        >
            {kompakt ? '↶' : 'Rückgängig'}
        </button>
    )
}