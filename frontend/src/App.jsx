import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider }    from './context/AuthContext'
import { CarritoProvider } from './context/CarritoContext'
import Header           from './components/layout/Header'
import Footer           from './components/layout/Footer'
import BotonWhatsApp    from './components/common/BotonWhatsApp'
import ProtectedRoute   from './components/common/ProtectedRoute'
import Home             from './pages/Home'
import Productos        from './pages/Productos'
import ProductoDetalle  from './pages/ProductoDetalle'
import Login            from './pages/Login'
import Registro         from './pages/Registro'
import Carrito          from './pages/Carrito'
import Checkout         from './pages/Checkout'
import PedidoConfirmado from './pages/PedidoConfirmado'
import MisPedidos       from './pages/MisPedidos'
import AdminLayout      from './pages/admin/AdminLayout'
import Dashboard        from './pages/admin/Dashboard'
import ProductosAdmin   from './pages/admin/ProductosAdmin'
import PedidosAdmin     from './pages/admin/PedidosAdmin'
import CatalogoAdmin    from './pages/admin/CatalogoAdmin'
import ReportesAdmin    from './pages/admin/ReportesAdmin'
import ClientesAdmin    from './pages/admin/ClientesAdmin'

const Layout = ({ children }) => (
  <><Header />{children}<Footer /><BotonWhatsApp /></>
)

const Placeholder = ({ t }) => (
  <div className="py-8 text-center text-gray-400">
    <div className="text-4xl mb-3">🔧</div>
    <p className="font-medium">{t}</p>
  </div>
)

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CarritoProvider>
          <Routes>
            {/* ── Públicas ── */}
            <Route path="/"              element={<Layout><Home /></Layout>} />
            <Route path="/productos"     element={<Layout><Productos /></Layout>} />
            <Route path="/productos/:id" element={<Layout><ProductoDetalle /></Layout>} />
            <Route path="/carrito"       element={<Layout><Carrito /></Layout>} />
            <Route path="/login"         element={<Login />} />
            <Route path="/registro"      element={<Registro />} />

            {/* ── Cliente autenticado ── */}
            <Route path="/checkout" element={
              <ProtectedRoute><Layout><Checkout /></Layout></ProtectedRoute>
            }/>
            <Route path="/pedido-confirmado/:id" element={
              <ProtectedRoute><Layout><PedidoConfirmado /></Layout></ProtectedRoute>
            }/>
            <Route path="/mis-pedidos" element={
              <ProtectedRoute><Layout><MisPedidos /></Layout></ProtectedRoute>
            }/>

            {/* ── Admin ── */}
            <Route path="/admin" element={
              <ProtectedRoute soloAdmin><AdminLayout /></ProtectedRoute>
            }>
              <Route index            element={<Dashboard />} />
              <Route path="productos" element={<ProductosAdmin />} />
              <Route path="pedidos"   element={<PedidosAdmin />} />
              <Route path="catalogo"  element={<CatalogoAdmin />} />
              <Route path="reportes"  element={<ReportesAdmin />} />
              <Route path="clientes"  element={<ClientesAdmin />} />
            </Route>

            {/* ── 404 ── */}
            <Route path="*" element={
              <Layout>
                <div className="max-w-7xl mx-auto px-4 py-16 text-center">
                  <h1 className="text-6xl font-bold text-gray-200 mb-4">404</h1>
                  <p className="text-gray-500">Página no encontrada</p>
                  <a href="/" className="btn-primary inline-block mt-6">Volver al inicio</a>
                </div>
              </Layout>
            }/>
          </Routes>
        </CarritoProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}
