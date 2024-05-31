import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    hmr: true, // 开启模块热更新
    port: 3005, // 前端服务端口
    proxy: {
      //配置代理
      '/api': {
        target: 'http://localhost:7071',
        changeOrigin: true,
        pathRewrite: {
          '^api': '/api'
        }
      }
    }
  },
  build: {
    chunkSizeWarningLimit: 3000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id
              .toString()
              .split('node_modules/')[1]
              .split('/')[0]
              .toString(0)
          }
        }
      }
    },
    chunkFileNames: (chunkInfo) => {
      const facadeModuleId = chunkInfo.facadeModuleId
        ? chunkInfo.facadeModuleId.split('/')
        : []
      const fileName = facadeModuleId[facadeModuleId.length - 2] || '[name]'
      return `js/${fileName}/[name].[hash].js`
    }
  }
})
