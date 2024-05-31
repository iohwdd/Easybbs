<template>
  <div class="comment-img">
    <el-image
      :src="src"
      :zoom-rate="1.2"
      :preview-src-list="imgList ? imgList : [src]"
      :initial-index="0"
      fit="scale-down"
      loading="lazy"
      hide-on-click-modal
    />
  </div>
</template>

<script setup>
const props = defineProps({
  src: {
    type: String
  },
  imgList: {
    type: Array
  }
})
</script>

<style lang="scss" scoped>
.comment-img {
  width: 80px;
  height: 80px;
  background: #eee;
}
</style>
