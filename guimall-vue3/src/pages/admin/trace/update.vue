<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑产地</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="产地名称" required>
          <a-input v-model:value="form.name" placeholder="如：桂林砂糖橘基地" allow-clear />
        </a-form-item>

        <a-form-item label="所在地区" required>
          <a-select v-model:value="form.region" placeholder="请选择省/大区" class="w-full" allow-clear>
            <a-select-option value="广西">广西</a-select-option>
            <a-select-option value="江西">江西</a-select-option>
            <a-select-option value="云南">云南</a-select-option>
            <a-select-option value="四川">四川</a-select-option>
            <a-select-option value="其他">其他</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="详细地址" required>
          <a-input v-model:value="form.address" placeholder="市、区县、乡镇或基地具体位置" allow-clear />
        </a-form-item>

        <a-form-item label="关联商品数">
          <a-input-number v-model:value="form.productCount" :min="0" class="w-full" disabled />
        </a-form-item>

        <a-form-item label="创建时间">
          <a-input v-model:value="form.createTime" disabled />
        </a-form-item>

        <a-form-item label="产地简介">
          <a-textarea
            v-model:value="form.intro"
            :rows="4"
            placeholder="选填：种植品类、基地规模、认证资质等说明"
            allow-clear
          />
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
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const form = reactive({
  id: null,
  name: '',
  region: undefined,
  address: '',
  productCount: 0,
  createTime: '',
  intro: ''
})

const mockById = {
  1: {
    id: 1,
    name: '桂林砂糖橘基地',
    region: '广西',
    address: '广西桂林市临桂区',
    productCount: 3,
    createTime: '2026-03-01',
    intro: '桂林核心产区，砂糖橘地理标志范围内。'
  },
  2: {
    id: 2,
    name: '赣南脐橙园',
    region: '江西',
    address: '江西赣州市',
    productCount: 2,
    createTime: '2026-03-05',
    intro: ''
  },
  3: {
    id: 3,
    name: '高原蔬菜合作社',
    region: '云南',
    address: '云南昆明周边',
    productCount: 5,
    createTime: '2026-03-08',
    intro: '高原冷凉蔬菜，供港基地。'
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
  router.push('/admin/trace/origin')
}

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入产地名称')
    return
  }
  if (!form.region) {
    message.warning('请选择所在地区')
    return
  }
  if (!form.address?.trim()) {
    message.warning('请输入详细地址')
    return
  }
  console.log('保存产地', { ...form })
  message.success('保存成功（演示数据，未请求后端）')
  goBack()
}
</script>
