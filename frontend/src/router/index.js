import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/article'
  },
  {
    path: '/article',
    name: 'ArticleList',
    component: () => import('@/views/ArticleList.vue'),
    meta: { title: '文章列表' }
  },
  {
    path: '/article/editor',
    name: 'ArticleEditor',
    component: () => import('@/views/ArticleEditor.vue'),
    meta: { title: '文章编辑器' }
  },
  {
    path: '/article/editor/:id',
    name: 'ArticleEditorEdit',
    component: () => import('@/views/ArticleEditor.vue'),
    meta: { title: '编辑文章' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - 执墨`
  }
  next()
})

export default router
