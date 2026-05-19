import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref('')
  const realName = ref('')
  const role = ref('')

  function setUserInfo(info) {
    token.value = info.token
    username.value = info.username
    realName.value = info.realName
    role.value = info.role
    localStorage.setItem('token', info.token)
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function loadFromStorage() {
    const saved = localStorage.getItem('userInfo')
    if (saved) {
      const info = JSON.parse(saved)
      username.value = info.username
      realName.value = info.realName
      role.value = info.role
    }
  }

  function logout() {
    token.value = ''
    username.value = ''
    realName.value = ''
    role.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  loadFromStorage()

  return { token, username, realName, role, setUserInfo, logout }
})
