<template>
  <div class="login-container">
    <div class="login-card">
      <h2>IoT智能监控系统</h2>
      <p class="subtitle">AI Agent驱动型物联网设备智能监控管理平台</p>
      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleSubmit">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading" size="large" style="width:100%;background:#16a085;border:none">
            {{ isRegister ? '注册' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="toggle">
        <el-button link type="primary" @click="isRegister = !isRegister" style="color:#16a085">
          {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
        </el-button>
      </div>
      <div v-if="isRegister" class="extra-field">
        <el-input v-model="form.realName" placeholder="真实姓名" prefix-icon="Avatar" size="large" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { auth } from '../api/index'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const isRegister = ref(false)

const form = reactive({ username: '', password: '', realName: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (isRegister.value) {
      await auth.register(form)
      ElMessage.success('注册成功，请登录')
      isRegister.value = false
    } else {
      const res = await auth.login({ username: form.username, password: form.password })
      userStore.setUserInfo(res.data)
      ElMessage.success('登录成功')
      router.push('/dashboard')
    }
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f0f1a 0%, #16213e 50%, #1a1a2e 100%);
}
.login-card {
  width: 400px;
  padding: 40px;
  background: rgba(22, 33, 62, 0.9);
  border-radius: 12px;
  border: 1px solid #2a2a4a;
  text-align: center;
}
.login-card h2 {
  color: #16a085;
  margin-bottom: 8px;
}
.subtitle {
  color: #a0a0b0;
  font-size: 13px;
  margin-bottom: 30px;
}
.toggle {
  margin-top: 10px;
}
.extra-field {
  margin-top: -10px;
  margin-bottom: 10px;
}
:deep(.el-input__wrapper) {
  background: #0f0f1a;
  box-shadow: 0 0 0 1px #2a2a4a inset;
}
:deep(.el-input__inner) {
  color: #e0e0e0;
}
</style>
