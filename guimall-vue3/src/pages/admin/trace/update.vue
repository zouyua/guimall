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

        <a-form-item label="省" name="province" :required="true">
          <a-input v-model:value="form.province" placeholder="如：广西壮族自治区" allow-clear />
        </a-form-item>

        <a-form-item label="市" name="city" :required="true">
          <a-input v-model:value="form.city" placeholder="如：桂林市" allow-clear />
        </a-form-item>

        <a-form-item label="区/县" name="region" :required="true">
          <a-input v-model:value="form.region" placeholder="如：临桂区/阳朔县" allow-clear />
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
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getTraceOriginDetail, updateTraceOrigin } from '@/api/admin/traceOrigin'

const router = useRouter()
const route = useRoute()
const formRef = ref()

const form = reactive({
  id: null,
  originName: '',
  province: '',
  city: '',
  region: '',
  description: ''
})

const rules = {
  originName: [{ required: true, message: '请输入产地名称', trigger: 'blur' }],
  province: [{ required: true, message: '请输入省', trigger: 'blur' }],
  city: [{ required: true, message: '请输入市', trigger: 'blur' }],
  region: [{ required: true, message: '请输入区/县', trigger: 'blur' }]
}

const loadDetail = async () => {
  const id = Number(route.query.id)
  if (!id) {
    message.warning('缺少产地ID')
    goBack()
    return
  }
  const rsp = await getTraceOriginDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取产地详情失败')
    goBack()
    return
  }
  Object.assign(form, {
    id: rsp.data.id,
    originName: rsp.data.originName || '',
    province: rsp.data.province || '',
    city: rsp.data.city || '',
    region: rsp.data.region || '',
    description: rsp.data.description || ''
  })
}

onMounted(() => {
  loadDetail()
})

const goBack = () => {
  router.push('/admin/trace/origin')
}

// 保存按钮入参与后端 UpdateOriginReqVO 对齐
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  const reqVO = {
    id: form.id,
    originName: form.originName.trim(),
    province: form.province.trim(),
    city: form.city.trim(),
    region: form.region.trim(),
    description: form.description?.trim() || ''
  }
  const rsp = await updateTraceOrigin(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '保存失败')
    return
  }
  message.success('保存成功')
  goBack()
}
</script>
