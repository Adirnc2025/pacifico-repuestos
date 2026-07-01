/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        primary:   { DEFAULT: '#0057A8', dark: '#003D78', light: '#1A6FBF' },
        secondary: { DEFAULT: '#29B6F6', dark: '#0288D1', light: '#81D4FA' },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      }
    }
  },
  plugins: []
}
