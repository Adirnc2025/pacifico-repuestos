import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { catalogoService, productoService } from '../services/catalogoService'
import { WHATSAPP_URL } from '../utils/constants'

export default function Home() {
  const [marcas,       setMarcas]      = useState([])
  const [modelos,      setModelos]     = useState([])
  const [generaciones, setGeneraciones]= useState([])
  const [motores,      setMotores]     = useState([])
  const [destacados,   setDestacados]  = useState([])
  const [sel, setSel] = useState({ marcaId:'', modeloId:'', generacionId:'', motorId:'' })
  const navigate = useNavigate()

  useEffect(() => {
    catalogoService.getMarcas().then(r => setMarcas(r.data)).catch(() => {})
    productoService.destacados().then(r => setDestacados(r.data)).catch(() => {})
  }, [])

  const handleMarca = async (v) => {
    setSel({ marcaId:v, modeloId:'', generacionId:'', motorId:'' })
    setModelos([]); setGeneraciones([]); setMotores([])
    if (v) catalogoService.getModelos(v).then(r => setModelos(r.data))
  }
  const handleModelo = async (v) => {
    setSel(s => ({ ...s, modeloId:v, generacionId:'', motorId:'' }))
    setGeneraciones([]); setMotores([])
    if (v) catalogoService.getGeneraciones(v).then(r => setGeneraciones(r.data))
  }
  const handleGeneracion = async (v) => {
    setSel(s => ({ ...s, generacionId:v, motorId:'' }))
    setMotores([])
    if (v) catalogoService.getMotores(v).then(r => setMotores(r.data))
  }
  const handleBuscar = () => {
    const p = new URLSearchParams()
    if (sel.marcaId)      p.set('marcaId', sel.marcaId)
    if (sel.modeloId)     p.set('modeloId', sel.modeloId)
    if (sel.generacionId) p.set('generacionId', sel.generacionId)
    if (sel.motorId)      p.set('motorId', sel.motorId)
    navigate(`/productos?${p.toString()}`)
  }

  const categorias = [
    { nombre: 'Reparación de Motor', icono: '⚙️', desc: 'Empaques, pistones, anillos, válvulas y más' },
    { nombre: 'Suspensión',          icono: '🔧', desc: 'Amortiguadores, espirales, rodamientos y más' },
    { nombre: 'Frenos',              icono: '🛑', desc: 'Pastillas, discos, zapatas, bombas y más' },
    { nombre: 'Dirección',           icono: '🎯', desc: 'Terminales, barras, cremalleras y más' },
    { nombre: 'Sistema Eléctrico',   icono: '⚡', desc: 'Baterías, alternadores, sensores y más' },
    { nombre: 'Lubricantes',         icono: '🛢️', desc: 'Aceites, filtros, aditivos y más' },
    { nombre: 'Accesorios',          icono: '🔩', desc: 'Iluminación, carcasas, parrillas y más' },
  ]

  const marcasLogos = ['Toyota','KIA','Hyundai','Changan','Suzuki','JAC','Nissan','Honda']

  const stats = [
    { num: '500+', label: 'Productos',         icono: '📦' },
    { num: '5+',   label: 'Años de experiencia',icono: '🏆' },
    { num: '100%', label: 'Garantía de calidad',icono: '✅' },
    { num: '2 días',label: 'Envío rápido',      icono: '🚚' },
  ]

  const sel_style = {
    flex: 1, padding: '14px 16px', border: '1px solid #1e3a5f',
    borderRadius: '8px', background: 'rgba(255,255,255,0.08)',
    color: '#fff', fontSize: '14px', cursor: 'pointer', outline: 'none',
    appearance: 'none', minWidth: '150px'
  }

  return (
    <main style={{ fontFamily: 'Inter, sans-serif' }}>

      {/* ═══ HERO BANNER ═══ */}
      <section style={{ position: 'relative', width: '100%', overflow: 'hidden' }}>
        <img
          src="/images/hero/hero-banner.jpg"
          alt="Pacífico Repuestos"
          style={{
            width: '100%',
            height: '620px',
            objectFit: 'cover',
            objectPosition: 'center 25%',
            display: 'block'
          }}
        />
        <div style={{
          position: 'absolute',
          top: 0, left: 0, right: 0, bottom: 0,
          display: 'flex',
          alignItems: 'center',
          padding: '0 60px'
        }}>
          <div style={{ maxWidth: '460px' }}>
            <div style={{
              display: 'inline-block',
              background: 'rgba(41,182,246,0.15)',
              border: '1px solid rgba(41,182,246,0.3)',
              color: '#29B6F6',
              padding: '5px 14px',
              borderRadius: '20px',
              fontSize: '11px',
              fontWeight: '600',
              letterSpacing: '2px',
              marginBottom: '14px'
            }}>
              AYACUCHO, PERÚ
            </div>
            <h1 style={{ color: '#fff', fontSize: '32px', fontWeight: '800', lineHeight: '1.15', margin: '0 0 4px' }}>
              Repuestos automotrices
            </h1>
            <h1 style={{ color: '#29B6F6', fontSize: '32px', fontWeight: '800', lineHeight: '1.15', margin: '0 0 14px' }}>
              de confianza
            </h1>
            <p style={{ color: '#e2e8f0', fontSize: '13px', lineHeight: '1.5', margin: '0 0 20px' }}>
              Encuentra el repuesto exacto para tu vehículo. Stock disponible,
              entrega local e interprovincial a todo el Perú.
            </p>
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
              <a href="/productos" style={{ textDecoration: 'none' }}>
                <button style={{
                  background: '#0057A8', color: '#fff', border: 'none',
                  padding: '11px 22px', borderRadius: '8px', cursor: 'pointer',
                  fontSize: '13px', fontWeight: '700'
                }}>
                  Ver catálogo completo
                </button>
              </a>
              <a href="https://wa.me/51981869554" target="_blank" rel="noreferrer" style={{ textDecoration: 'none' }}>
                <button style={{
                  background: 'rgba(255,255,255,0.1)', color: '#fff',
                  border: '2px solid rgba(255,255,255,0.3)',
                  padding: '11px 22px', borderRadius: '8px', cursor: 'pointer',
                  fontSize: '13px', fontWeight: '600'
                }}>
                  Consultar por WhatsApp
                </button>
              </a>
            </div>
          </div>
        </div>

        <div style={{
          position: 'absolute',
          bottom: 0, left: 0, right: 0,
          background: 'rgba(0,0,0,0.5)',
          display: 'grid',
          gridTemplateColumns: 'repeat(4,1fr)'
        }}>
          {[
            { num: '500+',   label: 'Productos' },
            { num: '5+',     label: 'Años de experiencia' },
            { num: '100%',   label: 'Garantía de calidad' },
            { num: '2 días', label: 'Envío rápido' },
          ].map(s => (
            <div key={s.label} style={{
              padding: '12px 16px',
              textAlign: 'center',
              borderRight: '1px solid rgba(255,255,255,0.1)'
            }}>
              <div style={{ color: '#29B6F6', fontSize: '18px', fontWeight: '800' }}>{s.num}</div>
              <div style={{ color: '#94a3b8', fontSize: '10px', marginTop: '2px' }}>{s.label}</div>
            </div>
          ))}
        </div>
      </section>

      {/* ═══ BUSCADOR POR VEHÍCULO ═══ */}
      <section style={{ background: '#f8fafc', padding: '40px 20px' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto' }}>
          <div style={{
            background: 'linear-gradient(135deg, #0a1628, #0d2040)',
            borderRadius: '16px', padding: '32px',
            border: '1px solid rgba(41,182,246,0.2)'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '24px' }}>
              <span style={{ fontSize: '22px' }}>🚗</span>
              <h2 style={{ color: '#fff', margin: 0, fontSize: '18px', fontWeight: '700' }}>
                ENCUENTRA REPUESTOS{' '}
                <span style={{ color: '#29B6F6' }}>COMPATIBLES</span>
                {' '}CON TU VEHÍCULO
              </h2>
            </div>

            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', marginBottom: '16px' }}>
              {/* Marca */}
              <div style={{ flex: 1, minWidth: '150px', position: 'relative' }}>
                <div style={{ color: '#94a3b8', fontSize: '11px', fontWeight: '600',
                              marginBottom: '6px', letterSpacing: '0.5px' }}>🏷 MARCA</div>
                <select value={sel.marcaId} onChange={e => handleMarca(e.target.value)}
                        style={sel_style}>
                  <option value="">Selecciona marca</option>
                  {marcas.map(m => <option key={m.id} value={m.id} style={{ color: '#000' }}>{m.nombre}</option>)}
                </select>
              </div>
              {/* Modelo */}
              <div style={{ flex: 1, minWidth: '150px' }}>
                <div style={{ color: '#94a3b8', fontSize: '11px', fontWeight: '600',
                              marginBottom: '6px', letterSpacing: '0.5px' }}>🚙 MODELO</div>
                <select value={sel.modeloId} onChange={e => handleModelo(e.target.value)}
                        disabled={!sel.marcaId} style={{ ...sel_style, opacity: sel.marcaId ? 1 : 0.5 }}>
                  <option value="">Selecciona modelo</option>
                  {modelos.map(m => <option key={m.id} value={m.id} style={{ color: '#000' }}>{m.nombre}</option>)}
                </select>
              </div>
              {/* Generación */}
              <div style={{ flex: 1, minWidth: '150px' }}>
                <div style={{ color: '#94a3b8', fontSize: '11px', fontWeight: '600',
                              marginBottom: '6px', letterSpacing: '0.5px' }}>📅 GENERACIÓN</div>
                <select value={sel.generacionId} onChange={e => handleGeneracion(e.target.value)}
                        disabled={!sel.modeloId} style={{ ...sel_style, opacity: sel.modeloId ? 1 : 0.5 }}>
                  <option value="">Selecciona generación</option>
                  {generaciones.map(g => <option key={g.id} value={g.id} style={{ color: '#000' }}>{g.nombre}</option>)}
                </select>
              </div>
              {/* Motor */}
              <div style={{ flex: 1, minWidth: '150px' }}>
                <div style={{ color: '#94a3b8', fontSize: '11px', fontWeight: '600',
                              marginBottom: '6px', letterSpacing: '0.5px' }}>⚙️ MOTOR</div>
                <select value={sel.motorId} onChange={e => setSel(s => ({ ...s, motorId: e.target.value }))}
                        disabled={!sel.generacionId} style={{ ...sel_style, opacity: sel.generacionId ? 1 : 0.5 }}>
                  <option value="">Selecciona motor</option>
                  {motores.map(m => <option key={m.id} value={m.id} style={{ color: '#000' }}>{m.nombre}</option>)}
                </select>
              </div>
              {/* Botón buscar */}
              <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'flex-end' }}>
                <div style={{ marginBottom: '6px', height: '17px' }} />
                <button onClick={handleBuscar} style={{
                  background: '#0057A8', color: '#fff', border: 'none',
                  padding: '14px 28px', borderRadius: '8px', cursor: 'pointer',
                  fontSize: '14px', fontWeight: '700', whiteSpace: 'nowrap',
                  display: 'flex', alignItems: 'center', gap: '8px',
                  boxShadow: '0 4px 12px rgba(0,87,168,0.4)', transition: 'all 0.2s'
                }}
                onMouseEnter={e => e.currentTarget.style.background = '#29B6F6'}
                onMouseLeave={e => e.currentTarget.style.background = '#0057A8'}>
                  🔍 Buscar repuestos
                </button>
              </div>
            </div>

            <div style={{ color: '#64748b', fontSize: '12px', textAlign: 'center' }}>
              🔒 Selecciona tu vehículo para ver los repuestos compatibles
            </div>
          </div>
        </div>
      </section>

      {/* ═══ FEATURES BAR ═══ */}
      <section style={{ background: '#fff', borderBottom: '1px solid #e2e8f0', padding: '24px 20px' }}>
        <div style={{
          maxWidth: '1280px', margin: '0 auto',
          display: 'grid', gridTemplateColumns: 'repeat(5,1fr)', gap: '16px'
        }}>
          {[
            { icon: '🏆', title: 'CALIDAD GARANTIZADA',   desc: 'Repuestos originales y alternativos de calidad' },
            { icon: '📦', title: 'GRAN STOCK DISPONIBLE', desc: 'Miles de repuestos para tu vehículo' },
            { icon: '🚚', title: 'ENVÍOS RÁPIDOS',        desc: 'Delivery en Ayacucho y envíos a todo el Perú' },
            { icon: '🎧', title: 'SOPORTE ESPECIALIZADO', desc: 'Asesoría técnica para encontrar el repuesto correcto' },
            { icon: '💳', title: 'PAGOS SEGUROS',         desc: 'Aceptamos todas las tarjetas y billeteras digitales' },
          ].map(f => (
            <div key={f.title} style={{ display: 'flex', alignItems: 'flex-start', gap: '12px' }}>
              <div style={{
                background: '#e0f2fe', borderRadius: '10px',
                width: '44px', height: '44px', flexShrink: 0,
                display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px'
              }}>{f.icon}</div>
              <div>
                <div style={{ fontSize: '11px', fontWeight: '700', color: '#0057A8',
                              letterSpacing: '0.5px', marginBottom: '3px' }}>{f.title}</div>
                <div style={{ fontSize: '12px', color: '#64748b', lineHeight: '1.4' }}>{f.desc}</div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ═══ CATEGORÍAS ═══ */}
      <section style={{ background: '#f8fafc', padding: '60px 20px' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
            <div>
              <div style={{ color: '#0057A8', fontSize: '13px', fontWeight: '700',
                            letterSpacing: '2px', marginBottom: '8px' }}>— CATEGORÍAS PRINCIPALES</div>
              <h2 style={{ margin: 0, fontSize: '28px', fontWeight: '800', color: '#0f172a' }}>
                Explora por categoría
              </h2>
            </div>
            <Link to="/productos" style={{
              color: '#0057A8', textDecoration: 'none', fontSize: '14px',
              fontWeight: '600', display: 'flex', alignItems: 'center', gap: '4px'
            }}>Ver todas las categorías →</Link>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7,1fr)', gap: '12px' }}>
            {categorias.map(cat => (
              <div key={cat.nombre} onClick={() => navigate('/productos')} style={{
                background: '#fff', border: '2px solid #e2e8f0', borderRadius: '12px',
                padding: '20px 12px', textAlign: 'center', cursor: 'pointer',
                transition: 'all 0.2s'
              }}
              onMouseEnter={e => {
                e.currentTarget.style.borderColor = '#0057A8'
                e.currentTarget.style.transform = 'translateY(-4px)'
                e.currentTarget.style.boxShadow = '0 8px 24px rgba(0,87,168,0.15)'
              }}
              onMouseLeave={e => {
                e.currentTarget.style.borderColor = '#e2e8f0'
                e.currentTarget.style.transform = 'none'
                e.currentTarget.style.boxShadow = 'none'
              }}>
                <div style={{ fontSize: '36px', marginBottom: '10px' }}>{cat.icono}</div>
                <div style={{ fontSize: '12px', fontWeight: '700', color: '#0f172a',
                              marginBottom: '6px' }}>{cat.nombre}</div>
                <div style={{ fontSize: '10px', color: '#94a3b8', lineHeight: '1.4' }}>{cat.desc}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ PRODUCTOS DESTACADOS ═══ */}
      {destacados.length > 0 && (
        <section style={{ background: '#fff', padding: '60px 20px' }}>
          <div style={{ maxWidth: '1280px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between',
                          alignItems: 'center', marginBottom: '32px' }}>
              <div>
                <div style={{ color: '#0057A8', fontSize: '13px', fontWeight: '700',
                              letterSpacing: '2px', marginBottom: '8px' }}>— PRODUCTOS DESTACADOS</div>
                <h2 style={{ margin: 0, fontSize: '28px', fontWeight: '800', color: '#0f172a' }}>
                  Los más solicitados
                </h2>
              </div>
              <Link to="/productos" style={{
                color: '#0057A8', textDecoration: 'none', fontSize: '14px', fontWeight: '600'
              }}>Ver todos →</Link>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(6,1fr)', gap: '16px' }}>
              {destacados.slice(0, 6).map(p => (
                <Link key={p.id} to={`/productos/${p.id}`} style={{ textDecoration: 'none' }}>
                  <div style={{
                    border: '2px solid #e2e8f0', borderRadius: '12px',
                    overflow: 'hidden', transition: 'all 0.2s', cursor: 'pointer'
                  }}
                  onMouseEnter={e => {
                    e.currentTarget.style.borderColor = '#0057A8'
                    e.currentTarget.style.transform = 'translateY(-4px)'
                  }}
                  onMouseLeave={e => {
                    e.currentTarget.style.borderColor = '#e2e8f0'
                    e.currentTarget.style.transform = 'none'
                  }}>
                    <div style={{ background: '#f1f5f9', height: '160px',
                                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                                  fontSize: '60px' }}>
                      {p.imagenPrincipal
                        ? <img src={p.imagenPrincipal} alt={p.nombre}
                               style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                        : '⚙️'}
                    </div>
                    <div style={{ padding: '12px' }}>
                      <div style={{ fontSize: '11px', color: '#29B6F6', fontWeight: '600',
                                    marginBottom: '4px' }}>{p.categoria}</div>
                      <div style={{ fontSize: '13px', fontWeight: '600', color: '#0f172a',
                                    marginBottom: '8px', lineHeight: '1.3',
                                    overflow: 'hidden', display: '-webkit-box',
                                    WebkitLineClamp: 2, WebkitBoxOrient: 'vertical' }}>
                        {p.nombre}
                      </div>
                      <div style={{ fontSize: '16px', fontWeight: '800', color: '#0057A8',
                                    marginBottom: '8px' }}>
                        S/ {Number(p.precio).toFixed(2)}
                      </div>
                      <button style={{
                        width: '100%', background: '#0057A8', color: '#fff', border: 'none',
                        padding: '8px', borderRadius: '6px', cursor: 'pointer',
                        fontSize: '12px', fontWeight: '600'
                      }}>🛒 Agregar al carrito</button>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ═══ MARCAS ═══ */}
      <section style={{ background: '#f8fafc', padding: '60px 20px' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto' }}>
          <div style={{ textAlign: 'center', marginBottom: '40px' }}>
            <div style={{ color: '#0057A8', fontSize: '13px', fontWeight: '700',
                          letterSpacing: '2px', marginBottom: '8px' }}>— MARCAS PRINCIPALES</div>
            <h2 style={{ margin: 0, fontSize: '28px', fontWeight: '800', color: '#0f172a' }}>
              Repuestos para todas las marcas
            </h2>
          </div>
          <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: '16px' }}>
            {marcasLogos.map(m => (
              <div key={m} onClick={() => navigate('/productos')} style={{
                background: '#fff', border: '2px solid #e2e8f0', borderRadius: '12px',
                padding: '20px 32px', cursor: 'pointer', transition: 'all 0.2s',
                display: 'flex', alignItems: 'center', justifyContent: 'center', minWidth: '120px'
              }}
              onMouseEnter={e => {
                e.currentTarget.style.borderColor = '#0057A8'
                e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,87,168,0.15)'
              }}
              onMouseLeave={e => {
                e.currentTarget.style.borderColor = '#e2e8f0'
                e.currentTarget.style.boxShadow = 'none'
              }}>
                <span style={{ fontWeight: '800', fontSize: '14px', color: '#1e293b',
                               letterSpacing: '1px' }}>{m}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ DELIVERY BANNER ═══ */}
      <section style={{
        background: 'linear-gradient(135deg, #0057A8, #0d2040)',
        padding: '60px 20px'
      }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto', textAlign: 'center' }}>
          <div style={{ fontSize: '48px', marginBottom: '16px' }}>🚚</div>
          <h2 style={{ color: '#fff', fontSize: '32px', fontWeight: '800', margin: '0 0 12px' }}>
            Delivery disponible
          </h2>
          <p style={{ color: '#93c5fd', fontSize: '16px', margin: '0 0 40px' }}>
            Entregamos tus repuestos donde estés en todo el Perú
          </p>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', maxWidth: '600px', margin: '0 auto' }}>
            {[
              { icono: '🏙️', titulo: 'Local — Ayacucho',   desc: 'Entrega el mismo día o al día siguiente dentro de la ciudad.' },
              { icono: '🗺️', titulo: 'Interprovincial',     desc: 'Envíos a Lima, Ica, Cusco, Huancavelica y más.' },
            ].map(d => (
              <div key={d.titulo} style={{
                background: 'rgba(255,255,255,0.08)', backdropFilter: 'blur(10px)',
                border: '1px solid rgba(255,255,255,0.15)',
                borderRadius: '16px', padding: '28px 24px', textAlign: 'left'
              }}>
                <div style={{ fontSize: '32px', marginBottom: '12px' }}>{d.icono}</div>
                <div style={{ color: '#29B6F6', fontWeight: '700', fontSize: '16px',
                              marginBottom: '8px' }}>{d.titulo}</div>
                <div style={{ color: '#94a3b8', fontSize: '14px', lineHeight: '1.6' }}>{d.desc}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* WhatsApp flotante */}
      <a href={WHATSAPP_URL} target="_blank" rel="noreferrer" style={{
        position: 'fixed', bottom: '24px', right: '24px', zIndex: 999,
        background: '#25D366', color: '#fff', textDecoration: 'none',
        borderRadius: '50px', padding: '14px 20px',
        display: 'flex', alignItems: 'center', gap: '8px',
        boxShadow: '0 4px 20px rgba(37,211,102,0.4)',
        fontSize: '14px', fontWeight: '700', transition: 'all 0.2s'
      }}>
        💬 WhatsApp
      </a>
    </main>
  )
}
