<template>
  <div>
    <DiaLog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="450px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        label-width="40px"
        @submit.prevent
        ref="formDataRef"
      >
        <!--input输入-->
        <el-form-item label="日期" prop="createTimeRange">
          <el-date-picker
            v-model="formData.createTimeRange"
            type="daterange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="loadRecord"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div class="data-item">
        <span class="record-type">类型</span>
        <span class="integral">积分</span>
        <span class="create-time">时间</span>
      </div>
      <DataList
        :loading="loading"
        :dataSource="recordInfo"
        @loadData="loadRecord"
        :noDataMsg="'暂无相关记录'"
      >
        <template #default="{ data }">
          <div class="data-item">
            <div class="record-type">{{ data.operTypeName }}</div>
            <div :class="['integral', data.integral > 0 ? 'add' : 'reduce']">
              {{ data.integral }}
            </div>
            <div class="create-time">{{ data.createTime }}</div>
          </div>
        </template>
      </DataList>
    </DiaLog>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const api = {
  updateUserInfo: '/ucenter/updateUserInfo',
  loadUserIntegralRecord: '/ucenter/loadUserIntegralRecord'
}
const formData = ref({})
const formDataRef = ref()
const router = useRouter()
const dialogConfig = reactive({
  show: false,
  title: '查看用户积分记录',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        dialogConfig.show = false
      }
    }
  ]
})
// 显示查看积分弹窗
const showRecord = () => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    loadRecord()
  })
}
defineExpose({ showRecord })

const loading = ref()
const recordInfo = ref({})
const loadRecord = async () => {
  loading.value = true
  let params = {
    pageNo: recordInfo.value.pageNo
  }
  if (formData.value.createTimeRange) {
    params.createTimeStart = formData.value.createTimeRange[0]
    params.createTimeEnd = formData.value.createTimeRange[1]
  }
  let result = await proxy.Request({
    url: api.loadUserIntegralRecord,
    params: params,
    showLoading: false
  })
  loading.value = false
  if (!result) {
    return
  }
  recordInfo.value = result.data
}
</script>

<style lang="scss" scoped>
.data-item {
  display: flex;
  border-bottom: 1px solid #ddd;
  align-items: center;
  padding: 5px 0px;
  .add {
    color: red;
  }
  .reduce {
    color: green;
  }
  .record-type {
    width: 120px;
  }
  .integral {
    width: 120px;
  }
  .create-time {
    flex: 1;
    text-align: center;
  }
}
</style>
