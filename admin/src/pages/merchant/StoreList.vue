<template>
  <div>
    <div class="page-head">
      <h2 class="page-title">门店管理</h2>
      <button class="btn btn-primary" @click="$router.push('/stores/new')">新增门店</button>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="stores.length === 0" class="empty">暂无门店</div>
    <div v-else class="card">
      <table class="table">
        <thead>
          <tr>
            <th>门店名称</th>
            <th>地址</th>
            <th>营业状态</th>
            <th>门店状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in stores" :key="s.id">
            <td>{{ s.name }}</td>
            <td>{{ s.address }}</td>
            <td>
              <span class="tag" :class="s.businessStatus === 'OPEN' ? 'tag-on' : 'tag-off'">
                {{ s.businessStatus === 'OPEN' ? '营业中' : '休息中' }}
              </span>
            </td>
            <td>
              <span class="tag" :class="s.status === 'ACTIVE' ? 'tag-on' : 'tag-off'">
                {{ s.status === 'ACTIVE' ? '启用' : '停用' }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm" @click="$router.push('/stores/' + s.id + '/edit')">编辑</button>
              <button class="btn btn-sm" @click="toggleBusiness(s)">
                {{ s.businessStatus === 'OPEN' ? '设为休息' : '设为营业' }}
              </button>
              <button class="btn btn-sm btn-danger" @click="toggleStatus(s)">
                {{ s.status === 'ACTIVE' ? '停用' : '启用' }}
              </button>
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
import { getSession } from '../../utils/session';

const stores = ref([]);
const loading = ref(true);

onMounted(load);

async function load() {
  loading.value = true;
  try {
    const session = getSession();
    const res = await request({
      url: '/api/stores?merchantId=' + session.merchantId + '&size=100'
    });
    stores.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
}

async function toggleBusiness(s) {
  const next = s.businessStatus === 'OPEN' ? 'CLOSED' : 'OPEN';
  try {
    await request({
      url: '/api/stores/' + s.id + '/business-status',
      method: 'PATCH',
      data: { businessStatus: next }
    });
    s.businessStatus = next;
  } catch (err) {
    alert(err.message);
  }
}

async function toggleStatus(s) {
  const next = s.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
  try {
    await request({
      url: '/api/stores/' + s.id + '/status',
      method: 'PATCH',
      data: { status: next }
    });
    s.status = next;
  } catch (err) {
    alert(err.message);
  }
}
</script>