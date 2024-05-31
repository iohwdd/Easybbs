<template>
  <div class="edit-post">
    <el-form
      class="edit-panel"
      :model="formData"
      :rules="rules"
      ref="formDataRef"
      label-width="60px"
      @submit.prevent
    >
      <div class="post-editor">
        <el-card :body-style="{ padding: '5px' }">
          <template #header>
            <div class="post-editor-title">
              <span>正文</span>
              <div class="change-editor-type">
                <span class="iconfont icon-change" @click="changeEditor"
                  >切换为{{
                    editorType == 1 ? '富文本编辑器' : 'markdown编辑器'
                  }}</span
                >
              </div>
            </div>
          </template>
          <el-form-item prop="content" label-width="0">
            <EditHtml
              :height="htmlEditorHeight"
              v-model="formData.content"
              v-if="editorType == 0"
            ></EditHtml>
            <EditMarkDown
              :height="markdownHeight"
              v-model="formData.markdownContent"
              @htmlContent="setHtmlContent"
              v-if="editorType == 1"
            ></EditMarkDown>
          </el-form-item>
        </el-card>
      </div>
      <div class="post-setting">
        <el-card :body-style="{ padding: '5px' }">
          <template #header>
            <span>设置</span>
          </template>
          <div class="setting-inner">
            <!--input输入-->
            <el-form-item label="标题" prop="title">
              <el-input
                clearable
                :maxlength="150"
                placeholder="提示信息"
                v-model.trim="formData.title"
              ></el-input>
            </el-form-item>
            <el-form-item prop="boardIds" label="板块">
              <el-cascader
                :placeholder="'请选择模块'"
                :options="boardList"
                :props="boardProps"
                clearable
                v-model="formData.boardIds"
                :style="{ width: '100%' }"
              ></el-cascader>
            </el-form-item>
            <el-form-item prop="cover" label="封面">
              <CoverUpload v-model="formData.cover"></CoverUpload>
            </el-form-item>
            <!--textarea输入-->
            <el-form-item label=" 摘要" prop="summary">
              <el-input
                clearable
                placeholder="提示信息"
                type="textarea"
                :rows="5"
                :maxlength="200"
                resize="none"
                show-word-limit
                v-model="formData.summary"
              ></el-input>
            </el-form-item>
            <el-form-item prop="cover" label="附件">
              <AttachmentSelector
                v-model="formData.attachment"
              ></AttachmentSelector>
            </el-form-item>
            <!--input输入-->
            <el-form-item
              label="积分"
              prop="integral"
              v-if="formData.attachment"
            >
              <el-input
                placeholder="提示信息"
                v-model="formData.integral"
              ></el-input>
              <span class="tips">附件下载积分：0积分表示无需积分即可下载</span>
            </el-form-item>
            <el-button
              type="primary"
              :style="{ width: '100%' }"
              @click="postHandler"
              >保存</el-button
            >
          </div>
        </el-card>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()
