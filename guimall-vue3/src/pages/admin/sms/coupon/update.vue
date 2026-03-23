<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑优惠券</span>
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
          <a-input v-model:value="form.name" allow-clear />
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
          <a-input-number v-model:value="form.totalCount" :min="1" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="已领取">
          <a-input-number v-model:value="form.received" :min="0" disabled class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="开始时间" required>
          <a-input v-model:value="form.startTime" allow-clear />
        </a-form-item>

        <a-form-item label="结束时间" required>
          <a-input v-model:value="form.endTime" allow-clear />
        </a-form-item>

        <a-form-item label="状态" required>
          <a-select v-model:value="form.status" class="w-full max-w-xs">
            <a-select-option value="未开始">未开始</a-select-option>
            <a-select-option value="进行中">进行中</a-select-option>
            <a-select-option value="已结束">已结束</a-select-option>
            <a-select-option value="已停用">已停用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.remark" :rows="3" allow-clear />
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
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const form = reactive({
  id: null,
  name: '',
  type: '满减',
  minAmount: undefined,
  reduceAmount: undefined,
  discount: 8.8,
  totalCount: 0,
  received: 0,
  startTime: '',
  endTime: '',
  status: '进行中',
  remark: ''
})

const mockById = {
  1: {
    id: 1,
    name: '新客满50减10',
    type: '满减',
    minAmount: 50,
    reduceAmount: 10,
    discount: 8.8,
    totalCount: 1000,
    received: 326,
    startTime: '2026-03-01 00:00:00',
    endTime: '2026-06-30 23:59:59',
    status: '进行中',
    remark: ''
  },
  2: {
    id: 2,
    name: '果蔬88折券',
    type: '折扣',
    minAmount: 30,
    reduceAmount: 0,
    discount: 8.8,
    totalCount: 500,
    received: 88,
    startTime: '2026-04-01 00:00:00',
    endTime: '2026-05-01 23:59:59',
    status: '未开始',
    remark: '限生鲜分类'
  },
  3: {
    id: 3,
    name: '春节满减',
    type: '满减',
    minAmount: 100,
    reduceAmount: 20,
    discount: 8.8,
    totalCount: 200,
    received: 200,
    startTime: '2026-01-01 00:00:00',
    endTime: '2026-02-01 23:59:59',
    status: '已结束',
    remark: ''
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(form, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/sms/coupon')
}

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入优惠券名称')
    return
  }
  console.log('保存优惠券', { ...form })
  message.success('保存成功（演示数据）')
  goBack()
}
</script>
