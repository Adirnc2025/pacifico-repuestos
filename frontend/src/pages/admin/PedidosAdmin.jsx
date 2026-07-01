import { useState, useEffect } from 'react'
import { pedidoService } from '../../services/pedidoService'
import { ESTADOS_PEDIDO } from '../../utils/constants'

const ESTADOS = ['PENDIENTE','CONFIRMADO','EN_PREPARACION','ENVIADO','ENTREGADO','CANCELADO']

export default function PedidosAdmin() {
  const [pedidos,  setPedidos]  = useState([])
  const [filtro,   setFiltro]   = useState('')
  const [cargando, setCargando] = useState(true)
  const [detalle,  setDetalle]  = useState(null)
  const [msg,      setMsg]      = useState({ texto:'', tipo:'' })

  useEffect(() => { cargar() }, [filtro])

  const cargar = () => {
    setCargando(true)
    pedidoService.listarTodos(filtro || null)
      .then(r => setPedidos(r.data))
      .finally(() => setCargando(false))
  }

  const cambiarEstado = async (id, estado) => {
    try {
      await pedidoService.cambiarEstado(id, estado)
      setMsg({ texto: `Estado actualizado a "${ESTADOS_PEDIDO[estado]?.label}"`, tipo:'ok' })
      if (detalle?.id === id) setDetalle(d => ({ ...d, estado }))
      cargar()
    } catch(e) {
      setMsg({ texto: e.response?.data?.error || 'Error al actualizar', tipo:'err' })
    }
    setTimeout(() => setMsg({ texto:'', tipo:'' }), 3000)
  }

  return (
    <div>
      <h2 className="text-xl font-bold text-gray-900 mb-4">Pedidos</h2>

      {msg.texto && (
        <div className={`mb-4 px-4 py-3 rounded-lg text-sm font-medium
          ${msg.tipo==='ok' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
          {msg.texto}
        </div>
      )}

      {/* Filtro estado */}
      <div className="flex flex-wrap gap-2 mb-5">
        <button onClick={() => setFiltro('')}
          className={`px-3 py-1.5 rounded-full text-xs font-medium transition-colors
            ${!filtro ? 'bg-primary text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'}`}>
          Todos
        </button>
        {ESTADOS.map(e => {
          const info = ESTADOS_PEDIDO[e]
          return (
            <button key={e} onClick={() => setFiltro(e)}
              className={`px-3 py-1.5 rounded-full text-xs font-medium transition-colors
                ${filtro===e ? 'bg-primary text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'}`}>
              {info?.label || e}
            </button>
          )
        })}
      </div>

      {/* Tabla */}
      {cargando ? (
        <div className="flex justify-center py-10">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"/>
        </div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-gray-50">
              <tr>
                {['N° Pedido','Cliente','Fecha','Total','Estado','Acciones'].map(h => (
                  <th key={h} className="text-left px-4 py-3 font-semibold text-gray-700">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {pedidos.map(p => {
                const estado = ESTADOS_PEDIDO[p.estado]
                return (
                  <tr key={p.id} className="hover:bg-gray-50">
                    <td className="px-4 py-3 font-mono text-xs font-medium text-primary">
                      {p.numeroPedido}
                    </td>
                    <td className="px-4 py-3">
                      <div className="font-medium">{p.cliente?.nombre}</div>
                      <div className="text-xs text-gray-500">{p.cliente?.telefono}</div>
                    </td>
                    <td className="px-4 py-3 text-gray-500 text-xs">
                      {new Date(p.fechaPedido).toLocaleString('es-PE')}
                    </td>
                    <td className="px-4 py-3 font-bold text-primary">
                      S/ {Number(p.total).toFixed(2)}
                    </td>
                    <td className="px-4 py-3">
                      <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${estado?.color}`}>
                        {estado?.label || p.estado}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex gap-2">
                        <button onClick={() => setDetalle(p)}
                          className="text-primary hover:underline text-xs font-medium">
                          Ver
                        </button>
                        <select value={p.estado} onChange={e => cambiarEstado(p.id, e.target.value)}
                          className="text-xs border border-gray-200 rounded px-1 py-0.5">
                          {ESTADOS.map(e => (
                            <option key={e} value={e}>{ESTADOS_PEDIDO[e]?.label || e}</option>
                          ))}
                        </select>
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal detalle */}
      {detalle && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg max-h-[85vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-bold">Pedido {detalle.numeroPedido}</h3>
                <button onClick={() => setDetalle(null)}
                  className="text-gray-400 hover:text-gray-600 text-xl">✕</button>
              </div>
              <div className="space-y-2 text-sm mb-4">
                <div className="flex justify-between">
                  <span className="text-gray-500">Cliente</span>
                  <span className="font-medium">{detalle.cliente?.nombre}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Teléfono</span>
                  <span>{detalle.cliente?.telefono || '—'}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Entrega</span>
                  <span>{detalle.tipoDelivery === 'RECOJO'
                    ? 'Recojo en tienda' : detalle.direccionEntrega}</span>
                </div>
                {detalle.zona && (
                  <div className="flex justify-between">
                    <span className="text-gray-500">Zona</span>
                    <span>{detalle.zona}</span>
                  </div>
                )}
                {detalle.observacion && (
                  <div className="flex justify-between">
                    <span className="text-gray-500">Nota</span>
                    <span className="text-right">{detalle.observacion}</span>
                  </div>
                )}
              </div>
              <div className="border-t border-gray-100 pt-4 space-y-2 text-sm">
                {detalle.detalles?.map((d, i) => (
                  <div key={i} className="flex justify-between">
                    <span>{d.productoNombre} × {d.cantidad}</span>
                    <span className="font-medium">S/ {Number(d.subtotal).toFixed(2)}</span>
                  </div>
                ))}
                <div className="flex justify-between text-gray-500 pt-1">
                  <span>Delivery</span>
                  <span>S/ {Number(detalle.costoDelivery).toFixed(2)}</span>
                </div>
                <div className="flex justify-between font-bold text-base pt-2 border-t border-gray-200">
                  <span>Total</span>
                  <span className="text-primary">S/ {Number(detalle.total).toFixed(2)}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
