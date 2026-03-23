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
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="农户姓名" required>
          <a-input v-model:value="form.name" placeholder="请输入姓名" allow-clear />
        </a-form-item>

        <a-form-item label="手机号" required>
          <a-input v-model:value="form.phone" placeholder="请输入手机号" maxlength="11" allow-clear />
        </a-form-item>

        <a-form-item label="所在地区">
          <a-input v-model:value="form.region" placeholder="如：广西桂林市临桂区" allow-clear />
        </a-form-item>

        <a-form-item label="头像地址">
          <a-input v-model:value="form.avatar" placeholder="图片 URL，留空则使用默认头像" allow-clear />
        </a-form-item>

        <a-form-item label="认证状态">
          <a-select v-model:value="form.certStatus" placeholder="请选择" class="w-full" allow-clear>
            <a-select-option :value="0">待审核</a-select-option>
            <a-select-option :value="1">已认证</a-select-option>
            <a-select-option :value="2">未通过</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="简介">
          <a-textarea
            v-model:value="form.intro"
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
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const form = reactive({
  name: '',
  phone: '',
  region: '',
  avatar: '',
  certStatus: 0,
  intro: ''
})

const goBack = () => {
  router.push('/admin/farmer')
}

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入农户姓名')
    return
  }
  if (!form.phone?.trim()) {
    message.warning('请输入手机号')
    return
  }
  if (!/^1\d{10}$/.test(form.phone.trim())) {
    message.warning('请输入正确的 11 位手机号')
    return
  }
  console.log('新增农户', { ...form })
  message.success('提交成功（演示数据，未请求后端）')
  goBack()
}
</script>
