<template>
  <div class="article-item">
    <div class="article-item-inner">
      <div class="article-body">
        <div class="user-info">
          <Avatar :userId="data.userId" :width="30"></Avatar>
          <router-link :to="'/user/' + data.userId" class="link-info">{{
            data.nickName
          }}</router-link>
          <el-divider direction="vertical"></el-divider>
          <div class="post-time">{{ data.postTime }}</div>
          <div class="address">&nbsp;·&nbsp;{{ data.userIpAddress }}</div>
          <el-divider direction="vertical"></el-divider>
          <router-link :to="`/forum/${data.pBoardId}`" class="link-info">{{
            data.pBoardName
          }}</router-link>
          <template v-if="data.boardName">
            <span>/&nbsp;</span>
            <router-link
              :to="`/forum/${data.pBoardId}/${data.boardId}`"
              class="link-info"
              >{{ data.boardName }}</router-link
            >
          </template>
        </div>
        <router-link :to="`/post/${data.articleId}`" class="title">
          <span class="top" v-if="data.topType == 1">置顶</span>
          <span class="tag tag-no-audit" v-if="data.status == 0">待审核</span>
          <span v-if="htmlTitle" v-html="data.title"></span>
          <span v-else>{{ data.title }}</span>
        </router-link>
        <div class="summary">
          {{ data.summary }}
        </div>
        <div class="article-info">
          <span class="iconfont icon-eye-solid">
            {{ data.readCount == 0 ? '阅读' : data.readCount }}
          </span>
          <span class="iconfont icon-good">
            {{ data.goodCount == 0 ? '点赞' : data.goodCount }}
          </span>
          <span class="iconfont icon-comment" v-if="showComment">
            {{ data.commentCount == 0 ? '评论' : data.commentCount }}
          </span>
          <span
            class="iconfont icon-edit edit-btn"
            v-if="showEdit"
            @click="gotoEdit(data.articleId)"
          >
            编辑
          </span>
        </div>
      </div>
      <!-- 点击图片也要跳转到文章详情 -->
      <router-link :to="`/post/${data.articleId}`"
        ><Cover :cover="data.cover" :width="100" v-if="data.cover"></Cover
      ></router-link>
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
const props = defineProps({
  showComment: {
    type: Boolean
  },
  data: {
    type: Object
  },
  showEdit: {
    type: Boolean
  },
  htmlTitle: {
    type: Boolean,
    default: false
  }
})
const gotoEdit = (articleId) => {
  router.push(`/editPost/${articleId}`)
}
</script>

<style lang="scss" scoped>
.article-item {
  padding: 5px 15px 0px 15px;
  .article-item-inner {
    display: flex;
    padding: 10px 0px;
    border-bottom: 1px solid #ddd;
    .article-body {
      flex: 1; //撑满整个剩余空间,将图片挤到最右边
      .user-info {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #4e5969;
        .link-info {
          margin-left: 5px;
          color: #494949;
          text-decoration: none;
        }
        .link-info:hover {
          color: var(--link);
        }
      }
      .title {
        display: inline-block; // 链接设置为块元素
        font-weight: bold;
        text-decoration: none;
        color: rgb(74, 74, 74);
        font-size: 16px;
        margin: 10px 0px;
        .top {
          font-size: 12px;
          border-radius: 3px;
          padding: 0 5px;
          margin-right: 5px;
          color: var(--pink);
          border: 1px solid var(--pink);
        }
      }
      .summary {
        font-size: 14px;
        color: #86909c;
      }
      .article-info {
        margin-top: 10px;
        display: flex;
        align-items: center;
        font-size: 13px;
        .iconfont {
          color: #86909c;
          margin-right: 25px;
          font-size: 14px;
        }
        .iconfont:before {
          padding: 5px;
        }
        .edit-btn {
          cursor: pointer;
          color: var(--link);
          font-size: 13px;
        }
      }
    }
  }
}
.article-item:hover {
  background: #f8f7f7;
}
</style>
