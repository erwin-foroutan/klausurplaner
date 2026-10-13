const TOKEN_KEY = 'klausurplaner-token'
const EMAIL_KEY = 'klausurplaner-email'

export function holeToken() {
    return localStorage.getItem(TOKEN_KEY)
}

export function holeEmail() {
    return localStorage.getItem(EMAIL_KEY)
}

export function speichereLogin(token: string, email: string) {
    localStorage.setItem(TOKEN_KEY, token)
    localStorage.setItem(EMAIL_KEY, email)
}

export function abmelden() {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(EMAIL_KEY)
}

export async function authFetch(url: string, init: RequestInit = {}) {
    const headers = new Headers(init.headers)
    const token = holeToken()
    if (token) headers.set('Authorization', 'Bearer ' + token)

    const res = await fetch(url, { ...init, headers })

    if (res.status === 401) {
        abmelden()
        window.location.reload()
    }
    return res
}