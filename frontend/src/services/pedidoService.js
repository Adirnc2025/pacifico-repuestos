import api from '../utils/axiosConfig'

export const pedidoService = {
  crear:        (data)   => api.post('/pedidos', data),
  misPedidos:   ()       => api.get('/pedidos/mis-pedidos'),
  detalle:      (id)     => api.get(`/pedidos/${id}`),
  listarTodos:  (estado) => api.get('/pedidos', { params: estado ? { estado } : {} }),
  cambiarEstado:(id, e)  => api.put(`/pedidos/${id}/estado`, { estado: e }),
  getZonas:     ()       => api.get('/delivery/zonas'),
}
