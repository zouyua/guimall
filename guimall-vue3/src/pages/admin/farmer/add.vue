<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增农户</span>
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
        <a-form-item label="农户姓名" name="name" :required="true">
          <a-input v-model:value="form.name" placeholder="请输入姓名" allow-clear />
        </a-form-item>

        <a-form-item label="手机号" name="phone" :required="true">
          <a-input v-model:value="form.phone" placeholder="请输入手机号" maxlength="11" allow-clear />
        </a-form-item>

        <a-form-item label="身份证号" name="idCard">
          <a-input v-model:value="form.idCard" placeholder="选填" allow-clear />
        </a-form-item>

        <a-form-item label="农场名称" name="farmName">
          <a-input v-model:value="form.farmName" placeholder="如：临桂金桔合作社" allow-clear />
        </a-form-item>

        <a-form-item label="省" name="province">
          <a-input v-model:value="form.province" placeholder="如：广西壮族自治区" allow-clear />
        </a-form-item>

        <a-form-item label="市" name="city">
          <a-input v-model:value="form.city" placeholder="如：桂林市" allow-clear />
        </a-form-item>

        <a-form-item label="区/县" name="region">
          <a-input v-model:value="form.region" placeholder="如：临桂区" allow-clear />
        </a-form-item>

        <a-form-item label="详细地址" name="detailAddress">
          <a-input v-model:value="form.detailAddress" placeholder="街道/乡镇/村信息" allow-clear />
        </a-form-item>

        <a-form-item label="头像地址" name="avatar">
          <a-input v-model:value="form.avatar" placeholder="图片 URL，留空则使用默认头像" allow-clear />
        </a-form-item>

        <a-form-item label="主要农产品" name="mainProduct">
          <a-input v-model:value="form.mainProduct" placeholder="如：金桔、罗汉果" allow-clear />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="form.status" placeholder="请选择" class="w-full">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="简介" name="description">
          <a-textarea
            v-model:value="form.description"
            :rows="4"
            placeholder="选填：种植品类、基地介绍等"
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
import { createFarmer } from '@/api/admin/farmer'

const router = useRouter()
const formRef = ref()

const form = reactive({
  name: '',
  phone: '',
  idCard: '',
  farmName: '',
  province: '广西壮族自治区',
  city: '桂林市',
  region: '',
  detailAddress: '',
  avatar: '',
  mainProduct: '',
  status: 1,
  description: ''
})

const rules = {
  name: [{ required: true, message: '请输入农户姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ]
}

const goBack = () => {
  router.push('/admin/farmer')
}

// 提交按钮入参与后端 CreateFarmerReqVO 字段严格对齐
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  const reqVO = {
    name: form.name.trim(),
    phone: form.phone.trim(),
    idCard: form.idCard?.trim() || '',
    avatar: form.avatar?.trim() || '',
    farmName: form.farmName?.trim() || '',
    province: form.province?.trim() || '',
    city: form.city?.trim() || '',
    region: form.region?.trim() || '',
    detailAddress: form.detailAddress?.trim() || '',
    mainProduct: form.mainProduct?.trim() || '',
    description: form.description?.trim() || '',
    status: form.status
  }
  const rsp = await createFarmer(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '新增农户失败')
    return
  }
  message.success('新增成功')
  goBack()
}
</script>
