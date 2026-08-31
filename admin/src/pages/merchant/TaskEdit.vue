<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑任务' : '新增任务' }}</h2>
    <div class="card">
      <div class="form-row">
        <label>任务标题</label>
        <input v-model="form.title" placeholder="如：到店打卡庐州烤鸭店" />
      </div>
      <div class="form-row">
        <label>任务说明</label>
        <textarea v-model="form.description" rows="3" placeholder="到店出示任务核销码，由店员完成验证等说明"></textarea>
      </div>
      <div class="form-row">
        <label>任务类型</label>
        <select v-model="form.taskType">
          <option value="STORE_VISIT">到店任务</option>
        </select>
      </div>
      <div class="form-row">
        <label>来源类型</label>
        <select v-model="form.sourceType">
          <option value="MERCHANT">商户</option>
          <option value="ACTIVITY">活动</option>
          <option value="RECOMMENDATION">推荐</option>
          <option value="OPERATION">运营</option>
        </select>
      </div>
      <div class="form-row">
        <label>关联门店</label>
        <select v-model="form.storeId">
          <option :value="null">不关联门店（全街区）</option>
          <option v-for="s in stores" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>
      </div>
      <div class="form-row two-col">
        <div>
          <label>奖励积分</label>
          <input type="number" v-model.number="form.rewardValue" min="0" />
        </div>
        <div>
          <label>奖励类型</label>
          <input value="积分(POINT)" disabled />
        </div>
      </div>
      <div class="form-row two-col">
        <div>
          <label>开始时间</label>
          <input type="datetime-local" v-model="form.startAt" />
        </div>
        <div>
          <label>结束时间</label>
          <input type="datetime-local" v-model="form.endAt" />
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
  title: '',
  description: '',
  taskType: 'STORE_VISIT',
  sourceType: 'MERCHANT',
  storeId: null,
  rewardType: 'POINT',
  rewardValue: 10,
  startAt: '',
  endAt: ''
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
  // 管理视图：可加载 DRAFT/DISABLED 任务进行编辑（用户公开视图仅返回 ACTIVE）
  const t = await request({ url: '/api/tasks/' + id + '?management=true' });
  form.value = {
    title: t.title,
    description: t.description || '',
    taskType: t.taskType,
    sourceType: t.sourceType,
    storeId: t.storeId,
    rewardType: t.rewardType,
    rewardValue: t.rewardValue,
    startAt: t.startAt ? t.startAt.substring(0, 16) : '',
    endAt: t.endAt ? t.endAt.substring(0, 16) : ''
  };
}

async function save() {
  saving.value = true;
  try {
    const body = {
      title: form.value.title,
      description: form.value.description,
      taskType: form.value.taskType,
      sourceType: form.value.sourceType,
      storeId: form.value.storeId,
      rewardType: form.value.rewardType,
      rewardValue: form.value.rewardValue,
      startAt: toApiDateTime(form.value.startAt),
      endAt: toApiDateTime(form.value.endAt)
    };
    if (isEdit.value) {
      await request({ url: '/api/tasks/' + id, method: 'PUT', data: body });
    } else {
      await request({ url: '/api/tasks', method: 'POST', data: body });
    }
    alert('保存成功');
    router.push('/tasks');
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}
</script>
