<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增产地</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="产地名称" name="originName" :required="true">
          <a-input v-model:value="form.originName" placeholder="如：桂林砂糖橘基地" allow-clear />
        </a-form-item>

        <a-form-item label="所在区域" name="region" :required="true">
          <a-cascader
            v-model:value="regionValue"
            :options="regionData"
            placeholder="请选择省/市/区"
            style="width: 100%"
            @change="onRegionChange"
          />
        </a-form-item>

        <a-form-item label="产地简介" name="description">
          <a-textarea
            v-model:value="form.description"
            :rows="4"
            placeholder="选填：种植品类、基地规模、认证资质等说明"
            allow-clear
          />
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { createTraceOrigin } from '@/api/admin/traceOrigin'
import regionData from '@/utils/regionData'

const router = useRouter()
const formRef = ref()

const regionValue = ref([])

const form = reactive({
  originName: '',
  province: '',
  city: '',
  region: '',
  description: ''
})

const onRegionChange = (value) => {
  if (value && value.length >= 2) {
    form.province = value[0]
    form.city = value[1]
    form.region = value[2] || ''
  } else {
    form.province = ''
    form.city = ''
    form.region = ''
  }
}

const rules = {
  originName: [{ required: true, message: '请输入产地名称', trigger: 'blur' }],
  region: [{ required: true, message: '请选择所在区域', trigger: 'change', validator: (_, __, callback) => { if (!form.province || !form.city) { callback(new Error('请选择所在区域')) } else { callback() } } }]
}

const goBack = () => {
  router.push('/admin/trace/origin')
}

// 提交按钮入参与后端 CreateOriginReqVO 对齐
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  const reqVO = {
    originName: form.originName.trim(),
    province: form.province.trim(),
    city: form.city.trim(),
    region: form.region.trim(),
    description: form.description?.trim() || ''
  }
  const rsp = await createTraceOrigin(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '新增产地失败')
    return
  }
  message.success('新增成功')
  goBack()
}
</script>
