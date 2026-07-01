import { Link } from 'react-router-dom'
import { CONTACTO, WHATSAPP_URL } from '../../utils/constants'

export default function Footer() {
  return (
    <footer style={{ fontFamily: 'Inter, sans-serif', background: '#0a0f1e', color: '#94a3b8' }}>

      {/* CUERPO DEL FOOTER */}
      <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '60px 20px 40px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr 1.5fr', gap: '40px' }}>

          {/* Columna 1 — Logo y descripción */}
          <div>
            <img
              src="/images/logo/logo.png"
              alt="Pacífico Repuestos"
              style={{ height: '42px', width: 'auto', objectFit: 'contain', marginBottom: '16px' }}
            />
            <p style={{ fontSize: '14px', lineHeight: '1.7', margin: '0 0 20px', color: '#64748b' }}>
              Tienda especializada en repuestos automotrices en Ayacucho, Perú.
              Calidad garantizada y entrega local e interprovincial a todo el Perú.
            </p>
            {/* Redes sociales */}
            <div style={{ display: 'flex', gap: '10px' }}>
              {['Facebook', 'Instagram', 'WhatsApp'].map(red => (
                <a key={red} href={red === 'WhatsApp' ? WHATSAPP_URL : '#'}
                   target="_blank" rel="noreferrer" style={{
                  background: '#1e293b', border: '1px solid #334155',
                  borderRadius: '8px', padding: '8px 12px',
                  color: '#94a3b8', textDecoration: 'none', fontSize: '12px',
                  transition: 'all 0.2s'
                }}
                onMouseEnter={e => {
                  e.currentTarget.style.borderColor = '#29B6F6'
                  e.currentTarget.style.color = '#29B6F6'
                }}
                onMouseLeave={e => {
                  e.currentTarget.style.borderColor = '#334155'
                  e.currentTarget.style.color = '#94a3b8'
                }}>
                  {red}
                </a>
              ))}
            </div>
          </div>

          {/* Columna 2 — Links rápidos */}
          <div>
            <h4 style={{ color: '#fff', fontWeight: '700', fontSize: '14px',
                         marginBottom: '20px', letterSpacing: '1px' }}>
              NAVEGACIÓN
            </h4>
            <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {[
                { to: '/',          label: 'Inicio'     },
                { to: '/productos', label: 'Catálogo'   },
                { to: '/productos', label: 'Categorías' },
                { to: '/productos', label: 'Marcas'     },
                { to: '/mis-pedidos', label: 'Mis pedidos' },
              ].map(({ to, label }) => (
                <li key={label}>
                  <Link to={to} style={{
                    color: '#64748b', textDecoration: 'none', fontSize: '14px',
                    transition: 'color 0.2s', display: 'flex', alignItems: 'center', gap: '6px'
                  }}
                  onMouseEnter={e => e.target.style.color = '#29B6F6'}
                  onMouseLeave={e => e.target.style.color = '#64748b'}>
                    → {label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Columna 3 — Horarios */}
          <div>
            <h4 style={{ color: '#fff', fontWeight: '700', fontSize: '14px',
                         marginBottom: '20px', letterSpacing: '1px' }}>
              HORARIOS
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {[
                { dia: 'Lunes – Viernes', hora: '8:00 AM – 7:00 PM' },
                { dia: 'Sábados',         hora: '8:00 AM – 6:00 PM' },
                { dia: 'Domingos',        hora: '9:00 AM – 1:00 PM' },
              ].map(h => (
                <div key={h.dia}>
                  <div style={{ color: '#94a3b8', fontSize: '12px' }}>{h.dia}</div>
                  <div style={{ color: '#e2e8f0', fontSize: '14px', fontWeight: '600' }}>{h.hora}</div>
                </div>
              ))}
            </div>
          </div>

          {/* Columna 4 — Contacto y locales */}
          <div>
            <h4 style={{ color: '#fff', fontWeight: '700', fontSize: '14px',
                         marginBottom: '20px', letterSpacing: '1px' }}>
              NUESTROS LOCALES
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>

              {/* Local 1 */}
              <div style={{
                background: '#1e293b', borderRadius: '10px', padding: '14px',
                border: '1px solid #334155'
              }}>
                <div style={{ color: '#29B6F6', fontSize: '11px', fontWeight: '700',
                              letterSpacing: '0.5px', marginBottom: '6px' }}>
                  📍 LOCAL 1 — CENTRO
                </div>
                <div style={{ color: '#e2e8f0', fontSize: '14px', fontWeight: '600' }}>
                  {CONTACTO.locales[0].direccion}
                </div>
                <div style={{ color: '#64748b', fontSize: '12px' }}>Ayacucho, Perú</div>
              </div>

              {/* Local 2 */}
              <div style={{
                background: '#1e293b', borderRadius: '10px', padding: '14px',
                border: '1px solid #334155'
              }}>
                <div style={{ color: '#29B6F6', fontSize: '11px', fontWeight: '700',
                              letterSpacing: '0.5px', marginBottom: '6px' }}>
                  📍 LOCAL 2 — MARISCAL
                </div>
                <div style={{ color: '#e2e8f0', fontSize: '14px', fontWeight: '600' }}>
                  {CONTACTO.locales[1].direccion}
                </div>
                <div style={{ color: '#64748b', fontSize: '12px' }}>Ayacucho, Perú</div>
              </div>

              {/* Teléfono y WhatsApp */}
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <a href={`tel:+${CONTACTO.telefono.replace(/\s/g,'')}`} style={{
                  color: '#94a3b8', textDecoration: 'none', fontSize: '14px',
                  display: 'flex', alignItems: 'center', gap: '8px'
                }}>
                  📞 {CONTACTO.telefono}
                </a>
                <a href={WHATSAPP_URL} target="_blank" rel="noreferrer" style={{
                  color: '#25D366', textDecoration: 'none', fontSize: '14px',
                  fontWeight: '600', display: 'flex', alignItems: 'center', gap: '8px'
                }}>
                  💬 WhatsApp: {CONTACTO.whatsapp}
                </a>
                <a href={`mailto:${CONTACTO.correo}`} style={{
                  color: '#94a3b8', textDecoration: 'none', fontSize: '13px',
                  display: 'flex', alignItems: 'center', gap: '8px'
                }}>
                  ✉ {CONTACTO.correo}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* BARRA INFERIOR */}
      <div style={{ borderTop: '1px solid #1e293b', padding: '20px' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto',
                      display: 'flex', justifyContent: 'space-between',
                      alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
          <div style={{ fontSize: '13px', color: '#475569' }}>
            © 2025 Pacífico Repuestos — Ayacucho, Perú. Todos los derechos reservados.
          </div>
          <div style={{ display: 'flex', gap: '8px' }}>
            {['Visa', 'Mastercard', 'Yape', 'Plin'].map(p => (
              <div key={p} style={{
                background: '#1e293b', border: '1px solid #334155',
                borderRadius: '6px', padding: '4px 10px',
                color: '#64748b', fontSize: '11px', fontWeight: '600'
              }}>{p}</div>
            ))}
          </div>
        </div>
      </div>
    </footer>
  )
}
