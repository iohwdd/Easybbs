<template>
  <div class="post-comment-panel">
    <Avatar :width="avatarWidth" :userId="userId"></Avatar>
    <div class="comment-form">
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        @submit.prevent
      >
        <!--textarea输入-->
        <el-form-item prop="content">
          <el-input
            clearable
            :placeholder="placeholderInfo"
            type="textarea"
            :maxlength="800"
            resize="none"
            show-word-limit
            v-model.trim="formData.content"
          ></el-input>
          <div class="insert-img" v-if="showInsertImg">
            <div class="pre-img" v-if="commentImg">
              <CommentImage :src="commentImg"></CommentImage>
              <span
                class="iconfont icon-remove"
                @click="removeCommentImg"
              ></span>
            </div>
            <el-upload
              v-else
              name="file"
              :show-file-list="false"
              accept=".mp,.jpg,.png,.tif,.gif,.pcx,.tga,.exif,.fpx,.svg,.psd,.cdr,.pcd,.dxf,.ufo,.eps,.ai,.raw,.WMF,.webp,.avif,.apng "
              :multiple="false"
              :http-request="selectImg"
            >
              <span class="iconfont icon-image"></span>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="send-btn" @click="postCommentDo">发表</div>
  </div>
</template>

<script setup>
import CommentImage from './CommentImage.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  avatarWidth: {
    type: Number
  },
  articleId: {
    type: String
  },
  pCommentId: {
    type: String
  },
  replyUserId: {
    type: String
  },
  userId: {
    type: String
  },
  showInsertImg: {
    type: Boolean
  },
  placeholderInfo: {
    type: String,
    default: '请文明发言...'
  }
})
const api = {
  postComment: '/comment/postComment'
}
// 表单信息
const formData = ref({})
const formDataRef = ref()
const checkPostComment = (rule, value, callback) => {
  if (value == null && formData.value.image == null) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}
const rules = {
  content: [
    { required: true, message: '请评论内容', validator: checkPostComment },
    {
      min: 5,
      message: '评论最少输入五个字'
    }
  ]
}
// 选择图片
const commentImg = ref(null)
const selectImg = (file) => {
  file = file.file
  let img = new FileReader()
  img.readAsDataURL(file)
  img.onload = ({ target }) => {
    let imgData = target.result
    commentImg.value = imgData
    formData.value.image = file
  }
}
const removeCommentImg = () => {
  commentImg.value = null
  formData.value.image = null
}
// 发布评论
const emit = defineEmits(['postCommentFinish'])
const postCommentDo = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = Object.assign({}, formData.value)
    params.articleId = props.articleId
    params.pCommentId = props.pCommentId
    params.replyUserId = props.replyUserId
    let result = await proxy.Request({
      url: api.postComment,
      params
    })

    if (!result) {
      return
    }
    proxy.Message.success('发表评论成功')
    formDataRef.value.resetFields()
    removeCommentImg()
    emit('postCommentFinish', result.data)
  })
}
</script>

<style lang="scss" scoped>
.post-comment-panel {
  display: flex;
  align-items: top;
  .comment-form {
    flex: 1;
    margin: 0px 10px;
    .el-textarea__inner {
      height: 60px;
    }
    .insert-img {
      line-height: normal;
      font-size: 20px;
      color: #595656;
      .pre-img {
        position: relative;
        margin-top: 10px;
        .iconfont {
          cursor: pointer;
          position: absolute;
          top: -5px;
          right: -10px;
          color: #595656;
        }
      }
    }
  }
  .send-btn {
    cursor: pointer;
    width: 60px;
    height: 60px;
    background: var(--link);
    color: #fff;
    text-align: center;
    line-height: 60px;
    border-radius: 5px;
  }
}
</style>
