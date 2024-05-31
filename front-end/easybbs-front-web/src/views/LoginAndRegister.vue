<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="400px"
      :showCancel="false"
      @close="closeDilaog()"
    >
      <el-form
        class="login-register"
        :model="formData"
        :rules="rules"
        ref="formDataRef"
      >
        <!--input输入-->
        <el-form-item prop="email">
          <el-input
            size="large"
            clearable
            placeholder="请输入邮箱"
            v-model.trim="formData.email"
          >
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 登录密码 -->
        <el-form-item prop="password" v-if="opType == 1">
          <el-input
            :type="passwordEyeType.passwordEyeOpen ? 'text' : 'password'"
            size="large"
            placeholder="请输入密码"
            v-model.trim="formData.password"
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
            <template #suffix>
              <span
                @click="eyeChange('passwordEyeOpen')"
                :class="[
                  'iconfont',
                  passwordEyeType.passwordEyeOpen
                    ? 'icon-eye'
                    : 'icon-close-eye'
                ]"
              ></span>
            </template>
          </el-input>
        </el-form-item>

        <!-- 注册 -->
        <div v-if="opType == 0 || opType == 2">
          <el-form-item prop="emailCode">
            <div class="send-email-panel">
              <el-input
                size="large"
                clearable
                placeholder="请输入邮箱验证码"
                v-model.trim="formData.emailCode"
              >
                <template #prefix>
                  <span class="iconfont icon-checkcode"></span>
                </template>
              </el-input>
              <el-button
                type="primary"
                size="large"
                class="send-email-btn"
                @click="showSendEmailDialog()"
                >获取验证码</el-button
              >
            </div>
            <el-popover placement="left" :width="450" trigger="click">
              <div>
                <p>1、在垃圾箱中查找邮箱验证码</p>
                <p>
                  2、在邮箱中 头像->设置->反垃圾->白名单->设置邮件地址白名单
                </p>
                <p>3、将邮箱【2023321332@qq.com】添加到白名单</p>
              </div>
              <template #reference>
                <span class="a-link" :style="{ 'font-size': '14px' }"
                  >未收到邮箱验证码？</span
                >
              </template>
            </el-popover>
          </el-form-item>
          <el-form-item prop="nickName" v-if="opType == 0">
            <el-input
              size="large"
              clearable
              placeholder="请输入昵称"
              v-model.trim="formData.nickName"
              maxlength="20"
            >
              <template #prefix>
                <span class="iconfont icon-account"></span>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="registerPassword">
            <el-input
              :type="
                passwordEyeType.registerPasswordEyeOpen ? 'text' : 'password'
              "
              size="large"
              placeholder="请输入密码"
              v-model.trim="formData.registerPassword"
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
              <template #suffix>
                <span
                  @click="eyeChange('registerPasswordEyeOpen')"
                  :class="[
                    'iconfont',
                    passwordEyeType.registerPasswordEyeOpen
                      ? 'icon-eye'
                      : 'icon-close-eye'
                  ]"
                ></span>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="reRegisterPassword">
            <el-input
              :type="
                passwordEyeType.reRegisterPasswordEyeOpen ? 'text' : 'password'
              "
              size="large"
              placeholder="请再次输入密码"
              v-model.trim="formData.reRegisterPassword"
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
              <template #suffix>
                <span
                  @click="eyeChange('reRegisterPasswordEyeOpen')"
                  :class="[
                    'iconfont',
                    passwordEyeType.reRegisterPasswordEyeOpen
                      ? 'icon-eye'
                      : 'icon-close-eye'
                  ]"
                ></span>
              </template>
            </el-input>
          </el-form-item>
        </div>
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              clearable
              placeholder="请输入验证码"
              v-model.trim="formData.checkCode"
              @keyup.enter="doSubmit"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrl"
              class="check-code"
              @click="changeCheckCode(0)"
            />
          </div>
        </el-form-item>
        <el-form-item v-if="opType == 1">
          <div class="rememberme-panel">
            <el-checkbox v-model="formData.rememberMe">记住我</el-checkbox>
          </div>
          <div class="no-account">
            <a href="javascript:void(0)" class="a-link" @click="showPanel(2)"
              >忘记密码?</a
            >
            <a href="javascript:void(0)" class="a-link" @click="showPanel(0)"
              >没有账号?</a
            >
          </div>
        </el-form-item>
        <el-form-item v-if="opType == 0">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)"
            >已有账号?</a
          >
        </el-form-item>
        <el-form-item v-if="opType == 2">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)"
            >去登录?</a
          >
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="op-btn" @click="doSubmit">
            <span v-if="opType == 0">注册</span>
            <span v-if="opType == 1">登录</span>
            <span v-if="opType == 2">重置密码</span>
          </el-button>
        </el-form-item>
      </el-form>
    </DiaLog>
    <DiaLog
      :show="dialogConfig4SendMailCode.show"
      :title="dialogConfig4SendMailCode.title"
      :buttons="dialogConfig4SendMailCode.buttons"
      width="500px"
      :showCancel="false"
      @close="dialogConfig4SendMailCode.show = false"
    >
      <el-form
        class="login-register"
        :model="formData4SendMailCode"
        :rules="rules"
        ref="formData4SendMailCodeRef"
        label-width="80px"
        @submit.prevent
      >
        <el-form-item label="邮箱">
          {{ formData.email }}
        </el-form-item>
        <el-form-item label="验证码" prop="checkCode">
          <!--input输入-->
          <div class="check-code-panel">
            <el-input
              size="large"
              placeholder="请输入验证码"
              v-model.trim="formData4SendMailCode.checkCode"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrl4SendMailCode"
              class="check-code"
              @click="changeCheckCode(1)"
            />
          </div>
        </el-form-item>
      </el-form>
    </DiaLog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import md5 from 'js-md5'
