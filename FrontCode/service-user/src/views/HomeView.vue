<template>
  <div>
    <h1>首页</h1>
    <button @click="logout">退出登录</button>
  </div>
  <main>
    <TheWelcome />
  </main>
</template>

<script setup>
import request from '../utils/request'
import TheWelcome from '../components/TheWelcome.vue'

// 示例：获取用户信息
const getUserInfo = async () => {
  const data = await request.get('/user/info')
  console.log('用户信息', data)
}

// 退出登录：清除token并跳转到SSO登出页
const logout = () => {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
  window.location.href = 'http://localhost:10100/sso/logout?redirect_uri=http://localhost:10302'
}
</script>
