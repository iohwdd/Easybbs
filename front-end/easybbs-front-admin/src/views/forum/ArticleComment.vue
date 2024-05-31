<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="700px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <div class="comment-list">
        <div
          class="comment-item"
          v-for="(item, index) in commentList"
          :key="index"
        >
          <div class="p-comment-item">
            <Avatar :userId="item.userId" :width="40"></Avatar>
          </div>
          <div class="content-info">
            <a
              target="_blank"
              class="a-link"
              :href="proxy.globalInfo.webDomain + '/user' + item.userId"
              >{{ item.nickName }}</a
            >
            <div class="comment-content" v-html="item.content"></div>
            <div v-if="item.imgPath">
              <CommentImage
                :src="
                  proxy.globalInfo.imageUrl + item.imgPath.replace('.', '_.')
                "
                :imgList="[proxy.globalInfo.imageUrl + item.imgPath]"
              ></CommentImage>
            </div>
            <div class="post-info">
              发布:{{ item.postTime }}&nbsp;·&nbsp;{{
                item.userIpAddress
              }}&nbsp;&nbsp;赞:{{ item.goodCount }}
            </div>
            <div v-if="item.children">
              <div
                class="s-comment-item"
                v-for="(sub, index) in item.children"
                :key="index"
              >
                <Avatar :userId="sub.userId" :width="40"></Avatar>
                <div class="content-info">
                  <a
                    target="_blank"
                    class="a-link"
                    :href="proxy.globalInfo.webDomain + '/user' + sub.userId"
                    >{{ sub.nickName }}</a
                  >
                  <span v-if="sub.replyUserId">
                    回复
                    <a
                      target="_blank"
                      class="a-link"
                      :href="
                        proxy.globalInfo.webDomain + '/user' + sub.replyUserId
                      "
                      >{{ sub.replyNickName }}</a
                    ></span
                  >
                  <div v-html="sub.content" class="comment-content"></div>
                  <div class="post-info">
                    发布: {{ sub.postTime }}&nbsp;·&nbsp;{{
                      sub.userIpAddress
                    }}
                    赞: {{ sub.goodCount }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </DiaLog>
  </div>
</template>

<script setup>
import CommentImage from '@/views/forum/CommentImage.vue'
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const dialogConfig = reactive({
  show: false,
  title: '查看评论',
  buttons: [
    {
      type: 'primary',
      text: '关闭',
      click: (e) => {
        dialogConfig.show = false
      }
    }
  ]
})

const api = {
  loadComment: '/forum/loadComment4Article'
}
const commentList = ref([])
const show = async (articleId) => {
  dialogConfig.show = true
  let result = await proxy.Request({
    url: api.loadComment,
    params: {
      articleId: articleId
    }
  })
  if (!result) {
    return
  }
  commentList.value = result.data.list
}
defineExpose({ show })
</script>

<style lang="scss" scoped>
.comment-list {
  .comment-item {
    display: flex;
    margin-top: 10px;
    border-bottom: 1px solid #ddd;
    padding: 10px 0px;
    .p-comment-item {
      display: flex;
    }
    .content-info {
      margin-left: 5px;
    }
    .comment-content {
      margin: 5px 0px;
    }
    .post-info {
      font-size: 12px;
      color: rgb(135, 130, 130);
    }
    .s-comment-item {
      display: flex;
      margin-top: 10px;
    }
  }
}
</style>
