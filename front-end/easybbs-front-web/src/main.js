import './assets/base.scss'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
//引入cookies
import VueCookies from 'vue-cookies'
//引入element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/icon/iconfont.css'
//引入store
import store from './store'
//全局组件
import DiaLog from '@/components/Dialog.vue'
import Avatar from '@/components/Avatar.vue'
import Cover from '@/components/Cover.vue'
import DataList from '@/components/DataList.vue'
import NoData from '@/components/NoData.vue'
import ImageViewer from '@/components/ImageViewer.vue'
import EditMarkDown from '@/components/EditMarkDown.vue'
import EditHtml from '@/components/EditHtml.vue'
import CoverUpload from '@/components/CoverUpload.vue'
import AttachmentSelector from '@/components/AttachmentSelector.vue'
//全局方法
import Verify from './utils/Verify'
import Message from './utils/Message'
import Request from '@/utils/Request'
import Utils from '@/utils/Utils'
import Confirm from '@/utils/Confirm'
import { ChatDotRound } from '@element-plus/icons-vue'
const app = createApp(App)
app.component('DiaLog', DiaLog)
app.component('Avatar', Avatar)
app.component('Cover', Cover)
app.component('DataList', DataList)
app.component('NoData', NoData)
app.component('ImageViewer', ImageViewer)
app.component('EditHtml', EditHtml)
app.component('EditMarkDown', EditMarkDown)
app.component('CoverUpload', CoverUpload)
app.component('AttachmentSelector', AttachmentSelector)
app.component('ChatDotRound', ChatDotRound)
app.use(router)
app.use(ElementPlus)
app.use(store)
app.config.globalProperties.Confirm = Confirm
app.config.globalProperties.Utils = Utils
app.config.globalProperties.Verify = Verify
app.config.globalProperties.Message = Message
app.config.globalProperties.Request = Request
app.config.globalProperties.VueCookies = VueCookies
app.config.globalProperties.globalInfo = {
  bodyWidth: 1300,
  avatarUrl: '/api/file/getAvatar/',
  imageUrl: '/api/file/getImage/'
}
app.mount('#app')
