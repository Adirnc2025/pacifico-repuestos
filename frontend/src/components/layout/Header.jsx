import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useCarrito } from '../../context/CarritoContext'

export default function Header() {
  const { usuario, logout } = useAuth()
  const { cantidad } = useCarrito()
  const navigate = useNavigate()
  const [busqueda, setBusqueda] = useState('')

  const handleBuscar = (e) => {
    e.preventDefault()
    if (busqueda.trim()) navigate(`/productos?q=${busqueda}`)
  }

  return (
    <header style={{ fontFamily: 'Inter, sans-serif', position: 'sticky', top: 0, zIndex: 100 }}>

      {/* TOP BAR */}
      <div style={{ background: '#0a0f1e', color: '#94a3b8', fontSize: '12px', padding: '7px 0' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 20px',
                      display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', gap: '24px' }}>
            <span>📍 Jr. Protzel N° 114, Ayacucho</span>
            <span style={{ color: '#334155' }}>|</span>
            <span>📍 Av. Mariscal Cáceres N° 258, Ayacucho</span>
          </div>
          <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
            <span>📞 981 869 554</span>
            <a href="https://wa.me/51981869554" target="_blank" rel="noreferrer"
               style={{ color: '#25D366', textDecoration: 'none', fontWeight: '600' }}>WhatsApp</a>
            <span style={{ color: '#334155' }}>|</span>
            <span style={{ cursor: 'pointer' }}>Facebook</span>
            <span style={{ cursor: 'pointer' }}>Instagram</span>
          </div>
        </div>
      </div>

      {/* MAIN HEADER */}
      <div style={{ background: '#fff', borderBottom: '1px solid #e2e8f0', padding: '14px 0' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 20px',
                      display: 'flex', alignItems: 'center', gap: '24px' }}>

          {/* LOGO */}
          <Link to="/" style={{ textDecoration: 'none', flexShrink: 0 }}>
            <img
              src="/images/logo/logo.png"
              alt="Pacífico Repuestos"
              style={{ height: '56px', width: 'auto', objectFit: 'contain' }}
            />
          </Link>

          {/* BUSCADOR */}
          <form onSubmit={handleBuscar} style={{ flex: 1, display: 'flex' }}>
            <input
              type="text"
              value={busqueda}
              onChange={e => setBusqueda(e.target.value)}
              placeholder="Buscar repuestos, marcas, modelos, motores..."
              style={{
                flex: 1, padding: '13px 20px', fontSize: '14px',
                border: '2px solid #cbd5e1', borderRight: 'none',
                borderRadius: '8px 0 0 8px', outline: 'none',
                background: '#f8fafc', color: '#1e293b',
                transition: 'border-color 0.2s'
              }}
              onFocus={e => e.target.style.borderColor = '#0057A8'}
              onBlur={e => e.target.style.borderColor = '#cbd5e1'}
            />
            <button type="submit" style={{
              background: '#0057A8', color: '#fff', border: 'none',
              padding: '13px 22px', borderRadius: '0 8px 8px 0',
              cursor: 'pointer', fontSize: '16px', fontWeight: '600',
              transition: 'background 0.2s'
            }}
            onMouseEnter={e => e.target.style.background = '#003d78'}
            onMouseLeave={e => e.target.style.background = '#0057A8'}>
              🔍
            </button>
          </form>

          {/* CARRITO */}
          <Link to="/carrito" style={{ textDecoration: 'none', position: 'relative' }}>
            <div style={{
              background: '#f1f5f9', border: '2px solid #e2e8f0',
              borderRadius: '8px', padding: '10px 16px',
              display: 'flex', alignItems: 'center', gap: '8px', color: '#0f172a',
              fontSize: '13px', fontWeight: '600', transition: 'all 0.2s', cursor: 'pointer'
            }}
            onMouseEnter={e => e.currentTarget.style.borderColor = '#0057A8'}
            onMouseLeave={e => e.currentTarget.style.borderColor = '#e2e8f0'}>
              <span style={{ fontSize: '20px' }}>🛒</span>
              <span style={{ color: '#64748b', fontSize: '12px' }}>
                {cantidad > 0 ? `${cantidad} items` : 'Carrito'}
              </span>
              {cantidad > 0 && (
                <span style={{
                  position: 'absolute', top: '-8px', right: '-8px',
                  background: '#29B6F6', color: '#fff', borderRadius: '50%',
                  width: '22px', height: '22px', fontSize: '11px', fontWeight: '800',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  border: '2px solid #fff'
                }}>{cantidad}</span>
              )}
            </div>
          </Link>

          {/* INGRESAR */}
          {usuario ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', flexShrink: 0 }}>
              {usuario.rol === 'ADMIN' && (
                <Link to="/admin" style={{
                  color: '#0057A8', textDecoration: 'none', fontSize: '13px', fontWeight: '600',
                  padding: '8px 14px', border: '2px solid #0057A8', borderRadius: '8px'
                }}>Panel Admin</Link>
              )}
              <button onClick={logout} style={{
                background: '#0057A8', color: '#fff', border: 'none',
                padding: '11px 18px', borderRadius: '8px', cursor: 'pointer',
                fontSize: '13px', fontWeight: '600'
              }}>Salir</button>
            </div>
          ) : (
            <Link to="/login" style={{ textDecoration: 'none', flexShrink: 0 }}>
              <button style={{
                background: '#0057A8', color: '#fff', border: 'none',
                padding: '12px 24px', borderRadius: '8px', cursor: 'pointer',
                fontSize: '14px', fontWeight: '700', display: 'flex',
                alignItems: 'center', gap: '8px',
                boxShadow: '0 2px 8px rgba(0,87,168,0.3)', transition: 'all 0.2s'
              }}
              onMouseEnter={e => e.currentTarget.style.background = '#003d78'}
              onMouseLeave={e => e.currentTarget.style.background = '#0057A8'}>
                👤 Ingresar
              </button>
            </Link>
          )}
        </div>
      </div>

      {/* NAV BAR */}
      <nav style={{ background: '#0057A8', boxShadow: '0 2px 8px rgba(0,0,0,0.2)' }}>
        <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '0 20px',
                      display: 'flex', alignItems: 'center' }}>
          {[
            { to: '/',                           icon: '🏠', label: 'Inicio'     },
            { to: '/productos',                  icon: '📦', label: 'Catálogo'   },
            { to: '/productos?vista=categorias', icon: '≡',  label: 'Categorías' },
            { to: '/productos?vista=marcas',     icon: '🏷', label: 'Marcas'     },
            { to: '/productos?destacado=true',   icon: '🔥', label: 'Ofertas',   badge: 'HOT' },
            { to: '/contacto',                   icon: '✉',  label: 'Contacto'  },
          ].map(({ to, icon, label, badge }) => (
            <Link key={label} to={to} style={{
              color: '#bfdbfe', textDecoration: 'none',
              padding: '12px 16px', fontSize: '13px', fontWeight: '500',
              display: 'flex', alignItems: 'center', gap: '6px',
              borderBottom: '3px solid transparent', transition: 'all 0.15s',
              whiteSpace: 'nowrap'
            }}
            onMouseEnter={e => {
              e.currentTarget.style.background = 'rgba(255,255,255,0.1)'
              e.currentTarget.style.color = '#fff'
            }}
            onMouseLeave={e => {
              e.currentTarget.style.background = 'transparent'
              e.currentTarget.style.color = '#bfdbfe'
            }}>
              <span>{icon}</span>
              <span>{label}</span>
              {badge && (
                <span style={{
                  background: '#ef4444', color: '#fff', fontSize: '9px',
                  fontWeight: '800', padding: '1px 5px', borderRadius: '4px',
                  letterSpacing: '0.5px'
                }}>{badge}</span>
              )}
            </Link>
          ))}
        </div>
      </nav>
    </header>
  )
}
