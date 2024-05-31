<template>
  <!-- 没有数据展示效果 -->
  <div
    v-if="!loading && dataSource.list != null && dataSource.list.length == 0"
  >
    <NoData :msg="noDataMsg"></NoData>
  </div>
  <!-- 加载过程中展示骨架效果 -->
  <div class="skeleton" v-if="loading">
    <el-skeleton :rows="5" animated />
  </div>
  <!-- 数据 -->
  <div v-for="(item, index) in dataSource.list" :key="index" v-else>
    <slot :data="item"></slot>
  </div>
  <!-- 分页 -->
  <div class="pagination">
    <el-pagination
      v-if="dataSource.pageTotal > 1"
      background
      :page-size="dataSource == null ? 15 : dataSource.pageSize"
      :total="dataSource.totalCount"
      :current-page="dataSource.pageNo"
      layout="prev, pager, next"
      @current-change="handlePageNoChange"
      style="text-align: right"
    />
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
const props = defineProps({
  dataSource: {
    type: Object
  },
  loading: {
    type: Boolean
  },
  noDataMsg: {
    type: String,
    default: '空空如也'
  }
})
const emit = defineEmits(['loadData'])
const handlePageNoChange = (pageNo) => {
  props.dataSource.pageNo = pageNo
  emit('loadData')
}
</script>

<style lang="scss" scoped>
.pagination {
  padding: 10px 0px 10px 10px;
}
.skeleton {
  padding: 10px;
}
</style>
