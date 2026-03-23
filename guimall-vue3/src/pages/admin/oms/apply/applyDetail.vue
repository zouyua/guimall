<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">退货详情</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="申请信息" class="mb-5">
      <a-descriptions bordered :column="2" size="middle" class="max-w-5xl">
        <a-descriptions-item label="退货单号" :span="2">{{ detail.applyNo }}</a-descriptions-item>
        <a-descriptions-item label="原订单号">{{ detail.orderNo }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag :color="statusColor[detail.status]">{{ detail.status }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="申请人">{{ detail.memberName }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detail.phone }}</a-descriptions-item>
        <a-descriptions-item label="退款金额">¥ {{ detail.refundAmount }}</a-descriptions-item>
        <a-descriptions-item label="申请时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="退货原因" :span="2">{{ detail.reason }}</a-descriptions-item>
        <a-descriptions-item label="问题说明" :span="2">{{ detail.description || '—' }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card :bordered="false" title="退货商品">
      <a-table
        :dataSource="detail.items"
        :columns="itemColumns"
        :pagination="false"
        rowKey="skuId"
        bordered
        size="small"
        class="w-full max-w-4xl"
      />
    </a-card>

  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const statusColor = {
  待审核: 'processing',
  已通过: 'success',
  已拒绝: 'error'
}

const detail = reactive({
  applyNo: '',
  orderNo: '',
  status: '',
  memberName: '',
  phone: '',
  refundAmount: 0,
  createTime: '',
  reason: '',
  description: '',
  items: []
})

const itemColumns = [
  { title: '商品名称', dataIndex: 'productName', align: 'center' },
  { title: '规格', dataIndex: 'spec', align: 'center' },
  { title: '退货数量', dataIndex: 'quantity', align: 'center' },
  { title: '单价', dataIndex: 'price', align: 'center', customRender: ({ text }) => `¥ ${text}` }
]

const mockById = {
  1: {
    applyNo: 'TH20260320001',
    orderNo: 'GM202603180015',
    status: '待审核',
    memberName: '用户C',
    phone: '136****0003',
    refundAmount: 45,
    createTime: '2026-03-20 14:00:00',
    reason: '商品破损',
    description: '外包装挤压，果实有损伤。',
    items: [{ skuId: 'x1', productName: '脐橙', spec: '3斤装', quantity: 1, price: 45 }]
  },
  2: {
    applyNo: 'TH20260319002',
    orderNo: 'GM202603170002',
    status: '已通过',
    memberName: '用户D',
    phone: '135****0004',
    refundAmount: 32.8,
    createTime: '2026-03-19 10:20:00',
    reason: '不想要了',
    description: '',
    items: [{ skuId: 'x2', productName: '生菜', spec: '500g', quantity: 8, price: 4.1 }]
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(detail, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/oms/apply')
}
</script>
