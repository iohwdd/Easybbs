<template>
  <div class="attachment-selector">
    <div class="attachment-show">
      <template v-if="modelValue">
        <div class="file-name" :title="modelValue.name">
          {{ modelValue.name }}
        </div>
        <div class="iconfont icon-del" @click="delFile"></div>
      </template>
      <el-upload
        v-else
        name="file"
        :show-file-list="false"
        accept=".zip,.rar"
        :multiple="false"
        :http-request="selectFile"
      >
        <el-button type="primary">选择文件</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  modelValue: {
    type: Object,
    default: null
  }
})
const emit = defineEmits(['update:modelValue'])
const selectFile = (file) => {
  emit('update:modelValue', file.file)
}
const delFile = () => {
  emit('update:modelValue', null)
}
</script>

<style lang="scss" scoped>
.attachment-selector {
  width: 100%;
  .attachment-show {
    display: flex;
    .file-name {
      color: var(--link);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      flex: 1;
    }
    .icon-del {
      width: 20px;
      color: var(--link);
      cursor: pointer;
    }
  }
}
</style>
