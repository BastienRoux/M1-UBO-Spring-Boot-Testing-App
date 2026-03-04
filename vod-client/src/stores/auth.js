import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  async function login(pseudo, password) {
    try {
      // Mock login to bypass deleted User Backend logic
      const mockUser = { id: 1, pseudo: pseudo, role: 'USER', age: 25 }
      user.value = mockUser
      token.value = 'mock-jwt-token-xyz'
      localStorage.setItem('token', token.value)
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  async function register(userData) {
    try {
      // Mock register to bypass deleted User Backend logic
      const mockUser = { id: 2, pseudo: userData.pseudo, role: 'USER', age: userData.age || 20 }
      user.value = mockUser
      token.value = 'mock-jwt-token-xyz'
      localStorage.setItem('token', token.value)
      return true
    } catch (error) {
      console.error('Registration failed:', error)
      throw error
    }
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  async function checkAuth() {
    if (token.value) {
      try {
        // Mock checkAuth to bypass deleted User Backend logic
        const mockUser = { id: 1, pseudo: 'Demo-User', role: 'USER', age: 25 }
        user.value = mockUser
      } catch (error) {
        logout()
      }
    }
  }

  return {
    user,
    token,
    isAuthenticated,
    isAdmin,
    login,
    register,
    logout,
    checkAuth
  }
})
