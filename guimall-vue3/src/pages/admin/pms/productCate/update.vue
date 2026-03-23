<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">更改商品分类</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="分类信息">
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="分类名称">
          <a-input v-model:value="form.name" placeholder="请输入分类名称" />
        </a-form-item>

        <a-form-item label="上级分类">
          <a-select v-model:value="form.parentId" class="w-full" placeholder="请选择">
            <a-select-option :value="0">一级分类</a-select-option>
            <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="商品单位">
          <a-input v-model:value="form.productUnit" placeholder="例如：斤 / 个 / 箱" />
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full" />
        </a-form-item>

        <a-form-item label="导航栏显示">
          <a-switch
            v-model:checked="form.navStatus"
            :checked-value="1"
            :un-checked-value="0"
          />
        </a-form-item>

        <a-form-item label="是否显示">
          <a-switch
            v-model:checked="form.showStatus"
            :checked-value="1"
            :un-checked-value="0"
          />
        </a-form-item>

        <a-form-item label="关键词">
          <a-input v-model:value="form.keywords" placeholder="用于搜索优化" />
        </a-form-item>

        <a-form-item label="分类图标">
          <a-input v-model:value="form.icon" placeholder="填写图标地址" />
        </a-form-item>

        <a-form-item label="分类描述">
          <a-textarea v-model:value="form.description" :rows="3" placeholder="请输入分类描述" />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const CATEGORY_SELECT_OPTIONS = [
  { id: 1, name: '水果' },
  { id: 2, name: '柑橘类' },
  { id: 3, name: '砂糖橘' },
  { id: 4, name: '脐橙' },
  { id: 5, name: '沃柑' },
  { id: 6, name: '热带水果' },
  { id: 7, name: '芒果' },
  { id: 8, name: '香蕉' },
  { id: 9, name: '蔬菜' },
  { id: 10, name: '叶菜类' },
  { id: 11, name: '生菜' },
  { id: 12, name: '粮油副食' },
  { id: 13, name: '大米' },
  { id: 14, name: '食用油' }
]

const CATEGORY_DETAIL_MOCK = {
  1: {
    id: 1,
    parentId: 0,
    name: '水果',
    productUnit: '斤',
    navStatus: 1,
    showStatus: 1,
    sort: 1,
    icon: '',
    keywords: '水果 新鲜',
    description: '新鲜水果分类'
  },
  2: {
    id: 2,
    parentId: 1,
    name: '柑橘类',
    productUnit: '斤',
    navStatus: 1,
    showStatus: 1,
    sort: 2,
    icon: '',
    keywords: '柑橘',
    description: '柑橘类子分类'
  },
  3: {
    id: 3,
    parentId: 2,
    name: '砂糖橘',
    productUnit: '斤',
    navStatus: 1,
    showStatus: 1,
    sort: 3,
    icon: '',
    keywords: '',
    description: ''
  }
}

const CATEGORY_LIST_MOCK = [
  { id: 1, name: '水果', parentName: '一级分类', sort: 1 },
  { id: 2, name: '柑橘类', parentName: '水果', sort: 2 },
  { id: 3, name: '砂糖橘', parentName: '柑橘类', sort: 3 },
  { id: 4, name: '脐橙', parentName: '柑橘类', sort: 4 },
  { id: 5, name: '沃柑', parentName: '柑橘类', sort: 5 },
  { id: 6, name: '热带水果', parentName: '水果', sort: 6 },
  { id: 7, name: '芒果', parentName: '热带水果', sort: 7 },
  { id: 8, name: '香蕉', parentName: '热带水果', sort: 8 },
  { id: 9, name: '蔬菜', parentName: '一级分类', sort: 9 },
  { id: 10, name: '叶菜类', parentName: '蔬菜', sort: 10 },
  { id: 11, name: '生菜', parentName: '叶菜类', sort: 11 },
  { id: 12, name: '粮油副食', parentName: '一级分类', sort: 12 },
  { id: 13, name: '大米', parentName: '粮油副食', sort: 13 },
  { id: 14, name: '食用油', parentName: '粮油副食', sort: 14 }
]

function getCategoryEditMockFromList(id) {
  const row = CATEGORY_LIST_MOCK.find((r) => r.id === id)
  if (!row) return null
  let parentId = 0
  if (row.parentName !== '一级分类') {
    const p = CATEGORY_SELECT_OPTIONS.find((o) => o.name === row.parentName)
    parentId = p ? p.id : 0
  }
  return {
    id: row.id,
    parentId,
    name: row.name,
    productUnit: '斤',
    navStatus: 1,
    showStatus: 1,
    sort: row.sort,
    icon: '',
    keywords: '',
    description: ''
  }
}

const categoryList = ref([...CATEGORY_SELECT_OPTIONS])

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

const loadMock = () => {
  const id = Number(route.query.id)
  const row =
    CATEGORY_DETAIL_MOCK[id] ||
    getCategoryEditMockFromList(id) ||
    {
      id: id || 1,
      parentId: 0,
      name: '分类',
      productUnit: '斤',
      navStatus: 1,
      showStatus: 1,
      sort: 1,
      icon: '',
      keywords: '',
      description: ''
    }
  Object.assign(form, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/pms/productCate')
}

const handleSubmit = () => {
  console.log('修改分类', { ...form })
  goBack()
}
</script>

<style scoped>
:deep(.ant-input),
:deep(.ant-input-number),
:deep(.ant-select-selector),
:deep(.ant-input-affix-wrapper) {
  border: 1px solid #d9d9d9 !important;
  border-radius: 6px !important;
}

:deep(.ant-input:hover),
:deep(.ant-select-selector:hover),
:deep(.ant-input-number:hover) {
  border-color: #bfbfbf !important;
}

:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-select-focused .ant-select-selector),
:deep(.ant-input-number-focused) {
  border-color: #4096ff !important;
  box-shadow: 0 0 0 2px rgba(64, 150, 255, 0.15);
}

:deep(.ant-select-selector) {
  height: 32px !important;
  display: flex;
  align-items: center;
}

:deep(.ant-input-number) {
  width: 100%;
}

:deep(textarea.ant-input) {
  border-radius: 6px !important;
}
</style>
