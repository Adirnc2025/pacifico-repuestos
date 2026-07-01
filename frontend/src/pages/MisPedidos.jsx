import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { pedidoService } from '../services/pedidoService'
import { ESTADOS_PEDIDO } from '../utils/constants'

export default function MisPedidos() {
  const [pedidos,  setPedidos]  = useState([])
  const [cargando, setCargando] = useState(true)
  const [seleccionado, setSeleccionado] = useState(null)

  useEffect(() => {
    pedidoService.misPedidos()
      .then(r => setPedidos(r.data))
      .finally(() => setCargando(false))
  }, [])

  if (cargando) return (
    <div className="flex justify-center py-20">
      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"/>
    </div>
  )

  if (pedidos.length === 0) return (
    <main className="max-w-3xl mx-auto px-4 py-16 text-center">
      <div className="text-6xl mb-4">📋</div>
      <h1 className="text-2xl font-bold text-gray-900 mb-2">Sin pedidos aún</h1>
      <p className="text-gray-500 mb-8">Cuando realices tu primer pedido aparecerá aquí</p>
      <Link to="/productos" className="btn-primary px-8 py-3">Ver catálogo</Link>
    </main>
  )

  return (
    <main className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Mis pedidos</h1>

      <div className="space-y-3">
        {pedidos.map(p => {
          const estado = ESTADOS_PEDIDO[p.estado] || { label: p.estado, color: 'bg-gray-100 text-gray-700' }
          return (
            <div key={p.id} className="card">
              <div className="flex items-start justify-between mb-3">
                <div>
                  <span className="font-bold text-primary">{p.numeroPedido}</span>
                  <span className={`ml-2 text-xs px-2 py-0.5 rounded-full font-medium ${estado.color}`}>
                    {estado.label}
                  </span>
                </div>
                <span className="text-sm font-bold text-gray-900">
                  S/ {Number(p.total).toFixed(2)}
                </span>
              </div>

              <div className="text-xs text-gray-500 mb-3">
                {new Date(p.fechaPedido).toLocaleString('es-PE')} —{' '}
                {p.tipoDelivery === 'RECOJO' ? 'Recojo en tienda' : p.direccionEntrega}
              </div>

              {/* Detalles expandibles */}
              {seleccionado === p.id && p.detalles?.length > 0 && (
                <div className="border-t border-gray-100 pt-3 space-y-2 mb-3">
                  {p.detalles.map((d, i) => (
                    <div key={i} className="flex justify-between text-sm">
                      <span className="text-gray-700">{d.productoNombre} × {d.cantidad}</span>
                      <span className="text-gray-900 font-medium">
                        S/ {Number(d.subtotal).toFixed(2)}
                      </span>
                    </div>
                  ))}
                  {Number(p.costoDelivery) > 0 && (
                    <div className="flex justify-between text-sm text-gray-500">
                      <span>Delivery</span>
                      <span>S/ {Number(p.costoDelivery).toFixed(2)}</span>
                    </div>
                  )}
                </div>
              )}

              <button
                onClick={() => setSeleccionado(seleccionado === p.id ? null : p.id)}
                className="text-xs text-primary hover:underline">
                {seleccionado === p.id ? 'Ocultar detalle' : 'Ver detalle'}
              </button>
            </div>
          )
        })}
      </div>
    </main>
  )
}
