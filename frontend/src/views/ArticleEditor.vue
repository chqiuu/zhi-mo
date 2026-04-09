<template>
  <div class="editor-page">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <span class="toolbar-title">{{ isEdit ? '编辑文章' : '新建文章' }}</span>
      </div>
      <div class="toolbar-right">
        <el-button type="primary" @click="handleSave">
          <el-icon><Check /></el-icon>
          保存
        </el-button>
      </div>
    </div>

    <div class="editor-container">
      <!-- 左侧编辑区 -->
      <div class="editor-panel">
        <!-- 导入功能 -->
        <div class="import-section">
          <el-button @click="showImportDialog = true">
            <el-icon><Upload /></el-icon>
            导入已有文章
          </el-button>
        </div>

        <!-- 标题 -->
        <div class="editor-card">
          <label class="editor-label">标题</label>
          <el-input
            v-model="form.title"
            placeholder="请输入文章标题"
            maxlength="200"
            show-word-limit
          />
        </div>

        <!-- 摘要 -->
        <div class="editor-card">
          <label class="editor-label">摘要</label>
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要（可选）"
            maxlength="500"
            show-word-limit
          />
        </div>

        <!-- 正文 -->
        <div class="editor-card">
          <label class="editor-label">正文</label>
          <div class="content-editor">
            <div class="editor-toolbar">
              <button type="button" @click="execCommand('bold')" title="加粗"><b>B</b></button>
              <button type="button" @click="execCommand('italic')" title="斜体"><i>I</i></button>
              <button type="button" @click="execCommand('underline')" title="下划线"><u>U</u></button>
              <span class="toolbar-divider">|</span>
              <button type="button" @click="execCommand('insertUnorderedList')" title="无序列表">• 列表</button>
              <button type="button" @click="execCommand('insertOrderedList')" title="有序列表">1. 列表</button>
              <span class="toolbar-divider">|</span>
              <button type="button" @click="execCommand('formatBlock', '<blockquote>')" title="引用">❝ 引用</button>
              <button type="button" @click="execCommand('formatBlock', '<h2>')" title="二级标题">H2</button>
              <button type="button" @click="execCommand('formatBlock', '<h3>')" title="三级标题">H3</button>
            </div>
            <div
              ref="contentRef"
              class="content-editable"
              contenteditable="true"
              @input="handleContentInput"
              @paste="handlePaste"
            ></div>
          </div>
        </div>

        <!-- 复制功能区 -->
        <div class="editor-card copy-section">
          <label class="editor-label">📋 一键复制到公众号</label>
          <div class="copy-buttons">
            <el-button class="copy-btn" @click="copyTitle">
              <el-icon><Document /></el-icon>
              复制标题
            </el-button>
            <el-button class="copy-btn" @click="copySummary">
              <el-icon><DocumentCopy /></el-icon>
              复制摘要
            </el-button>
            <el-button class="copy-btn wx" @click="copyWechat">
              <el-icon><Share /></el-icon>
              复制微信公众号格式
            </el-button>
          </div>
          <div class="copy-tip">
            <el-icon><InfoFilled /></el-icon>
            点击「复制微信公众号格式」可生成带内联样式的HTML，直接粘贴到公众号后台
          </div>
        </div>
      </div>

      <!-- 右侧预览区 -->
      <div class="preview-panel">
        <div class="preview-header">
          <span>📱 预览效果</span>
        </div>
        <div class="preview-phone">
          <div class="preview-content" v-html="previewHtml"></div>
        </div>
      </div>
    </div>

    <!-- 导入弹窗 -->
    <el-dialog v-model="showImportDialog" title="导入文章" width="600px">
      <el-input
        v-model="importUrl"
        placeholder="请输入微信文章链接"
        clearable
      />
      <template #footer>
        <el-button @click="showImportDialog = false">取消</el-button>
        <el-button type="primary" @click="handleImport">确认导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { articleApi } from '@/api'

const router = useRouter()
const route = useRoute()

const contentRef = ref(null)
const showImportDialog = ref(false)
const importUrl = ref('')
const isEdit = computed(() => !!route.params.id)

const form = ref({
  id: null,
  title: '',
  summary: '',
  content: ''
})

const previewHtml = computed(() => {
  const title = form.value.title || '文章标题'
  const summary = form.value.summary || '文章摘要将在此处显示'
  const content = form.value.content || '<p style="color:#999;text-align:center;">正文内容将在此处显示</p>'
  return `
    <div class="article-container">
      <h1 class="article-title">${title}</h1>
      <div class="article-summary">${summary}</div>
      <div class="article-content">${content}</div>
    </div>
  `
})

const loadArticle = async (id) => {
  try {
    const res = await articleApi.getById(id)
    form.value = {
      id: res.data.id,
      title: res.data.title,
      summary: res.data.summary,
      content: res.data.content
    }
    if (contentRef.value) {
      contentRef.value.innerHTML = res.data.content || ''
    }
  } catch (e) {
    console.error(e)
  }
}

const execCommand = (command, value = null) => {
  document.execCommand(command, false, value)
  contentRef.value?.focus()
}

const handleContentInput = () => {
  form.value.content = contentRef.value?.innerHTML || ''
}

const handlePaste = (e) => {
  e.preventDefault()
  const text = e.clipboardData.getData('text/plain')
  document.execCommand('insertText', false, text)
}

