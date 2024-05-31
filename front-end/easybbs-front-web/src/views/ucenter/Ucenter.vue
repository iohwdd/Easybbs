<template>
  <div
    class="container-body ucenter"
    :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
  >
    <!-- 导航栏 -->
    <div class="user-banner">
      <router-link to="/" class="a-link">首页</router-link>
      <span class="iconfont icon-right"></span>
      <span>个人中心</span>
    </div>
    <div class="ucenter-panel">
      <div class="user-aside">
        <!-- 头像 -->
        <div class="avatar-panel">
          <div
            class="edit-btn a-link"
            v-if="isCurrentUser"
            @click="updateUserInfo"
          >
            修改资料
          </div>
          <div class="avatar-inner">
            <Avatar :userId="userInfo.userId" :width="120"></Avatar>
          </div>
          <div class="nick-name">
            <span>{{ userInfo.nickName }}</span>
            <span v-if="userInfo.sex == 0" class="iconfont icon-man"></span>
            <span v-if="userInfo.sex == 1" class="iconfont icon-woman"></span>
          </div>
          <div class="desc">{{ userInfo.personDescription }}</div>
        </div>
        <!-- 扩展信息 -->
        <div class="user-extend-panel">
          <div class="info-item">
            <div class="label iconfont icon-integral">积分</div>
            <div
              class="value a-link"
              v-if="isCurrentUser"
              @click="showIntegralRecord"
            >
              {{ userInfo.currentIntegral }}
            </div>
            <div v-else class="value" @click="showIntegralRecord">
              {{ userInfo.currentIntegral }}
            </div>
          </div>
          <div class="info-item">
            <div class="label iconfont icon-like">获赞</div>
            <div class="value">{{ userInfo.likeCount }}</div>
          </div>
          <div class="info-item">
            <div class="label iconfont icon-post">发帖</div>
            <div class="value">{{ userInfo.postCount }}</div>
          </div>
          <div class="info-item">
            <div class="label iconfont icon-register">加入</div>
            <div class="value">{{ userInfo.joinTime }}</div>
          </div>
          <div class="info-item">
            <div class="label iconfont icon-login">最后登录</div>
            <div class="value">{{ userInfo.lastLoginTime }}</div>
          </div>
        </div>
      </div>
      <!-- 个人发帖/评论/点赞 -->
      <div class="article-panel">
        <el-tabs :model-value="activeTabName" @tab-change="changeTab">
          <el-tab-pane label="发帖" :name="0"></el-tab-pane>
          <el-tab-pane label="评论" :name="1"></el-tab-pane>
          <el-tab-pane label="点赞" :name="2"></el-tab-pane>
        </el-tabs>
        <div class="article-list">
          <DataList
            :loading="loading"
            :dataSource="articleListInfo"
            @loadData="loadArticle"
            :noDataMsg="'暂无相关文章'"
          >
            <template #default="{ data }">
              <ArticleListItem
                :data="data"
                :showEdit="activeTabName == 0 && isCurrentUser"
                :showComment="showComment"
              ></ArticleListItem>
            </template>
          </DataList>
        </div>
      </div>
    </div>
    <UcenterEditUserInfo
      ref="ucenterEditUserInfoRef"
      @resetUserInfo="resetUserInfoHandler"
    ></UcenterEditUserInfo>
    <UserIntegralRecord ref="userIntegralRecordRef"></UserIntegralRecord>
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  watch
} from 'vue'
import UserIntegralRecord from '@/views/ucenter/UserIntegralRecord.vue'
import UcenterEditUserInfo from '@/views/ucenter/UcenterEditUserInfo.vue'
import ArticleListItem from '@/views/forum/ArticleListItem.vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
const store = useStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const userId = ref(null)
const userInfo = ref({})
const api = {
  getUserInfo: '/ucenter/getUserInfo',
  loadUserArticle: '/ucenter/loadUserArticle'
}
const loadUserInfo = async () => {
  let result = await proxy.Request({
    url: api.getUserInfo,
    params: {
      userId: userId.value
    },
    errorCallback: () => {
      setTimeout(() => {
        router.push('/')
      }, 1500)
    }
  })
  if (!result) {
    return
  }
  userInfo.value = result.data
}

const activeTabName = ref(0) // 0发帖 1评论 2点赞
const changeTab = (type) => {
  activeTabName.value = type
  loadArticle()
}
const loading = ref()
// 文章数据 bug
const articleListInfo = ref({})
const loadArticle = async () => {
  loading.value = true
  let params = {
    pageNo: articleListInfo.value.pageNo,
    type: activeTabName.value,
    userId: userId.value
  }
  let result = await proxy.Request({
    url: api.loadUserArticle,
    params: params
  })
  loading.value = false
  if (!result) {
    return
  }
  articleListInfo.value = result.data
}
const isCurrentUser = ref(false)
const resetCurrentUser = () => {
  const loginUserInfo = store.getters.getLoginUserInfo
  if (loginUserInfo && loginUserInfo.userId === userId.value) {
    isCurrentUser.value = true
  } else {
    isCurrentUser.value = false
  }
}
// 用户个人信息
const ucenterEditUserInfoRef = ref(null)
const updateUserInfo = () => {
  ucenterEditUserInfoRef.value.showEditUserInfoDialog(userInfo.value)
}
const resetUserInfoHandler = (data) => {
  userInfo.value = data
}
// 用户积分记录
const userIntegralRecordRef = ref(null)
const showIntegralRecord = () => {
  userIntegralRecordRef.value.showRecord()
}
watch(
  () => store.state.loginUserInfo,
  (newVal, oldVal) => {
    resetCurrentUser()
  },
  { immediate: true, deep: true }
)
watch(
  () => route.params.userId,
  (newVal, oldVal) => {
    if (newVal) {
      userId.value = newVal
      loadUserInfo()
      resetCurrentUser()
      loadArticle()
    }
  },
  { immediate: true, deep: true }
)
// 评论显示与否
const showComment = ref(false)
watch(
  () => store.state.sysSetting,
  (newVal, oldVal) => {
    if (newVal) {
      showComment.value = newVal.commentOpen
    }
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.ucenter {
  .user-banner {
    line-height: 35px;
    color: #878585;
    .icon-right {
      padding: 0px 5px;
    }
  }
  .ucenter-panel {
    display: flex;
    .user-aside {
      width: 300px;
      margin-right: 10px;
      .avatar-panel {
        background: #fff;
        text-align: center;
        padding: 10px;
        .edit-btn {
          text-align: right;
          font-size: 14px;
        }
        .avatar-inner {
          display: flex;
          justify-content: center;
        }
        .nick-name {
          .iconfont {
            margin-left: 5px;
            color: var(--link);
          }
        }
        .desc {
          margin-top: 5px;
          text-align: left;
          font-size: 14px;
          color: #838181;
        }
      }
    }
    .user-extend-panel {
      margin-top: 10px;
      background: #fff;
      padding: 10px;
      .info-item {
        display: flex;
        justify-content: space-between;
        .label {
          font-size: 13px;
        }
        .label::before {
          font-size: 22px;
          color: #888888;
          padding-right: 5px;
        }
        .value {
          font-size: 13px;
        }
      }
    }
    .article-panel {
      flex: 1;
      background: #fff;
      padding: 0px 10px 10px 10px;
    }
  }
}
</style>
