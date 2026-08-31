<template>
  <div>
    <h2 class="page-title">商户信息</h2>
    <div v-if="!merchant" class="empty">加载中…</div>
    <div v-else class="card">
      <div class="form-row">
        <label>商户名称</label>
        <input v-model="form.name" />
      </div>
      <div class="form-row">
        <label>商户类型</label>
        <select v-model="form.type">
          <option v-for="t in types" :key="t" :value="t">{{ t }}</option>
        </select>
      </div>
      <div class="form-row">
        <label>联系人</label>
        <input v-model="form.contactPerson" />
      </div>
      <div class="form-row">
        <label>联系电话</label>
        <input v-model="form.contactPhone" />
      </div>
      <div class="form-row">
        <label>商户简介</label>
        <textarea v-model="form.introduction" rows="4"></textarea>
      </div>
      <div class="form-row">
        <label>商户 Logo</label>
        <ImageUpload v-model="form.logoFileId" :initial-url="merchant.logoUrl" />
      </div>
      <div class="form-row">
        <span class="tag">状态：{{ merchant.status }}</span>
      </div>
      <button class="btn btn-primary" :disabled="saving" @click="save">{{ saving ? '保存中…' : '保存' }}</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import request from '../../api/request';
import { getSession } from '../../utils/session';
import ImageUpload from '../../components/ImageUpload.vue';

const types = ['FOOD', 'RETAIL', 'ENTERTAINMENT', 'SERVICE', 'OTHER'];
const merchant = ref(null);
const form = ref({});
const saving = ref(false);

onMounted(load);

async function load() {
  const session = getSession();
  merchant.value = await request({ url: '/api/merchants/' + session.merchantId });
  form.value = {
    name: merchant.value.name,
    type: merchant.value.type,
    contactPerson: merchant.value.contactPerson,
    contactPhone: merchant.value.contactPhone,
    introduction: merchant.value.introduction,
    logoFileId: merchant.value.logoFileId
  };
}

async function save() {
  saving.value = true;
  try {
    const session = getSession();
    const updated = await request({
      url: '/api/merchants/' + session.merchantId,
      method: 'PUT',
      data: form.value
    });
    merchant.value = updated;
    form.value = {
      name: updated.name,
      type: updated.type,
      contactPerson: updated.contactPerson,
      contactPhone: updated.contactPhone,
      introduction: updated.introduction,
      logoFileId: updated.logoFileId
    };
    alert('保存成功');
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}
</script>

<style scoped>
.page-title {
  margin: 0 0 16px;
  font-size: 20px;
}
</style>