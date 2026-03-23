<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增优惠券</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="优惠券名称" required>
          <a-input v-model:value="form.name" placeholder="如：新客满50减10" allow-clear />
        </a-form-item>

        <a-form-item label="类型" required>
          <a-radio-group v-model:value="form.type">
            <a-radio value="满减">满减</a-radio>
            <a-radio value="折扣">折扣</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item v-if="form.type === '满减'" label="满减规则" required>
          <div class="flex flex-wrap items-center gap-2">
            <span>满</span>
            <a-input-number v-model:value="form.minAmount" :min="0" :precision="2" class="!w-32" />
            <span>元，减</span>
            <a-input-number v-model:value="form.reduceAmount" :min="0" :precision="2" class="!w-32" />
            <span>元</span>
          </div>
        </a-form-item>

        <a-form-item v-if="form.type === '折扣'" label="折扣规则" required>
          <div class="flex flex-wrap items-center gap-2">
            <span>满</span>
            <a-input-number v-model:value="form.minAmount" :min="0" :precision="2" class="!w-32" />
            <span>元，打</span>
            <a-input-number v-model:value="form.discount" :min="0.1" :max="9.9" :step="0.1" :precision="1" class="!w-28" />
            <span>折</span>
          </div>
        </a-form-item>

        <a-form-item label="发行数量" required>
          <a-input-number v-model:value="form.totalCount" :min="1" :precision="0" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="开始时间" required>
          <a-input v-model:value="form.startTime" placeholder="如：2026-03-01 00:00:00" allow-clear />
        </a-form-item>

        <a-form-item label="结束时间" required>
          <a-input v-model:value="form.endTime" placeholder="如：2026-06-30 23:59:59" allow-clear />
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
  name: '',
  type: '满减',
  minAmount: undefined,
  reduceAmount: undefined,
  discount: 8.8,
  totalCount: 100,
  startTime: '',
  endTime: '',
  remark: ''
})

const goBack = () => {
  router.push('/admin/sms/coupon')
}

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入优惠券名称')
    return
  }
  if (form.type === '满减') {
    if (form.minAmount == null || form.reduceAmount == null) {
      message.warning('请填写满减金额')
      return
    }
  } else {
    if (form.minAmount == null || form.discount == null) {
      message.warning('请填写折扣规则')
      return
    }
  }
  if (!form.startTime?.trim() || !form.endTime?.trim()) {
    message.warning('请填写开始与结束时间')
    return
  }
  console.log('新增优惠券', { ...form })
  message.success('提交成功（演示数据）')
  goBack()
}
</script>
