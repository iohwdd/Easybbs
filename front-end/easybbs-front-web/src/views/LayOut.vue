<template>
  <div class="header" v-if="showHeader">
    <div
      class="header-content"
      :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
    >
      <!-- logo -->
      <router-link to="/" class="logo">
        <span>Easybbs</span>
      </router-link>
      <!-- 板块信息 -->
      <div class="menu-panel">
        <span
          to="/"
          @click="goHome()"
          :class="['menu-item', activePBoardId == undefined ? 'active' : '']"
          >首页</span
        >
        <template v-for="(board, index) in boardList" :key="index">
          <el-popover
            placement="bottom-start"
            :width="300"
            trigger="hover"
            v-if="board.children.length > 0"
          >
            <!-- 一级板块信息 -->
            <template #reference>
              <span
                :class="[
                  'menu-item',
                  board.boardId == activePBoardId ? 'active' : ''
                ]"
                @click="boardClickHandle(board)"
              >
                {{ board.boardName }}
              </span>
            </template>
            <!-- 二级板块信息 -->
            <div class="sub-board-list">
              <span
                v-for="(subBoard, index) in board.children"
                :class="[
                  'sub-board',
                  subBoard.boardId == activeBoardId ? 'active' : ''
                ]"
                :key="index"
                @click="subBoardClickHandle(subBoard)"
                to="/"
              >
                {{ subBoard.boardName }}
              </span>
            </div>
          </el-popover>
          <span
            :class="[
              'menu-item',
              board.boardId == activePBoardId ? 'active' : ''
            ]"
            v-else
            @click="boardClickHandle(board)"
          >
            {{ board.boardName }}
          </span>
        </template>
      </div>
      <!-- 登录，注册，用户信息 -->
      <div class="user-info-panel">
        <div class="op-btn">
          <el-button type="primary" @click="postArticle">
            发帖<span class="iconfont icon-add"></span>
          </el-button>
          <el-button type="primary" @click="goSearch">
            搜索<span class="iconfont icon-search"></span>
          </el-button>
        </div>
        <!-- 消息中心 -->
        <template v-if="userInfo.userId">
          <!-- 个人消息 -->
          <div class="message-info">
            <el-dropdown>
              <el-badge
                :value="messageCountInfo.total"
                class="item"
                :hidden="
                  messageCountInfo.total == 0 || messageCountInfo.total == null
                "
              >
                <div class="iconfont icon-message"></div>
              </el-badge>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    @click="gotoMessage('reply')"
                    class="message-item"
                  >
                    <span class="text"> 回复我的 </span>
                    <span class="count-tag" v-if="messageCountInfo.reply > 0">{{
                      messageCountInfo.reply > 99
                        ? '...'
                        : messageCountInfo.reply
                    }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item
                    @click="gotoMessage('likePost')"
                    class="message-item"
                  >
                    <span class="text"> 赞了我的文章 </span>
                    <span
                      class="count-tag"
                      v-if="messageCountInfo.likePost > 0"
                      >{{
                        messageCountInfo.likePost > 99
                          ? '...'
                          : messageCountInfo.likePost
                      }}</span
                    >
                  </el-dropdown-item>
                  <el-dropdown-item
                    @click="gotoMessage('downloadAttachment')"
                    class="message-item"
                    ><span class="text"> 下载了我的附件 </span>
                    <span
                      class="count-tag"
                      v-if="messageCountInfo.downloadAttachment > 0"
                      >{{
                        messageCountInfo.downloadAttachment > 99
                          ? '...'
                          : messageCountInfo.downloadAttachment
                      }}</span
                    ></el-dropdown-item
                  >
                  <el-dropdown-item
                    @click="gotoMessage('likeComment')"
                    class="message-item"
                    ><span class="text"> 赞了我的评论 </span>
                    <span
                      class="count-tag"
                      v-if="messageCountInfo.likeComment > 0"
                      >{{
                        messageCountInfo.likeComment > 99
                          ? '...'
                          : messageCountInfo.likeComment
                      }}</span
                    ></el-dropdown-item
                  >
                  <el-dropdown-item
                    @click="gotoMessage('sys')"
                    class="message-item"
                    ><span class="text"> 系统消息 </span>
                    <span class="count-tag" v-if="messageCountInfo.sys > 0">{{
                      messageCountInfo.sys > 99 ? '...' : messageCountInfo.sys
                    }}</span></el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <!-- 个人信息 -->
          <div class="chart">
            <el-badge
              :value="chartMsgCount"
              :hidden="chartMsgCount == null || chartMsgCount == 0"
              class="item"
            >
              <el-icon class="chat-dot" @click="toChart">
                <chat-dot-round />
              </el-icon>
            </el-badge>
          </div>
          <!-- 个人头像 -->
          <div class="user-info">
            <el-dropdown>
              <avatar :userId="userInfo.userId" :addLink="false"></avatar>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="gotoUcenter(userInfo.userId)"
                    >我的主页</el-dropdown-item
                  >
                  <el-dropdown-item @click="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
        <!-- 登录/注册 -->
        <el-button-group :style="{ 'margin-left': '10px' }" v-else>
          <el-button type="primary" plain @click="loginAndRegister(1)"
            >登录</el-button
          >
          <el-button type="primary" plain @click="loginAndRegister(0)"
            >注册</el-button
          >
        </el-button-group>
      </div>
    </div>
  </div>
  <!-- 二级路由出口 -->
  <div class="body-content"><router-view></router-view></div>
  <!-- footer -->
  <div class="footer" v-if="showFooter">
    <div
      class="footer-content"
      :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
    >
      <el-row>
        <el-col :span="6" class="item">
          <!-- logo -->
          <div class="logo-item">
            <router-link to="/" class="logo">
              <span>Easybbs</span>
            </router-link>
            <span class="info">一个干活满满的编程社区</span>
          </div>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">网站相关</div>
          <div><a href="">bala</a></div>
          <div><a href="">bala</a></div>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">友情链接</div>
          <span class="info">暂无友情链接</span>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">关注站长</div>
        </el-col>
      </el-row>
    </div>
  </div>
  <el-backtop :right="100"></el-backtop>
  <!-- 登录，注册面板 -->
  <LoginAndRegister ref="loginAndRegisterRef"></LoginAndRegister>
</template>

<script setup>
import LoginAndRegister from './LoginAndRegister.vue'
import { ref, reactive, getCurrentInstance, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()
const store = useStore()
const api = {
  getUserInfo: '/getUserInfo',
  loadBoard: '/board/loadBoard',
  getMessageCount: '/ucenter/getMessageCount',
  logout: '/logout',
  getSysSetting: '/getSysSetting',
  getChartMsgCount: '/chart/getChartMsgCount'
}
const showHeader = ref(true)
const goHome = () => {
  router.push('/')
}
const boardClickHandle = (board) => {
  router.push(`/forum/${board.boardId}`)
}

const subBoardClickHandle = (subBoard) => {
  console.log(subBoard)
  router.push(`/forum/${subBoard.pBoardId}/${subBoard.boardId}`)
}
//获取滚动条高度
const getScrollTop = () => {
  let scrollTop =
    document.documentElement.scrollTop ||
    window.pageYOffset ||
    document.body.scrollTop
  return scrollTop
}
// 滚轮滑到一定距离隐藏导航栏
const initScroll = () => {
  let initScrollTop = getScrollTop()
  let scrollType = 0
  window.addEventListener('scroll', () => {
    let currentScrollTop = getScrollTop()
    if (currentScrollTop > initScrollTop) {
      scrollType = 1
    } else {
      scrollType = 0
    }
    initScrollTop = currentScrollTop
    if (scrollType == 1 && currentScrollTop > 100) {
      showHeader.value = false
    } else {
      showHeader.value = true
    }
  })
}
const showDialog = ref(true)
const buttons = [
  {
    text: '确定',
    type: 'primary'
  }
]
const loginAndRegisterRef = ref()
const loginAndRegister = (type) => {
  loginAndRegisterRef.value.showPanel(type)
}
onMounted(() => {
  initScroll()
  getUserInfo()
  loadSysSetting()
})
// 获取用户信息
const getUserInfo = async () => {
  let result = await proxy.Request({
    url: api.getUserInfo
  })
  if (!result) {
    return
  }
  store.commit('updateLoginUserInfo', result.data)
}
//获取板块信息
const boardList = ref([])
const loadBoard = async () => {
  let result = await proxy.Request({
    url: api.loadBoard
  })
  if (!result) {
    return
  }
  boardList.value = result.data
  store.commit('saveBoardList', result.data)
}
loadBoard()
// 监听 用户登录信息
const userInfo = ref({})
watch(
  () => store.state.loginUserInfo,
  (newVal, oldVal) => {
    if (newVal != undefined && newVal != null) {
      userInfo.value = newVal
    } else {
      userInfo.value = {}
    }
  },
  { immediate: true, deep: true }
)
// 监听 是否展示登录框
watch(
  () => store.state.showLogin,
  (newVal) => {
    if (newVal) {
      // 展示登录框
      loginAndRegister(1)
    }
  },
  { immediate: true, deep: true }
)
// 当在一个组件内产生的数据，需要在其它组件中要到时 -> 跨组件数据状态管理 -> store
// 当前选择的一级板块
const activePBoardId = ref(0)
watch(
  () => store.state.activePBoardId,
  (newVal, oldVal) => {
    if (newVal != 0) {
      activePBoardId.value = newVal
    }
  },
  { immediate: true, deep: true }
)
// 当前选择的二级板块
const activeBoardId = ref(0)
watch(
  () => store.state.activeBoardId,
  (newVal, oldVal) => {
    if (newVal != 0) {
      activeBoardId.value = newVal
    }
  },
  { immediate: true, deep: true }
)
// 发帖
const postArticle = () => {
  if (userInfo.value.userId == null || userInfo.value.userId == undefined) {
    proxy.Message.error('请先登录再发帖')
    loginAndRegister(1)
  } else {
    router.push('/newPost')
  }
}
// 消息中心
const gotoMessage = (type) => {
  router.push(`/user/message/${type}`)
}
const messageCountInfo = ref({})
const loadMessageCount = async () => {
  let result = await proxy.Request({
    url: api.getMessageCount
  })
  if (!result) {
    return
  }
  messageCountInfo.value = result.data
  store.commit('updateMessageCountInfo', result.data)
}
watch(
  () => store.state.messageCountInfo,
  (newVal, oldVal) => {
    messageCountInfo.value = newVal || {}
  },
  { immediate: true, deep: true }
)
watch(
  () => store.state.loginUserInfo,
  (newVal, oldVal) => {
    if (newVal) {
      debugger
      loadMessageCount()
      getChartMsgCount()
    }
  },
  { immediate: true, deep: true }
)
// 跳转到个人中心
const gotoUcenter = (userId) => {
  router.push(`/user/${userId}`)
}
// 退出登录
const logout = () => {
  proxy.Confirm('确认退出登录吗？', async () => {
    let result = proxy.Request({
      url: api.logout
    })
    if (!result) {
      return
    }
    store.commit('updateLoginUserInfo', null)
  })
}
// 获取系统设置
const loadSysSetting = async () => {
  let result = await proxy.Request({
    url: api.getSysSetting
  })
  if (!result) {
    return
  }
  store.commit('setSysSetting', result.data)
}
// 搜索
const goSearch = () => {
  router.push('/search')
}
const showFooter = ref(false)
watch(
  () => route.path,
  (newVal, oldVal) => {
    if (
      newVal.indexOf('newPost') != -1 ||
      newVal.indexOf('editPost') != -1 ||
      newVal.indexOf('chart') != -1
    ) {
      showFooter.value = false
    } else {
      showFooter.value = true
    }
  },
  { immediate: true, deep: true }
)
// 聊天
const toChart = () => {
  router.push('/user/chart')
}
// 获取聊天未读消息数量
const chartMsgCount = ref(null)
const getChartMsgCount = async () => {
  let result = await proxy.Request({
    url: api.getChartMsgCount,
    params: {
      currentUserId: userInfo.value.userId
    }
  })
  if (!result) {
    return
  }
  chartMsgCount.value = result.data
  store.commit('updateChartMsgCount', result.data)
}
watch(
  () => store.state.chartMsgCount,
  (newVal, oldVal) => {
    chartMsgCount.value = newVal
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss">
.chart {
  display: inline-block;
  position: relative;
  margin-right: 20px;
}

.item {
  display: inline-flex;
  align-items: center;
}

.chat-dot {
  cursor: pointer;
  color: gray; /* 设置图标颜色为灰色 */
  font-size: 24px; /* 设置图标大小，根据需要调整 */
}
.header {
  //导航栏
  top: 0px;
  position: fixed;
  height: 60px;
  box-shadow: 0 2px 6px #ddd;
  width: 100%;
  background: #fff;
  z-index: 1000;
  text-align: center; //文本居中
  .header-content {
    margin: 0px auto;
    align-items: center;
    display: flex;
    height: 60px;
  }
  .logo {
    display: block;
    text-decoration: none;
    margin-right: 5px;
    span {
      font-size: 35px;
    }
  }
  .menu-panel {
    flex: 1;
    .menu-item {
      margin-left: 20px;
      cursor: pointer;
    }
    .active {
      color: var(--link);
    }
  }
  .user-info-panel {
    width: 400px;
    display: flex;
    align-items: center;
    .op-btn {
      .iconfont {
        margin-left: 5px;
      }
      .el-button + .el-button {
        margin-left: 5px;
      }
    }
    .op-btnL:last-child {
      margin-left: 0px !important;
    }
    .message-info {
      cursor: pointer;
      margin-left: 5px;
      margin-right: 25px;
      .icon-message {
        font-size: 25px;
        color: #8e8989;
      }
    }
  }
}

.sub-board-list {
  display: flex;
  flex-wrap: wrap;
  .sub-board {
    padding: 0px 10px;
    border-radius: 20px;
    margin-right: 10px;
    background: #ece9e9;
    border: 1px solid #ddd;
    color: rgb(78, 75, 75);
    margin-top: 10px;
  }
  .sub-board:hover {
    color: var(--link);
  }
  .active {
    color: #fff;
    background: var(--link);
  }
  .active:hover {
    color: #fff;
  }
}
.message-item {
  display: flex;
  justify-content: space-around;
  .text {
    flex: 1;
  }
  .count-tag {
    min-width: 20px;
    height: 15px;
    line-height: 15px;
    display: inline-block;
    background: #f56c6c;
    border-radius: 10px;
    font-size: 13px;
    text-align: center;
    color: #fff;
    margin-left: 10px;
  }
}
.body-content {
  margin-top: 60px;
  position: relative;
  min-height: calc(100vh - 210px);
}
.footer {
  background: #e9e9e9;
  height: 140px;
  margin-top: 10px;
  .footer-content {
    margin: 0px auto;
    padding-top: 10px;
    .item {
      text-align: left;
      .title {
        font-size: 18px;
        margin-bottom: 10px;
      }
      a {
        font-size: 14px;
        text-decoration: none;
        color: var(--text2);
        line-height: 25px;
      }
    }
  }
  .logo {
    display: block;
    text-decoration: none;
    margin-right: 5px;
    span {
      font-size: 25px;
    }
  }
  .info {
    padding-top: 10px;
    color: #676565;
  }
}
</style>
