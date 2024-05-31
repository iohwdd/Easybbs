<template>
  <div class="comment-body">
    <div class="comment-title">
      <div class="title">
        评论<span class="count">{{ commentListInfo.totalCount }}</span>
      </div>
      <div class="tab">
        <span
          @click="orderComment(0)"
          :class="['tab-item', orderType == 0 ? 'active' : '']"
          >热榜</span
        >
        <el-divider direction="vertical"></el-divider>
        <span
          @click="orderComment(1)"
          :class="['tab-item', orderType == 1 ? 'active' : '']"
          >最新</span
        >
      </div>
    </div>
    <!-- 发送评论 -->
    <div class="comment-form-panel">
      <PostComment
        :articleId="articleId"
        :pCommentId="'0'"
        :avatarWidth="50"
        :userId="currentUserInfo.userId"
        :showInsertImg="currentUserInfo.userId != null"
        @postCommentFinish="postCommentFinish"
      ></PostComment>
    </div>
    <!-- 评论列表 -->
    <div class="comment-list">
      <DataList
        :dataSource="commentListInfo"
        :loading="loading"
        :noDataMsg="'暂无评论, 赶紧占沙发吧~'"
        @loadData="loadComment"
      >
        <template #default="{ data }">
          <CommentListItem
            :articleId="articleId"
            :commentData="data"
            :articleUserId="articleUserId"
            :currentUserId="currentUserInfo.userId"
            @hiddenAllReply="hiddenAllReplyHandler"
            @reloadData="loadComment"
          ></CommentListItem>
        </template>
      </DataList>
    </div>
  </div>
</template>

<script setup>
import PostComment from './PostComment.vue'
import CommentListItem from './CommentListItem.vue'
import { ref, reactive, getCurrentInstance, watch } from 'vue'
import { useStore } from 'vuex'
const { proxy } = getCurrentInstance()
const store = useStore()
const props = defineProps({
  articleId: {
    type: String
  },
  articleUserId: {
    type: String
  }
})
const api = {
  loadComment: '/comment/loadComment',
  postComment: '/comment/postComment',
  doLike: '/comment/doLike'
}

// 评论列表
const commentListInfo = ref({})
const orderType = ref(0)
const loading = ref(null)
const loadComment = async () => {
  let params = {
    pageNo: commentListInfo.value.pageNo,
    articleId: props.articleId,
    orderType: orderType.value
  }
  loading.value = true
  let result = await proxy.Request({
    url: api.loadComment,
    showLoading: false,
    params: params
  })
  loading.value = false
  if (!result) {
    return
  }
  commentListInfo.value = result.data
}
loadComment()
// 当前用户信息
const currentUserInfo = ref({})
watch(
  () => store.state.loginUserInfo,
  (newVal, oldVal) => {
    currentUserInfo.value = newVal || {}
  },
  { immediate: true, deep: true }
)
// 隐藏所有回复框
const hiddenAllReplyHandler = () => {
  commentListInfo.value.list.forEach((element) => {
    element.showReply = false
  })
}
const emit = defineEmits(['updateCommentCount'])
// 评论发布完成 -> 父组件更新评论列表
const postCommentFinish = (resultData) => {
  commentListInfo.value.list.unshift(resultData)
  //更新评论数量
  const totalCount = commentListInfo.value.totalCount + 1
  commentListInfo.value.totalCount = totalCount
  // 更新左侧评论小图标的评论数量
  emit('updateCommentCount')
}
// 评论排序 热榜0 最新1
const orderComment = (type) => {
  orderType.value = type
  loadComment()
}
</script>

<style lang="scss" scoped>
.comment-body {
  .comment-title {
    display: flex;
    align-items: center;
    .title {
      font-size: 20px;
      .count {
        font-size: 14px;
        padding: 0px 10px;
      }
    }
    .tab {
      .tab-item {
        cursor: pointer;
      }
      .active {
        color: var(--link);
      }
    }
  }
  .comment-form-panel {
    margin-top: 20px;
  }
}
</style>
