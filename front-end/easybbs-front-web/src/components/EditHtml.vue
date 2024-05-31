<template>
  <div class="editor-html">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    ></Toolbar>
    <Editor
      :style="{ height: height + 'px', 'overflow-y': 'hidden' }"
      :model-value="modelValue"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="onChange"
    ></Editor>
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, ref, shallowRef } from 'vue'
import { Toolbar, Editor } from '@wangeditor/editor-for-vue'
import { useStore } from 'vuex'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()
const store = useStore()
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

const mode = ref('default')
const editorRef = shallowRef()

const toolbarConfig = {
  excludeKeys: ['uploadVideo']
}

const editorConfig = {
  placeholder: '请输入内容...',
  excludeKeys: ['uploadVideo'],
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 3 * 1024 * 1024,
      server: '/api/file/uploadImage', //请求地址
      fieldName: 'file', //请求字段名
      customInsert(responseData, insertFn) {
        if (responseData.code == 200) {
          // 插入图片到编辑器中
          insertFn(
            proxy.globalInfo.imageUrl + responseData.data.fileName,
            '',
            ''
          )
        } else if (responseData.code == 901) {
          store.commit('showLogin', true)
          store.commit('updateLoginUserInfo', null)
          return
        }
        proxy.Message.error(responseData.info)
      }
    }
  }
}
const emit = defineEmits(['update:modelValue'])
const onChange = (editor) => {
  emit('update:modelValue', editor.getHtml())
}
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy() //销毁编辑器
})
const handleCreated = (editor) => {
  editorRef.value = editor
}
</script>

<style lang="scss" scoped>
.editor-html {
  border: 1px solid #ddd;
  z-index: 1001; //这里不能太大，比导航条大1就可以了，太大会把element弹窗遮住
}
</style>
