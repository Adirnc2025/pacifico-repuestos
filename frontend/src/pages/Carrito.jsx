import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useCarrito } from '../context/CarritoContext'
import { useAuth } from '../context/AuthContext'

export default function Carrito() {
  const { items, actualizar, eliminar, vaciar, total } = useCarrito()
  const { usuario } = useAuth()
  const navigate = useNavigate()
  const [msg, setMsg] = useState('')

  if (items.length === 0) return (
    <main className="max-w-3xl mx-auto px-4 py-16 text-center">
      <div className="text-6xl mb-4">🛒</div>
      <h1 className="text-2xl font-bold text-gray-900 mb-2">Tu carrito está vacío</h1>
      <p className="text-gray-500 mb-8">Agrega repuestos desde el catálogo</p>
      <Link to="/productos" className="btn-primary px-8 py-3">Ver catálogo</Link>
    </main>
  )

  const handleProceder = () => {
    if (!usuario) {
      setMsg('Debes iniciar sesión para continuar')
      setTimeout(() => navigate('/login'), 1500)
      return
    }
    navigate('/checkout')
  }

  return (
    <main className="max-w-5xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">
        Carrito ({items.length} {items.length === 1 ? 'producto' : 'productos'})
      </h1>

      {msg && (
        <div className="bg-yellow-50 border border-yellow-200 text-yellow-800
                        rounded-lg px-4 py-3 text-sm mb-4">{msg}</div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Lista de productos */}
        <div className="lg:col-span-2 space-y-3">
          {items.map(item => (
            <div key={item.id} className="card flex gap-4">
              <div className="w-20 h-20 bg-gray-50 rounded-lg flex-shrink-0 overflow-hidden">
                {item.imagenPrincipal
                  ? <img src={item.imagenPrincipal} alt={item.nombre}
                         className="w-full h-full object-cover" />
                  : <div className="w-full h-full flex items-center justify-center text-3xl">⚙️</div>
                }
              </div>
              <div className="flex-1">
                <p className="font-semibold text-gray-900 text-sm">{item.nombre}</p>
                <p className="text-xs text-gray-500 mb-2">Cód: {item.codigo}</p>
                <div className="flex items-center justify-between">
                  <div className="flex items-center border border-gray-300 rounded-lg">
                    <button onClick={() => actualizar(item.id, item.cantidad - 1)}
                      className="px-2.5 py-1 hover:bg-gray-100 rounded-l-lg text-sm">−</button>
                    <span className="px-3 py-1 text-sm font-medium">{item.cantidad}</span>
                    <button onClick={() => actualizar(item.id, item.cantidad + 1)}
                      disabled={item.cantidad >= item.stock}
                      className="px-2.5 py-1 hover:bg-gray-100 rounded-r-lg text-sm
                                 disabled:opacity-40">+</button>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className="font-bold text-primary">
                      S/ {(item.precio * item.cantidad).toFixed(2)}
                    </span>
                    <button onClick={() => eliminar(item.id)}
                      className="text-red-400 hover:text-red-600 transition-colors text-sm">✕</button>
                  </div>
                </div>
              </div>
            </div>
          ))}
          <button onClick={vaciar}
            className="text-sm text-gray-400 hover:text-red-500 transition-colors">
            Vaciar carrito
          </button>
        </div>

        {/* Resumen */}
        <div className="card h-fit">
          <h2 className="font-bold text-gray-900 mb-4">Resumen</h2>
          <div className="space-y-2 text-sm mb-4">
            {items.map(i => (
              <div key={i.id} className="flex justify-between text-gray-600">
                <span className="truncate mr-2">{i.nombre} x{i.cantidad}</span>
                <span>S/ {(i.precio * i.cantidad).toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="border-t border-gray-200 pt-3 mb-4">
            <div className="flex justify-between font-bold text-lg">
              <span>Total</span>
              <span className="text-primary">S/ {total.toFixed(2)}</span>
            </div>
            <p className="text-xs text-gray-400 mt-1">+ costo de delivery según zona</p>
          </div>
          <button onClick={handleProceder} className="btn-primary w-full py-3">
            Proceder al pedido
          </button>
          <Link to="/productos"
            className="block text-center text-sm text-gray-500 hover:text-primary mt-3">
            ← Seguir comprando
          </Link>
        </div>
      </div>
    </main>
  )
}
