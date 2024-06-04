<template>
  <div class="comment-item">
    <Avatar :userId="commentData.userId" :width="50"></Avatar>
    <div class="comment-info">
      <!-- 评论用户名称 -->
      <div class="nick-name">
        <span class="name" @click="goToUcenter(commentData.userId)">{{
          commentData.nickName
        }}</span>
        <span class="tag-author" v-if="commentData.userId == articleUserId"
          >作者</span
        >
      </div>
      <!-- 评价内容 -->
      <div class="comment-content">
        <div>
          <span class="tag tag-topping" v-if="commentData.topType == 1"
            >置顶</span
          >
          <span class="tag no-audit" v-if="commentData.status == 0"
            >待审核</span
          >
          <span v-html="commentData.content"></span>
        </div>

        <div>
          <CommentImage
            v-if="commentData.imgPath"
            :imgList="[proxy.globalInfo.imageUrl + commentData.imgPath]"
            :src="
              proxy.globalInfo.imageUrl + commentData.imgPath.replace('.', '_.')
            "
          ></CommentImage>
        </div>
      </div>
      <!-- 点赞/评论 -->
      <div class="op-panel">
        <div class="time">
          <span>{{ commentData.postTime }}</span>
          <span class="address"
            >&nbsp;·&nbsp;{{ commentData.userIpAddress }}</span
          >
        </div>
        <div
          :class="[
            'iconfont icon-good',
            commentData.likeType == 1 ? 'active' : ''
          ]"
          @click="doLike(commentData)"
        >
          <span>{{
            commentData.goodCount > 0 ? commentData.goodCount : '点赞'
          }}</span>
        </div>
        <div
          class="iconfont icon-comment"
          @click="showReplyPanel(commentData, 0)"
        >
          <span>回复</span>
        </div>
        <el-dropdown v-if="articleUserId == currentUserId">
          <div class="iconfont icon-more"></div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="opTop(commentData)">
                {{ commentData.topType == 0 ? '设为置顶' : '取消置顶' }}
              </el-dropdown-item>
              <el-dropdown-item @click="del(commentData.commentId)">
                删除评论
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <!-- 子评论 -->
      <div class="comment-sub-list">
        <div
          class="comment-sub-item"
          v-for="(sub, index) in commentData.children"
          :key="index"
        >
          <Avatar :userId="sub.userId" :width="30"></Avatar>
          <div class="comment-sub-info">
            <div class="nick-name">
              <span class="name" @click="goToUcenter(sub.userId)">{{
                sub.nickName
              }}</span>
              <span class="tag-author" v-if="sub.userId == articleUserId"
                >作者</span
              >
              <span class="reply-name">回复</span>
              <span @click="goToUcenter(sub.replyUserId)" class="a-link"
                >@{{ sub.replyNickName }}</span
              >
              <span>：</span>
              <span class="sub-content" v-html="sub.content"></span>
            </div>
            <div class="op-panel">
              <div class="time">
                <span>{{ sub.postTime }}</span>
                <span class="address"
                  >&nbsp;·&nbsp;{{ sub.userIpAddress }}</span
                >
              </div>
              <div
                :class="[
                  'iconfont icon-good',
                  sub.likeType == 1 ? 'active' : ''
                ]"
                @click="doLike(sub)"
              >
                <span>{{ sub.goodCount > 0 ? sub.goodCount : '点赞' }}</span>
              </div>
              <div
                class="iconfont icon-comment"
                @click="showReplyPanel(sub, 1)"
              >
                <span>回复</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 回复 -->
      <div class="reply-info" v-if="commentData.showReply">
        <PostComment
          :articleId="articleId"
          :avatarWidth="30"
          :userId="currentUserId"
          :showInsertImg="false"
          :pCommentId="pCommentId"
          :replyUserId="replyUserId"
          @postCommentFinish="postCommentFinish"
          :placeholderInfo="placeholderInfo"
        ></PostComment>
      </div>
    </div>
  </div>
</template>

