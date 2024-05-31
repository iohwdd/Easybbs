<template>
  <div class="cover-upload">
    <el-upload
      name="file"
      :show-file-list="false"
      accept=".mp,.jpg,.png,.tif,.gif,.pcx,.tga,.exif,.fpx,.svg,.psd,.cdr,.pcd,.dxf,.ufo,.eps,.ai,.raw,.WMF,.webp,.avif,.apng "
      :multiple="false"
      :http-request="uploadImage"
    >
      <div class="cover-upload-btn">
        <!-- 上传图片 -->
        <template v-if="localFile">
          <img :src="localFile" />
        </template>
        <!-- 本地图片 -->
        <template v-else>
          <img
            :src="
              (imageUrlPrefix ? imageUrlPrefix : proxy.globalInfo.imageUrl) +
              modelValue.imageUrl
            "
            v-if="modelValue && modelValue.imageUrl"
          />
          <span class="iconfont icon-add" v-else></span>
        </template>
      </div>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  modelValue: {
    type: Object,
    default: null
  },
  imageUrlPrefix: {
    type: String
  }
})
const localFile = ref(null)
watch(
  () => props.modelValue,
  (newVal, oldVal) => {
    localFile.value = null
  },
  { immediate: true, deep: true }
)

const emit = defineEmits(['update:modelValue'])
const uploadImage = (file) => {
  file = file.file
  let img = new FileReader()
  img.readAsDataURL(file)
  img.onload = ({ target }) => {
    localFile.value = target.result
  }
  emit('update:modelValue', file)
}
</script>

<style lang="scss" scoped>
.cover-upload {
  .cover-upload-btn {
    width: 150px;
    height: 150px;
    display: flex;
    overflow: hidden;
    align-items: center;
    justify-content: center;
    background: #f8f6f6;
    .iconfont {
      font-size: 50px;
      color: #ddd;
    }
    img {
      width: 100%;
    }
  }
}
</style>
