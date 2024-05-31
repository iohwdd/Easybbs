<template>
  <div>
    <div class="top-panel">
      <el-form
        :model="searchFormData"
        label-width="50px"
        ref="searchFormDataRef"
      >
        <el-row>
          <el-col :span="4">
            <el-form-item label="内容">
              <el-input
                clearable
                placeholder="支持模糊搜索"
                v-model.trim="searchFormData.contentFuzzy"
                @keyup.native="loadDataList"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="昵称">
              <el-input
                clearable
                placeholder="请输入昵称"
                v-model.trim="searchFormData.nickNameFuzzy"
                @keyup.native="loadDataList"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="状态">
              <el-select
                v-model="searchFormData.status"
                clearable
                placeholder="请选择"
                :style="{ widht: '100%' }"
              >
                <el-option :value="1" label="已审核"></el-option>
                <el-option :value="0" label="待审核"></el-option>
                <el-option :value="-1" label="已删除"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" :style="{ 'padding-left': '10px' }">
            <el-button-group>
              <el-button type="primary" @click="loadDataList">搜索</el-button>
              <el-button
                type="success"
                @click="auditBatch"
                :disabled="selectBatchList.length == 0"
                >批量审批</el-button
              >
              <el-button
                type="danger"
                @click="delBatch"
                :disabled="selectBatchList.length == 0"
                >批量删除</el-button
              >
            </el-button-group>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="data-list">
      <Table
        ref="tableRef"
        :columns="columns"
        :dataSource="tableData"
        :fetch="loadDataList"
        :options="tableOptions"
        @rowSelected="setRowSelected"
      >
        <!-- 用户信息 -->
        <template #userInfo="{ row, index }">
          <div class="user-info">
            <Avatar :userId="row.userId"></Avatar>
            <div class="name-info">
              <div>
                <a
                  :href="`${proxy.globalInfo.webDomain}user/${row.userId}`"
                  class="a-link"
                  target="_blank"
                  >{{ row.nickName }}</a
                >
              </div>
              <div>{{ row.userIpAddress }}</div>
            </div>
          </div>
        </template>
        <!-- 查看文章 -->
        <template #contentInfo="{ index, row }">
          <span>
            <a
              class="a-link"
              target="_blank"
              :href="proxy.globalInfo.webDomain + 'post/' + row.articleId"
              >查看文章</a
            >
          </span>
          <div v-html="row.content"></div>
          <div v-if="row.imgPath">
            <CommentImage
              :src="proxy.globalInfo.imageUrl + row.imgPath.replace('.', '_.')"
              :imgList="[proxy.globalInfo.imageUrl + row.imgPath]"
            ></CommentImage>
          </div>
        </template>
        <!-- 状态 -->
        <template #statusInfo="{ index, row }">
          <div v-if="row.status == -1" :style="{ color: 'red' }">已删除</div>
          <div v-if="row.status == 0" :style="{ color: 'red' }">待审核</div>
          <div v-if="row.status == 1" :style="{ color: 'green' }">已审核</div>
        </template>
        <!-- 操作信息 -->
        <template #op="{ index, row }">
          <div class="op" v-if="row.status == 0 || row.status == 1">
            <el-dropdown trigger="click">
              <span class="iconfont icon-more"></span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="delComment(row)"
                    >删除</el-dropdown-item
                  >
                  <el-dropdown-item @click="audit(row)" v-if="row.status == 0">
                    审核
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
        <template #postTime="{ index, row }"> {{ row.postTime }}</template>
        <template #goodCount="{ index, row }"> {{ row.goodCount }}</template>
      </Table>
    </div>
  </div>
</template>

<script setup>
import CommentImage from './CommentImage.vue'
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const api = {
  loadDataList: '/forum/loadComment',
  auditComment: '/forum/auditComment',
  delComment: '/forum/delComment'
}
const searchFormData = ref({})
const searchFormDataRef = ref()
const columns = [
  {
    label: '用户信息',
    prop: 'avatar',
    width: 200,
    scopedSlots: 'userInfo'
  },
  {
    label: '评论内容',
    prop: 'content',
    scopedSlots: 'contentInfo'
  },
  {
    label: '点赞',
    width: '150',
    scopedSlots: 'goodCount'
  },
  {
    label: '状态',
    prop: 'status',
    width: 100,
    scopedSlots: 'statusInfo'
  },
  {
    label: '发布时间',
    width: 180,
    scopedSlots: 'postTime'
  },
  {
    label: '发布地址',
    prop: 'userIpAddress',
    width: 100
  },
  {
    label: '操作',
    prop: 'op',
    width: 80,
    scopedSlots: 'op'
  }
]

const tableRef = ref()
const tableData = ref({})
const tableOptions = ref({
  selectType: 'checkbox',
  extHeight: 55
})
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  Object.assign(params, searchFormData.value)
  let result = await proxy.Request({
    url: api.loadDataList,
    params
  })
  if (!result) {
    return
  }
  tableData.value = result.data
}

const delComment = (data) => {
  proxy.Confirm(`你确定要删除该评论吗？`, async () => {
    let result = await proxy.Request({
      url: api.delComment,
      params: {
        commentIds: data.commentId
      }
    })
    if (!result) {
      return
    }
    loadDataList()
    proxy.Message.success('删除成功')
  })
}
const audit = (data) => {
  proxy.Confirm(`你确定要审核该评论吗？`, async () => {
    let result = await proxy.Request({
      url: api.auditComment,
      params: {
        commentIds: data.commentId
      }
    })
    if (!result) {
      return
    }
    loadDataList()
    proxy.Message.success('审核成功')
  })
}
//设置多行选中
// 批量选择
const selectBatchList = ref([])
const setRowSelected = (rows) => {
  selectBatchList.value = []
  rows.forEach((element) => {
    selectBatchList.value.push(element.commentId)
  })
}
const auditBatch = async () => {
  proxy.Confirm('你确定要批量审核吗？', async () => {
    let result = await proxy.Request({
      url: api.auditComment,
      params: {
        commentIds: selectBatchList.value.join(',')
      }
    })
    if (!result) {
      return
    }
    tableRef.value.clearSelection()
    await loadDataList()
    proxy.Message.success('审核成功')
  })
}
const delBatch = async () => {
  proxy.Confirm('你确定要批量删除吗？', async () => {
    let result = await proxy.Request({
      url: api.delComment,
      params: {
        commentIds: selectBatchList.value.join(',')
      }
    })
    if (!result) {
      return
    }
    tableRef.value.clearSelection()
    await loadDataList()
    proxy.Message.success('删除成功')
  })
}
</script>

<style lang="scss" scoped>
.data-list {
  .user-info {
    display: flex;
    align-items: center;
    .name-info {
      margin-left: 5px;
      font-size: 13px;
    }
  }
  .op {
    .iconfont {
      cursor: pointer;
    }
  }
}
</style>
