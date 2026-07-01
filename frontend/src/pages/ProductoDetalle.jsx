import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { productoService } from '../services/catalogoService'
import { useCarrito } from '../context/CarritoContext'
import { WHATSAPP_URL } from '../utils/constants'

export default function ProductoDetalle() {
  const { id } = useParams()
  const [producto, setProducto] = useState(null)
  const [imgActiva, setImgActiva] = useState(0)
  const [cantidad,  setCantidad]  = useState(1)
  const [msg,       setMsg]       = useState('')
  const { agregar } = useCarrito()
  const navigate = useNavigate()

  useEffect(() => {
    productoService.detalle(id).then(r => setProducto(r.data)).catch(() => navigate('/productos'))
  }, [id])

  if (!producto) return (
    <div className="flex justify-center py-32">
      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"/>
    </div>
  )

  const imagenes = producto.imagenes?.length ? producto.imagenes : [null]

  const handleAgregar = () => {
    agregar(producto, cantidad)
    setMsg('¡Producto agregado al carrito!')
    setTimeout(() => setMsg(''), 2500)
  }

  return (
    <main className="max-w-6xl mx-auto px-4 py-8">
      <button onClick={() => navigate(-1)}
        className="text-sm text-gray-500 hover:text-primary mb-4 flex items-center gap-1">
        ← Volver
      </button>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Galería */}
        <div>
          <div className="aspect-square bg-gray-50 rounded-xl overflow-hidden mb-3">
            {imagenes[imgActiva] ? (
              <img src={imagenes[imgActiva]} alt={producto.nombre}
                   className="w-full h-full object-cover" />
            ) : (
              <div className="w-full h-full flex items-center justify-center text-8xl">⚙️</div>
            )}
          </div>
          {imagenes.length > 1 && (
            <div className="flex gap-2">
              {imagenes.map((url, i) => (
                <button key={i} onClick={() => setImgActiva(i)}
                  className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-colors
                    ${imgActiva === i ? 'border-primary' : 'border-gray-200'}`}>
                  {url
                    ? <img src={url} alt="" className="w-full h-full object-cover" />
                    : <span className="text-2xl">⚙️</span>}
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Info */}
        <div>
          <span className="text-sm text-secondary font-medium">{producto.categoria}</span>
          <h1 className="text-2xl font-bold text-gray-900 mt-1 mb-2">{producto.nombre}</h1>
          <p className="text-sm text-gray-500 mb-4">Código: {producto.codigo}
            {producto.medidas && ` | Medidas: ${producto.medidas}`}</p>
          <p className="text-3xl font-bold text-primary mb-4">
            S/ {Number(producto.precio).toFixed(2)}
          </p>

          {producto.descripcion && (
            <p className="text-gray-600 text-sm mb-4 leading-relaxed">{producto.descripcion}</p>
          )}

          {/* Stock */}
          <div className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-sm font-medium mb-6
            ${producto.stock > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
            {producto.stock > 0 ? `✓ Disponible (${producto.stock} en stock)` : '✗ Sin stock'}
          </div>

          {/* Cantidad + botones */}
          {producto.stock > 0 && (
            <div className="flex items-center gap-3 mb-4">
              <div className="flex items-center border border-gray-300 rounded-lg">
                <button onClick={() => setCantidad(c => Math.max(1,c-1))}
                  className="px-3 py-2 hover:bg-gray-100 rounded-l-lg">−</button>
                <span className="px-4 py-2 font-medium">{cantidad}</span>
                <button onClick={() => setCantidad(c => Math.min(producto.stock,c+1))}
                  className="px-3 py-2 hover:bg-gray-100 rounded-r-lg">+</button>
              </div>
              <button onClick={handleAgregar} className="btn-primary flex-1 py-2.5">
                Agregar al carrito
              </button>
            </div>
          )}

          {msg && <p className="text-green-600 text-sm font-medium mb-3">{msg}</p>}

          <a href={WHATSAPP_URL} target="_blank" rel="noopener noreferrer"
             className="flex items-center justify-center gap-2 border-2 border-green-500
                        text-green-600 rounded-lg py-2.5 hover:bg-green-50 transition-colors">
            Consultar por WhatsApp
          </a>
        </div>
      </div>

      {/* Compatibilidades */}
      {producto.compatibilidades?.length > 0 && (
        <div className="mt-10">
          <h2 className="text-xl font-bold text-gray-900 mb-4">
            Compatibilidades ({producto.compatibilidades.length})
          </h2>
          <div className="overflow-x-auto">
            <table className="w-full text-sm border-collapse">
              <thead>
                <tr className="bg-gray-50">
                  {['Marca','Modelo','Generación','Motor','Descripción'].map(h => (
                    <th key={h} className="text-left px-4 py-3 font-semibold text-gray-700
                                           border-b border-gray-200">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {producto.compatibilidades.map((c, i) => (
                  <tr key={i} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="px-4 py-3 font-medium">{c.marca}</td>
                    <td className="px-4 py-3">{c.modelo}</td>
                    <td className="px-4 py-3 text-gray-500">{c.generacion}</td>
                    <td className="px-4 py-3">
                      <span className="bg-primary/10 text-primary px-2 py-0.5 rounded font-mono text-xs">
                        {c.motorCodigo}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-gray-500">{c.motorDescripcion}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </main>
  )
}
