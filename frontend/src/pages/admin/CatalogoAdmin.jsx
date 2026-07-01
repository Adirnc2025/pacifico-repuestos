import { useState, useEffect } from 'react'
import { catalogoService } from '../../services/catalogoService'
import api from '../../utils/axiosConfig'

export default function CatalogoAdmin() {
  const [marcas,      setMarcas]      = useState([])
  const [modelos,     setModelos]     = useState([])
  const [generaciones,setGeneraciones]= useState([])
  const [motores,     setMotores]     = useState([])
  const [sel, setSel] = useState({ marcaId:'', modeloId:'', generacionId:'' })
  const [form, setForm] = useState({ nombre:'', extra:'' })
  const [modo, setModo] = useState('')   // 'marca'|'modelo'|'generacion'|'motor'
  const [msg,  setMsg]  = useState({ texto:'', tipo:'' })

  useEffect(() => { cargarMarcas() }, [])

  const cargarMarcas = () =>
    catalogoService.getMarcas().then(r => setMarcas(r.data))

  const cargarModelos = (marcaId) => {
    setSel(s => ({ ...s, marcaId, modeloId:'', generacionId:'' }))
    setModelos([]); setGeneraciones([]); setMotores([])
    if (marcaId) catalogoService.getModelos(marcaId).then(r => setModelos(r.data))
  }

  const cargarGeneraciones = (modeloId) => {
    setSel(s => ({ ...s, modeloId, generacionId:'' }))
    setGeneraciones([]); setMotores([])
    if (modeloId) catalogoService.getGeneraciones(modeloId).then(r => setGeneraciones(r.data))
  }

  const cargarMotores = (generacionId) => {
    setSel(s => ({ ...s, generacionId }))
    setMotores([])
    if (generacionId) catalogoService.getMotores(generacionId).then(r => setMotores(r.data))
  }

  const guardar = async () => {
    try {
      if (modo === 'marca')
        await api.post('/marcas', { nombre: form.nombre })
      else if (modo === 'modelo')
        await api.post('/modelos', { marcaId: sel.marcaId, nombre: form.nombre })
      else if (modo === 'generacion')
        await api.post('/generaciones', {
          modeloId: sel.modeloId, nombre: form.nombre,
          anioInicio: form.extra?.split('-')[0] || null,
          anioFin:    form.extra?.split('-')[1] || null,
        })
      else if (modo === 'motor')
        await api.post('/motores', {
          generacionId: sel.generacionId, codigo: form.nombre,
          descripcion: form.extra
        })
      setMsg({ texto: 'Guardado correctamente', tipo: 'ok' })
      setForm({ nombre:'', extra:'' }); setModo('')
      cargarMarcas()
    } catch(e) {
      setMsg({ texto: e.response?.data?.error || 'Error al guardar', tipo: 'err' })
    }
    setTimeout(() => setMsg({ texto:'', tipo:'' }), 3000)
  }

  const Seccion = ({ titulo, items, onSelect, seleccionado, onAgregar }) => (
    <div className="card">
      <div className="flex justify-between items-center mb-3">
        <h3 className="font-semibold text-gray-900 text-sm">{titulo}</h3>
        <button onClick={onAgregar}
          className="text-xs bg-primary text-white px-3 py-1 rounded-lg hover:bg-primary-dark">
          + Agregar
        </button>
      </div>
      <div className="space-y-1 max-h-48 overflow-y-auto">
        {items.length === 0
          ? <p className="text-xs text-gray-400 text-center py-4">Sin registros</p>
          : items.map(i => (
            <button key={i.id} onClick={() => onSelect && onSelect(i.id)}
              className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors
                ${seleccionado === String(i.id)
                  ? 'bg-primary text-white'
                  : 'hover:bg-gray-50 text-gray-700'}`}>
              {i.nombre} {i.extra ? <span className="text-xs opacity-70">({i.extra})</span> : ''}
            </button>
          ))
        }
      </div>
    </div>
  )

  return (
    <div>
      <h2 className="text-xl font-bold text-gray-900 mb-6">Catálogo automotriz</h2>

      {msg.texto && (
        <div className={`mb-4 px-4 py-3 rounded-lg text-sm font-medium
          ${msg.tipo==='ok' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
          {msg.texto}
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <Seccion titulo="Marcas" items={marcas} seleccionado={sel.marcaId}
          onSelect={cargarModelos} onAgregar={() => setModo('marca')} />
        <Seccion titulo="Modelos" items={modelos} seleccionado={sel.modeloId}
          onSelect={cargarGeneraciones} onAgregar={() => sel.marcaId && setModo('modelo')} />
        <Seccion titulo="Generaciones" items={generaciones} seleccionado={sel.generacionId}
          onSelect={cargarMotores} onAgregar={() => sel.modeloId && setModo('generacion')} />
        <Seccion titulo="Motores" items={motores}
          onAgregar={() => sel.generacionId && setModo('motor')} />
      </div>

      {/* Formulario */}
      {modo && (
        <div className="card max-w-md">
          <h3 className="font-semibold text-gray-900 mb-3 capitalize">
            Nuevo {modo}
          </h3>
          <div className="space-y-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                {modo === 'motor' ? 'Código (ej: 2GD-FTV)' : 'Nombre'}
              </label>
              <input type="text" value={form.nombre}
                onChange={e => setForm({...form, nombre: e.target.value})}
                className="input-field" />
            </div>
            {modo === 'generacion' && (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Años (ej: 2015-2023)
                </label>
                <input type="text" value={form.extra} placeholder="2015-2023"
                  onChange={e => setForm({...form, extra: e.target.value})}
                  className="input-field" />
              </div>
            )}
            {modo === 'motor' && (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Descripción
                </label>
                <input type="text" value={form.extra} placeholder="Motor diesel 2.4L"
                  onChange={e => setForm({...form, extra: e.target.value})}
                  className="input-field" />
              </div>
            )}
          </div>
          <div className="flex gap-3 mt-4">
            <button onClick={() => { setModo(''); setForm({nombre:'',extra:''}) }}
              className="flex-1 border border-gray-300 text-gray-700 rounded-lg py-2
                         hover:bg-gray-50 text-sm">
              Cancelar
            </button>
            <button onClick={guardar} className="flex-1 btn-primary py-2 text-sm">
              Guardar
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
