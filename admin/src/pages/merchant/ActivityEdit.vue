<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑活动' : '新增活动' }}</h2>
    <div class="card">
      <div class="form-row">
        <label>活动标题</label>
        <input v-model="form.title" />
      </div>
      <div class="form-row">
        <label>所属街区</label>
        <select v-model="form.streetAreaId">
          <option :value="null">请选择街区</option>
          <option v-for="sa in streetAreas" :key="sa.id" :value="sa.id">{{ sa.name }}</option>
        </select>
      </div>
      <div class="form-row">
        <label>活动类型</label>
        <select v-model="form.activityType">
          <option value="">请选择类型</option>
          <option v-for="t in activityTypes" :key="t" :value="t">{{ typeLabel(t) }}</option>
        </select>
      </div>
      <div class="form-row">
        <label>活动摘要</label>
        <textarea v-model="form.summary" rows="2"></textarea>
      </div>
      <div class="form-row">
        <label>活动详情</label>
        <textarea v-model="form.description" rows="4"></textarea>
      </div>
      <div class="form-row">
        <label>活动封面</label>
        <ImageUpload v-model="form.coverFileId" :initial-url="initialCoverUrl" />
      </div>
      <div class="form-row two-col">
        <div>
          <label>开始时间</label>
          <input type="datetime-local" v-model="form.startTime" />
        </div>
        <div>
          <label>结束时间</label>
          <input type="datetime-local" v-model="form.endTime" />
        </div>
      </div>
      <div class="form-row">
        <label>活动地点</label>
        <input v-model="form.location" />
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
import ImageUpload from '../../components/ImageUpload.vue';

const route = useRoute();
const router = useRouter();

const id = route.params.id;
const isEdit = computed(() => !!id);

const activityTypes = ['FESTIVAL', 'PERFORMANCE', 'EXHIBITION', 'PROMOTION', 'CULTURE', 'OTHER'];
const TYPE_LABELS = {
  FESTIVAL: '节庆',
  PERFORMANCE: '演出',
  EXHIBITION: '展览',
  PROMOTION: '促销',
  CULTURE: '文化',
  OTHER: '其他'
};

const streetAreas = ref([]);
const form = ref({
  streetAreaId: null,
  title: '',
  summary: '',
  description: '',
  coverFileId: null,
  activityType: '',
  startTime: '',
  endTime: '',
  location: ''
});
const initialCoverUrl = ref('');
const saving = ref(false);

onMounted(load);

function typeLabel(v) {
  return TYPE_LABELS[v] || v;
}

// datetime-local 值为 yyyy-MM-ddTHH:mm，补足秒后交给后端 LocalDateTime 解析
function toApiDateTime(v) {
  if (!v) return null;
  return v + ':00';
}

async function load() {
  const saRes = await request({ url: '/api/street-areas?size=100' });
  streetAreas.value = saRes.data || [];

  if (!isEdit.value) return;
  // 管理视图：可加载 DRAFT/OFFLINE 活动进行编辑（用户公开视图仅返回 PUBLISHED）
  const a = await request({ url: '/api/activities/' + id + '?management=true' });
  form.value = {
    streetAreaId: a.streetAreaId,
    title: a.title,
    summary: a.summary || '',
    description: a.description || '',
    coverFileId: a.coverFileId,
    activityType: a.activityType,
    startTime: a.startTime ? a.startTime.substring(0, 16) : '',
    endTime: a.endTime ? a.endTime.substring(0, 16) : '',
    location: a.location || ''
  };
  initialCoverUrl.value = a.coverImageUrl;
}

async function save() {
  saving.value = true;
  try {
    const body = {
      streetAreaId: form.value.streetAreaId,
      title: form.value.title,
      summary: form.value.summary,
      description: form.value.description,
      coverFileId: form.value.coverFileId,
      activityType: form.value.activityType,
      startTime: toApiDateTime(form.value.startTime),
      endTime: toApiDateTime(form.value.endTime),
      location: form.value.location
    };
    if (isEdit.value) {
      await request({ url: '/api/activities/' + id, method: 'PUT', data: body });
    } else {
      await request({ url: '/api/activities', method: 'POST', data: body });
    }
    alert('保存成功');
    router.push('/activities');
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}
</script>