<script setup>
import CommentImage from './CommentImage.vue'
import PostComment from './PostComment.vue'
import { ref, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()
const api = {
  doLike: '/comment/doLike',
  changeTopType: '/comment/changeTopType',
  delComment: '/forum/delComment'
}
const props = defineProps({
  commentData: {
    type: Object
  },
  articleId: {
    type: String
  },
  articleUserId: {
    type: String
  },
  currentUserId: {
    type: String
  }
})
// 显示评论框
const placeholderInfo = ref(null)
const pCommentId = ref('0')
const replyUserId = ref(null)
const emit = defineEmits(['hiddenAllReply', 'reloadData'])
// 显示回复框
const showReplyPanel = (curData, type) => {
  let haveShow = curData.showReply == undefined ? false : curData.showReply
  emit('hiddenAllReply')
  if (type == 0) {
    curData.showReply = !haveShow
  } else {
    props.commentData.showReply = true
  }
  pCommentId.value = props.commentData.commentId
  replyUserId.value = curData.userId
  placeholderInfo.value = '回复 @' + curData.nickName
  //   curData.showReply = !curData.showReply
}
// 评论发布完成 -> 父组件更新评论列表
// 二级评论返回的是所有子评论（children）
const postCommentFinish = (resultData) => {
  props.commentData.children = resultData
  placeholderInfo.value = undefined
  emit('hiddenAllReply')
  //   const children = props.commentData.children || []
  //   children.unshift(resultData)
}
// 前往用户个人中心
const goToUcenter = (userId) => {
  router.push(`/user/${userId}`)
}
// 点赞
const doLike = async (data) => {
  let result = await proxy.Request({
    url: api.doLike,
    showLoading: false,
    params: {
      commentId: data.commentId
    }
  })
  if (!result) {
    return
  }
  data.likeType = result.data.likeType
  data.goodCount = result.data.goodCount
}
// 置顶
const opTop = async (data) => {
  let result = await proxy.Request({
    url: api.changeTopType,
    params: {
      commentId: data.commentId,
      topType: data.topType == 0 ? 1 : 0
    }
  })
  if (!result) {
    return
  }
  // 通知父组件重新加载评论 -> 置顶/取消置顶 生效
  emit('reloadData')
}
//删除
const del = (commentId) => {
  proxy.Confirm(`你确定要删除该评论吗？`, async () => {
    let result = await proxy.Request({
      url: api.delComment,
      params: {
        commentIds: commentId
      }
    })
    if (!result) {
      return
    }
    proxy.Message.success('删除成功')
    emit('reloadData')
  })
}
</script>

<style lang="scss" scoped>
.comment-item {
  display: flex;
  padding-top: 15px;
  .comment-info {
    flex: 1;
    margin-left: 10px;
    border-bottom: 1px solid #ddd;
    padding-bottom: 15px;
    .nick-name {
      .name {
        font-size: 14px;
        color: var(--text2);
        margin-right: 5px;
        cursor: pointer;
      }
      .tag-author {
        padding: 0px 3px;
        background: var(--pink);
        border-radius: 3px;
        font-size: 12px;
        color: #fff;
      }
    }
    .comment-content {
      margin-top: 5px;
      font-size: 15px;
      line-height: 22px;
      .tag {
        margin-right: 5px;
        border-radius: 3px;
        padding: 0px 5px;
        font-size: 12px;
      }
      .tag-topping {
        color: var(--pink);
        border: 1px solid var(--pink);
      }
      .no-audit {
        color: var(--text2);
        border: 1px solid var(--text2);
      }
    }
    .op-panel {
      display: flex;
      color: var(--text2);
      font-size: 13px;
      margin-top: 5px;
      align-items: center;
      .time {
        margin-right: 20px;
      }
      .iconfont {
        margin-right: 15px;
        font-size: 14px;
        cursor: pointer;
      }
      .iconfont::before {
        margin-right: 3px;
      }
      .active {
        color: var(--link);
      }
    }
    .comment-sub-list {
      margin-top: 10px;
      .comment-sub-item {
        display: flex;
        margin: 8px 0px;
        font-size: 14px;
        .comment-sub-info {
          margin-left: 5px;
          .nick-name {
            .name {
            }
            .tag-author {
            }
            .reply-name {
              margin: 0px 5px;
            }
            .sub-content {
              font-size: 15px;
            }
            .a-link {
              cursor: pointer;
            }
          }
        }
      }
    }
  }
  .reply-info {
    margin-top: 15px;
  }
}
</style>
