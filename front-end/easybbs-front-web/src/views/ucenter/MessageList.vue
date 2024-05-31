<template>
  <div
    class="container-body message-center"
    :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
  >
    <!-- 导航栏 -->
    <div class="user-banner">
      <router-link to="/" class="a-link">首页</router-link>
      <span class="iconfont icon-right"></span>
      <span>个人中心</span>
    </div>
    <div class="message-panel">
      <div class="tab-list">
        <el-tabs :model-value="activeTabName" @tab-change="tabChange">
          <el-tab-pane name="reply">
            <template #label>
              <div class="tab-item">
                <span>回复我的</span>
                <span class="count-tag" v-if="messageCountInfo.reply > 0">{{
                  messageCountInfo.reply > 99 ? '...' : messageCountInfo.reply
                }}</span>
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane name="likePost">
            <template #label>
              <div class="tab-item">
                <span>赞了我的文章</span>
                <span class="count-tag" v-if="messageCountInfo.likePost > 0">{{
                  messageCountInfo.likePost > 99
                    ? '...'
                    : messageCountInfo.likePost
                }}</span>
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane name="downloadAttachment">
            <template #label>
              <div class="tab-item">
                <span>下载了我的附件</span>
                <span
                  class="count-tag"
                  v-if="messageCountInfo.downloadAttachment > 0"
                  >{{
                    messageCountInfo.downloadAttachment > 99
                      ? '...'
                      : messageCountInfo.downloadAttachment
                  }}</span
                >
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane name="likeComment">
            <template #label>
              <div class="tab-item">
                <span>赞了我的评论</span>
                <span
                  class="count-tag"
                  v-if="messageCountInfo.likeComment > 0"
                  >{{
                    messageCountInfo.likeComment > 99
                      ? '...'
                      : messageCountInfo.likeComment
                  }}</span
                >
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane name="sys">
            <template #label>
              <div class="tab-item">
                <span>系统消息</span>
                <span class="count-tag" v-if="messageCountInfo.sys > 0">{{
                  messageCountInfo.sys > 99 ? '...' : messageCountInfo.sys
                }}</span>
              </div>
            </template>
          </el-tab-pane>
        </el-tabs>
        <router-link :to="`/user/${userId}`" class="a-link gotoUcenter"
          >&lt;&lt;个人中心</router-link
        >
      </div>

      <div class="message-list">
        <DataList
          :loading="loading"
          :dataSource="messageListInfo"
          @loadData="loadMessageList"
        >
          <template #default="{ data }">
            <!-- 系统消息 -->
            <div class="message-item" v-if="data.messageType == 0">
              <div class="message-content">
                <span v-html="data.messageContent"></span>
                <span class="create-time">{{ data.createTime }}</span>
              </div>
            </div>
            <!-- 回复了我的 -->
            <div class="message-item" v-if="data.messageType == 1">
              <template v-if="data.pCommentId == null">
                <Avatar :userId="data.sendUserId" :width="50"></Avatar>
                <div class="message-content">
                  <router-link :to="`/user/${data.sendUserId}`" class="a-link"
                    >@{{ data.sendNickName }}</router-link
                  >
                  对我的文章【<router-link
                    :to="`/post/${data.articleId}`"
                    class="a-link"
                    >{{ data.articleTitle }}</router-link
                  >】发表了评论
                  <span class="create-time">{{ data.createTime }}</span>
                  <div class="reply-content" v-html="data.messageContent"></div>
                </div>
              </template>
              <template v-else>
                <Avatar :userId="data.sendUserId" :width="50"></Avatar>
                <div class="message-content">
                  <router-link :to="`/user/${data.sendUserId}`" class="a-link"
                    >@{{ data.sendNickName }}</router-link
                  >
                  回复了我的评论【<router-link
                    :to="`/post/${data.articleId}`"
                    class="a-link"
                    >{{ data.pCommentContent }}</router-link
                  >】
                  <span class="create-time">{{ data.createTime }}</span>
                  <div class="reply-content" v-html="data.messageContent"></div>
                </div>
              </template>
            </div>
            <!-- 赞了我的文章 -->
            <div class="message-item" v-if="data.messageType == 2">
              <Avatar :userId="data.sendUserId" :width="50"></Avatar>
              <div class="message-content">
                <router-link :to="`/user/${data.sendUserId}`" class="a-link"
                  >@{{ data.sendNickName }}</router-link
                >
                赞了我的文章【<router-link
                  :to="`/post/${data.articleId}`"
                  class="a-link"
                  >{{ data.articleTitle }}</router-link
                >】
                <span class="create-time">{{ data.createTime }}</span>
              </div>
            </div>
            <!-- 赞了我的评论 -->
            <div class="message-item" v-if="data.messageType == 3">
              <Avatar :userId="data.sendUserId" :width="50"></Avatar>
              <div class="message-content">
                <router-link :to="`/user/${data.sendUserId}`" class="a-link"
                  >@{{ data.sendNickName }}</router-link
                >
                在文章【<router-link
                  :to="`/post/${data.articleId}`"
                  class="a-link"
                  >{{ data.articleTitle }}</router-link
                >】中赞了我的评论
                <span class="create-time">{{ data.createTime }}</span>
                <div class="reply-content" v-html="data.messageContent"></div>
              </div>
            </div>
            <!-- 下载了我的附件 -->
            <div class="message-item" v-if="data.messageType == 4">
              <Avatar :userId="data.sendUserId" :width="50"></Avatar>
              <div class="message-content">
                <router-link :to="`/user/${data.sendUserId}`" class="a-link"
                  >@{{ data.sendNickName }}</router-link
                >
                在文章【<router-link
                  :to="`/post/${data.articleId}`"
                  class="a-link"
                  >{{ data.articleTitle }}</router-link
                >】中下载了我的附件
                <span class="create-time">{{ data.createTime }}</span>
              </div>
            </div>
          </template>
        </DataList>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const store = useStore()
