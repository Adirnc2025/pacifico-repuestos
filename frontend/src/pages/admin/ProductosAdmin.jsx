import { useState, useEffect } from 'react'
import { productoService, catalogoService } from '../../services/catalogoService'

export default function ProductosAdmin() {
  const [productos,  setProductos]  = useState([])
  const [categorias, setCategorias] = useState([])
  const [modal,      setModal]      = useState(false)
  const [editando,   setEditando]   = useState(null)
  const [form,       setForm]       = useState({
    codigo:'', nombre:'', descripcion:'', precio:'',
    medidas:'', stock:0, categoriaId:'', destacado:false
  })
  const [msg, setMsg] = useState({ texto:'', tipo:'' })

  useEffect(() => {
    cargar()
    catalogoService.getCategorias().then(r => setCategorias(r.data))
  }, [])

  const cargar = () =>
    productoService.listar({}).then(r => setProductos(r.data))

  const abrirCrear = () => {
    setEditando(null)
    setForm({ codigo:'', nombre:'', descripcion:'', precio:'',
              medidas:'', stock:0, categoriaId:'', destacado:false })
    setModal(true)
  }

  const abrirEditar = (p) => {
    setEditando(p)
    setForm({ codigo:p.codigo, nombre:p.nombre, descripcion:p.descripcion||'',
              precio:p.precio, medidas:p.medidas||'', stock:p.stock,
              categoriaId:p.categoriaId||'', destacado:p.destacado })
    setModal(true)
  }

  const guardar = async () => {
    try {
      const data = { ...form, precio: parseFloat(form.precio),
                     stock: parseInt(form.stock),
                     categoriaId: form.categoriaId || null }
      if (editando) await productoService.editar(editando.id, data)
      else          await productoService.crear(data)
      setModal(false)
      setMsg({ texto: editando ? 'Producto actualizado' : 'Producto creado', tipo:'ok' })
      cargar()
    } catch(e) {
      setMsg({ texto: e.response?.data?.error || 'Error al guardar', tipo:'err' })
    }
    setTimeout(() => setMsg({ texto:'', tipo:'' }), 3000)
  }

  const eliminar = async (id) => {
    if (!confirm('¿Eliminar este producto?')) return
    try {
      await productoService.eliminar(id)
      setMsg({ texto:'Producto eliminado', tipo:'ok' })
      cargar()
    } catch(e) {
      setMsg({ texto: e.response?.data?.error || 'Error al eliminar', tipo:'err' })
    }
    setTimeout(() => setMsg({ texto:'', tipo:'' }), 3000)
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-gray-900">Productos</h2>
        <button onClick={abrirCrear} className="btn-primary text-sm px-4 py-2">
          + Nuevo producto
        </button>
      </div>

      {msg.texto && (
        <div className={`mb-4 px-4 py-3 rounded-lg text-sm font-medium
          ${msg.tipo==='ok' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
          {msg.texto}
        </div>
      )}

      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="bg-gray-50">
            <tr>
              {['Código','Nombre','Precio','Stock','Categoría','Destacado','Acciones'].map(h => (
                <th key={h} className="text-left px-4 py-3 font-semibold text-gray-700">{h}</th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {productos.map(p => (
              <tr key={p.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 font-mono text-xs">{p.codigo}</td>
                <td className="px-4 py-3 font-medium max-w-xs truncate">{p.nombre}</td>
                <td className="px-4 py-3 text-primary font-semibold">
                  S/ {Number(p.precio).toFixed(2)}
                </td>
                <td className="px-4 py-3">
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium
                    ${p.stock > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {p.stock}
                  </span>
                </td>
                <td className="px-4 py-3 text-gray-500">{p.categoria || '—'}</td>
                <td className="px-4 py-3">{p.destacado ? '⭐' : '—'}</td>
                <td className="px-4 py-3">
                  <div className="flex gap-2">
                    <button onClick={() => abrirEditar(p)}
                      className="text-primary hover:underline text-xs font-medium">Editar</button>
                    <button onClick={() => eliminar(p.id)}
                      className="text-red-500 hover:underline text-xs font-medium">Eliminar</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Modal */}
      {modal && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <h3 className="text-lg font-bold mb-4">
                {editando ? 'Editar producto' : 'Nuevo producto'}
              </h3>
              <div className="space-y-3">
                {[
                  { k:'codigo',      label:'Código',      type:'text'   },
                  { k:'nombre',      label:'Nombre',      type:'text'   },
                  { k:'precio',      label:'Precio (S/)', type:'number' },
                  { k:'stock',       label:'Stock',       type:'number' },
                  { k:'medidas',     label:'Medidas',     type:'text'   },
                ].map(({ k, label, type }) => (
                  <div key={k}>
                    <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
                    <input type={type} value={form[k]}
                      onChange={e => setForm({...form, [k]: e.target.value})}
                      className="input-field" />
                  </div>
                ))}

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Categoría</label>
                  <select value={form.categoriaId}
                    onChange={e => setForm({...form, categoriaId: e.target.value})}
                    className="input-field">
                    <option value="">-- Sin categoría --</option>
                    {categorias.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
                  <textarea value={form.descripcion} rows={3}
                    onChange={e => setForm({...form, descripcion: e.target.value})}
                    className="input-field" />
                </div>

                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" checked={form.destacado}
                    onChange={e => setForm({...form, destacado: e.target.checked})}
                    className="w-4 h-4 text-primary rounded" />
                  <span className="text-sm font-medium text-gray-700">Producto destacado</span>
                </label>
              </div>

              <div className="flex gap-3 mt-6">
                <button onClick={() => setModal(false)}
                  className="flex-1 border border-gray-300 text-gray-700 rounded-lg py-2
                             hover:bg-gray-50 transition-colors text-sm">
                  Cancelar
                </button>
                <button onClick={guardar} className="flex-1 btn-primary py-2 text-sm">
                  {editando ? 'Guardar cambios' : 'Crear producto'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
