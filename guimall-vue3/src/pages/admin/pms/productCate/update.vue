<template>
  <a-modal
    v-model:open="visible"
    title="编辑分类"
    width="650px"
    @ok="handleSubmit"
    @cancel="visible=false"
  >

    <a-form
      :model="form"
      layout="horizontal"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
    >

      <!-- 分类名称 -->
      <a-form-item label="分类名称">
        <a-input
          v-model:value="form.name"
          placeholder="请输入分类名称"
        />
      </a-form-item>

      <!-- 上级分类 -->
      <a-form-item label="上级分类">
        <a-select v-model:value="form.parentId">

          <a-select-option :value="0">
            一级分类
          </a-select-option>

          <a-select-option
            v-for="item in categoryList"
            :key="item.id"
            :value="item.id"
          >
            {{ item.name }}
          </a-select-option>

        </a-select>
      </a-form-item>

      <!-- 商品单位 -->
      <a-form-item label="商品单位">
        <a-input
          v-model:value="form.productUnit"
          placeholder="例如：斤 / 个 / 箱"
        />
      </a-form-item>

      <!-- 排序 -->
      <a-form-item label="排序">
        <a-input-number
          v-model:value="form.sort"
          :min="0"
        />
      </a-form-item>

      <!-- 导航栏显示 -->
      <a-form-item label="导航栏显示">
        <a-switch
          v-model:checked="form.navStatus"
          :checked-value="1"
          :un-checked-value="0"
        />
      </a-form-item>

      <!-- 是否显示 -->
      <a-form-item label="是否显示">
        <a-switch
          v-model:checked="form.showStatus"
          :checked-value="1"
          :un-checked-value="0"
        />
      </a-form-item>

      <!-- 关键词 -->
      <a-form-item label="关键词">
        <a-input
          v-model:value="form.keywords"
          placeholder="用于搜索优化"
        />
      </a-form-item>

      <!-- 分类图标 -->
      <a-form-item label="分类图标">
        <a-input
          v-model:value="form.icon"
          placeholder="填写图标地址"
        />
      </a-form-item>

      <!-- 分类描述 -->
      <a-form-item label="分类描述">
        <a-textarea
          v-model:value="form.description"
          :rows="3"
          placeholder="请输入分类描述"
        />
      </a-form-item>

    </a-form>

  </a-modal>
</template>

<script setup>

import { ref, reactive } from 'vue'

/* 弹窗控制 */
const visible = ref(false)

/* 分类列表 */
const categoryList = ref([])

/* 表单数据 */
const form = reactive({

  id: null,
  parentId: 0,
  name: '',
  productUnit: '',
  navStatus: 1,
  showStatus: 1,
  sort: 0,
  icon: '',
  keywords: '',
  description: ''

})

/* 打开弹窗 */
const open = (data) => {

  visible.value = true

  Object.assign(form, data)

}

/* 提交 */
const handleSubmit = () => {

  console.log('修改分类', form)

  visible.value = false

}

/* 暴露方法 */
defineExpose({
  open
})

</script>

<style scoped>

/* 统一输入组件样式 */
:deep(.ant-input),
:deep(.ant-input-number),
:deep(.ant-select-selector),
:deep(.ant-input-affix-wrapper) {
  border: 1px solid #d9d9d9 !important;
  border-radius: 6px !important;
}

/* hover 效果 */
:deep(.ant-input:hover),
:deep(.ant-select-selector:hover),
:deep(.ant-input-number:hover) {
  border-color: #bfbfbf !important;
}

/* focus 效果 */
:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-select-focused .ant-select-selector),
:deep(.ant-input-number-focused) {
  border-color: #4096ff !important;
  box-shadow: 0 0 0 2px rgba(64,150,255,.15);
}

/* select高度统一 */
:deep(.ant-select-selector) {
  height: 32px !important;
  display: flex;
  align-items: center;
}

/* input-number宽度 */
:deep(.ant-input-number) {
  width: 100%;
}

/* textarea */
:deep(textarea.ant-input) {
  border-radius: 6px !important;
}

</style>