<template>
  <div>
    <h2 class="page-title">优惠券核销</h2>
    <div class="card">
      <div class="form-row">
        <label>核销码</label>
        <input
          v-model="redeemCode"
          placeholder="输入用户出示的核销码（兼容扫码枪输入）"
          @keyup.enter="redeem"
        />
      </div>
      <div class="form-actions">
        <button class="btn btn-primary" :disabled="redeeming || !redeemCode" @click="redeem">
          {{ redeeming ? '核销中…' : '核销' }}
        </button>
      </div>
    </div>

    <!-- 核销结果 -->
    <div v-if="result" class="card result-card">
      <div class="result-title result-ok">核销成功</div>
      <table class="table">
        <tbody>
          <tr><td>优惠券</td><td>{{ result.couponName }}</td></tr>
          <tr><td>权益</td><td>{{ result.discountText }}</td></tr>
          <tr><td>门店</td><td>{{ result.storeName }}</td></tr>
          <tr><td>核销码</td><td>{{ result.redeemCode }}</td></tr>
          <tr><td>核销时间</td><td>{{ formatTime(result.redeemedAt) }}</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import request from '../../api/request';

const redeemCode = ref('');
const redeeming = ref(false);
const result = ref(null);

// Demo 阶段说明：核销码手动输入为主要方式，扫码枪作为键盘输入设备直接录入文本框，
// 不实现 Web 摄像头扫码。
function formatTime(v) {
  if (!v) return '';
  return v.replace('T', ' ').substring(0, 19);
}

async function redeem() {
  redeeming.value = true;
  result.value = null;
  try {
    const res = await request({
      url: '/api/coupons/redeem',
      method: 'POST',
      data: { redeemCode: redeemCode.value.trim() }
    });
    result.value = res;
    redeemCode.value = '';
  } catch (err) {
    // 已核销/过期/停用/不存在等均为后端明确业务错误(4xx)，直接提示
    alert(err.message);
  } finally {
    redeeming.value = false;
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
