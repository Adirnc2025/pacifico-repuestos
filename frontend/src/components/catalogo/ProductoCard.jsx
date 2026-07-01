import { Link } from 'react-router-dom'
import { useCarrito } from '../../context/CarritoContext'

function PlaceholderIcon() {
  return (
    <div className="w-full h-full flex flex-col items-center justify-center bg-gray-50 text-gray-300">
      <svg className="w-16 h-16 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1}
          d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0
             002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0
             001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0
             00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0
             00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0
             00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0
             00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0
             001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1}
          d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
      </svg>
      <span className="text-xs text-gray-400">Sin imagen</span>
    </div>
  )
}

export default function ProductoCard({ producto }) {
  const { agregar } = useCarrito()
  const hayStock = producto.stock > 0

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100
                    hover:shadow-lg hover:border-primary/20 transition-all duration-200
                    flex flex-col overflow-hidden group">

      {/* Imagen */}
      <Link to={`/productos/${producto.id}`} className="block relative">
        <div style={{
          height: '200px', minHeight: '200px', maxHeight: '200px',
          overflow: 'hidden', background: '#f1f5f9',
          display: 'flex', alignItems: 'center', justifyContent: 'center'
        }}>
          {producto.imagenPrincipal ? (
            <img
              src={producto.imagenPrincipal}
              alt={producto.nombre}
              style={{ width: '100%', height: '100%', objectFit: 'cover' }}
              className="group-hover:scale-105 transition-transform duration-300"
              onError={(e) => {
                e.target.style.display = 'none'
                e.target.parentElement.innerHTML = '<div style="font-size:48px;color:#cbd5e1">⚙️</div>'
              }}
            />
          ) : (
            <PlaceholderIcon />
          )}
        </div>
        {/* Badge destacado */}
        {producto.destacado && (
          <span className="absolute top-2 left-2 bg-secondary text-white
                           text-[10px] font-bold px-2 py-0.5 rounded-full uppercase tracking-wide">
            Destacado
          </span>
        )}
        {/* Badge agotado */}
        {!hayStock && (
          <div className="absolute inset-0 bg-black/40 flex items-center justify-center">
            <span className="bg-white text-gray-700 text-xs font-bold px-3 py-1 rounded-full">
              Agotado
            </span>
          </div>
        )}
      </Link>

      {/* Info */}
      <div className="flex-1 flex flex-col p-4">
        {producto.categoria && (
          <span className="text-[11px] text-secondary font-semibold uppercase tracking-wide mb-1">
            {producto.categoria}
          </span>
        )}
        <Link to={`/productos/${producto.id}`}
              className="font-semibold text-gray-900 text-sm leading-snug
                         hover:text-primary transition-colors line-clamp-2 mb-1">
          {producto.nombre}
        </Link>
        <div className="text-[11px] text-gray-400 mb-3 font-mono">#{producto.codigo}</div>

        <div className="mt-auto">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xl font-extrabold text-primary">
              S/ {Number(producto.precio).toFixed(2)}
            </span>
            {hayStock && (
              <span className="text-[11px] bg-green-50 text-green-700 border border-green-100
                               px-2 py-0.5 rounded-full font-medium">
                Stock: {producto.stock}
              </span>
            )}
          </div>
          <button
            onClick={() => agregar(producto)}
            disabled={!hayStock}
            className={`w-full py-2.5 rounded-xl text-sm font-semibold transition-colors
              ${hayStock
                ? 'bg-primary hover:bg-primary-dark text-white active:scale-95'
                : 'bg-gray-100 text-gray-400 cursor-not-allowed'}`}>
            {hayStock ? 'Agregar al carrito' : 'No disponible'}
          </button>
        </div>
      </div>
    </div>
  )
}