const { proxy } = getCurrentInstance()
const store = useStore()
const router = useRouter()
const route = useRoute()

// 登录/注册/重置密码 弹窗
const showPanel = (type) => {
  opType.value = type
  // 每次弹窗都要对表单进行一次数据重置操作，以免旧数据残留
  resetForm()
}
// 对外暴露showPanel显示弹窗方法
defineExpose({
  showPanel
})
// 请求路径
const api = {
  checkCode: '/api/checkCode',
  sendEmailCode: '/sendEmailCode',
  register: '/register',
  login: '/login',
  resetPwd: '/resetPwd'
}
// 登录/注册的验证码
const checkCodeUrl = ref(api.checkCode)
// 发送邮箱验证码时所需要的验证码
const checkCodeUrl4SendMailCode = ref(api.checkCode)
// 刷新验证码
const changeCheckCode = (type) => {
  // 登录/注册弹窗的验证码 与 发送邮箱弹窗的验证码 分开管理
  if (type == 0) {
    checkCodeUrl.value =
      api.checkCode + '?type=' + type + '&time=' + new Date().getTime()
  } else {
    checkCodeUrl4SendMailCode.value =
      api.checkCode + '?type=' + type + '&time=' + new Date().getTime()
  }
}
// 校验重复密码
const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.registerPassword) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}
// 表单校验规则
const rules = {
  email: [
    { required: true, message: '请输入邮箱' },
    { max: 150, message: '邮箱太长' },
    { validator: proxy.Verify.email, message: '请输入正确的邮箱' }
  ],
  password: [{ required: true, message: '请输入密码' }],
  emailCode: [{ required: true, message: '请输入邮箱验证码' }],
  nickName: [{ required: true, message: '请输入昵称' }],
  registerPassword: [
    { required: true, message: '请输入密码' },
    {
      validator: proxy.Verify.password,
      message: '密码至少含有数字,字母或特殊字符 8 -15 位'
    }
  ],
  reRegisterPassword: [
    { required: true, message: '请再次输入密码' },
    { validator: checkRePassword, message: '两次输入的密码不一致' }
  ],
  checkCode: [{ required: true, message: '请输入图片验证码' }]
}
//重置表单
const resetForm = () => {
  dialogConfig.show = true
  if (opType.value == 0) {
    dialogConfig.title = '注册'
  } else if (opType.value == 1) {
    dialogConfig.title = '登录'
  } else if (opType.value == 2) {
    dialogConfig.title = '重置密码'
  }
  nextTick(() => {
    changeCheckCode(0) //刷新验证码
    formDataRef.value.resetFields() //重置表单
    formData.value = {}
    // 登录 -> 检查是否以前记住密码，即是否能从cookie中拿到loginInfo信息
    if (opType.value == 1) {
      const cookieLoginInfo = proxy.VueCookies.get('loginInfo')
      if (cookieLoginInfo) {
        formData.value = cookieLoginInfo
      }
    }
  })
}
// 0注册 1登录 2重置密码
const opType = ref(1)
// 设置密码显示/隐藏
const passwordEyeType = reactive({
  passwordEyeOpen: false,
  registerPasswordEyeOpen: false,
  reRegisterPasswordEyeOpen: false
})
const eyeChange = (type) => {
  passwordEyeType[type] = !passwordEyeType[type]
}
// 登录/注册表单数据
const formData = ref({})
const formDataRef = ref()
// 登录/注册弹窗 属性设置
const dialogConfig = reactive({
  show: false,
  title: '标题'
})
// 发送邮箱验证码弹窗 表单数据
const formData4SendMailCodeRef = ref()
const formData4SendMailCode = ref({})

