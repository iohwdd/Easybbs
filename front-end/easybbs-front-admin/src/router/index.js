import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: '登录页面',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      component: () => import('@/views/Layout.vue'),
      redirect: '/forum/article',
      children: [
        {
          path: '/forum',
          name: '内容管理',
          children: [
            {
              path: 'article',
              name: '帖子管理',
              component: () => import('@/views/forum/ArticleList.vue')
            },
            {
              path: 'comment',
              name: '评论管理',
              component: () => import('@/views/forum/CommentList.vue')
            },
            {
              path: 'board',
              name: '板块管理',
              component: () => import('@/views/forum/BoardList.vue')
            }
          ]
        },
        {
          path: '/user',
          name: '用户管理',
          children: [
            {
              path: 'list',
              name: '用户列表',
              component: () => import('@/views/user/UserList.vue')
            }
          ]
        },
        {
          path: '/settings',
          name: '设置',
          children: [
            {
              path: 'sys',
              name: '系统设置',
              component: () => import('@/views/settings/SysSettings.vue')
            }
          ]
        }
      ]
    }
  ]
})
router.beforeEach((to, from, next) => {
  // 检查是否需要登录才能访问
  const isLoginRequired = to.matched.some(record => record.meta.requiresAuth);

  // 如果需要登录，且未登录，则跳转到登录页面
  if (isLoginRequired && !store.state.loginUserInfo) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
});
export default router
