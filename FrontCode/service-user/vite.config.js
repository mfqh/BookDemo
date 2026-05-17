import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 10302,
    strictPort: false,
    proxy: {
      // 代理业务服务请求，避免跨域
      '/api': {
        target: 'http://localhost:10202',
        changeOrigin: true
      },
      // 代理认证中心请求（可选，也可以直接访问）
      '/sso': {
        target: 'http://localhost:10100',
        changeOrigin: true
      }
    }
  }
})