// 发送邮箱验证码弹窗 属性设置
const dialogConfig4SendMailCode = reactive({
  show: false,
  title: '发送邮箱验证码',
  buttons: [
    {
      type: 'primary',
      text: '发送验证码',
      click: () => {
        sendEmailCode()
      }
    }
  ]
})

// 显示 发送邮箱验证码弹窗
const showSendEmailDialog = () => {
  // 校验 注册表单里的邮箱 是否合法
  formDataRef.value.validateField('email', (valid) => {
    if (!valid) {
      return
    }
    dialogConfig4SendMailCode.show = true
    // 更新 发送邮箱验证码弹窗 的表单数据以及重置表单
    nextTick(() => {
      changeCheckCode(1)
      formData4SendMailCodeRef.value.resetFields()
      formData4SendMailCode.value = {
        email: formData.value.email
      }
    })
  })
}
// 发送请求 -> 发送邮件
const sendEmailCode = () => {
  formData4SendMailCodeRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    //json转对象传递给后端
    const params = Object.assign({}, formData4SendMailCode.value)
    params.type = opType.value == 0 ? 0 : 1
    let result = await proxy.Request({
      url: api.sendEmailCode,
      params: params,
      // 错误回调函数
      errorCallBack: () => {
        // 重置验证码
        changeCheckCode(1)
      }
    })
    if (!result) {
      return
    }
    proxy.Message.success('验证码发送成功,请登录邮箱查看')
    dialogConfig4SendMailCode.show = false
  })
}
// 登录/注册/重置密码 表单提交
const doSubmit = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    // 请求数据封装
    let params = {}
    Object.assign(params, formData.value)

    // 注册
    if (opType.value == 0 || opType.value == 2) {
      // 设置后端要求属性password，删除冗余属性
      params.password = params.registerPassword
      delete params.registerPassword
      delete params.reRegisterPassword
    }
    // 登录
    if (opType.value == 1) {
      let cookieLoginInfo = proxy.VueCookies.get('loginInfo')
      let cookiePassword =
        cookieLoginInfo == null ? null : cookieLoginInfo.password
      if (params.password !== cookiePassword) {
        params.password = md5(params.password)
      }
    }
    // 请求url
    let url = null
    if (opType.value == 0) {
      url = api.register
    } else if (opType.value == 1) {
      url = api.login
    } else if (opType.value == 2) {
      url = api.resetPwd
    }
    let result = await proxy.Request({
      url: url,
      params: params,
      errorCallback: () => {
        changeCheckCode(0)
      }
    })
    if (!result) {
      return
    }
    //注册返回
    if (opType.value == 0) {
      proxy.Message.success('注册成功,请登录')
      showPanel(1)
    } else if (opType.value == 1) {
      //登录
      if (params.rememberMe) {
        let loginInfo = {
          email: params.email,
          password: params.password,
          rememberMe: params.rememberMe
        }
        proxy.VueCookies.set('loginInfo', loginInfo, '7d')
      } else {
        proxy.VueCookies.remove('loginInfo')
      }
      dialogConfig.show = false
      proxy.Message.success('登录成功')
      store.commit('updateLoginUserInfo', result.data)
    } else if (opType.value == 2) {
      //重置密码
      showPanel(1)
      proxy.Message.success('重置密码成功,请登录')
    }
  })
}
const closeDilaog = () => {
  dialogConfig.show = false
  store.commit('showLogin', false)
}
</script>
<style lang="scss">
.login-register {
  .send-email-panel {
    display: flex;
    width: 100%;
    justify-content: space-between;
    .send-email-btn {
      margin-left: 5px;
    }
  }
  .check-code-panel {
    display: flex;
    .check-code {
      margin-left: 5px;
      cursor: pointer;
    }
  }
  .rememberme-panel {
    width: 100%;
  }
  .no-account {
    display: flex;
    width: 100%;
    justify-content: space-between;
  }
  .op-btn {
    width: 100%;
  }
}
</style>
