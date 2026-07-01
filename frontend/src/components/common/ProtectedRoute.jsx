import { Navigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function ProtectedRoute({ children, soloAdmin = false }) {
  const { usuario, cargando } = useAuth()

  if (cargando) return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"/>
    </div>
  )

  if (!usuario) return <Navigate to="/login" replace />
  if (soloAdmin && usuario.rol !== 'ADMIN') return <Navigate to="/" replace />

  return children
}
