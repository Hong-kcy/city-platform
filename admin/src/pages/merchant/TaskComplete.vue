<template>
  <div>
    <h2 class="page-title">任务完成验证</h2>
    <div class="card">
      <div class="form-row">
        <label>任务核销码</label>
        <input
          v-model="taskCode"
          placeholder="输入用户出示的任务核销码（兼容扫码枪输入）"
          @keyup.enter="complete"
        />
      </div>
      <div class="form-actions">
        <button class="btn btn-primary" :disabled="completing || !taskCode" @click="complete">
          {{ completing ? '验证中…' : '完成任务' }}
        </button>
      </div>
    </div>

    <!-- 完成结果 -->
    <div v-if="result" class="card result-card">
      <div class="result-title result-ok">任务完成，奖励已发放</div>
      <table class="table">
        <tbody>
          <tr><td>任务</td><td>{{ result.title }}</td></tr>
          <tr><td>门店</td><td>{{ result.storeName || '-' }}</td></tr>
          <tr><td>奖励</td><td>{{ result.rewardValue }} 积分</td></tr>
          <tr><td>核销码</td><td>{{ taskCodeDone }}</td></tr>
          <tr><td>完成时间</td><td>{{ formatTime(result.completedAt) }}</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import request from '../../api/request';

const taskCode = ref('');
const taskCodeDone = ref('');
const completing = ref(false);
const result = ref(null);

// 到店任务 Demo 阶段验证方式：用户出示任务核销码，商户输入完成验证；
// 不做 GPS/围栏判断，不实现 Web 摄像头扫码。
function formatTime(v) {
  if (!v) return '';
  return v.replace('T', ' ').substring(0, 19);
}

async function complete() {
  completing.value = true;
  result.value = null;
  try {
    const res = await request({
      url: '/api/tasks/complete',
      method: 'POST',
      data: { taskCode: taskCode.value.trim() }
    });
    result.value = res;
    taskCodeDone.value = taskCode.value.trim().toUpperCase();
    taskCode.value = '';
  } catch (err) {
    // 已完成/未启用/未开始/已结束/不存在等均为后端明确业务错误(4xx)，直接提示
    alert(err.message);
  } finally {
    completing.value = false;
  }
}
</script>

<style scoped>
.result-card {
  margin-top: 24px;
}

.result-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}

.result-ok {
  color: #16a085;
}
</style>
