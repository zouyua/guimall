<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">更改商品信息</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="商品名称">
          <a-input v-model:value="form.name" placeholder="请输入商品名称" />
        </a-form-item>

        <a-form-item label="商品分类">
          <a-select v-model:value="form.categoryId" placeholder="请选择分类" class="w-full" allow-clear>
            <a-select-option :value="1">水果</a-select-option>
            <a-select-option :value="2">蔬菜</a-select-option>
            <a-select-option :value="3">粮油副食</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="关联农户">
          <a-select v-model:value="form.farmerId" placeholder="请选择农户" class="w-full" allow-clear>
            <a-select-option :value="1">张三</a-select-option>
            <a-select-option :value="2">李四</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="主图地址">
          <a-input v-model:value="form.pic" placeholder="图片 URL" />
        </a-form-item>

        <a-form-item label="销售价格">
          <a-input-number v-model:value="form.price" :min="0" :precision="2" class="w-full" />
        </a-form-item>

        <a-form-item label="市场价">
          <a-input-number v-model:value="form.originalPrice" :min="0" :precision="2" class="w-full" />
        </a-form-item>

        <a-form-item label="库存">
          <a-input-number v-model:value="form.stock" :min="0" class="w-full" />
        </a-form-item>

        <a-form-item label="销量">
          <a-input-number v-model:value="form.sale" :min="0" class="w-full" disabled />
        </a-form-item>

        <a-form-item label="单位">
          <a-input v-model:value="form.unit" placeholder="如：斤、箱" />
        </a-form-item>

        <a-form-item label="上架">
          <a-switch v-model:checked="form.publishStatus" />
        </a-form-item>

        <a-form-item label="新品">
          <a-switch v-model:checked="form.newStatus" />
        </a-form-item>

        <a-form-item label="推荐">
          <a-switch v-model:checked="form.recommendStatus" />
        </a-form-item>

        <a-form-item label="商品描述">
          <a-textarea v-model:value="form.description" :rows="4" placeholder="请输入商品描述" />
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
import { reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const form = reactive({
  id: null,
  name: '',
  categoryId: undefined,
  farmerId: undefined,
  pic: '',
  price: undefined,
  originalPrice: undefined,
  stock: 0,
  sale: 0,
  unit: '斤',
  publishStatus: true,
  newStatus: false,
  recommendStatus: false,
  description: ''
})

const mockById = {
  1: {
    id: 1,
    name: '砂糖橘',
    categoryId: 1,
    farmerId: 1,
    pic: 'https://picsum.photos/80',
    price: 12.5,
    originalPrice: 15,
    stock: 100,
    sale: 200,
    unit: '斤',
    publishStatus: true,
    newStatus: true,
    recommendStatus: false,
    description: '新鲜砂糖橘，产地直发。'
  },
  2: {
    id: 2,
    name: '脐橙',
    categoryId: 1,
    farmerId: 2,
    pic: 'https://picsum.photos/81',
    price: 18.9,
    originalPrice: 22,
    stock: 80,
    sale: 120,
    unit: '斤',
    publishStatus: false,
    newStatus: false,
    recommendStatus: true,
    description: '赣南脐橙。'
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(form, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/pms/product')
}

const handleSubmit = () => {
  console.log('更新商品', { ...form })
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
