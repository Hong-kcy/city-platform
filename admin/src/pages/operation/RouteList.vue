<template>
  <div>
    <div class="page-head">
      <h2 class="page-title">路线管理</h2>
      <button class="btn btn-primary" @click="$router.push('/routes/new')">新增路线</button>
    </div>

    <div class="card search-bar">
      <input v-model="keyword" placeholder="按路线名称搜索" @keyup.enter="load" />
      <select v-model="themeFilter">
        <option value="">全部主题</option>
        <option v-for="t in themes" :key="t" :value="t">{{ themeLabel(t) }}</option>
      </select>
      <button class="btn" @click="load">查询</button>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="routes.length === 0" class="empty">暂无路线</div>
    <div v-else class="card">
      <table class="table">
        <thead>
          <tr>
            <th>路线名称</th>
            <th>主题</th>
            <th>街区</th>
            <th>预计时长</th>
            <th>POI数</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in routes" :key="r.id">
            <td>{{ r.name }}</td>
            <td>{{ themeLabel(r.theme) }}</td>
            <td>{{ r.streetAreaName }}</td>
            <td>{{ durationText(r.estimatedDuration) }}</td>
            <td>{{ r.poiCount }}</td>
            <td>
              <span class="tag" :class="r.status === 'ACTIVE' ? 'tag-on' : 'tag-off'">
                {{ statusLabel(r.status) }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm" @click="$router.push('/routes/' + r.id + '/edit')">编辑</button>
              <button v-if="r.status === 'ACTIVE'" class="btn btn-sm btn-danger" @click="change(r, 'INACTIVE')">停用</button>
              <button v-if="r.status === 'INACTIVE'" class="btn btn-sm btn-primary" @click="change(r, 'ACTIVE')">启用</button>
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

const routes = ref([]);
const loading = ref(true);
const keyword = ref('');
const themeFilter = ref('');

const themes = ['FRIEND_PHOTO', 'SOLO_RELAX', 'FAMILY_FUN', 'SLOW_WALK', 'OTHER'];
const THEME_LABELS = {
  FRIEND_PHOTO: '闺蜜出片',
  SOLO_RELAX: '社恐友好',
  FAMILY_FUN: '亲子放电',
  SLOW_WALK: '周末慢逛',
  OTHER: '其他'
};
const STATUS_LABELS = { ACTIVE: '启用', INACTIVE: '停用' };

onMounted(load);

function themeLabel(v) {
  return THEME_LABELS[v] || v;
}

function statusLabel(v) {
  return STATUS_LABELS[v] || v;
}

function durationText(minutes) {
  if (minutes == null) return '';
  if (minutes < 60) return minutes + '分钟';
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return m === 0 ? h + '小时' : h + '小时' + m + '分钟';
}

async function load() {
  loading.value = true;
  try {
    const params = new URLSearchParams({ size: '100' });
    if (keyword.value) params.set('name', keyword.value);
    if (themeFilter.value) params.set('theme', themeFilter.value);
    const res = await request({ url: '/api/experience-routes?' + params.toString() });
    routes.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
}

async function change(r, status) {
  try {
    await request({
      url: '/api/experience-routes/' + r.id + '/status',
      method: 'PATCH',
      data: { status }
    });
    r.status = status;
  } catch (err) {
    alert(err.message);
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
}

.search-bar input,
.search-bar select {
  padding: 6px 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.search-bar input {
  width: 220px;
}
</style>
