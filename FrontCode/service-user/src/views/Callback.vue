<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

onMounted(async () => {
  const urlParams = new URLSearchParams(window.location.search)
  const code = urlParams.get('code')

  if (!code) {
    alert('未获取到授权码，请重试')
    window.location.href = '/'
    return
  }

  try {
    const data = new URLSearchParams();
    data.append('grant_type', 'authorization_code');
    data.append('code', code);
    data.append('redirect_uri', 'http://localhost:10302/callback');

    const config = {
      method: 'post',
      url: 'http://localhost:10100/sso/oauth2/token',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Basic c2VydmljZS11c2VyOnVzZXItc2VjcmV0LTEyMw=='
      },
      data: data
    };

    const res = await axios(config);

    // 1. 先执行存 token 的代码，确保一定执行
    console.log('✅ 获取token成功:', res.data)
    localStorage.setItem('access_token', res.data.access_token)
    localStorage.setItem('refresh_token', res.data.refresh_token)

    // 2. 存完 token 再跳转，这时候组件才卸载
    router.push('/')
  } catch (err) {
    console.error('❌ 获取token失败:', err.response?.data || err)
    alert('登录失败，请重试（错误信息：' + (err.response?.data?.error_description || err.message) + '）')
  }
})
</script>