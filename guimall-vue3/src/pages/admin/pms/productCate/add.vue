<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">添加商品分类</span>
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
        <a-button type="primary" @click="handleSubmit">提交</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()

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

const categoryList = ref([...CATEGORY_SELECT_OPTIONS])

const form = reactive({
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

const goBack = () => {
  router.push('/admin/pms/productCate')
}

const handleSubmit = () => {
  console.log('新增分类', { ...form })
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
