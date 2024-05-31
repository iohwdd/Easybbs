<template>
  <div class="layout-body">
    <el-container>
      <el-aside class="aside" :style="{ width: asideWidth + 'px' }">
        <div class="logo">
          <span v-if="!menuCollapse">Easybbs管理后台</span>
        </div>
        <div class="menu-panel">
          <el-menu
            :default-openeds="defalutOpends"
            :collapse-transition="false"
            :collapse="menuCollapse"
            :default-active="defaultActive"
            router
            background-color="#3d3c4a"
            active-text-color="#fff"
            text-color="#fff"
            class="el-menu-vertical-demo"
          >
            <template v-for="item in menuList" :key="item.path">
              <el-sub-menu v-if="item.childern" :index="item.path">
                <template #title>
                  <i :class="['iconfont', item.icon]"></i>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
                <el-menu-item
                  v-for="subItem in item.childern"
                  :index="subItem.path"
                  :key="subItem.path"
                >
                  <span class="menu-name">{{ subItem.menuName }}</span>
                </el-menu-item>
              </el-sub-menu>
              <el-menu-item v-else :index="item.path">
                <template #title>
                  <i :class="['iconfont', item.icon]"></i>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div
            :class="[
              'op-menu',
              'iconfont',
              menuCollapse ? 'icon-expand' : 'icon-collapse'
            ]"
            @click="opMenu"
          ></div>
          <!-- 面包屑 -->
          <div class="menu-bread">
            <el-breadcrumb>
              <template
                v-for="(item, index) in menuBreadCrumbList"
                :key="index"
              >
                <el-breadcrumb-item v-if="item.name">
                  {{ item.name }}
                </el-breadcrumb-item>
              </template>
            </el-breadcrumb>
          </div>
        </el-header>
        <el-main class="main-content">
          <!-- 标签栏 -->
          <div class="tag-content">
            <el-tabs
              type="card"
              v-model="defaultActive"
              @tab-change="tabClick"
              @edit="editTab"
            >
              <el-tab-pane
                v-for="(item, index) in tabList"
                :key="index"
                :name="item.path"
                :label="item.menuName"
                :closable="tabList.length > 1"
              >
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="content-body">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  watch,
  onMounted
} from 'vue'
import { useRouter, useRoute } from 'vue-router'
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
onMounted(() => {
  let userInfo = proxy.VueCookies.get('userInfo')
  if (userInfo == null) {
    proxy.Message.error('请先登录')
    //router.push('/login')
  }
})
// 系统菜单
const menuList = [
  {
    menuName: '内容管理',
    icon: 'icon-article',
    path: '/forum',
    childern: [
      {
        menuName: '帖子管理',
        path: '/forum/article'
      },
      {
        menuName: '评论管理',
        path: '/forum/comment'
      },
      {
        menuName: '板块管理',
        path: '/forum/board'
      }
    ]
  },
  {
    menuName: '用户管理',
    icon: 'icon-user',
    path: '/user',
    childern: [
      {
        menuName: '用户列表',
        path: '/user/list'
      }
    ]
  },
  {
    menuName: '设置',
    icon: 'icon-settings',
    path: '/settings',
    childern: [
      {
        menuName: '系统设置',
        path: '/settings/sys'
      }
    ]
  }
]
const menuMap = ref({})

//默认展开的菜单
const defalutOpends = ref([])
const init = () => {
  menuList.forEach((item) => {
    defalutOpends.value.push(item.path)
    item.childern.forEach((subItem) => {
      menuMap[subItem.path] = subItem
    })
  })
}
init()
//收起关闭菜单
const menuCollapse = ref(false)
const asideWidth = ref(250)
const opMenu = () => {
  menuCollapse.value = !menuCollapse.value
  if (menuCollapse.value) {
    asideWidth.value = 63
  } else {
    asideWidth.value = 250
  }
}
//tab操作
const tabList = ref([])
const tabClick = (e) => {
  router.push(e)
}
const editTab = (targetKey, action) => {
  if (action !== 'remove') {
    return
  }
  // 删除标签
  let tabs = tabList.value
  let currentMenu = defaultActive.value.path
  if (targetKey == defaultActive.value) {
    tabs.forEach((item, index) => {
      if (item.path == targetKey) {
        let nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          currentMenu = nextTab.path
        }
      }
    })
  }
  tabList.value = tabList.value.filter((item) => {
    return item.path !== targetKey
  })
  if (currentMenu !== defaultActive.value) {
    router.push(currentMenu)
  }
}
//面包屑导航
const menuBreadCrumbList = ref([])
//默认选中
const defaultActive = ref()
watch(
  () => route,
  (newVal, oldVal) => {
    defaultActive.value = route.path
    menuBreadCrumbList.value = route.matched
    let currentMenu = tabList.value.find((item) => {
      return item.path == defaultActive.value
    })
    if (!currentMenu) {
      tabList.value.push(menuMap[route.path])
    }
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
// 菜单收起样式
.el-popper {
  border: none !important;
  .el-menu-item.is-active {
    background: #2c2c8a;
  }
  .el-menu-item:hover {
    color: #d8d8ee;
  }
  .el-menu-popup {
    padding: 0px;
  }
}
.layout-body {
  //background: #fff;
  .aside {
    background: #3d3c4a;
    .logo {
      background: #191828;
      display: flex;
      color: #fff;
      height: 50px;
      align-items: center;
      font-size: 18px;
      padding-left: 5px;
    }
    .menu-panel {
      height: calc(100vh - 50px);
    }
    .menu-name {
      padding-left: 10px;
    }
    .el-menu {
      border-right: none;
    }
    .el-menu-item {
      background: #353544;
    }
    .el-menu-item.is-active {
      color: #fff;
      background: #2c2c8a;
      //background: var(-el-color-primary);
    }
    .el-menu-item:hover {
      color: #d8d8ee;
    }
  }
  .header {
    background: #fff;
    border-bottom: 1px solid #ddd;
    height: 50px;
    line-height: 50px;
    padding: 0px 10px !important;
    display: flex;
    align-items: center;
    .op-menu {
      font-size: 20px;
      cursor: pointer;
      color: #3a3a40;
    }
    .menu-bread {
      margin-left: 10px;
    }
  }
  .main-content {
    padding: 0px;
    .tag-content {
      .el-tabs--border-card {
        border: none;
      }
      .el-tabs__content {
        display: none;
      }
    }
    .content-body {
      overflow: hidden;
      padding: 10px 10px 5px 10px;
    }
  }
}
</style>
