import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  const [cargando, setCargando] = useState(true)

  useEffect(() => {
    const token   = localStorage.getItem('token')
    const userData = localStorage.getItem('usuario')
    if (token && userData) {
      setUsuario(JSON.parse(userData))
    }
    setCargando(false)
  }, [])

  const login = (data) => {
    localStorage.setItem('token', data.token)
    localStorage.setItem('usuario', JSON.stringify({
      id: data.usuarioId, nombre: data.nombre,
      correo: data.correo, rol: data.rol
    }))
    setUsuario({ id: data.usuarioId, nombre: data.nombre,
                 correo: data.correo, rol: data.rol })
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('usuario')
    setUsuario(null)
  }

  const esAdmin = () => usuario?.rol === 'ADMIN'

  return (
    <AuthContext.Provider value={{ usuario, login, logout, esAdmin, cargando }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
