<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="450px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        label-width="40px"
        @submit.prevent
        ref="formDataRef"
      >
        <!--input输入-->
        <el-form-item label="昵称" prop="nickName">
          <el-input
            v-model="formData.nickName"
            :value="formData.nickName"
          ></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <CoverUpload
            :imageUrlPrefix="proxy.globalInfo.avatarUrl"
            v-model="formData.avatar"
          ></CoverUpload>
        </el-form-item>
        <!-- 单选 -->
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="formData.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <!--textarea输入-->
        <el-form-item label="简介" prop="personDescription">
          <el-input
            clearable
            placeholder="请输入你的简介"
            type="textarea"
            :rows="5"
            :maxlength="100"
            resize="none"
            show-word-limit
            v-model="formData.personDescription"
          ></el-input>
        </el-form-item>
      </el-form>
    </DiaLog>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const api = {
  updateUserInfo: '/ucenter/updateUserInfo'
}
const formData = ref({})
const formDataRef = ref()
const router = useRouter()
const dialogConfig = reactive({
  show: false,
  title: '编辑个人信息',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        updateUserInfoHandler()
      }
    }
  ]
})
const emit = defineEmits(['resetUserInfo'])
const updateUserInfoHandler = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    let result = await proxy.Request({
      url: api.updateUserInfo,
      params: params
    })
    if (!result) {
      return
    }
    dialogConfig.show = false
    if (params.avatar instanceof File) {
      router.go(0) //刷新页面
    } else {
      emit('resetUserInfo', params)
    }
  })
}
// 显示修改个人信息弹窗
const showEditUserInfoDialog = (userInfo) => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    const dataInfo = JSON.parse(JSON.stringify(userInfo))
    dataInfo.avatar = {
      imageUrl: dataInfo.userId
    }
    formData.value = dataInfo
  })
}
defineExpose({ showEditUserInfoDialog })
</script>

<style lang="scss" scoped></style>
