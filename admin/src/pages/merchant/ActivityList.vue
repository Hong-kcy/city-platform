<template>
  <div>
    <div class="page-head">
      <h2 class="page-title">活动管理</h2>
      <button class="btn btn-primary" @click="$router.push('/activities/new')">新增活动</button>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="activities.length === 0" class="empty">暂无活动</div>
    <div v-else class="card">
      <table class="table">
        <thead>
          <tr>
            <th>标题</th>
            <th>类型</th>
            <th>街区</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="a in activities" :key="a.id">
            <td>{{ a.title }}</td>
            <td>{{ typeLabel(a.activityType) }}</td>
            <td>{{ a.streetAreaName }}</td>
            <td>{{ formatTime(a.startTime) }}</td>
            <td>{{ formatTime(a.endTime) }}</td>
            <td>
              <span class="tag" :class="a.status === 'PUBLISHED' ? 'tag-on' : 'tag-off'">
                {{ statusLabel(a.status) }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm" @click="$router.push('/activities/' + a.id + '/edit')">编辑</button>
              <button v-if="a.status === 'DRAFT'" class="btn btn-sm btn-primary" @click="change(a, 'PUBLISHED')">发布</button>
              <button v-if="a.status === 'PUBLISHED'" class="btn btn-sm btn-danger" @click="change(a, 'OFFLINE')">下线</button>
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

const activities = ref([]);
const loading = ref(true);

const TYPE_LABELS = {
  FESTIVAL: '节庆',
  PERFORMANCE: '演出',
  EXHIBITION: '展览',
  PROMOTION: '促销',
  CULTURE: '文化',
  OTHER: '其他'
};
const STATUS_LABELS = { DRAFT: '草稿', PUBLISHED: '已发布', OFFLINE: '已下线' };

onMounted(load);

function typeLabel(v) {
  return TYPE_LABELS[v] || v;
}

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
    const res = await request({ url: '/api/activities?size=100' });
    activities.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
}

async function change(a, status) {
  try {
    await request({
      url: '/api/activities/' + a.id + '/status',
      method: 'PATCH',
      data: { status }
    });
    a.status = status;
  } catch (err) {
    alert(err.message);
  }
}
</script>