<template>
  <el-container style="height:100vh">
    <el-aside width="220px" style="background:#16213e">
      <div class="logo">
        <el-icon :size="24" color="#16a085"><Monitor /></el-icon>
        <span>IoT智能监控</span>
      </div>
      <el-menu :default-active="activeMenu" router background-color="#16213e" text-color="#a0a0b0" active-text-color="#16a085">
        <el-menu-item index="/dashboard"><el-icon><DataLine /></el-icon><span>首页仪表盘</span></el-menu-item>
        <el-menu-item index="/devices"><el-icon><Cpu /></el-icon><span>设备管理</span></el-menu-item>
        <el-menu-item index="/monitor"><el-icon><Monitor /></el-icon><span>实时监控</span></el-menu-item>
        <el-menu-item index="/history"><el-icon><Document /></el-icon><span>历史数据</span></el-menu-item>
        <el-menu-item index="/alerts"><el-icon><Bell /></el-icon><span>告警管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#1a1a2e;display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #2a2a4a">
        <h3 style="color:#e0e0e0;margin:0">{{ currentTitle }}</h3>
        <div style="display:flex;align-items:center;gap:16px">
          <span style="color:#a0a0b0">{{ userStore.realName || userStore.username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>退出
          </el-button>
        </div>
      </el-header>
      <el-main style="background:#0f0f1a;padding:20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || 'IoT智能监控')

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #16a085;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #2a2a4a;
}
.el-menu {
  border-right: none;
}
</style>
