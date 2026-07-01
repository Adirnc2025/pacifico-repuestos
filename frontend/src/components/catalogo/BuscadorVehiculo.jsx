import { useState, useEffect } from 'react'
import { catalogoService } from '../../services/catalogoService'

export default function BuscadorVehiculo({ onFiltrar }) {
  const [marcas,      setMarcas]      = useState([])
  const [modelos,     setModelos]     = useState([])
  const [generaciones,setGeneraciones]= useState([])
  const [motores,     setMotores]     = useState([])
  const [sel, setSel] = useState({ marcaId:'', modeloId:'', generacionId:'', motorId:'' })

  useEffect(() => { catalogoService.getMarcas().then(r => setMarcas(r.data)).catch(() => {}) }, [])

  const handleMarca = async (marcaId) => {
    setSel({ marcaId, modeloId:'', generacionId:'', motorId:'' })
    setModelos([]); setGeneraciones([]); setMotores([])
    if (marcaId) {
      const r = await catalogoService.getModelos(marcaId)
      setModelos(r.data)
    }
  }

  const handleModelo = async (modeloId) => {
    setSel(s => ({ ...s, modeloId, generacionId:'', motorId:'' }))
    setGeneraciones([]); setMotores([])
    if (modeloId) {
      const r = await catalogoService.getGeneraciones(modeloId)
      setGeneraciones(r.data)
    }
  }

  const handleGeneracion = async (generacionId) => {
    setSel(s => ({ ...s, generacionId, motorId:'' }))
    setMotores([])
    if (generacionId) {
      const r = await catalogoService.getMotores(generacionId)
      setMotores(r.data)
    }
  }

  const handleMotor = (motorId) => setSel(s => ({ ...s, motorId }))

  const handleBuscar = () => {
    const params = {}
    if (sel.marcaId)      params.marcaId      = sel.marcaId
    if (sel.modeloId)     params.modeloId     = sel.modeloId
    if (sel.generacionId) params.generacionId = sel.generacionId
    if (sel.motorId)      params.motorId      = sel.motorId
    onFiltrar(params)
  }

  const Select = ({ label, value, onChange, options, disabled }) => (
    <div className="flex-1 min-w-[150px]">
      <label className="block text-xs font-medium text-gray-600 mb-1">{label}</label>
      <select value={value} onChange={e => onChange(e.target.value)} disabled={disabled}
        className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm
                   focus:outline-none focus:ring-2 focus:ring-primary/50
                   disabled:bg-gray-100 disabled:cursor-not-allowed">
        <option value="">-- Selecciona --</option>
        {options.map(o => <option key={o.id} value={o.id}>{o.nombre}</option>)}
      </select>
    </div>
  )

  return (
    <div className="bg-white rounded-xl border border-gray-200 p-4 shadow-sm">
      <h3 className="text-sm font-semibold text-gray-700 mb-3">
        🔍 Buscar por vehículo
      </h3>
      <div className="flex flex-wrap gap-3 items-end">
        <Select label="Marca"      value={sel.marcaId}      onChange={handleMarca}
                options={marcas}   disabled={false} />
        <Select label="Modelo"     value={sel.modeloId}     onChange={handleModelo}
                options={modelos}  disabled={!sel.marcaId} />
        <Select label="Generación" value={sel.generacionId} onChange={handleGeneracion}
                options={generaciones} disabled={!sel.modeloId} />
        <Select label="Motor"      value={sel.motorId}      onChange={handleMotor}
                options={motores.map(m => ({id:m.id, nombre: m.nombre + (m.extra ? ` — ${m.extra}` : '')}))}
                disabled={!sel.generacionId} />
        <button onClick={handleBuscar}
          className="btn-primary px-6 py-2 text-sm self-end">
          Buscar
        </button>
      </div>
    </div>
  )
}
