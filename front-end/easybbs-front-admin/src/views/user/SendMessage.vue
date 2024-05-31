<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="500px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
      >
        <!--textarea输入-->
        <el-form-item label="消息内容" prop="message">
          <el-input
            clearable
            placeholder="请输入消息内容"
            type="textarea"
            :rows="5"
            :maxlength="200"
            resize="none"
            show-word-limit
            v-model.trim="formData.message"
          ></el-input>
        </el-form-item>

        <!--input输入-->
        <el-form-item label="积分" prop="integral">
          <el-input
            clearable
            placeholder="积分，为空或者0不增减积分，可以为负数"
            v-model.trim="formData.integral"
          ></el-input>
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
const api = {
  sendMessage: '/user/sendMessage'
}
const dialogConfig = reactive({
  show: false,
  title: '标题',
  buttons: [
    {
      type: 'danger',
      text: '确定',
      click: (e) => {
        submitForm()
      }
    }
  ]
})
const formData = ref({})
const formDataRef = ref()
const rules = {
  message: [{ required: true, message: '请输入消息内容' }]
}
const sendMessage = (data) => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = {
      userId: data.userId,
      nickName: data.nickName
    }
  })
}
defineExpose({ sendMessage })

const emit = defineEmits(['reload'])
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    let result = await proxy.Request({
      url: api.sendMessage,
      params
    })
    if (!result) {
      return
    }
    dialogConfig.show = false
    proxy.Message.success('发送成功')
    emit('reload')
  })
}
</script>

<style lang="scss" scoped></style>
