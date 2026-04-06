<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回
        </a-button>
        <span class="text-base font-semibold">添加商品类型</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="类型信息">
      <a-alert
        class="mb-6"
        type="info"
        show-icon
        message="使用说明"
        description="商品类型用于为商品定义「规格属性」和「参数属性」。例如创建类型「桂林水果」后，可在列表中展开该类型，添加规格（如：净含量 5斤/10斤）和参数（如：产地、保质期）。商品在添加/编辑时选择此类型，即可填写对应的规格和参数值。"
      />
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item name="name" label="类型名称" :required="true">
          <a-input v-model:value="form.name" placeholder="如：桂林水果、桂林蔬菜、礼盒套装" />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">提交</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>

      <a-card class="mt-6" size="small" title="创建类型后的操作步骤">
        <a-steps direction="vertical" :current="-1" size="small">
          <a-step title="创建类型" description="输入类型名称（如「桂林水果」），点击提交" />
          <a-step title="添加规格属性" description="返回列表，展开该类型，在「规格」标签页点击新增规格（如：净含量），设置可选值（5斤,10斤）" />
          <a-step title="添加参数属性" description="在「参数」标签页点击新增参数（如：产地、保质期），用于商品详情展示" />
          <a-step title="关联到商品" description="在商品添加/编辑页面选择「属性分类」为此类型，即可填写规格和参数值" />
        </a-steps>
      </a-card>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { createProductAttrCategory } from '@/api/admin/productAttrCategory'

const router = useRouter()
const formRef = ref(null)

const rules = {
  name: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
}

const form = reactive({
  name: ''
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
  await createProductAttrCategory({ name: form.name.trim() })
  message.success('新增成功')
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