const handleSave = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!form.value.content) {
    ElMessage.warning('请输入正文')
    return
  }

  try {
    if (isEdit.value) {
      await articleApi.update(form.value)
    } else {
      await articleApi.save(form.value)
    }
    ElMessage.success('保存成功')
    router.push('/article')
  } catch (e) {
    console.error(e)
  }
}

const handleImport = async () => {
  if (!importUrl.value) {
    ElMessage.warning('请输入文章链接')
    return
  }
  try {
    const res = await articleApi.import({ sourceUrl: importUrl.value })
    form.value = {
      id: null,
      title: res.data.title,
      summary: res.data.summary,
      content: res.data.content
    }
    if (contentRef.value) {
      contentRef.value.innerHTML = res.data.content || ''
    }
    showImportDialog.value = false
    ElMessage.success('导入成功')
  } catch (e) {
    console.error(e)
  }
}

const copyTitle = () => {
  navigator.clipboard.writeText(form.value.title)
  ElMessage.success('标题已复制')
}

const copySummary = () => {
  navigator.clipboard.writeText(form.value.summary)
  ElMessage.success('摘要已复制')
}

const copyWechat = () => {
  const title = form.value.title
  const content = form.value.content

  const wechatHtml = `
<h1 style="text-align:center;font-size:22px;margin-bottom:20px;">${title}</h1>
<div style="background:#f5f5f5;padding:15px;border-left:4px solid #667eea;margin-bottom:20px;">${form.value.summary}</div>
${content}
  `.trim()

  const blob = new Blob([wechatHtml], { type: 'text/html' })
  const clipboardItem = new ClipboardItem({ 'text/html': blob })
  navigator.clipboard.write([clipboardItem])
  ElMessage.success('微信公众号格式已复制，可直接粘贴到公众号后台')
}

const goBack = () => {
  router.push('/article')
}

onMounted(() => {
  if (isEdit.value) {
    loadArticle(route.params.id)
  }
})
</script>

<style lang="scss" scoped>
.editor-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.toolbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: linear-gradient(135deg, #667eea, #764ba2);
  padding: 12px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 100;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);

  .toolbar-left {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .toolbar-title {
    color: white;
    font-size: 16px;
    font-weight: 500;
  }

  :deep(.el-button) {
    color: white;
    background: rgba(255, 255, 255, 0.2);
    border: none;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
    }
  }
}

.editor-container {
  display: flex;
  padding-top: 70px;
  min-height: calc(100vh - 70px);
}

.editor-panel {
  flex: 1;
  padding: 20px;
  max-width: 900px;
}

.import-section {
  margin-bottom: 16px;
}

.editor-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .editor-label {
    display: block;
    font-weight: 600;
    margin-bottom: 12px;
    color: #333;
  }
}

.content-editor {
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  overflow: hidden;
}

.editor-toolbar {
  background: #f5f5f5;
  padding: 8px 12px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;

  button {
    padding: 4px 12px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background: white;
    cursor: pointer;
    font-size: 13px;

    &:hover {
      border-color: #667eea;
      color: #667eea;
    }
  }

  .toolbar-divider {
    color: #dcdfe6;
  }
}

.content-editable {
  min-height: 400px;
  padding: 16px;
  outline: none;

  &:focus {
    border-color: #667eea;
  }

  :deep(p) {
    margin-bottom: 12px;
    line-height: 1.8;
  }

  :deep(h2) {
    font-size: 18px;
    margin: 20px 0 12px;
    padding-left: 12px;
    border-left: 4px solid #667eea;
  }

  :deep(h3) {
    font-size: 16px;
    margin: 16px 0 10px;
    font-weight: 600;
  }

  :deep(blockquote) {
    background: #f0f5ff;
    border-left: 4px solid #667eea;
    padding: 12px 16px;
    margin: 16px 0;
  }

  :deep(ul), :deep(ol) {
    padding-left: 24px;
    margin-bottom: 12px;
  }

  :deep(li) {
    line-height: 1.8;
    margin-bottom: 4px;
  }

  :deep(pre) {
    background: #1e1e1e;
    color: #d4d4d4;
    padding: 16px;
    border-radius: 6px;
    overflow-x: auto;
    font-family: 'Courier New', monospace;
  }

  :deep(img) {
    max-width: 100%;
    border-radius: 6px;
    margin: 12px 0;
  }
}

.copy-section {
  .copy-buttons {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
    margin-bottom: 12px;
  }

  .copy-btn {
    &.wx {
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: white;
      border: none;

      &:hover {
        opacity: 0.9;
      }
    }
  }

  .copy-tip {
    color: #999;
    font-size: 12px;
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.preview-panel {
  width: 420px;
  background: #f0f0f0;
  padding: 20px;
  position: fixed;
  right: 0;
  top: 70px;
  bottom: 0;
  overflow-y: auto;

  .preview-header {
    margin-bottom: 16px;
    font-weight: 600;
    color: #666;
  }

  .preview-phone {
    background: white;
    border-radius: 24px;
    padding: 40px 16px 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .preview-content {
      min-height: 400px;

      :deep(.article-container) {
        font-family: 'Microsoft YaHei', sans-serif;
        font-size: 15px;
        line-height: 1.8;
        color: #333;

        .article-title {
          font-size: 22px;
          text-align: center;
          margin-bottom: 16px;
          font-weight: 600;
        }

        .article-summary {
          background: #f5f5f5;
          padding: 12px;
          border-left: 3px solid #667eea;
          margin-bottom: 20px;
          font-size: 13px;
          color: #666;
        }

        .article-content {
          text-align: justify;

          p {
            margin-bottom: 12px;
          }
        }
      }
    }
  }
}
</style>
