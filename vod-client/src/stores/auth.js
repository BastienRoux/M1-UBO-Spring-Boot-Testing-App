import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('currentUser') || 'null'))
  const token = ref(localStorage.getItem('token') || null)

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  function getMockUsers() {
    return JSON.parse(localStorage.getItem('mockUsers') || '[]')
  }

  function setMockUsers(users) {
    localStorage.setItem('mockUsers', JSON.stringify(users))
  }

  function setCurrentUser(current) {
    user.value = current
    localStorage.setItem('currentUser', JSON.stringify(current))
  }

  async function login(pseudo, password) {
    try {
      // Mock login: restore existing mock account by pseudo, or create one.
      const users = getMockUsers()
      let mockUser = users.find(u => u.pseudo === pseudo)

      if (!mockUser) {
        const nextId = users.length ? Math.max(...users.map(u => Number(u.id) || 0)) + 1 : 1
        mockUser = { id: nextId, pseudo, role: 'USER', age: 25 }
        users.push(mockUser)
        setMockUsers(users)
      }

      setCurrentUser(mockUser)
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
      const users = getMockUsers()
      const existing = users.find(u => u.pseudo === userData.pseudo)
      if (existing) {
        throw new Error('Ce pseudo existe deja')
      }

      const nextId = users.length ? Math.max(...users.map(u => Number(u.id) || 0)) + 1 : 1
      const mockUser = {
        id: nextId,
        pseudo: userData.pseudo,
        role: 'USER',
        age: userData.age || 20
      }

      users.push(mockUser)
      setMockUsers(users)
      setCurrentUser(mockUser)
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
    localStorage.removeItem('currentUser')
  }

  async function checkAuth() {
    if (token.value) {
      try {
        const storedUser = localStorage.getItem('currentUser')
        if (storedUser) {
          user.value = JSON.parse(storedUser)
          return
        }

        // Backward compatibility with older sessions where only token existed.
        const users = getMockUsers()
        if (users.length > 0) {
          setCurrentUser(users[0])
        } else {
          setCurrentUser({ id: 1, pseudo: 'Demo-User', role: 'USER', age: 25 })
        }
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
