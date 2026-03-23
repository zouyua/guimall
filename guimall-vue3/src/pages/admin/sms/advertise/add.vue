<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增轮播图</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="轮播信息">
      <p class="mb-4 text-sm text-gray-500">
        图片将展示在前端首页轮播区域，仅作展示，无需配置跳转。
      </p>
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="轮播标题" required>
          <a-input v-model:value="form.title" placeholder="后台识别用，如：春季活动" allow-clear />
        </a-form-item>

        <a-form-item label="轮播图片" required>
          <a-input v-model:value="form.pic" placeholder="图片 URL，建议与前端轮播组件比例一致" allow-clear />
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="启用">
          <a-switch v-model:checked="form.enabled" />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.remark" :rows="3" placeholder="选填" allow-clear />
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
  title: '',
  pic: '',
  sort: 1,
  enabled: true,
  remark: ''
})

const goBack = () => {
  router.push('/admin/sms/advertise')
}

const handleSubmit = () => {
  if (!form.title?.trim() || !form.pic?.trim()) {
    message.warning('请填写轮播标题与轮播图片')
    return
  }
  console.log('新增轮播图', { ...form })
  message.success('提交成功（演示数据）')
  goBack()
}
</script>
