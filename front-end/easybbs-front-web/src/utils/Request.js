import axios from 'axios'
import Message from '@/utils/Message'
import { ElLoading } from 'element-plus'
import { useStore } from 'vuex'
const instance = axios.create({
  baseURL: '/api',
  timeout: 10 * 1000
})
const contentTypeForm = 'application/x-www-form-urlencoded'
const contentTypeJson = 'application/json'
let loading = null
const store = useStore()
// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 呈现加载loading效果
    if (config.showLoading) {
      loading = ElLoading.service({
        locak: true,
        text: '加载中...',
        background: 'rgba(0,0,0,0.7)'
      })
    }
    return config
  },
  (error) => {
    // 请求失败,关闭加载loading效果，避免一直显示
    if (error.config.showLoading && loading) {
      loading.close()
    }
    Message.error('请求发送失败')
    return Promise.error('请求发送失败')
  }
)
// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    const { showLoading, errorCallback, showError } = response.config
    // 响应成功 -> 关闭loading效果
    if (showLoading && loading) {
      loading.close()
    }
    // 初步解析响应数据，检查响应状态码
    const responseData = response.data
    //正常返回
    if (responseData.code == 200) {
      return responseData
    } else if (responseData.code == 901) {
      //登录超时
      store.commit('updateLoginUserInfo', null)
      store.commit('showLogin', true)
      return Promise.reject({ showError: showError, msg: responseData.info })
    } else {
      if (errorCallback) {
        errorCallback(responseData)
      }
      return Promise.reject({ showError: showError, msg: responseData.info })
    }
  },
  (error) => {
    // 响应失败 -> 关闭loading效果,
    if (error.config.showLoading && loading) {
      loading.close()
    }
    return Promise.reject({ showError: true, msg: '网络异常' })
  }
)

const Request = (config) => {
  const {
    url,
    params,
    dataType,
    showLoading = true,
    errorCallback,
    showError = true
  } = config
  let contentType = contentTypeForm
  // 将数据转化为表单数据提交
  let formData = new FormData()
  for (let key in params) {
    formData.append(key, params[key] == undefined ? '' : params[key])
  }

  if (dataType != null && dataType === 'json') {
    contentType = contentTypeJson
  }
  // 设置请求头
  let headers = {
    'Content-Type': contentType,
    'X-Requested-With': 'XMLHttpRequest'
  }
  return instance
    .post(url, formData, {
      // 配置项 config
      headers: headers,
      showLoading: showLoading,
      errorCallback: errorCallback,
      showError: showError
    })
    .catch((error) => {
      if (error.showError) {
        Message.error(error.msg)
      }
      return null
    })
}

export default Request