const formData = ref({})
const formDataRef = ref()
const articleId = ref()
const markdownHeight = window.innerHeight - 80 - 80
const htmlEditorHeight = window.innerHeight - 80 - 120
const api = {
  loadBoard: '/forum/loadBoard4Post',
  postArticle: '/forum/postArticle',
  articleDetail4Update: '/forum/articleDetail4Update',
  updateArticle: '/forum/updateArticle'
}
const rules = {
  title: [{ required: true, message: '请输入内容' }],
  boardIds: [{ required: true, message: '请输入板块' }],
  content: [{ required: true, message: '请输入正文' }],
  integral: [
    { required: true, message: '请输入下载所需积分' },
    { validator: proxy.Verify.number, message: '积分只能是数字' }
  ]
}
//提交更改
const postHandler = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    //设置板块ID
    if (params.boardIds.length == 1) {
      params.pBoardId = params.boardIds[0]
    } else if (params.boardIds.length == 2) {
      params.pBoardId = params.boardIds[0]
      params.boardId = params.boardIds[1]
    }
    delete params.boardIds
    //设置编辑器类型
    params.editorType = editorType.value
    //获取内容
    const contentText = params.content.replace(/<(?!img).*?>/g, '')
    if (contentText == null) {
      proxy.Message.warning('正文不能为空')
      return
    }
    if (params.attachment != null) {
      params.attachmentType = 1
    } else {
      params.attachmentType = 0
    }
    //封面 -> 后端是接收一个文件流,如果没有更改，存的是图片的地址而非文件流，传递到后端则会报错，所以要删除
    if (!(params.cover instanceof File)) {
      delete params.cover
    }
    //附件
    if (!(params.attachment instanceof File)) {
      delete params.attachment
    }
    let result = await proxy.Request({
      url: params.articleId ? api.updateArticle : api.postArticle,
      params: params
    })
    if (!result) {
      return
    }
    proxy.Message.success('保存成功')
    let articleId = result.data != null ? result.data : params.articleId
    router.push(`/post/${articleId}`)
  })
}
const getArticleDetail = async () => {
  nextTick(async () => {
    formDataRef.value.resetFields()
    if (articleId.value) {
      //修改
      let result = await proxy.Request({
        url: api.articleDetail4Update,
        params: {
          articleId: articleId.value
        },
        showError: false,
        errorCallback: (resp) => {
          ElMessageBox.alert(resp.info, '错误', {
            'show-close': false,
            callback: (action) => {
              router.go(-1)
            }
          })
        }
      })
      if (!result) {
        return
      }
      let articleInfo = result.data.forumArticle
      //设置编辑器
      editorType.value = articleInfo.editorType
      //回显板块
      articleInfo.boardIds = []
      articleInfo.boardIds.push(articleInfo.pBoardId)
      if (articleInfo.boardId != null && articleInfo.boardId != 0) {
        articleInfo.boardIds.push(articleInfo.boardId)
      }
      //回显封面
      if (articleInfo.cover) {
        articleInfo.cover = { imageUrl: articleInfo.cover }
      }
      // 回显附件
      if (result.data.attachment) {
        articleInfo.attachment = { name: result.data.attachment.fileName }
        articleInfo.integral = result.data.attachment.integral
      }
      formData.value = articleInfo
    } else {
      //新增
      formData.value = {}
      editorType.value = proxy.VueCookies.get('editorType') || 0
    }
  })
}
//设置markdown编辑器的富文本信息
const setHtmlContent = (htmlContent) => {
  formData.value.content = htmlContent
}
// 监听路由变化 -> 判断是新增还是修改
watch(
  () => route,
  (newVal, oldVal) => {
    if (
      newVal.path.indexOf('/newPost') != -1 ||
      newVal.path.indexOf('/editPost') != -1
    ) {
      articleId.value = newVal.params.articleId
      getArticleDetail()
    }
  },
  { immediate: true, deep: true }
)
const boardList = ref([])
const boardProps = {
  multiple: false,
  checkStrictly: true,
  value: 'boardId',
  label: 'boardName'
}
const loadBoard = async () => {
  let result = await proxy.Request({
    url: api.loadBoard
  })
  if (!result) {
    return
  }
  boardList.value = result.data
}
loadBoard()
// 富文本 0 markdown 1
const editorType = ref(null)
const changeEditor = () => {
  proxy.Confirm('切换编辑器会清空当前内容,确定切换吗？', () => {
    editorType.value = editorType.value == 0 ? 1 : 0
    formData.value.content = ''
    formData.value.markdownContent = ''
    proxy.VueCookies.set('editorType', editorType.value, -1)
  })
}
</script>

<style lang="scss" scoped>
.edit-post {
  .edit-panel {
    display: flex;
    .el-card__header {
      padding: 10px;
    }
    .post-editor {
      flex: 1;
      .post-editor-title {
        display: flex;
        justify-content: space-between;
        .change-editor-type {
          color: var(--link);
          cursor: pointer;
          font-size: 14px;
        }
      }
    }
    .post-setting {
      width: 450px;
      margin-left: 10px;
      .setting-inner {
        max-height: calc(100vh - 120px);
        overflow: auto;
      }
      .tips {
        color: #797979;
        font-size: 13px;
      }
    }
  }
}
</style>
