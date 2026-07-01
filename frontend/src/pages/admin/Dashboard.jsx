import { useState, useEffect } from 'react'
import api from '../../utils/axiosConfig'

export default function Dashboard() {
  const [data,     setData]     = useState(null)
  const [cargando, setCargando] = useState(true)

  useEffect(() => {
    api.get('/reportes/dashboard')
      .then(r => setData(r.data))
      .finally(() => setCargando(false))
  }, [])

  if (cargando) return (
    <div className="flex justify-center py-20">
      <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary"/>
    </div>
  )

  const tarjetas = [
    { label:'Total productos',     valor: data?.totalProductos  ?? 0, icono:'📦', color:'bg-blue-50   border-blue-200',   texto:'text-blue-700'   },
    { label:'Pedidos hoy',         valor: data?.pedidosHoy      ?? 0, icono:'📋', color:'bg-green-50  border-green-200',  texto:'text-green-700'  },
    { label:'Pedidos pendientes',  valor: data?.pedidosPendientes ?? 0, icono:'⏳', color:'bg-yellow-50 border-yellow-200', texto:'text-yellow-700' },
    { label:'Stock bajo',          valor: data?.stockBajoCount  ?? 0, icono:'⚠️', color:'bg-red-50    border-red-200',    texto:'text-red-700'    },
  ]

  return (
    <div>
      <h2 className="text-xl font-bold text-gray-900 mb-6">Dashboard</h2>

      {/* Tarjetas métricas */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        {tarjetas.map(t => (
          <div key={t.label} className={`border rounded-xl p-5 ${t.color}`}>
            <div className="text-2xl mb-2">{t.icono}</div>
            <div className={`text-3xl font-bold ${t.texto}`}>{t.valor}</div>
            <div className="text-sm text-gray-600 mt-1">{t.label}</div>
          </div>
        ))}
      </div>

      {/* Productos con stock bajo */}
      {data?.stockBajo?.length > 0 && (
        <div className="card">
          <h3 className="font-bold text-gray-900 mb-4">⚠️ Productos con stock bajo</h3>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-50">
                <tr>
                  {['Código','Nombre','Stock actual'].map(h => (
                    <th key={h} className="text-left px-4 py-2 font-semibold text-gray-700">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {data.stockBajo.map(p => (
                  <tr key={p.productoId} className="hover:bg-gray-50">
                    <td className="px-4 py-2 font-mono text-xs">{p.codigo}</td>
                    <td className="px-4 py-2 font-medium">{p.nombre}</td>
                    <td className="px-4 py-2">
                      <span className="bg-red-100 text-red-700 px-2 py-0.5
                                       rounded-full text-xs font-medium">
                        {p.stock} unid.
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
