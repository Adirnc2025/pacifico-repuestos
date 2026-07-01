import { useState, useEffect, useRef } from 'react'
import { useSearchParams } from 'react-router-dom'
import { productoService, catalogoService } from '../services/catalogoService'
import BuscadorVehiculo from '../components/catalogo/BuscadorVehiculo'
import ProductoCard from '../components/catalogo/ProductoCard'

export default function Productos() {
  const [productos,   setProductos]   = useState([])
  const [categorias,  setCategorias]  = useState([])
  const [marcas,      setMarcas]      = useState([])
  const [cargando,    setCargando]    = useState(false)
  const [catActiva,   setCatActiva]   = useState(null)
  const [searchParams] = useSearchParams()
  const q              = searchParams.get('q')
  const vista          = searchParams.get('vista')
  const soloDestacados = searchParams.get('destacado')
  const filtrosRef     = useRef(null)

  useEffect(() => {
    catalogoService.getCategorias().then(r => setCategorias(r.data))
    catalogoService.getMarcas().then(r => setMarcas(r.data)).catch(() => {})
  }, [])

  useEffect(() => {
    if (q) {
      buscarPorTexto(q)
      return
    }
    if (soloDestacados === 'true') {
      cargarDestacados()
      return
    }
    cargar({})
    setCatActiva(null)
  }, [q, vista, soloDestacados])

  useEffect(() => {
    if ((vista === 'categorias' || vista === 'marcas') && filtrosRef.current) {
      setTimeout(() => {
        filtrosRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' })
      }, 100)
    }
  }, [vista])

  const cargar = async (params) => {
    setCargando(true)
    try {
      const r = await productoService.listar(params)
      setProductos(r.data)
    } finally { setCargando(false) }
  }

  const cargarDestacados = async () => {
    setCargando(true)
    try {
      const r = await productoService.destacados()
      setProductos(r.data)
    } finally { setCargando(false) }
  }

  const buscarPorTexto = async (texto) => {
    if (texto.length < 3) return
    setCargando(true)
    try {
      const r = await productoService.buscar(texto)
      setProductos(r.data)
    } finally { setCargando(false) }
  }

  const filtrarPorCategoria = (catId) => {
    setCatActiva(catId)
    cargar(catId ? { categoriaId: catId } : {})
  }

  const filtrarPorMarca = (marcaId) => {
    cargar(marcaId ? { marcaId } : {})
  }

  const titulo = () => {
    if (q)                    return `Resultados: "${q}"`
    if (soloDestacados === 'true') return '🔥 Ofertas y Destacados'
    if (vista === 'marcas')   return 'Filtrar por Marca'
    return 'Catálogo de Repuestos'
  }

  return (
    <main className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">{titulo()}</h1>

      {/* Buscador por vehículo */}
      <div className="mb-6">
        <BuscadorVehiculo onFiltrar={cargar} />
      </div>

      {/* Sección de marcas (visible cuando vista=marcas) */}
      {vista === 'marcas' && marcas.length > 0 && (
        <div ref={filtrosRef} style={{
          background: '#f8fafc', borderRadius: '12px', padding: '20px',
          marginBottom: '24px', border: '1px solid #e2e8f0'
        }}>
          <p style={{ fontSize: '13px', fontWeight: '700', color: '#0057A8',
                      letterSpacing: '1px', marginBottom: '14px' }}>
            FILTRAR POR MARCA
          </p>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
            <button onClick={() => filtrarPorMarca(null)} style={{
              padding: '8px 18px', borderRadius: '20px', fontSize: '13px',
              fontWeight: '600', cursor: 'pointer', border: '2px solid #0057A8',
              background: '#0057A8', color: '#fff'
            }}>Todas</button>
            {marcas.map(m => (
              <button key={m.id} onClick={() => filtrarPorMarca(m.id)} style={{
                padding: '8px 18px', borderRadius: '20px', fontSize: '13px',
                fontWeight: '600', cursor: 'pointer', border: '2px solid #e2e8f0',
                background: '#fff', color: '#1e293b', transition: 'all 0.15s'
              }}
              onMouseEnter={e => { e.currentTarget.style.borderColor = '#0057A8'; e.currentTarget.style.color = '#0057A8' }}
              onMouseLeave={e => { e.currentTarget.style.borderColor = '#e2e8f0'; e.currentTarget.style.color = '#1e293b' }}>
                {m.nombre}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Filtro categorías */}
      <div ref={vista === 'categorias' ? filtrosRef : null}
           style={vista === 'categorias' ? {
             background: '#eff6ff', borderRadius: '12px', padding: '16px 20px',
             marginBottom: '24px', border: '2px solid #bfdbfe'
           } : { marginBottom: '24px' }}>
        {vista === 'categorias' && (
          <p style={{ fontSize: '13px', fontWeight: '700', color: '#0057A8',
                      letterSpacing: '1px', marginBottom: '12px' }}>
            FILTRAR POR CATEGORÍA
          </p>
        )}
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => filtrarPorCategoria(null)}
            className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors
              ${!catActiva ? 'bg-primary text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'}`}>
            Todos
          </button>
          {categorias.map(c => (
            <button key={c.id}
              onClick={() => filtrarPorCategoria(c.id)}
              className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors
                ${catActiva === c.id ? 'bg-primary text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'}`}>
              {c.nombre}
            </button>
          ))}
        </div>
      </div>

      {/* Resultados */}
      {cargando ? (
        <div className="flex justify-center py-20">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"/>
        </div>
      ) : productos.length === 0 ? (
        <div className="text-center py-20 text-gray-500">
          <div className="text-5xl mb-4">🔍</div>
          <p className="text-lg font-medium">No encontramos repuestos</p>
          <p className="text-sm mt-1">Intenta con otros filtros o contáctanos por WhatsApp</p>
        </div>
      ) : (
        <div>
          <p className="text-sm text-gray-500 mb-4">{productos.length} productos encontrados</p>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
            {productos.map(p => <ProductoCard key={p.id} producto={p} />)}
          </div>
        </div>
      )}
    </main>
  )
}
