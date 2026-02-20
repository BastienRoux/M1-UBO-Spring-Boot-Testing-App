import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 12080,
    proxy: {
      '/api': {
        target: 'http://localhost:12081',
        changeOrigin: true
      }
    }
  }
})
