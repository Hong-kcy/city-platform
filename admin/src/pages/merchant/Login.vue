<template>
  <div class="login-wrap">
    <div class="card login-card">
      <h2>商户管理后台登录</h2>
      <p class="hint">Demo 阶段以「选择商户身份」方式进入；后端商户/运营鉴权待后续接入。</p>

      <div v-if="loading" class="empty">加载商户中…</div>
      <div v-else-if="merchants.length === 0" class="empty">暂无可登录商户</div>
      <div v-else class="merchant-list">
        <div v-for="m in merchants" :key="m.id" class="merchant-item" @click="enter(m)">
          <img v-if="m.logoUrl" :src="resolveImageUrl(m.logoUrl)" class="logo" alt="" />
          <div>
            <div class="name">{{ m.name }}</div>
            <div class="sub">{{ m.type }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request, { resolveImageUrl } from '../../api/request';
import { setSession } from '../../utils/session';

const router = useRouter();
const merchants = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const res = await request({ url: '/api/merchants' });
    merchants.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
});

function enter(m) {
  setSession({ merchantId: m.id, merchantName: m.name });
  router.push('/merchant');
}
</script>

<style scoped>
.login-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #eef2f5;
}

.login-card {
  width: 420px;
}

.hint {
  color: #999;
  font-size: 13px;
  margin-bottom: 16px;
}

.merchant-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 10px;
}

.merchant-item:hover {
  border-color: #16a085;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
  background: #eee;
}

.name {
  font-weight: 500;
}

.sub {
  color: #999;
  font-size: 12px;
}
</style>