import { NavLink, Route, Routes } from 'react-router'
import Dashboard from './Dashboard'
import Verlauf from './Verlauf'

function navKlasse({ isActive }: { isActive: boolean }) {
    return (
        'rounded-md px-3 py-1.5 text-sm font-medium ' +
        (isActive ? 'bg-gray-900 text-white' : 'text-gray-600 hover:bg-gray-100')
    )
}

function App() {
    return (
        <div className="min-h-screen bg-gray-50">
            <nav className="border-b bg-white">
                <div className="mx-auto flex max-w-6xl items-center gap-2 px-6 py-3">
                    <span className="mr-4 font-bold">Klausurplaner</span>
                    <NavLink to="/" end className={navKlasse}>
                        Dashboard
                    </NavLink>
                    <NavLink to="/verlauf" className={navKlasse}>
                        Studienverlauf
                    </NavLink>
                </div>
            </nav>

            <main className="mx-auto max-w-6xl p-6 sm:p-8">
                <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/verlauf" element={<Verlauf />} />
                </Routes>
            </main>
        </div>
    )
}

export default App