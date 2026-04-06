<template>
  <div class="rich-editor-wrapper">
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      class="rich-editor-toolbar"
    />
    <Editor
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      class="rich-editor-content"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup>
import { ref, shallowRef, watch, onBeforeUnmount } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const props = defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef()
const valueHtml = ref(props.modelValue || '')

// 精简工具栏：只保留常用功能
const toolbarConfig = {
  toolbarKeys: [
    'bold', 'italic', 'underline', 'through', 'color', 'bgColor',
    '|',
    'fontSize', 'lineHeight',
    '|',
    'bulletedList', 'numberedList',
    '|',
    'justifyLeft', 'justifyCenter', 'justifyRight',
    '|',
    'insertLink', 'uploadImage',
    '|',
    'undo', 'redo'
  ]
}

const editorConfig = {
  placeholder: '请输入商品描述内容...',
  MENU_CONF: {
    uploadImage: {
      // 禁用上传，用户可通过插入链接方式引用图片
      customUpload(file, insertFn) {
        // 后续可以对接 uploadFile API
        const reader = new FileReader()
        reader.onload = () => insertFn(reader.result, '', '')
        reader.readAsDataURL(file)
      }
    }
  }
}

const handleCreated = (editor) => {
  editorRef.value = editor
}

// 编辑器内容 → 父组件
watch(valueHtml, (val) => {
  emit('update:modelValue', val)
})

// 父组件 → 编辑器（回填场景）
watch(() => props.modelValue, (val) => {
  if (val !== valueHtml.value) {
    valueHtml.value = val || ''
  }
})

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) editor.destroy()
})
</script>

<style scoped>
.rich-editor-wrapper {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  overflow: hidden;
  line-height: normal;
}

.rich-editor-toolbar {
  border-bottom: 1px solid #f0f0f0;
}

.rich-editor-content {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}
</style>
