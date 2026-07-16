import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCarrito } from '../context/CarritoContext'
import { useAuth } from '../context/AuthContext'
import { pedidoService } from '../services/pedidoService'

export default function Checkout() {
  const { items, total, vaciar } = useCarrito()
  const { usuario } = useAuth()
  const navigate = useNavigate()

  const [zonas,    setZonas]    = useState([])
  const [form,     setForm]     = useState({
    tipoDelivery: 'RECOJO', zonaId: '', direccionEntrega: '', telefono: '', observacion: ''
  })
  const [cargando, setCargando] = useState(false)
  const [error,    setError]    = useState('')

  useEffect(() => {
    if (!usuario) { navigate('/login'); return }
    if (items.length === 0) { navigate('/carrito'); return }
    pedidoService.getZonas().then(r => setZonas(r.data))
  }, [])

  const zonaSeleccionada = zonas.find(z => z.id === parseInt(form.zonaId))
  const costoDelivery    = zonaSeleccionada ? zonaSeleccionada.tarifa : 0
  const totalFinal       = total + Number(costoDelivery)

  const handleSubmit = async () => {
    if (form.tipoDelivery !== 'RECOJO' && !form.direccionEntrega.trim()) {
      setError('Ingresa la dirección de entrega')
      return
    }
    const telefonoLimpio = form.telefono.replace(/\s/g, '')
    if (!/^\d{9,15}$/.test(telefonoLimpio)) {
      setError('Ingresa un teléfono de contacto válido (solo números, entre 9 y 15 dígitos)')
      return
    }
    setCargando(true)
    setError('')
    try {
      // El backend no tiene un campo dedicado para el teléfono del pedido,
      // así que se antepone a las observaciones para no tocar el backend.
      const observacionConTelefono = `Tel: ${telefonoLimpio}` +
        (form.observacion.trim() ? ` - ${form.observacion.trim()}` : '')

      const payload = {
        items: items.map(i => ({ productoId: i.id, cantidad: i.cantidad })),
        tipoDelivery:     form.tipoDelivery,
        direccionEntrega: form.direccionEntrega,
        zonaId:           form.zonaId ? parseInt(form.zonaId) : null,
        observacion:      observacionConTelefono,
      }
      const { data } = await pedidoService.crear(payload)
      vaciar()
      navigate(`/pedido-confirmado/${data.id}`, { state: { pedido: data } })
    } catch (e) {
      setError(e.response?.data?.error || 'Error al procesar el pedido')
    } finally { setCargando(false) }
  }

  const tiposDelivery = [
    { value:'RECOJO',          label:'🏪 Recoger en tienda',    desc:'Gratis — Ayacucho' },
    { value:'LOCAL',           label:'🛵 Delivery local',        desc:'Ayacucho ciudad'   },
    { value:'INTERPROVINCIAL', label:'🚚 Delivery interprovincial', desc:'Lima, Ica, Cusco…' },
  ]

  return (
    <main className="max-w-5xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Confirmar pedido</h1>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 rounded-lg
                        px-4 py-3 text-sm mb-4">{error}</div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Formulario */}
        <div className="lg:col-span-2 space-y-5">
          {/* Tipo de delivery */}
          <div className="card">
            <h2 className="font-semibold text-gray-900 mb-3">Tipo de entrega</h2>
            <div className="space-y-2">
              {tiposDelivery.map(t => (
                <label key={t.value}
                  className={`flex items-center gap-3 p-3 border-2 rounded-lg cursor-pointer transition-colors
                    ${form.tipoDelivery === t.value
                      ? 'border-primary bg-primary/5'
                      : 'border-gray-200 hover:border-gray-300'}`}>
                  <input type="radio" name="tipo" value={t.value}
                    checked={form.tipoDelivery === t.value}
                    onChange={e => setForm({...form, tipoDelivery: e.target.value, zonaId:''})}
                    className="text-primary" />
                  <div>
                    <div className="font-medium text-sm">{t.label}</div>
                    <div className="text-xs text-gray-500">{t.desc}</div>
                  </div>
                </label>
              ))}
            </div>
          </div>

          {/* Zona y dirección */}
          {form.tipoDelivery !== 'RECOJO' && (
            <div className="card space-y-3">
              <h2 className="font-semibold text-gray-900">Datos de entrega</h2>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Zona de delivery
                </label>
                <select value={form.zonaId}
                  onChange={e => setForm({...form, zonaId: e.target.value})}
                  className="input-field">
                  <option value="">-- Selecciona zona --</option>
                  {zonas
                    .filter(z => form.tipoDelivery === 'LOCAL'
                      ? z.tipo === 'LOCAL' : z.tipo === 'INTERPROVINCIAL')
                    .map(z => (
                      <option key={z.id} value={z.id}>
                        {z.nombre} — S/ {Number(z.tarifa).toFixed(2)}
                      </option>
                    ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Dirección de entrega
                </label>
                <input type="text" value={form.direccionEntrega}
                  onChange={e => setForm({...form, direccionEntrega: e.target.value})}
                  placeholder="Av. Principal 123, Dist., Ciudad"
                  className="input-field" />
              </div>
            </div>
          )}

          {/* Teléfono de contacto */}
          <div className="card">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Teléfono de contacto
            </label>
            <input type="tel" value={form.telefono}
              onChange={e => setForm({...form, telefono: e.target.value.replace(/[^\d\s]/g, '')})}
              placeholder="Ej: 981 869 554"
              required
              className="input-field" />
          </div>

          {/* Observaciones */}
          <div className="card">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Observaciones (opcional)
            </label>
            <textarea rows={2} value={form.observacion}
              onChange={e => setForm({...form, observacion: e.target.value})}
              placeholder="Horario preferido, referencias, etc."
              className="input-field" />
          </div>
        </div>

        {/* Resumen del pedido */}
        <div className="card h-fit">
          <h2 className="font-bold text-gray-900 mb-4">Tu pedido</h2>
          <div className="space-y-2 text-sm mb-4">
            {items.map(i => (
              <div key={i.id} className="flex justify-between text-gray-600">
                <span className="truncate mr-2">{i.nombre} x{i.cantidad}</span>
                <span>S/ {(i.precio * i.cantidad).toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="border-t border-gray-200 pt-3 space-y-1 text-sm">
            <div className="flex justify-between text-gray-600">
              <span>Subtotal</span>
              <span>S/ {total.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-gray-600">
              <span>Delivery</span>
              <span>{costoDelivery > 0 ? `S/ ${Number(costoDelivery).toFixed(2)}` : 'Gratis'}</span>
            </div>
            <div className="flex justify-between font-bold text-base pt-1 border-t border-gray-200 mt-1">
              <span>Total</span>
              <span className="text-primary">S/ {totalFinal.toFixed(2)}</span>
            </div>
          </div>
          <p className="text-xs text-gray-400 mt-3 mb-4">
            💳 El pago se coordina por WhatsApp o en tienda
          </p>
          <button onClick={handleSubmit} disabled={cargando}
            className="btn-primary w-full py-3">
            {cargando ? 'Procesando...' : 'Confirmar pedido'}
          </button>
        </div>
      </div>
    </main>
  )
}
