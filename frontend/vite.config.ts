import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

const backend = 'http://localhost:8080'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/auth': backend,
      '/module': backend,
      '/studienprofil': backend,
      '/semester': backend,
      '/pruefungen': backend,
      '/lerneinheiten': backend,
      '/lernziele': backend,
    },
  },
})