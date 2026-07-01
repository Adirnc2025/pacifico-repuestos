import { useState } from 'react'
import api from '../../utils/axiosConfig'

export default function ReportesAdmin() {
  const hoy   = new Date().toISOString().split('T')[0]
  const hace7 = new Date(Date.now() - 7*24*60*60*1000).toISOString().split('T')[0]

  const [desde,    setDesde]    = useState(hace7)
  const [hasta,    setHasta]    = useState(hoy)
  const [reporte,  setReporte]  = useState(null)
  const [cargando, setCargando] = useState(false)
  const [error,    setError]    = useState('')

  const generar = async () => {
    setCargando(true)
    setError('')
    try {
      const { data } = await api.get(`/reportes/ventas?desde=${desde}&hasta=${hasta}`)
      setReporte(data)
    } catch {
      setError('Error al generar el reporte')
    } finally { setCargando(false) }
  }

  const ESTADO_COLOR = {
    PENDIENTE:      'bg-yellow-100 text-yellow-800',
    CONFIRMADO:     'bg-blue-100 text-blue-800',
    EN_PREPARACION: 'bg-purple-100 text-purple-800',
    ENVIADO:        'bg-orange-100 text-orange-800',
    ENTREGADO:      'bg-green-100 text-green-800',
    CANCELADO:      'bg-red-100 text-red-800',
  }

  return (
    <div>
      <h2 className="text-xl font-bold text-gray-900 mb-6">Reporte de ventas</h2>

      {/* Filtros */}
      <div className="card mb-6">
        <div className="flex flex-wrap items-end gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Desde</label>
            <input type="date" value={desde} onChange={e => setDesde(e.target.value)}
              className="input-field w-auto" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Hasta</label>
            <input type="date" value={hasta} onChange={e => setHasta(e.target.value)}
              className="input-field w-auto" />
          </div>
          <button onClick={generar} disabled={cargando}
            className="btn-primary px-6 py-2">
            {cargando ? 'Generando...' : 'Generar reporte'}
          </button>
        </div>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 rounded-lg
                        px-4 py-3 text-sm mb-4">{error}</div>
      )}

      {/* Resultados */}
      {reporte && (
        <>
          {/* Resumen */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
            <div className="card text-center">
              <div className="text-3xl font-bold text-primary">{reporte.totalPedidos}</div>
              <div className="text-sm text-gray-500 mt-1">Pedidos en el período</div>
            </div>
            <div className="card text-center">
              <div className="text-3xl font-bold text-green-600">
                S/ {Number(reporte.totalIngresos).toFixed(2)}
              </div>
              <div className="text-sm text-gray-500 mt-1">Total ingresos</div>
            </div>
            <div className="card text-center">
              <div className="text-3xl font-bold text-secondary">
                {reporte.totalPedidos > 0
                  ? `S/ ${(reporte.totalIngresos / reporte.totalPedidos).toFixed(2)}`
                  : '—'}
              </div>
              <div className="text-sm text-gray-500 mt-1">Ticket promedio</div>
            </div>
          </div>

          {/* Tabla */}
          {reporte.filas?.length > 0 ? (
            <div className="card">
              <h3 className="font-bold text-gray-900 mb-4">Detalle de pedidos</h3>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead className="bg-gray-50">
                    <tr>
                      {['N° Pedido','Cliente','Fecha','Estado','Total'].map(h => (
                        <th key={h}
                          className="text-left px-4 py-3 font-semibold text-gray-700">
                          {h}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-100">
                    {reporte.filas.map(f => (
                      <tr key={f.pedidoId} className="hover:bg-gray-50">
                        <td className="px-4 py-3 font-mono text-xs text-primary font-medium">
                          {f.numeroPedido}
                        </td>
                        <td className="px-4 py-3 font-medium">{f.cliente}</td>
                        <td className="px-4 py-3 text-gray-500 text-xs">
                          {new Date(f.fecha).toLocaleString('es-PE')}
                        </td>
                        <td className="px-4 py-3">
                          <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                            ${ESTADO_COLOR[f.estado] || 'bg-gray-100 text-gray-700'}`}>
                            {f.estado}
                          </span>
                        </td>
                        <td className="px-4 py-3 font-bold text-primary">
                          S/ {Number(f.total).toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot className="bg-gray-50 border-t-2 border-gray-200">
                    <tr>
                      <td colSpan={4}
                        className="px-4 py-3 font-bold text-gray-700 text-right">
                        TOTAL
                      </td>
                      <td className="px-4 py-3 font-bold text-primary text-lg">
                        S/ {Number(reporte.totalIngresos).toFixed(2)}
                      </td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          ) : (
            <div className="card text-center py-10 text-gray-400">
              <div className="text-4xl mb-3">📊</div>
              <p>No hay pedidos en este período</p>
            </div>
          )}
        </>
      )}
    </div>
  )
}
