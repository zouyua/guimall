<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回
        </a-button>
        <span class="text-base font-semibold">更改商品类型</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="类型信息">
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item name="name" label="类型名称" :required="true">
          <a-input v-model:value="form.name" placeholder="如：规格、参数、产地类型" />
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
import { reactive, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import {
  fetchProductAttrCategoryList,
  updateProductAttrCategory
} from '@/api/admin/productAttrCategory'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)

const rules = {
  name: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
}

const form = reactive({
  id: null,
  name: ''
})
const fetchDetail = async () => {
  const id = Number(route.query.id)
  if (!id || Number.isNaN(id)) return

  const rsp = await fetchProductAttrCategoryList({ current: 1, size: 1000 })
  const row = (rsp?.data || []).find((i) => Number(i.id) === id)
  if (!row) {
    message.error('未找到商品类型')
    return
  }

  Object.assign(form, {
    id: row.id,
    name: row.name ?? ''
  })
}

onMounted(() => {
  fetchDetail()
})

const goBack = () => {
  router.push('/admin/pms/productAttr/productAttrList')
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }
  await updateProductAttrCategory({
    id: form.id,
    name: form.name.trim()
  })
  message.success('保存成功')
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
