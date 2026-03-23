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
  region: undefined,
  address: '',
  intro: ''
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
  console.log('新增产地', { ...form })
  message.success('提交成功（演示数据，未请求后端）')
  goBack()
}
</script>
