<template>
  <div class="image-upload">
    <div class="preview" v-if="previewUrl">
      <img :src="previewUrl" alt="preview" />
    </div>
    <div class="preview empty" v-else>暂无图片</div>
    <input type="file" accept="image/*" @change="onChange" />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { uploadFile, resolveImageUrl } from '../api/request';

const props = defineProps({
  modelValue: { type: [Number, String], default: null },
  initialUrl: { type: String, default: '' }
});
const emit = defineEmits(['update:modelValue']);

const previewUrl = ref(resolveImageUrl(props.initialUrl));

watch(() => props.initialUrl, (val) => {
  previewUrl.value = resolveImageUrl(val);
});

async function onChange(e) {
  const file = e.target.files && e.target.files[0];
  if (!file) return;
  try {
    const result = await uploadFile(file);
    previewUrl.value = resolveImageUrl(result.url);
    emit('update:modelValue', result.id);
  } catch (err) {
    alert(err.message);
  }
}
</script>

<style scoped>
.preview {
  width: 160px;
  height: 120px;
  border: 1px dashed #ccc;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview.empty {
  color: #bbb;
  font-size: 12px;
}
</style>