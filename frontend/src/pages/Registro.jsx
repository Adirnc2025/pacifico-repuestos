import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authService } from '../services/authService'

export default function Registro() {
  const [form, setForm] = useState({ nombre:'', correo:'', password:'', telefono:'' })
  const [error, setError] = useState('')
  const [cargando, setCargando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (form.password.length < 8) {
      setError('La contraseña debe tener mínimo 8 caracteres')
      return
    }
    setCargando(true)
    setError('')
    try {
      const { data } = await authService.register(form)
      login(data)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.error || 'Error al registrarse')
    } finally {
      setCargando(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-lg p-8 w-full max-w-md">
        <h1 className="text-2xl font-bold text-center mb-6">Crear cuenta</h1>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 rounded-lg
                          px-4 py-3 text-sm mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          {[
            { key:'nombre',   label:'Nombre completo',    type:'text',     placeholder:'Juan Pérez'       },
            { key:'correo',   label:'Correo electrónico', type:'email',    placeholder:'tu@correo.com'    },
            { key:'telefono', label:'Teléfono',           type:'tel',      placeholder:'966 000 000'      },
            { key:'password', label:'Contraseña',         type:'password', placeholder:'Mínimo 8 caracteres' },
          ].map(({ key, label, type, placeholder }) => (
            <div key={key}>
              <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
              <input type={type} value={form[key]} placeholder={placeholder}
                onChange={e => setForm({ ...form, [key]: e.target.value })}
                required={key !== 'telefono'} className="input-field" />
            </div>
          ))}
          <button type="submit" disabled={cargando} className="btn-primary w-full py-3">
            {cargando ? 'Registrando...' : 'Crear cuenta'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-500 mt-6">
          ¿Ya tienes cuenta?{' '}
          <Link to="/login" className="text-primary font-medium hover:underline">Ingresar</Link>
        </p>
      </div>
    </div>
  )
}
