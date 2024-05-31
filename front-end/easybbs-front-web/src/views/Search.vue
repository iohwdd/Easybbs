<template>
  <div
    class="container-body search-body"
    :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
  >
    <div
      class="search-panel"
      :style="{
        'padding-top': startSearch ? '0px' : searchPanelHeight + '200px'
      }"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        @submit.prevent
      >
        <!--input输入-->
        <el-form-item prop="keyword">
          <el-input
            clearable
            placeholder="请输入搜索关键词"
            v-model.trim="formData.keyword"
            @keyup.enter="search"
            @focus="startSearchHandler"
            @change="keywordChange"
          >
            <template #suffix>
              <span
                class="iconfont icon-search"
                @click="search"
                @blur="formData.keyword = $event.target.value.trim()"
              ></span>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="article-list">
      <DataList
        :loading="loading"
        :dataSource="articleListInfo"
        @loadData="search"
      >
        <template #default="{ data }">
          <ArticleListItem
            :data="data"
            :showComment="showComment"
            :htmlTitle="true"
          ></ArticleListItem>
        </template>
      </DataList>
    </div>
  </div>
</template>

<script setup>
import ArticleListItem from '@/views/forum/ArticleListItem.vue'
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const store = useStore()

const api = {
  loadArticle: '/forum/search'
}
const startSearch = ref(false)
const startSearchHandler = () => {
  startSearch.value = true
}
const formData = ref({})
const formDataRef = ref()
const rules = {
  keyword: [
    { required: true, message: '请输入关键字' },
    { min: 3, message: '最少3个字' }
  ]
}
const searchPanelHeight = (window.innerHeight - 60 - 140 - 200) / 2
const loading = ref()
const articleListInfo = ref({})

const search = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    loading.value = true
    let params = {
      pageNo: articleListInfo.value.pageNo,
      keyword: formData.value.keyword
    }
    let result = await proxy.Request({
      url: api.loadArticle,
      params: params,
      showLoading: false // 这里加载时只需要显示骨架效果，故取消转圈加载效果
    })
    loading.value = false
    if (!result) {
      return
    }
    let list = result.data.list
    list.forEach((element) => {
      element.title = element.title.replace(
        params.keyword,
        "<span style='color : red'>" + params.keyword + '</span>'
      )
    })
    articleListInfo.value = result.data
  })
}
const keywordChange = () => {
  if (formData.value.keyword == '') {
    articleListInfo.value = {}
  }
}
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
.search-body {
  background: #fff;
  margin-top: 70px;
  padding: 10px;
  min-height: calc(100vh - 100px);
  .search-panel {
    display: flex;
    justify-content: center;

    .el-input {
      width: 700px;
    }
    .iconfont {
      cursor: pointer;
    }
  }
}
</style>
