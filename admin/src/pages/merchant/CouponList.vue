<template>
  <div>
    <div class="page-head">
      <h2 class="page-title">优惠券管理</h2>
      <button class="btn btn-primary" @click="$router.push('/coupons/new')">新增优惠券</button>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="coupons.length === 0" class="empty">暂无优惠券</div>
    <div v-else class="card">
      <table class="table">
        <thead>
          <tr>
            <th>名称</th>
            <th>权益</th>
            <th>门店</th>
            <th>有效期开始</th>
            <th>有效期结束</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in coupons" :key="c.id">
            <td>{{ c.name }}</td>
            <td>{{ c.discountText }}</td>
            <td>{{ c.storeName }}</td>
            <td>{{ formatTime(c.validFrom) }}</td>
            <td>{{ formatTime(c.validTo) }}</td>
            <td>
              <span class="tag" :class="c.effectiveStatus === 'ACTIVE' ? 'tag-on' : 'tag-off'">
                {{ statusLabel(c.effectiveStatus) }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm" @click="$router.push('/coupons/' + c.id + '/edit')">编辑</button>
              <button v-if="c.status === 'ACTIVE'" class="btn btn-sm btn-danger" @click="change(c, 'INACTIVE')">停用</button>
              <button v-if="c.status === 'INACTIVE'" class="btn btn-sm btn-primary" @click="change(c, 'ACTIVE')">启用</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import request from '../../api/request';

const coupons = ref([]);
const loading = ref(true);

const STATUS_LABELS = {
  ACTIVE: '生效中',
  INACTIVE: '已停用',
  NOT_STARTED: '未开始',
  EXPIRED: '已过期'
};

onMounted(load);

function statusLabel(v) {
  return STATUS_LABELS[v] || v;
}

function formatTime(v) {
  if (!v) return '';
  return v.replace('T', ' ').substring(0, 16);
}

async function load() {
  loading.value = true;
  try {
    const res = await request({ url: '/api/coupons?size=100' });
    coupons.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
}

async function change(c, status) {
  try {
    await request({
      url: '/api/coupons/' + c.id + '/status',
      method: 'PATCH',
      data: { status }
    });
    await load();
  } catch (err) {
    alert(err.message);
  }
}
</script>
