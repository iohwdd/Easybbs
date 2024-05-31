<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="400px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form :model="formData" ref="formDataRef" label-width="120px">
        <!--input输入-->
        <el-form-item label="发布人">
          <Avatar :userId="formData.userId" :width="40"></Avatar>
          <span :style="{ 'margin-left': '5px' }">{{ formData.nickName }}</span>
        </el-form-item>

        <el-form-item label="文件名">{{ formData.fileName }}</el-form-item>
        <el-form-item label="大小">{{
          proxy.Utils.sizeToStr(formData.fileSize)
        }}</el-form-item>
        <el-form-item label="下载所需积分">{{
          formData.integral
        }}</el-form-item>
        <el-form-item label="">
          <a
            target="_blank"
            class="a-link"
            :href="'/api/forum/attachmentDownload?fileId=' + formData.fileId"
            >下载</a
          >
        </el-form-item>
      </el-form>
    </DiaLog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const dialogConfig = reactive({
  show: false,
  title: '标题',
  buttons: [
    {
      type: 'primary',
      text: '关闭',
      click: (e) => {
        dialogConfig.show = false
      }
    }
  ]
})
const api = {
  getAttachement: '/forum/getAttachment'
}
const formData = ref({})

const show = async (nickName, articleId) => {
  dialogConfig.show = true
  let result = await proxy.Request({
    url: api.getAttachement,
    params: {
      articleId
    }
  })
  if (!result) {
    return
  }
  formData.value = result.data
  formData.value.nickName = nickName
}
defineExpose({ show })
</script>

<style lang="scss" scoped></style>
