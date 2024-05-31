<template>
  <div>
    <el-dialog
      :model-value="show"
      :show-close="showClose"
      :title="title"
      :width="width"
      :close-on-click-modal="false"
      :top="top"
      class="cust-dialog"
      @close="close"
    >
      <div class="dialog-body">
        <slot></slot>
      </div>
      <template v-if="(buttons && buttons.length > 0) || showCancel">
        <div class="dialog-fotter">
          <el-button link @click="close" v-if="showCancel">取消</el-button>
          <el-button
            v-for="(btn, index) in buttons"
            :key="index"
            :type="btn.type || 'primary'"
            @click="btn.click"
            >{{ btn.text }}</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { defineComponent } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: true
  },
  title: {
    type: String,
    default: '标题'
  },
  showClose: {
    type: Boolean,
    default: true
  },
  width: {
    type: String,
    default: '30%'
  },
  top: {
    type: String,
    default: '50px'
  },
  buttons: {
    type: Array
  },
  showCancel: {
    type: Boolean,
    default: true
  }
})
defineComponent({
  name: 'DiaLog',
  props
})
const emit = defineEmits()
const close = () => {
  emit('close')
}
</script>

<style lang="scss">
.cust-dialog {
  margin-bottom: 10px;
  .dialog-body {
    border-top: 1px solid #ddd;
    border-bottom: 1px solid #ddd;
    padding: 15px;
    min-height: 100px;
    max-height: calc(100vh - 250px);
    overflow: auto;
  }
  .dialog-fotter {
    text-align: right;
    padding: 10px 20px;
  }
}
</style>
