<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">文旅街区 · 管理后台</div>
      <div class="menu-group">商户中心</div>
      <router-link to="/merchant" class="menu-item">商户信息</router-link>
      <router-link to="/stores" class="menu-item">门店管理</router-link>
      <router-link to="/activities" class="menu-item">活动管理</router-link>
      <router-link to="/coupons" class="menu-item">优惠券管理</router-link>
      <router-link to="/coupon-redeem" class="menu-item">优惠券核销</router-link>
      <router-link to="/tasks" class="menu-item">任务管理</router-link>
      <router-link to="/task-complete" class="menu-item">任务完成验证</router-link>

      <div class="menu-group">运营中心</div>
      <router-link to="/routes" class="menu-item">路线管理</router-link>
      <router-link to="/operation" class="menu-item">运营后台（预留）</router-link>

      <div class="sidebar-footer">
        <div class="session-name">{{ session ? session.merchantName || ('商户#' + session.merchantId) : '' }}</div>
        <button class="btn" @click="logout">退出</button>
      </div>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { getSession, clearSession } from '../utils/session';

const router = useRouter();
const session = computed(() => getSession());

function logout() {
  clearSession();
  router.push('/login');
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background: #1f2d3d;
  color: #cbd5df;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.brand {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  padding: 0 20px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  margin-bottom: 12px;
}

.menu-group {
  padding: 10px 20px 6px;
  font-size: 12px;
  color: #7f8c9b;
}

.menu-item {
  display: block;
  padding: 10px 20px;
  color: #cbd5df;
  font-size: 14px;
}

.menu-item:hover {
  background: #2c3a4d;
}

.menu-item.router-link-active {
  background: #16a085;
  color: #fff;
}

.sidebar-footer {
  margin-top: auto;
  padding: 16px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.session-name {
  color: #fff;
  margin-bottom: 10px;
  font-size: 13px;
}

.content {
  flex: 1;
  padding: 24px;
  overflow: auto;
}
</style>