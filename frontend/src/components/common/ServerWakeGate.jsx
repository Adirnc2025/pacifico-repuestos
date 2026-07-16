import { useEffect, useState } from 'react'
import api from '../../utils/axiosConfig'

export default function ServerWakeGate({ children }) {
  const [estado, setEstado] = useState('conectando') // conectando | listo | timeout

  useEffect(() => {
    api.get('/auth/health')
      .then(() => setEstado('listo'))
      .catch(() => setEstado('timeout'))
  }, [])

  if (estado === 'listo') return children

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="text-center max-w-sm">
        {estado === 'conectando' ? (
          <>
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-5" />
            <p className="text-gray-700 font-medium">
              Conectando con el servidor, esto puede tomar unos segundos...
            </p>
          </>
        ) : (
          <>
            <div className="text-4xl mb-4">⏳</div>
            <p className="text-gray-700 font-medium mb-4">
              El servidor está iniciando, por favor recarga la página en un momento
            </p>
            <button onClick={() => window.location.reload()} className="btn-primary px-6 py-2">
              Reintentar
            </button>
          </>
        )}
      </div>
    </div>
  )
}
