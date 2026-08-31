<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑路线' : '新增路线' }}</h2>
    <div class="card">
      <div class="form-row">
        <label>路线名称</label>
        <input v-model="form.name" placeholder="如：闺蜜出片半日游" />
      </div>
      <div class="form-row">
        <label>所属街区</label>
        <select v-model="form.streetAreaId" :disabled="isEdit">
          <option :value="null">请选择街区</option>
          <option v-for="sa in streetAreas" :key="sa.id" :value="sa.id">{{ sa.name }}</option>
        </select>
        <span v-if="isEdit" class="hint">路线归属街区创建后不可变更</span>
      </div>
      <div class="form-row">
        <label>路线主题</label>
        <select v-model="form.theme">
          <option value="">请选择主题</option>
          <option v-for="t in themes" :key="t" :value="t">{{ themeLabel(t) }}</option>
        </select>
      </div>
      <div class="form-row">
        <label>预计时长(分钟)</label>
        <input v-model.number="form.estimatedDuration" type="number" min="1" />
      </div>
      <div class="form-row">
        <label>路线描述</label>
        <textarea v-model="form.description" rows="3"></textarea>
      </div>
      <div class="form-actions">
        <button class="btn" @click="$router.back()">取消</button>
        <button class="btn btn-primary" :disabled="saving" @click="save">
          {{ saving ? '保存中…' : '保存' }}
        </button>
      </div>
    </div>

    <!-- POI 顺序管理（仅编辑已有路线时可用，先保存基本信息） -->
    <div v-if="isEdit" class="card">
      <h3 class="section-title">路线 POI（按体验顺序）</h3>

      <div class="poi-add-row">
        <select v-model="addPoiId">
          <option :value="null">选择要添加的 POI</option>
          <option v-for="p in candidatePois" :key="p.id" :value="p.id">
            {{ p.name }}（{{ p.poiType }}）
          </option>
        </select>
        <input v-model="addReason" placeholder="推荐理由（可选）" />
        <button class="btn" :disabled="!addPoiId || poiSaving" @click="addPoi">添加</button>
      </div>

      <div v-if="items.length === 0" class="empty">尚未添加 POI</div>
      <table v-else class="table">
        <thead>
          <tr>
            <th>顺序</th>
            <th>POI</th>
            <th>类型</th>
            <th>关联门店</th>
            <th>推荐理由</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(it, idx) in items" :key="it.poiId">
            <td>{{ idx + 1 }}</td>
            <td>{{ it.poiName }}</td>
            <td>{{ it.poiType }}</td>
            <td>{{ it.storeName || '—' }}</td>
            <td>{{ it.recommendationReason || '—' }}</td>
            <td>
              <button class="btn btn-sm" :disabled="idx === 0 || poiSaving" @click="move(idx, -1)">上移</button>
              <button class="btn btn-sm" :disabled="idx === items.length - 1 || poiSaving" @click="move(idx, 1)">下移</button>
              <button class="btn btn-sm btn-danger" :disabled="poiSaving" @click="removePoi(it)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
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

const themes = ['FRIEND_PHOTO', 'SOLO_RELAX', 'FAMILY_FUN', 'SLOW_WALK', 'OTHER'];
const THEME_LABELS = {
  FRIEND_PHOTO: '闺蜜出片',
  SOLO_RELAX: '社恐友好',
  FAMILY_FUN: '亲子放电',
  SLOW_WALK: '周末慢逛',
  OTHER: '其他'
};

const streetAreas = ref([]);
const form = ref({
  streetAreaId: null,
  name: '',
  theme: '',
  description: '',
  estimatedDuration: null
});
const saving = ref(false);

// POI 管理
const items = ref([]);
const candidatePois = ref([]);
const addPoiId = ref(null);
const addReason = ref('');
const poiSaving = ref(false);

onMounted(load);

function themeLabel(v) {
  return THEME_LABELS[v] || v;
}

async function load() {
  const saRes = await request({ url: '/api/street-areas?size=100' });
  streetAreas.value = saRes.data || [];

  if (!isEdit.value) return;
  await loadDetail();
}

async function loadDetail() {
  // 管理视图：可加载 INACTIVE 路线进行编辑（用户公开视图仅返回 ACTIVE）
  const d = await request({ url: '/api/experience-routes/' + id + '?management=true' });
  form.value = {
    streetAreaId: d.streetAreaId,
    name: d.name,
    theme: d.theme,
    description: d.description || '',
    estimatedDuration: d.estimatedDuration
  };
  items.value = d.items || [];
  await loadCandidates(d.streetAreaId);
}

// 候选 POI：同街区 ACTIVE 且尚未加入路线
async function loadCandidates(streetAreaId) {
  const res = await request({
    url: '/api/pois?streetAreaId=' + streetAreaId + '&status=ACTIVE&size=100'
  });
  const inRoute = new Set(items.value.map((it) => it.poiId));
  candidatePois.value = (res.data || []).filter((p) => !inRoute.has(p.id));
}

async function save() {
  saving.value = true;
  try {
    const body = {
      name: form.value.name,
      theme: form.value.theme,
      description: form.value.description,
      estimatedDuration: form.value.estimatedDuration
    };
    if (isEdit.value) {
      await request({ url: '/api/experience-routes/' + id, method: 'PUT', data: body });
      alert('保存成功');
      await loadDetail();
    } else {
      await request({
        url: '/api/experience-routes',
        method: 'POST',
        data: Object.assign({ streetAreaId: form.value.streetAreaId }, body)
      });
      alert('创建成功，可继续添加路线 POI');
      router.push('/routes');
    }
  } catch (err) {
    alert(err.message);
  } finally {
    saving.value = false;
  }
}

async function addPoi() {
  poiSaving.value = true;
  try {
    const d = await request({
      url: '/api/experience-routes/' + id + '/pois',
      method: 'POST',
      data: { poiId: addPoiId.value, recommendationReason: addReason.value || null }
    });
    items.value = d.items || [];
    addPoiId.value = null;
    addReason.value = '';
    await loadCandidates(d.streetAreaId);
  } catch (err) {
    alert(err.message);
  } finally {
    poiSaving.value = false;
  }
}

async function removePoi(it) {
  if (!confirm('确认将「' + it.poiName + '」移出路线？')) return;
  poiSaving.value = true;
  try {
    const d = await request({
      url: '/api/experience-routes/' + id + '/pois/' + it.poiId,
      method: 'DELETE'
    });
    items.value = d.items || [];
    await loadCandidates(d.streetAreaId);
  } catch (err) {
    alert(err.message);
  } finally {
    poiSaving.value = false;
  }
}

// 上移/下移：交换相邻两项后提交全量顺序
async function move(idx, delta) {
  const next = items.value.slice();
  const target = idx + delta;
  const tmp = next[idx];
  next[idx] = next[target];
  next[target] = tmp;
  poiSaving.value = true;
  try {
    const d = await request({
      url: '/api/experience-routes/' + id + '/pois/order',
      method: 'PUT',
      data: { poiIds: next.map((it) => it.poiId) }
    });
    items.value = d.items || [];
  } catch (err) {
    alert(err.message);
  } finally {
    poiSaving.value = false;
  }
}
</script>

<style scoped>
.section-title {
  font-size: 15px;
  margin-bottom: 12px;
}

.poi-add-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.poi-add-row select {
  width: 260px;
}

.poi-add-row input {
  flex: 1;
}

.hint {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}
</style>
