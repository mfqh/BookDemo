import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Callback from '../views/Callback.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true } // 需要登录才能访问
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (About.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/AboutView.vue'),
  }, 
  {
    path: '/callback',
    name: 'Callback',
    component: Callback,
    meta: { requiresAuth: false } // 回调页面不需要登录
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫：未登录拦截
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('access_token')

  // 1. 如果是回调页面，直接放行（用来拿code换token）
  if (to.path === '/callback') {
    next()
    return
  }

  // 2. 如果页面需要登录，且没有token → 跳转到SSO登录页
  if (to.meta.requiresAuth && !token) {
    redirectToSSO()
    return
  }

  // 3. 已登录或不需要登录，直接放行
  next()
})

// 跳转到认证中心授权地址
function redirectToSSO() {
  const clientId = 'service-user' // 你的客户端ID
  const redirectUri = encodeURIComponent('http://localhost:10302/callback') // 回调地址，必须和后端注册的一致
  const ssoAuthUrl = `http://localhost:10100/sso/oauth2/authorize?client_id=${clientId}&response_type=code&redirect_uri=${redirectUri}&scope=read`
  
  window.location.href = ssoAuthUrl
}

export default router
