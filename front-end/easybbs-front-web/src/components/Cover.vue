<template>
  <div class="cover" :style="{ width: width + 'px', height: width + 'px' }">
    <el-image
      :style="{ width: width + 'px', height: width + 'px' }"
      fit="scale-down"
      loading="lazy"
      :src="proxy.globalInfo.imageUrl + cover"
    >
    </el-image>
  </div>
</template>

<script setup>
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  cover: {
    type: String
  },
  width: {
    type: Number,
    default: 60
  }
})
</script>

<style lang="scss" scoped>
.cover {
  background: #ddd;
  border-radius: 5px;
  overflow: hidden;
}
</style>
