<template>
  <div
    class="container-body article-list-body"
    :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
  >
    <!-- 展示二级板块 -->
    <div class="sub-board">
      <span :class="['board-item', boardId == 0 ? 'active' : '']">
        <router-link :to="`/forum/${pBoardId}`">全部</router-link>
      </span>
      <span v-for="(item, index) in subBoardList" :key="index">
        <span :class="['board-item', item.boardId == boardId ? 'active' : '']">
          <router-link :to="`/forum/${item.pboardId}/${item.boardId}`">{{
            item.boardName
          }}</router-link>
        </span>
      </span>
    </div>
    <div class="article-panel">
      <div class="top-tab">
        <div
          :class="['tab', orderType == 0 ? 'active' : '']"
          @click="changeOrderType(0)"
        >
          热榜
        </div>
        <el-divider direction="vertical"></el-divider>
        <div
          :class="['tab', orderType == 1 ? 'active' : '']"
          @click="changeOrderType(1)"
        >
          发布时间
        </div>
        <el-divider direction="vertical"></el-divider>
        <div
          :class="['tab', orderType == 2 ? 'active' : '']"
          @click="changeOrderType(2)"
        >
          最新
        </div>
      </div>
      <div class="article-list">
        <DataList
          :loading="loading"
          :dataSource="articleListInfo"
          @loadData="loadArticle()"
        >
          <template #default="{ data }">
            <ArticleListItem
              :data="data"
              :showComment="showComment"
            ></ArticleListItem>
          </template>
        </DataList>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, watch } from 'vue'
import { useRoute } from 'vue-router'
import ArticleListItem from './ArticleListItem.vue'
import { useStore } from 'vuex'
const route = useRoute()
const { proxy } = getCurrentInstance()
const store = useStore()
const api = {
  loadArticle: '/forum/loadArticle'
}
// 一级板块
const pBoardId = ref(0)
// 二级板块
const boardId = ref(0)
// 文章列表
const loading = ref()
// 排序 -> 0热榜 1发布时间 2最新
const orderType = ref(0)
// 文章数据
const articleListInfo = ref({})
// 二级板块列表
const subBoardList = ref([])
const changeOrderType = (type) => {
  orderType.value = type
  loadArticle()
}

const loadArticle = async () => {
  loading.value = true
  let params = {
    pageNo: articleListInfo.value.pageNo,
    orderType: orderType.value,
    pBoardId: pBoardId.value,
    boardId: boardId.value
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
  articleListInfo.value = result.data
}

const setSubBoard = () => {
  subBoardList.value = store.getters.getSubBoardList(pBoardId.value)
}
// 监测板块信息,以便更新板块数据
watch(
  () => route.params,
  (newVal, oldVal) => {
    pBoardId.value = newVal.pBoardId
    boardId.value = newVal.boardId || 0
    setSubBoard()
    loadArticle()
    store.commit('setActivePBoardId', newVal.pBoardId)
    store.commit('setActiveBoardId', newVal.boardId)
  },
  { immediate: true, deep: true }
)
// 监测板块数据变化,以便更新子板块数据
watch(
  () => store.state.boardList,
  (newVal, oldVal) => {
    setSubBoard()
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
.article-list-body {
  .sub-board {
    padding: 10px 0px;
    .board-item {
      background: #fff;
      border-radius: 15px;
      padding: 2px 10px;
      margin-right: 10px;
      color: #909090;
      cursor: pointer;
      font-size: 14px;
    }
    .active {
      background: var(--link);
      a {
        color: #fff;
      }
    }
  }
  .article-panel {
    background: #fff;
    .top-tab {
      display: flex;
      align-items: center;
      padding: 10px 15px;
      font-size: 15px;
      border-bottom: 1px solid #ddd;
    }
    .tab {
      cursor: pointer;
    }
    .active {
      color: var(--link);
    }
  }
}
</style>
