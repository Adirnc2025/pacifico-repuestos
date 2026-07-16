import { useEffect, useState } from 'react'
import api from '../../utils/axiosConfig'

const MAX_INTENTOS = 5
const INTERVALO_REINTENTO_MS = 10000
const TIMEOUT_PING_MS = 150000 // Render (free tier) puede tardar hasta ~2 min en despertar

export default function ServerWakeGate({ children }) {
  const [estado,  setEstado]  = useState('conectando') // conectando | listo | timeout
  const [intento, setIntento] = useState(1)
  const [clave,   setClave]   = useState(0) // cambiar fuerza reiniciar el efecto (botón Reintentar)

  useEffect(() => {
    let cancelado = false

    const intentarPing = (numeroIntento) => {
      setIntento(numeroIntento)
      setEstado('conectando')

      api.get('/categorias', { timeout: TIMEOUT_PING_MS })
        .then(() => { if (!cancelado) setEstado('listo') })
        .catch(() => {
          if (cancelado) return
          if (numeroIntento < MAX_INTENTOS) {
            setTimeout(() => { if (!cancelado) intentarPing(numeroIntento + 1) }, INTERVALO_REINTENTO_MS)
          } else {
            setEstado('timeout')
          }
        })
    }

    intentarPing(1)

    return () => { cancelado = true }
  }, [clave])

  if (estado === 'listo') return children

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="text-center max-w-sm">
        {estado === 'conectando' ? (
          <>
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-5" />
            <p className="text-gray-700 font-medium mb-1">
              Conectando con el servidor... intento {intento} de {MAX_INTENTOS}
            </p>
            <p className="text-gray-500 text-sm">
              El servidor está iniciando, esto puede tomar hasta 2 minutos la primera vez...
            </p>
          </>
        ) : (
          <>
            <div className="text-4xl mb-4">⏳</div>
            <p className="text-gray-700 font-medium mb-4">
              El servidor está iniciando, por favor recarga la página en un momento
            </p>
            <button onClick={() => setClave(c => c + 1)} className="btn-primary px-6 py-2">
              Reintentar
            </button>
          </>
        )}
      </div>
    </div>
  )
}
