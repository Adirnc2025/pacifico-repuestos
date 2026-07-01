import { useLocation, Link } from 'react-router-dom'
import { WHATSAPP_URL } from '../utils/constants'

export default function PedidoConfirmado() {
  const { state } = useLocation()
  const pedido    = state?.pedido

  return (
    <main className="max-w-2xl mx-auto px-4 py-16 text-center">
      <div className="text-6xl mb-4">✅</div>
      <h1 className="text-3xl font-bold text-gray-900 mb-2">¡Pedido confirmado!</h1>
      {pedido && (
        <div className="bg-primary/5 border border-primary/20 rounded-xl p-5 my-6 text-left">
          <div className="flex justify-between text-sm mb-2">
            <span className="text-gray-600">Número de pedido</span>
            <span className="font-bold text-primary">{pedido.numeroPedido}</span>
          </div>
          <div className="flex justify-between text-sm mb-2">
            <span className="text-gray-600">Estado</span>
            <span className="bg-yellow-100 text-yellow-800 px-2 py-0.5 rounded-full
                             text-xs font-medium">Pendiente</span>
          </div>
          <div className="flex justify-between text-sm mb-2">
            <span className="text-gray-600">Total</span>
            <span className="font-bold">S/ {Number(pedido.total).toFixed(2)}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Entrega</span>
            <span>{pedido.tipoDelivery === 'RECOJO'
              ? 'Recojo en tienda' : pedido.direccionEntrega}</span>
          </div>
        </div>
      )}
      <p className="text-gray-500 text-sm mb-8">
        Nos contactaremos contigo pronto para confirmar el pago y coordinar la entrega.
      </p>
      <div className="flex flex-col sm:flex-row gap-3 justify-center">
        <a href={WHATSAPP_URL} target="_blank" rel="noopener noreferrer"
           className="bg-green-500 hover:bg-green-600 text-white px-6 py-3
                      rounded-lg font-medium transition-colors">
          Coordinar por WhatsApp
        </a>
        <Link to="/mis-pedidos"
          className="border-2 border-primary text-primary px-6 py-3 rounded-lg
                     font-medium hover:bg-primary/5 transition-colors">
          Ver mis pedidos
        </Link>
      </div>
    </main>
  )
}
