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
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="类型名称">
          <a-input v-model:value="form.name" placeholder="如：规格、颜色、产地" />
        </a-form-item>

        <a-form-item label="关联分类">
          <a-select v-model:value="form.categoryId" placeholder="请选择分类" class="w-full">
            <a-select-option :value="1">水果</a-select-option>
            <a-select-option :value="2">蔬菜</a-select-option>
            <a-select-option :value="3">粮油副食</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="录入方式">
          <a-select v-model:value="form.inputType" placeholder="请选择" class="w-full">
            <a-select-option value="唯一文本">唯一文本</a-select-option>
            <a-select-option value="单选">单选</a-select-option>
            <a-select-option value="多选">多选</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full" />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.remark" :rows="3" placeholder="选填" />
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
  inputType: '单选',
  sort: 0,
  remark: ''
})

const categoryNameToId = {
  水果: 1,
  蔬菜: 2,
  粮油副食: 3
}

const mockById = {
  1: {
    id: 1,
    name: '规格',
    categoryName: '水果',
    inputType: '多选',
    sort: 1,
    remark: '如：大果、中果'
  },
  2: {
    id: 2,
    name: '产地',
    categoryName: '水果',
    inputType: '唯一文本',
    sort: 2,
    remark: ''
  },
  3: {
    id: 3,
    name: '包装',
    categoryName: '蔬菜',
    inputType: '单选',
    sort: 1,
    remark: '箱装/袋装'
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(form, {
    id: row.id,
    name: row.name,
    categoryId: categoryNameToId[row.categoryName],
    inputType: row.inputType,
    sort: row.sort,
    remark: row.remark
  })
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/pms/productAttr/productAttrList')
}

const handleSubmit = () => {
  console.log('更新商品类型', { ...form })
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
