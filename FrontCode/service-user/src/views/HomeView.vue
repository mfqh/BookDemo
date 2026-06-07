<template>
  <div>
    <h1>首页</h1>
    <button @click="logout">退出登录</button>

    <div style="margin-top: 20px;">
      <button @click="getUserInfo">获取用户信息</button>
      <p v-if="loading">加载中...</p>
      <p v-if="error" style="color: red;">{{ error }}</p>
      <pre v-if="userInfo" style="background: #f5f5f5; padding: 10px;">{{ userInfo }}</pre>
    </div>
  </div>
  <main>
    <TheWelcome />
  </main>
</template>

<script setup>
import { ref } from 'vue'
import request from '../utils/request'
import TheWelcome from '../components/TheWelcome.vue'

// 状态管理
const loading = ref(false)
const error = ref('')
const userInfo = ref(null)

// 示例：获取用户信息接口
const getUserInfo = async () => {
  loading.value = true
  error.value = ''
  userInfo.value = null

  try {
    const res = await request.get('/user/info')
    userInfo.value = res.data
    console.log('✅ 接口成功:', res)
  } catch (err) {
    console.error('❌ 接口失败:', err)
    // 只在 401 时提示过期，其他错误显示真实信息
    if (err.response?.status === 401) {
      error.value = '登录已过期，请重新登录'
    } else if (err.message === 'Network Error') {
      error.value = '网络错误，请检查服务是否启动/跨域配置'
    } else {
      error.value = `请求失败：${err.message || '未知错误'}`
    }
  } finally {
    loading.value = false
  }
}

// 退出登录：清除token并跳转到SSO登出页
const logout = () => {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
  window.location.href = 'http://localhost:10100/sso/logout?redirect_uri=http://localhost:10302'
}
</script>