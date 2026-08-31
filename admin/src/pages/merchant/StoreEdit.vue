<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑门店' : '新增门店' }}</h2>
    <div class="card">
      <div class="form-row">
        <label>门店名称</label>
        <input v-model="form.name" />
      </div>
      <div class="form-row">
        <label>门店地址</label>
        <input v-model="form.address" />
      </div>
      <div class="form-row two-col">
        <div>
          <label>经度</label>
          <input v-model="form.longitude" />
        </div>
        <div>
          <label>纬度</label>
          <input v-model="form.latitude" />
        </div>
      </div>
      <div class="form-row">
        <label>联系电话</label>
        <input v-model="form.phone" />
      </div>
      <div class="form-row">
        <label>营业时间</label>
        <input v-model="form.businessHours" placeholder="如 09:00-22:00" />
      </div>
      <div class="form-row">
        <label>门店图片</label>
        <ImageUpload v-model="form.coverImageFileId" :initial-url="initialCoverUrl" />
      </div>
      <div class="form-actions">
        <button class="btn" @click="$router.back()">取消</button>
        <button class="btn btn-primary" :disabled="saving" @click="save">
          {{ saving ? '保存中…' : '保存' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import request from '../../api/request';
import { getSession } from '../../utils/session';
import ImageUpload from '../../components/ImageUpload.vue';

const route = useRoute();
const router = useRouter();

const id = route.params.id;
const isEdit = computed(() => !!id);

const form = ref({
  name: '',
  address: '',
  longitude: '',
  latitude: '',
  phone: '',
  businessHours: '',
  coverImageFileId: null
});
const initialCoverUrl = ref('');
const saving = ref(false);

onMounted(load);

async function load() {
  if (!isEdit.value) return;
  const s = await request({ url: '/api/stores/' + id });
  form.value = {
    name: s.name,
    address: s.address,
    longitude: s.longitude != null ? String(s.longitude) : '',
    latitude: s.latitude != null ? String(s.latitude) : '',
    phone: s.phone || '',
    businessHours: s.businessHours || '',
    coverImageFileId: s.coverImageFileId
  };
  initialCoverUrl.value = s.coverImageUrl;
}

async function save() {
  saving.value = true;
  try {
    const body = {
      name: form.value.name,
      address: form.value.address,
      longitude: form.value.longitude === '' ? null : Number(form.value.longitude),
      latitude: form.value.latitude === '' ? null : Number(form.value.latitude),
      phone: form.value.phone,
      businessHours: form.value.businessHours,
      coverImageFileId: form.value.coverImageFileId
    };
    if (isEdit.value) {
      await request({ url: '/api/stores/' + id, method: 'PUT', data: body });
    } else {
      const session = getSession();
      await request({
        url: '/api/merchants/' + session.merchantId + '/stores',
        method: 'POST',
        data: body
      });
    }
    alert('保存成功');
    router.push('/stores');
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}
</script>