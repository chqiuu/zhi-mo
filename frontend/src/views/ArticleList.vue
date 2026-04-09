<template>
  <div class="page-container">
    <div class="page-header">
      <h2>📝 文章管理</h2>
      <el-button type="primary" @click="goToEditor">
        <el-icon><Plus /></el-icon>
        新建文章
      </el-button>
    </div>

    <div class="card">
      <el-table :data="articleList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="goToEditor(row.id)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { articleApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const articleList = ref([])

const loadArticleList = async () => {
  loading.value = true
  try {
    const res = await articleApi.list()
    articleList.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const goToEditor = (id) => {
  if (id) {
    router.push(`/article/editor/${id}`)
  } else {
    router.push('/article/editor')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这篇文章吗？', '提示', {
      type: 'warning'
    })
    await articleApi.delete(id)
    ElMessage.success('删除成功')
    loadArticleList()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadArticleList()
})
</script>
