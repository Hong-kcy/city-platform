import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// 开发代理：前端同源请求 /api、/uploads、/static，转发到本地 Spring Boot 后端，
// 避免跨域（后端未单独配置 CORS）。
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
      '/uploads': { target: 'http://localhost:8080', changeOrigin: true },
      '/static': { target: 'http://localhost:8080', changeOrigin: true }
    }
  }
});