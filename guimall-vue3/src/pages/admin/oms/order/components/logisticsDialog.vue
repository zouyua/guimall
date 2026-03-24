<template>
  <a-modal
    v-model:open="visible"
    title="填写物流信息"
    ok-text="确认发货"
    cancel-text="取消"
    :confirmLoading="confirmLoading"
    destroy-on-close
    @ok="handleOk"
  >
    <div class="mb-3 text-sm text-gray-500" v-if="current">
      订单号：<span class="text-gray-800">{{ current.orderNo }}</span>
    </div>
    <a-form :model="form" layout="vertical">
      <a-form-item label="物流公司" required>
        <a-select v-model:value="form.company" placeholder="请选择" class="w-full">
          <a-select-option value="顺丰速运">顺丰速运</a-select-option>
          <a-select-option value="中通快递">中通快递</a-select-option>
          <a-select-option value="圆通速递">圆通速递</a-select-option>
          <a-select-option value="韵达快递">韵达快递</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="物流单号" required>
        <a-input v-model:value="form.trackingNo" placeholder="请输入运单号" allow-clear />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'

const emit = defineEmits(['submit'])

const visible = ref(false)
const current = ref(null)
const confirmLoading = ref(false)

const form = reactive({
  company: undefined,
  trackingNo: ''
})

const resetForm = () => {
  form.company = undefined
  form.trackingNo = ''
}

const open = (record) => {
  current.value = record
  resetForm()
  visible.value = true
}

const close = () => {
  visible.value = false
}

const handleOk = async () => {
  if (!form.company || !form.trackingNo?.trim()) {
    message.warning('请选择物流公司并填写运单号')
    return Promise.reject()
  }
  confirmLoading.value = true
  try {
    await emit('submit', {
      order: current.value,
      deliveryCompany: form.company,
      deliverySn: form.trackingNo.trim()
    })
    visible.value = false
  } finally {
    confirmLoading.value = false
  }
}

defineExpose({ open, close })
</script>
