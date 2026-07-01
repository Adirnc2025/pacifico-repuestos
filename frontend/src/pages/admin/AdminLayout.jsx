import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

const menu = [
  { to: '/admin',           label: '📊 Dashboard',    end: true },
  { to: '/admin/productos', label: '📦 Productos'              },
  { to: '/admin/catalogo',  label: '🚗 Catálogo'               },
  { to: '/admin/pedidos',   label: '🛒 Pedidos'                },
  { to: '/admin/reportes',  label: '📈 Reportes'               },
  { to: '/admin/clientes',  label: '👥 Clientes'               },
]

export default function AdminLayout() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  return (
    <div className="min-h-screen bg-gray-50 flex">
      <aside className="w-56 bg-primary text-white flex-shrink-0 flex flex-col">
        <div className="p-5 border-b border-white/10">
          <div className="font-bold text-lg">Pacífico Repuestos</div>
          <div className="text-blue-200 text-xs mt-0.5">Panel Admin</div>
        </div>
        <nav className="flex-1 p-3 space-y-1">
          {menu.map(({ to, label, end }) => (
            <NavLink key={to} to={to} end={end}
              className={({ isActive }) =>
                `flex items-center px-3 py-2.5 rounded-lg text-sm transition-colors
                 ${isActive ? 'bg-white/20 font-semibold' : 'hover:bg-white/10 text-blue-100'}`}>
              {label}
            </NavLink>
          ))}
        </nav>
        <div className="p-4 border-t border-white/10">
          <p className="text-xs text-blue-200 mb-2 truncate">{usuario?.nombre}</p>
          <button onClick={() => { logout(); navigate('/') }}
            className="text-xs bg-white/10 hover:bg-white/20 px-3 py-1.5
                       rounded-lg w-full transition-colors">
            Cerrar sesión
          </button>
        </div>
      </aside>
      <main className="flex-1 p-6 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
