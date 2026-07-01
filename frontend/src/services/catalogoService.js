import api from '../utils/axiosConfig'

export const catalogoService = {
  getMarcas:       ()          => api.get('/marcas'),
  getModelos:      (marcaId)   => api.get(`/modelos?marcaId=${marcaId}`),
  getGeneraciones: (modeloId)  => api.get(`/generaciones?modeloId=${modeloId}`),
  getMotores:      (genId)     => api.get(`/motores?generacionId=${genId}`),
  getCategorias:   ()          => api.get('/categorias'),
}

export const productoService = {
  listar:       (params)  => api.get('/productos', { params }),
  buscar:       (q)       => api.get(`/productos/buscar?q=${q}`),
  destacados:   ()        => api.get('/productos/destacados'),
  detalle:      (id)      => api.get(`/productos/${id}`),
  crear:        (data)    => api.post('/productos', data),
  editar:       (id,data) => api.put(`/productos/${id}`, data),
  eliminar:     (id)      => api.delete(`/productos/${id}`),
  agregarCompat:(id,data) => api.post(`/productos/${id}/compatibilidades`, data),
  agregarImagen:(id,url)  => api.post(`/productos/${id}/imagenes?url=${encodeURIComponent(url)}&esPrincipal=false`),
}
