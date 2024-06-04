<template>
  <div class="private-message">
    <div class="sidebar">
      <ul>
        <li
          v-for="user in users"
          :key="user.id"
          @click="selectUser(user)"
          :class="{
            active: selectedUser != null && user.userId === selectedUser.userId
          }"
        >
          <div class="avatar-wrapper">
            <el-badge
              :value="user.noReadCount"
              :hidden="user.noReadCount == null || user.noReadCount == 0"
              class="badge"
            >
              <avatar :userId="user.userId"></avatar>
            </el-badge>
          </div>

          <div class="user-info">
            <span>{{ user.nickName }}</span>
            <p>{{ user.lastMessage }}</p>
          </div>
        </li>
      </ul>
    </div>
    <div class="chat-panel" v-if="selectedUser">
      <div class="chat-content">
        <div class="chat-header">
          <span>Chat with {{ selectedUser.name }}</span>
        </div>
        <div class="chat-messages">
          <div v-for="(message, index) in message4History" :key="message.id">
            <div v-if="shouldShowTimestamp(index)" class="timestamp">
              {{ formatTimestamp(message.createdTime) }}
            </div>
            <div
              :class="[
                'message',
                {
                  'message-sent': message.senderId === currentUser.userId,
                  'message-received': message.receiverId === currentUser.userId
                }
              ]"
            >
              <div class="message-content">{{ message.text }}</div>
            </div>
          </div>
        </div>
        <div class="chat-input">
          <input
            type="text"
            v-model="inputMessage"
            placeholder="Type a message..."
          />
          <button @click="sendMessage" @keydown.enter.prevent="sendMessage">
            Send
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
const route = useRoute()
const router = useRouter()
const store = useStore()
const { proxy } = getCurrentInstance()
const api = {
  loadMessageList: '/chart/loadMessageList',
  loadMessageHistory: '/chart/loadMessageHistory',
  loadUsers: '/chart/loadUsers',
  sendMessage: '/chart/sendMessage',
  getChartMsgCount: '/chart/getChartMsgCount'
}
//获取消息记录
const selectedUser = ref(null)
const message4History = ref({})
const selectUser = async (sender) => {
  selectedUser.value = sender
  await loadMessageHistory(sender)
  getChartMsgCount()
}
const loadMessageHistory = async (sender) => {
  let result = await proxy.Request({
    url: api.loadMessageHistory,
    params: {
      senderId: sender.userId,
      receiverId: currentUser.value.userId
    }
  })
  if (!result) {
    return
  }
  message4History.value = result.data
  loadUsers(currentUser.value.userId)
}
// 获取聊天未读消息数量
const chartMsgCount = ref(null)
const getChartMsgCount = async () => {
  let result = await proxy.Request({
    url: api.getChartMsgCount,
    params: {
      currentUserId: currentUser.value.userId
    }
  })
  if (!result) {
    return
  }
  chartMsgCount.value = result.data
  store.commit('updateChartMsgCount', result.data)
}
// 获取消息列表的用户信息
const users = ref({})
const currentUser = ref(JSON.parse(sessionStorage.getItem('userInfo'))) // 当前用户
const loadUsers = async (currentUserId) => {
  let result = await proxy.Request({
    url: api.loadUsers,
    params: {
      currentUserId
    }
  })
  if (!result) {
    return
  }
  users.value = result.data
}
loadUsers(currentUser.value.userId)

// 判断是否需要显示时间戳
const shouldShowTimestamp = (index) => {
  if (index === 0) return true
  const currentMessageTime = new Date(message4History.value[index].createdTime)
  const previousMessageTime = new Date(
    message4History.value[index - 1].createdTime
  )
  const timeDifference =
    (currentMessageTime - previousMessageTime) / (1000 * 60) // 转换为分钟
  return timeDifference > 5
}

// 格式化时间戳
const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleString()
}

//发送消息
const inputMessage = ref('')
const sendMessage = async () => {
  if (inputMessage.value.trim()) {
    let result = await proxy.Request({
      url: api.sendMessage,
      params: {
        senderId: currentUser.value.userId,
        receiverId: selectedUser.value.userId,
        text: inputMessage.value
      }
    })
    if (!result) {
      return
    }
    inputMessage.value = ''
    await loadMessageHistory(selectedUser.value)
  }
}
// 滚动到最底部的方法
const scrollToBottom = () => {
  nextTick(() => {
    const chatMessages = document.querySelector('.chat-messages')
    if (chatMessages) {
      chatMessages.scrollTop = chatMessages.scrollHeight
    }
  })
}

// 监听 message4History 变化，确保加载历史消息后滚动到底部
watch(message4History, () => {
  scrollToBottom()
})
</script>

<style scoped>
/* 通用样式 */
body,
html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: Arial, sans-serif;
}

/* 私信界面样式 */
.private-message {
  display: flex;
  height: 91vh;
  width: 100vw;
}

/* 侧边栏样式 */
.sidebar {
  width: 250px;
  background: #fff;
  color: #2c3e50;
  padding: 20px;
  overflow-y: auto;
  border-right: 1px solid #ddd;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  cursor: pointer;
  transition: background 0.3s;
  border-bottom: 1px solid #ddd;
}

.sidebar li:not(:last-child)::after {
  content: '';
  display: block;
  height: 1px;
  background: #e6e6e6;
  margin: 0 15px;
}

.sidebar li:hover {
  background: #f0f0f0;
}

.sidebar li.active {
  background: #3498db;
  color: #fff;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 15px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-info span {
  font-weight: bold;
  color: inherit;
}

.user-info p {
  margin: 0;
  color: #7f8c8d;
}

/* 聊天面板样式 */
.chat-panel {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  background: #ecf0f1;
}

.chat-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.chat-header {
  padding: 10px;
  background: #3498db;
  color: #fff;
  font-weight: bold;
  border-radius: 5px 5px 0 0;
  margin: 20px;
  margin-bottom: 0;
}

/* 聊天消息列表样式 */
.chat-messages {
  flex-grow: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
  margin: 0 20px 20px 20px;
  max-height: calc(91vh - 190px); /* Set max height to ensure scrolling */
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.message-sent {
  justify-content: flex-end;
}

.message-received {
  justify-content: flex-start;
}

.message-content {
  max-width: 60%;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}

.message-sent .message-content {
  background: #95ec69;
  color: #fff;
}

.message-received .message-content {
  background: #fff;
  color: #2c3e50;
  border: 1px solid #ddd;
}

/* 时间戳样式 */
.timestamp {
  text-align: center;
  margin: 10px 0;
  color: #999;
  font-size: 12px;
}

/* 聊天输入框样式 */
.chat-input {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fff;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
  margin: 0 20px 20px 20px;
  border-radius: 0 0 5px 5px;
  height: 70px; /* 增加输入框的高度 */
}

.chat-input input {
  flex-grow: 1;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
  margin-right: 10px;
  height: 100%; /* 使输入框高度占满父元素 */
}

.chat-input button {
  padding: 10px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.chat-input button:hover {
  background: #2980b9;
}
/* 自定义滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1; /* 滚动条轨道颜色 */
  border-radius: 10px; /* 轨道圆角 */
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c9c8c8; /* 滚动条滑块颜色 */
  border-radius: 10px; /* 滑块圆角 */
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #e8e7e7; /* 滑块在悬停时的颜色 */
}
</style>
