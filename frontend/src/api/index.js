import axios from 'axios'

const request = axios.create({
  baseURL: '/zhi-mo/api',
  timeout: 30000
})

request.interceptors.response.use(
  response => response.data,
  error => {
    ElementPlus.ElMessage.error(error.message || '请求失败')
    return Promise.reject(error)
  }
)

export const articleApi = {
  list: () => request.get('/article/list'),
  getById: (id) => request.get(`/article/${id}`),
  save: (data) => request.post('/article', data),
  update: (data) => request.put('/article', data),
  delete: (id) => request.delete(`/article/${id}`),
  import: (data) => request.post('/article/import', data)
}
