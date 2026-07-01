import { useState, useEffect } from 'react'
import api from '../../utils/axiosConfig'

export default function ClientesAdmin() {
  const [clientes, setClientes] = useState([])
  const [cargando, setCargando] = useState(true)
  const [busqueda, setBusqueda] = useState('')

  useEffect(() => {
    api.get('/clientes')
      .then(r => setClientes(r.data))
      .catch(e => console.error('Error clientes:', e))
      .finally(() => setCargando(false))
  }, [])

  const filtrados = clientes.filter(c =>
    c.nombre.toLowerCase().includes(busqueda.toLowerCase()) ||
    c.correo.toLowerCase().includes(busqueda.toLowerCase())
  )

  return (
    <div>
      <div style={{ display:'flex', justifyContent:'space-between',
                    alignItems:'center', marginBottom:'20px' }}>
        <h2 style={{ fontSize:'20px', fontWeight:'500', margin:0 }}>
          Clientes ({clientes.length})
        </h2>
        <input
          type="text"
          placeholder="Buscar por nombre o correo..."
          value={busqueda}
          onChange={e => setBusqueda(e.target.value)}
          style={{ padding:'8px 14px', border:'1px solid var(--border)',
                   borderRadius:'8px', fontSize:'13px', width:'260px',
                   background:'var(--surface-2)', color:'var(--text-primary)' }}
        />
      </div>

      {cargando ? (
        <div style={{ textAlign:'center', padding:'40px', color:'var(--text-secondary)' }}>
          Cargando clientes...
        </div>
      ) : filtrados.length === 0 ? (
        <div style={{ textAlign:'center', padding:'40px', color:'var(--text-secondary)' }}>
          No hay clientes registrados aún
        </div>
      ) : (
        <div style={{ overflowX:'auto' }}>
          <table style={{ width:'100%', borderCollapse:'collapse', fontSize:'13px' }}>
            <thead>
              <tr style={{ background:'var(--surface-1)' }}>
                {['ID','Nombre','Correo','Teléfono','Dirección','Estado'].map(h => (
                  <th key={h} style={{ textAlign:'left', padding:'10px 14px',
                                       fontWeight:'500', color:'var(--text-secondary)',
                                       borderBottom:'0.5px solid var(--border)' }}>
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtrados.map(c => (
                <tr key={c.id} style={{ borderBottom:'0.5px solid var(--border)' }}
                    onMouseEnter={e => e.currentTarget.style.background='var(--surface-1)'}
                    onMouseLeave={e => e.currentTarget.style.background='transparent'}>
                  <td style={{ padding:'10px 14px', color:'var(--text-muted)' }}>#{c.id}</td>
                  <td style={{ padding:'10px 14px', fontWeight:'500' }}>{c.nombre}</td>
                  <td style={{ padding:'10px 14px', color:'var(--text-secondary)' }}>{c.correo}</td>
                  <td style={{ padding:'10px 14px', color:'var(--text-secondary)' }}>
                    {c.telefono || '—'}
                  </td>
                  <td style={{ padding:'10px 14px', color:'var(--text-secondary)' }}>
                    {c.direccion || '—'}
                  </td>
                  <td style={{ padding:'10px 14px' }}>
                    <span style={{
                      padding:'2px 10px', borderRadius:'20px', fontSize:'11px',
                      fontWeight:'500',
                      background: c.activo ? 'var(--bg-success)' : 'var(--bg-danger)',
                      color: c.activo ? 'var(--text-success)' : 'var(--text-danger)'
                    }}>
                      {c.activo ? 'Activo' : 'Inactivo'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
