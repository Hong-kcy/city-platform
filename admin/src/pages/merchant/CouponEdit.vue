<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑优惠券' : '新增优惠券' }}</h2>
    <div class="card">
      <div class="form-row">
        <label>所属门店</label>
        <select v-model="form.storeId" :disabled="isEdit">
          <option :value="null">请选择门店</option>
          <option v-for="s in stores" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>
      </div>
      <div class="form-row">
        <label>优惠券名称</label>
        <input v-model="form.name" placeholder="如：烤鸭满减券" />
      </div>
      <div class="form-row">
        <label>权益文本</label>
        <input v-model="form.discountText" placeholder="如：满100减20" />
      </div>
      <div class="form-row">
        <label>使用说明</label>
        <textarea v-model="form.description" rows="3" placeholder="到店出示核销码等使用规则"></textarea>
      </div>
      <div class="form-row two-col">
        <div>
          <label>有效期开始</label>
          <input type="datetime-local" v-model="form.validFrom" />
        </div>
        <div>
          <label>有效期结束</label>
          <input type="datetime-local" v-model="form.validTo" />
        </div>
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

const route = useRoute();
const router = useRouter();

const id = route.params.id;
const isEdit = computed(() => !!id);

const stores = ref([]);
const form = ref({
  storeId: null,
  name: '',
  discountText: '',
  description: '',
  validFrom: '',
  validTo: ''
});
const saving = ref(false);

onMounted(load);

// datetime-local 值为 yyyy-MM-ddTHH:mm，补足秒后交给后端 LocalDateTime 解析
function toApiDateTime(v) {
  if (!v) return null;
  return v + ':00';
}

async function load() {
  const sRes = await request({ url: '/api/stores?size=100' });
  stores.value = sRes.data || [];

  if (!isEdit.value) return;
  // 管理视图：可加载 INACTIVE 优惠券进行编辑（用户公开视图仅返回 ACTIVE）
  const c = await request({ url: '/api/coupons/' + id + '?management=true' });
  form.value = {
    storeId: c.storeId,
    name: c.name,
    discountText: c.discountText,
    description: c.description || '',
    validFrom: c.validFrom ? c.validFrom.substring(0, 16) : '',
    validTo: c.validTo ? c.validTo.substring(0, 16) : ''
  };
}

async function save() {
  saving.value = true;
  try {
    const body = {
      name: form.value.name,
      discountText: form.value.discountText,
      description: form.value.description,
      validFrom: toApiDateTime(form.value.validFrom),
      validTo: toApiDateTime(form.value.validTo)
    };
    if (isEdit.value) {
      await request({ url: '/api/coupons/' + id, method: 'PUT', data: body });
    } else {
      await request({ url: '/api/coupons', method: 'POST', data: { ...body, storeId: form.value.storeId } });
    }
    alert('保存成功');
    router.push('/coupons');
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}
</script>
