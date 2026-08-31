// 全局配置：后端 API 基地址
// 开发阶段指向本地 Spring Boot 后端。
// 小程序正式环境需配置为微信平台备案的 HTTPS 合法域名。
const API_BASE_URL = 'http://localhost:8080';

module.exports = {
  API_BASE_URL
};