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
        <a-descriptions-item label="退货单号" :span="2">TH{{ String(detail.id || '').padStart(8, '0') }}</a-descriptions-item>
        <a-descriptions-item label="原订单号">{{ detail.orderSn }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag :color="APPLY_STATUS_COLOR[detail.status]">{{ APPLY_STATUS_LABEL[detail.status] || '-' }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="申请人">{{ detail.memberUsername }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detail.returnPhone }}</a-descriptions-item>
        <a-descriptions-item label="退款金额">¥ {{ detail.returnAmount }}</a-descriptions-item>
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
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getOrderReturnApplyDetail } from '@/api/admin/orderReturnApply'

const router = useRouter()
const route = useRoute()

const APPLY_STATUS_LABEL = { 0: '待处理', 1: '退货中', 2: '已完成', 3: '已拒绝' }
const APPLY_STATUS_COLOR = { 0: 'processing', 1: 'blue', 2: 'success', 3: 'error' }

const detail = reactive({
  id: null,
  orderNo: '',
  status: 0,
  memberUsername: '',
  returnPhone: '',
  refundAmount: 0,
  createTime: '',
  reason: '',
  description: '',
  items: []
})

const itemColumns = [
  { title: '商品名称', dataIndex: 'productName', align: 'center' },
  { title: '规格', dataIndex: 'productAttr', align: 'center' },
  { title: '退货数量', dataIndex: 'productCount', align: 'center' },
  { title: '原价', dataIndex: 'productPrice', align: 'center', customRender: ({ text }) => `¥ ${text}` },
  { title: '实付', dataIndex: 'realAmount', align: 'center', customRender: ({ text }) => `¥ ${text}` }
]

onMounted(() => {
  loadDetail()
})

const loadDetail = async () => {
  const id = Number(route.query.id)
  if (!id) {
    message.warning('缺少退货申请ID')
    goBack()
    return
  }
  const rsp = await getOrderReturnApplyDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取退货详情失败')
    goBack()
    return
  }
  const d = rsp.data
  Object.assign(detail, {
    id: d.id,
    orderNo: d.orderSn,
    status: d.status,
    memberUsername: d.memberUsername || '',
    returnPhone: d.returnPhone || '',
    refundAmount: d.returnAmount || 0,
    createTime: d.createTime || '',
    reason: d.reason || '',
    description: d.description || '',
    items: [
      {
        id: d.id,
        productName: d.productName || '',
        productAttr: d.productAttr || '',
        productCount: d.productCount || 0,
        productPrice: d.productPrice || 0,
        realAmount: d.returnAmount || 0
      }
    ]
  })
}

const goBack = () => {
  router.push('/admin/oms/apply')
}
</script>
