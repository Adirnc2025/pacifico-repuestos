import { createContext, useContext, useState } from 'react'

const CarritoContext = createContext(null)

export function CarritoProvider({ children }) {
  const [items, setItems] = useState([])

  const agregar = (producto, cantidad = 1) => {
    setItems(prev => {
      const existe = prev.find(i => i.id === producto.id)
      if (existe) {
        return prev.map(i =>
          i.id === producto.id
            ? { ...i, cantidad: Math.min(i.cantidad + cantidad, i.stock) }
            : i
        )
      }
      return [...prev, { ...producto, cantidad }]
    })
  }

  const actualizar = (id, cantidad) => {
    if (cantidad <= 0) return eliminar(id)
    setItems(prev => prev.map(i => i.id === id ? { ...i, cantidad } : i))
  }

  const eliminar = (id) => setItems(prev => prev.filter(i => i.id !== id))

  const vaciar = () => setItems([])

  const total = items.reduce((sum, i) => sum + i.precio * i.cantidad, 0)
  const cantidad = items.reduce((sum, i) => sum + i.cantidad, 0)

  return (
    <CarritoContext.Provider value={{ items, agregar, actualizar, eliminar, vaciar, total, cantidad }}>
      {children}
    </CarritoContext.Provider>
  )
}

export const useCarrito = () => useContext(CarritoContext)
