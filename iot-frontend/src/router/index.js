import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('../views/LoginView.vue') },
  {
    path: '/main',
    component: () => import('../layout/MainLayout.vue'),
    children: [
      { path: '', redirect: '/dashboard' },
      { path: '/dashboard', component: () => import('../views/DashboardView.vue'), meta: { title: '首页仪表盘' } },
      { path: '/devices', component: () => import('../views/DeviceView.vue'), meta: { title: '设备管理' } },
      { path: '/monitor', component: () => import('../views/MonitorView.vue'), meta: { title: '实时监控' } },
      { path: '/history', component: () => import('../views/HistoryView.vue'), meta: { title: '历史数据' } },
      { path: '/alerts', component: () => import('../views/AlertView.vue'), meta: { title: '告警管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
    return
  }
  const token = localStorage.getItem('token')
  if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
