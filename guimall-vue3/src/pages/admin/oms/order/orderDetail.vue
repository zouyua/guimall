<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">订单详情</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="订单信息" class="mb-5">
      <a-descriptions bordered :column="2" size="middle" class="max-w-5xl">
        <a-descriptions-item label="订单编号" :span="2">{{ order.orderNo }}</a-descriptions-item>
        <a-descriptions-item label="订单状态">
          <a-tag :color="statusColor[order.status]">{{ order.status }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="下单时间">{{ order.createTime }}</a-descriptions-item>
        <a-descriptions-item label="下单用户">{{ order.memberName }}</a-descriptions-item>
        <a-descriptions-item label="手机号">{{ order.phone }}</a-descriptions-item>
        <a-descriptions-item label="支付方式">{{ order.payType }}</a-descriptions-item>
        <a-descriptions-item label="支付时间">{{ order.payTime || '—' }}</a-descriptions-item>
        <a-descriptions-item label="订单备注" :span="2">{{ order.note || '无' }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card :bordered="false" title="收货信息" class="mb-5">
      <a-descriptions bordered :column="1" size="middle" class="max-w-3xl">
        <a-descriptions-item label="收货人">{{ order.receiverName }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ order.receiverPhone }}</a-descriptions-item>
        <a-descriptions-item label="收货地址">{{ order.address }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card :bordered="false" title="商品明细">
      <a-table
        :dataSource="order.items"
        :columns="itemColumns"
        :pagination="false"
        rowKey="skuId"
        bordered
        size="small"
        class="w-full max-w-4xl"
      />
      <div class="mt-4 text-right max-w-4xl">
        <span class="text-base font-semibold">合计：¥ {{ order.totalAmount }}</span>
      </div>
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
  待付款: 'orange',
  待发货: 'blue',
  已发货: 'cyan',
  已完成: 'success',
  已关闭: 'default'
}

const order = reactive({
  id: null,
  orderNo: '',
  status: '',
  createTime: '',
  memberName: '',
  phone: '',
  payType: '',
  payTime: '',
  note: '',
  receiverName: '',
  receiverPhone: '',
  address: '',
  totalAmount: 0,
  items: []
})

const itemColumns = [
  { title: '商品名称', dataIndex: 'productName', align: 'center' },
  { title: '规格', dataIndex: 'spec', align: 'center' },
  { title: '单价', dataIndex: 'price', align: 'center', customRender: ({ text }) => `¥ ${text}` },
  { title: '数量', dataIndex: 'quantity', align: 'center' },
  { title: '小计', dataIndex: 'subtotal', align: 'center', customRender: ({ text }) => `¥ ${text}` }
]

const mockById = {
  1: {
    id: 1,
    orderNo: 'GM202603200001',
    status: '待付款',
    createTime: '2026-03-20 10:12:00',
    memberName: '用户A',
    phone: '138****0001',
    payType: '微信',
    payTime: '',
    note: '',
    receiverName: '张三',
    receiverPhone: '13800138001',
    address: '广西壮族自治区桂林市临桂区××路××号',
    totalAmount: 128.5,
    items: [
      { skuId: 's1', productName: '砂糖橘', spec: '5斤装', price: 25.5, quantity: 2, subtotal: 51 },
      { skuId: 's2', productName: '脐橙', spec: '礼盒', price: 38.75, quantity: 2, subtotal: 77.5 }
    ]
  },
  2: {
    id: 2,
    orderNo: 'GM202603190088',
    status: '待发货',
    createTime: '2026-03-19 15:30:22',
    memberName: '用户B',
    phone: '139****0002',
    payType: '支付宝',
    payTime: '2026-03-19 15:31:00',
    note: '请尽快发货',
    receiverName: '李四',
    receiverPhone: '13900139002',
    address: '江西省赣州市××区××街道',
    totalAmount: 56,
    items: [{ skuId: 's3', productName: '生菜', spec: '500g', price: 4, quantity: 14, subtotal: 56 }]
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(order, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/oms/order')
}
</script>
