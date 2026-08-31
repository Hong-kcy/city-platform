<template>
  <div>
    <div class="page-head">
      <h2 class="page-title">任务管理</h2>
      <button class="btn btn-primary" @click="$router.push('/tasks/new')">新增任务</button>
    </div>

    <div v-if="loading" class="empty">加载中…</div>
    <div v-else-if="tasks.length === 0" class="empty">暂无任务</div>
    <div v-else class="card">
      <table class="table">
        <thead>
          <tr>
            <th>标题</th>
            <th>类型</th>
            <th>来源</th>
            <th>门店</th>
            <th>奖励</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in tasks" :key="t.id">
            <td>{{ t.title }}</td>
            <td>到店任务</td>
            <td>{{ sourceLabel(t.sourceType) }}</td>
            <td>{{ t.storeName || '-' }}</td>
            <td>{{ t.rewardValue }}积分</td>
            <td>{{ formatTime(t.startAt) }}</td>
            <td>{{ formatTime(t.endAt) }}</td>
            <td>
              <span class="tag" :class="t.effectiveStatus === 'ACTIVE' ? 'tag-on' : 'tag-off'">
                {{ statusLabel(t.effectiveStatus) }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm" @click="$router.push('/tasks/' + t.id + '/edit')">编辑</button>
              <button v-if="t.status === 'DRAFT'" class="btn btn-sm btn-primary" @click="change(t, 'ACTIVE')">启用</button>
              <button v-if="t.status === 'ACTIVE'" class="btn btn-sm btn-danger" @click="change(t, 'DISABLED')">停用</button>
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

const tasks = ref([]);
const loading = ref(true);

const SOURCE_LABELS = {
  ACTIVITY: '活动',
  RECOMMENDATION: '推荐',
  OPERATION: '运营',
  MERCHANT: '商户'
};
const STATUS_LABELS = {
  DRAFT: '草稿',
  ACTIVE: '进行中',
  DISABLED: '已停用',
  PENDING: '未开始',
  ENDED: '已结束'
};

onMounted(load);

function sourceLabel(v) {
  return SOURCE_LABELS[v] || v;
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
    const res = await request({ url: '/api/tasks?size=100' });
    tasks.value = res.data || [];
  } catch (err) {
    alert(err.message);
  } finally {
    loading.value = false;
  }
}

async function change(t, status) {
  try {
    await request({
      url: '/api/tasks/' + t.id + '/status',
      method: 'PATCH',
      data: { status }
    });
    await load();
  } catch (err) {
    alert(err.message);
  }
}
</script>
