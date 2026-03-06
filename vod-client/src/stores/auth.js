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
      // Mock login avec mapping vers les vrais utilisateurs de la base
      const realUsers = {
        'john_doe': { id: 1, pseudo: 'john_doe', role: 'USER', age: 30 },
        'jane_smith': { id: 2, pseudo: 'jane_smith', role: 'USER', age: 25 },
        'admin': { id: 3, pseudo: 'admin', role: 'ADMIN', age: 35 },
        'alice_wonder': { id: 4, pseudo: 'alice_wonder', role: 'USER', age: 28 },
        'bob_martin': { id: 5, pseudo: 'bob_martin', role: 'USER', age: 20 }
      }

      const mockUser = realUsers[pseudo]
      
      if (!mockUser) {
        // Si l'utilisateur n'existe pas dans les vrais utilisateurs, on refuse
        console.error('Utilisateur non trouvé. Utilisez: john_doe, jane_smith, admin, alice_wonder, ou bob_martin')
        return false
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
          const parsedUser = JSON.parse(storedUser)
          // Vérifier que l'utilisateur a un ID valide (1-5)
          const validIds = [1, 2, 3, 4, 5]
          if (parsedUser.id && validIds.includes(Number(parsedUser.id))) {
            user.value = parsedUser
            return
          } else {
            // ID invalide, déconnecter
            console.warn('Utilisateur avec ID invalide détecté, déconnexion...')
            logout()
            return
          }
        }

        // Pas d'utilisateur stocké valide, déconnecter
        logout()
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
