import request from './request'

export const auth = {
  login: data => request.post('/auth/login', data),
  register: data => request.post('/auth/register', data)
}

export const device = {
  add: data => request.post('/device/add', data),
  update: (id, data) => request.put(`/device/update/${id}`, data),
  delete: id => request.delete(`/device/delete/${id}`),
  getById: id => request.get(`/device/${id}`),
  list: params => request.get('/device/list', { params })
}

export const dataApi = {
  report: data => request.post('/data/report', data),
  list: params => request.get('/data/list', { params }),
  getByDevice: (deviceId, params) => request.get(`/data/device/${deviceId}`, { params }),
  getLatest: deviceId => request.get(`/data/latest/${deviceId}`)
}

export const alertApi = {
  list: params => request.get('/alert/list', { params }),
  handle: id => request.put(`/alert/handle/${id}`)
}

export const dashboard = {
  getStats: () => request.get('/dashboard/stats')
}

export const agentApi = {
  startSimulator: () => request.post('/agent/simulator/start'),
  stopSimulator: () => request.post('/agent/simulator/stop'),
  setInterval: interval => request.post('/agent/simulator/interval', { interval }),
  query: command => request.post('/agent/query', { command }),
  inspect: () => request.post('/agent/inspect')
}