const api = {
  loadMessageList: '/ucenter/loadMessageList'
}
const activeTabName = ref('reply')
const messageCountInfo = ref({})
const tabChange = (type) => {
  router.push(`/user/message/${type}`)
}
const loading = ref()
const messageListInfo = ref({})
const loadMessageList = async () => {
  loading.value = true
  let params = {
    pageNo: messageListInfo.value.pageNo,
    code: activeTabName.value
  }
  let result = await proxy.Request({
    url: api.loadMessageList,
    params: params,
    showLoading: false // 这里加载时只需要显示骨架效果，故取消转圈加载效果
  })
  loading.value = false
  if (!result) {
    return
  }
  messageListInfo.value = result.data
  console.log(messageListInfo.value)
  store.commit('readMessage', activeTabName.value)
}
const userId = ref(null)
watch(
  () => store.state.loginUserInfo,
  (newVal, oldVal) => {
    userId.value = newVal.userId
  },
  { immediate: true, deep: true }
)
// 监听路由（type）变化，更新 新type 的消息列表
watch(
  () => route.params.type,
  (newVal, oldVal) => {
    activeTabName.value = newVal
    loadMessageList()
  },
  { immediate: true, deep: true }
)
watch(
  () => store.state.messageCountInfo,
  (newVal, oldVal) => {
    messageCountInfo.value = newVal || {}
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.message-center {
  .user-banner {
    line-height: 35px;
    color: #878585;
    .icon-right {
      padding: 0px 5px;
    }
  }
  .message-panel {
    background: #fff;
    padding: 10px;
    margin-top: 5px;
    .tab-list {
      position: relative;
      .tab-item {
        position: relative;
        padding: 0px 10px;
        .count-tag {
          right: -12px;
          position: absolute;
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
      .gotoUcenter {
        position: absolute;
        top: 5px;
        right: 10px;
        font-size: 14px;
      }
    }
    .message-list {
      .message-item {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        font-size: 14px;
        border-bottom: 1px solid #ddd;
        padding: 10px;
        .message-content {
          margin-left: 5px;
          .create-time {
            color: #9ba7b9;
            margin-left: 10px;
          }
        }
        .reply-content {
          border-left: 2px solid rgb(157, 157, 235);
          padding-left: 5px;
          margin-top: 5px;
        }
      }
    }
  }
}
</style>
