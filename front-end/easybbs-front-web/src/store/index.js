import { createStore } from 'vuex'
export default createStore({
  state: {
    // 用户信息
    loginUserInfo: null,
    // 是否展示登录界面 -> 用于登录超时时，并进行只有登录后才能进行的操作，提示用户进行重新登录
    showLogin: false,
    // 板块信息 -> 展示板块导航栏
    boardList: [],
    //当前一级板块
    activePBoardId: 0,
    //二级板块
    activeBoardId: 0,
    // 消息数量
    messageCountInfo: {},
    //私信数量
    chartMsgCount: {},
    //系统设置
    sysSetting: {}
  },
  getters: {
    getLoginUserInfo: (state) => {
      return state.loginUserInfo
    },
    getBoardList: (state) => {
      return state.boardList
    },
    getSubBoardList: (state) => (boardId) => {
      let board = state.boardList.find((item) => {
        return item.boardId == boardId
      })
      return board ? board.children : []
    },
    getActivePBoardId: (state) => {
      return state.activePBoardId
    },
    getActiveBoardId: (state) => {
      return state.activeBoardId
    },
    getMessageCountInfo: (state) => {
      return state.messageCountInfo
    },
    getChartMsgCountInfo: (state) => {
      return state.chartMsgCount
    }
  },
  mutations: {
    updateLoginUserInfo: (state, value) => {
      state.loginUserInfo = value
    },
    showLogin: (state, value) => {
      state.showLogin = value
    },
    saveBoardList: (state, value) => {
      state.boardList = value
    },
    setActivePBoardId: (state, value) => {
      state.activePBoardId = value
    },
    setActiveBoardId: (state, value) => {
      state.activeBoardId = value
    },
    updateMessageCountInfo: (state, value) => {
      state.messageCountInfo = value
    },
    updateChartMsgCount: (state, value) => {
      debugger
      state.chartMsgCount = value
    },
    readMessage: (state, value) => {
      state.messageCountInfo.total =
        state.messageCountInfo.total - state.messageCountInfo[value]
      state.messageCountInfo[value] = 0
    },
    setSysSetting: (state, value) => {
      state.sysSetting = value
    }
  },
  actions: {},
  modules: {}
})
