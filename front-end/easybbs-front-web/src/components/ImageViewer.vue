<template>
  <div class="image-viewer">
    <el-image-viewer
      :url-list="imageList"
      :initial-index="previewImgIndex"
      hide-on-click-modal
      @close="closeImgViewer"
      v-if="previewImgIndex != null"
    >
    </el-image-viewer>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  imageList: {
    type: Array
  }
})
const previewImgIndex = ref(null)
const show = (index) => {
  stopScroll()
  previewImgIndex.value = index
}
defineExpose({ show })
// 关闭图片预览
const closeImgViewer = () => {
  startScroll()
  previewImgIndex.value = null
}
// 禁用滚动条
const stopScroll = () => {
  document.body.style.overflow = 'hidden'
}
// 启用滚动条
const startScroll = () => {
  document.body.style.overflow = 'auto'
}
</script>

<style lang="scss" scoped></style>
