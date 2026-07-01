export const WHATSAPP_NUMBER = '51981869554'
export const WHATSAPP_MSG    = 'Hola, quisiera consultar sobre repuestos automotrices.'
export const WHATSAPP_URL    = `https://wa.me/${WHATSAPP_NUMBER}?text=${encodeURIComponent(WHATSAPP_MSG)}`

export const CONTACTO = {
  telefono:   '981 869 554',
  whatsapp:   '981 869 554',
  correo:     'info@pacificorepuestos.com',
  horario:    'Lun–Sáb: 8am – 7pm',
  locales: [
    {
      nombre:    'Local 1 — Centro',
      direccion: 'Jr. Protzel N° 114',
      ciudad:    'Ayacucho, Perú'
    },
    {
      nombre:    'Local 2 — Mariscal',
      direccion: 'Av. Mariscal Cáceres N° 258',
      ciudad:    'Ayacucho, Perú'
    }
  ]
}

export const ESTADOS_PEDIDO = {
  PENDIENTE:      { label: 'Pendiente',      color: 'bg-yellow-100 text-yellow-800' },
  CONFIRMADO:     { label: 'Confirmado',     color: 'bg-blue-100 text-blue-800'     },
  EN_PREPARACION: { label: 'En preparación', color: 'bg-purple-100 text-purple-800' },
  ENVIADO:        { label: 'Enviado',        color: 'bg-orange-100 text-orange-800' },
  ENTREGADO:      { label: 'Entregado',      color: 'bg-green-100 text-green-800'   },
  CANCELADO:      { label: 'Cancelado',      color: 'bg-red-100 text-red-800'       },
}
