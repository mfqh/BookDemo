import axios from 'axios'
import router from '../router'

const request = axios.create({
  baseURL: 'http://localhost:10202/api', // 你的业务服务地址
  timeout: 5000
})

// 请求拦截器：自动添加token到请求头
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('access_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：处理token过期
request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    // 401：token无效或过期，跳回SSO登录
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      // 跳转到SSO登录页
      const clientId = 'service-user'
      const redirectUri = encodeURIComponent('http://localhost:10302/callback')
      const ssoAuthUrl = `http://localhost:10100/sso/oauth2/authorize?client_id=${clientId}&response_type=code&redirect_uri=${redirectUri}&scope=read`
      window.location.href = ssoAuthUrl
    }
    return Promise.reject(error)
  }
)

export default request