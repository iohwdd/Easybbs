<template>
  <v-md-editor
    :model-value="modelValue"
    :height="height + 'px'"
    :disabled-menus="[]"
    :include-leave="[1, 2, 3, 4, 5, 6]"
    @upload-image="uploadImageHandler"
    @change="change"
  >
  </v-md-editor>
</template>

<script setup>
import VMdEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css' // 样式文件，根据需要导入
import hljs from 'highlight.js' // 导入 highlight.js

// github代码高亮风格
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()
VMdEditor.use(githubTheme, {
  Hljs: hljs
})
const props = defineProps({
  modelValue: {
    type: String,
    defalut: ''
  },
  height: {
    type: Number,
    defalut: 500
  }
})
const emit = defineEmits(['update:modelValue', 'htmlContent'])
const change = (markDownContent, htmlContent) => {
  emit('update:modelValue', markDownContent)
  emit('htmlContent', htmlContent)
}
const uploadImageHandler = async (event, insertImage, file) => {
  let result = await proxy.Request({
    url: 'file/uploadImage',
    params: {
      file: file[0]
    }
  })
  if (!result) {
    return
  }
  const url = proxy.globalInfo.imageUrl + result.data.fileName
  insertImage({
    url: url,
    desc: '图片'
  })
}
</script>

<style lang="scss" scoped></style>
