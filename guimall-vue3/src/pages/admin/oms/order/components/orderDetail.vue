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
          <a-tag :color="ORDER_STATUS_COLOR[order.status]">{{ ORDER_STATUS_LABEL[order.status] || '-' }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="下单时间">{{ order.createTime }}</a-descriptions-item>
        <a-descriptions-item label="下单用户">{{ order.memberUsername }}</a-descriptions-item>
        <a-descriptions-item label="支付金额">¥ {{ order.payAmount }}</a-descriptions-item>
        <a-descriptions-item label="订单总额">¥ {{ order.totalAmount }}</a-descriptions-item>
        <a-descriptions-item label="运费">¥ {{ order.freightAmount }}</a-descriptions-item>
        <a-descriptions-item label="物流公司">{{ order.deliveryCompany || '—' }}</a-descriptions-item>
        <a-descriptions-item label="物流单号">{{ order.deliverySn || '—' }}</a-descriptions-item>
        <a-descriptions-item label="订单备注" :span="2">{{ order.note || '无' }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card :bordered="false" title="收货信息" class="mb-5">
      <a-descriptions bordered :column="1" size="middle" class="max-w-3xl">
        <a-descriptions-item label="收货人">{{ order.receiverName }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ order.receiverPhone }}</a-descriptions-item>
        <a-descriptions-item label="收货地址">{{ order.receiverDetailAddress }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card :bordered="false" title="商品明细">
      <a-table
        :dataSource="order.items"
        :columns="itemColumns"
        :pagination="false"
        rowKey="id"
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
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getOrderDetail } from '@/api/admin/order'

const router = useRouter()
const route = useRoute()

const ORDER_STATUS_LABEL = { 0: '待付款', 1: '待发货', 2: '已发货', 3: '已完成', 4: '已关闭', 5: '无效订单' }
const ORDER_STATUS_COLOR = { 0: 'orange', 1: 'blue', 2: 'cyan', 3: 'success', 4: 'default', 5: 'default' }

const order = reactive({
  id: null,
  orderNo: '',
  status: 0,
  createTime: '',
  memberUsername: '',
  payAmount: 0,
  note: '',
  receiverName: '',
  receiverPhone: '',
  receiverDetailAddress: '',
  totalAmount: 0,
  freightAmount: 0,
  deliveryCompany: '',
  deliverySn: '',
  items: []
})

const itemColumns = [
  { title: '商品名称', dataIndex: 'productName', align: 'center' },
  { title: '规格', dataIndex: 'productAttr', align: 'center' },
  { title: '单价', dataIndex: 'productPrice', align: 'center', customRender: ({ text }) => `¥ ${text}` },
  { title: '数量', dataIndex: 'productQuantity', align: 'center' },
  { title: '小计', dataIndex: 'realAmount', align: 'center', customRender: ({ text }) => `¥ ${text}` }
]

onMounted(() => {
  loadDetail()
})

const loadDetail = async () => {
  const id = Number(route.query.id)
  if (!id) {
    message.warning('缺少订单ID')
    goBack()
    return
  }
  const rsp = await getOrderDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取订单详情失败')
    goBack()
    return
  }
  const d = rsp.data
  Object.assign(order, {
    id: d.id,
    orderNo: d.orderSn,
    status: d.status,
    createTime: d.createTime,
    memberUsername: d.memberUsername || '',
    payAmount: d.payAmount || 0,
    note: d.note || '',
    receiverName: d.receiverName || '',
    receiverPhone: d.receiverPhone || '',
    receiverDetailAddress: d.receiverDetailAddress || '',
    totalAmount: d.totalAmount || 0,
    freightAmount: d.freightAmount || 0,
    deliveryCompany: d.deliveryCompany || '',
    deliverySn: d.deliverySn || '',
    items: d.items || []
  })
}

const goBack = () => {
  router.push('/admin/oms/order')
}
</script>
