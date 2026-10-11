export default function Fortschritt({ ects }: { ects: number }) {
    const prozent = Math.round((ects / 180) * 100)

    return (
        <div className="mt-4">
            <div className="flex justify-between text-sm text-gray-600">
                <span>{ects} / 180 ECTS</span>
                <span>{prozent} %</span>
            </div>
            <div className="mt-1 h-3 w-full rounded-full bg-gray-200">
                <div
                    className="h-3 rounded-full bg-green-500"
                    style={{ width: prozent + '%' }}
                />
            </div>
        </div>
    )
}