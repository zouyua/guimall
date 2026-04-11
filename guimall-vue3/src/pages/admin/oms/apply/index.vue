<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="退货单号">
          <a-input v-model:value="searchApplyNo" placeholder="请输入退货单号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="处理状态">
          <a-select v-model:value="applyStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="0">待处理</a-select-option>
            <a-select-option :value="1">退货中</a-select-option>
            <a-select-option :value="2">已完成</a-select-option>
            <a-select-option :value="3">已拒绝</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="退货申请">

      <a-table :dataSource="pagedData" :columns="columns" :pagination="false" rowKey="id" bordered class="w-full" />

      <div class="mt-6 flex justify-center">
        <a-pagination
          v-model:current="current"
          v-model:pageSize="size"
          :total="total"
          show-size-changer
          :pageSizeOptions="[10, 20, 50]"
          :show-total="(t) => `共 ${t} 条`"
        />
      </div>

    </a-card>

  </div>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag, message } from 'ant-design-vue'
import { EyeOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons-vue'
import { fetchOrderReturnApplyList, updateOrderReturnApplyStatus } from '@/api/admin/orderReturnApply'

const router = useRouter()

const searchApplyNo = ref('')
const applyStatus = ref()

const APPLY_STATUS_LABEL = { 0: '待处理', 1: '退货中', 2: '已完成', 3: '已拒绝' }
const APPLY_STATUS_COLOR = { 0: 'processing', 1: 'blue', 2: 'success', 3: 'error' }

const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '退货单号', dataIndex: 'id', align: 'center', customRender: ({ text }) => `TH${String(text).padStart(8, '0')}` },
  { title: '原订单号', dataIndex: 'orderSn', align: 'center' },
  { title: '申请人', dataIndex: 'memberUsername', align: 'center' },
  {
    title: '退款金额',
    dataIndex: 'returnAmount',
    align: 'center',
    customRender: ({ text }) => `¥ ${text}`
  },
  { title: '商品', dataIndex: 'productName', align: 'center', ellipsis: true },
  { title: '快递单号', dataIndex: 'deliverySn', align: 'center', ellipsis: true, customRender: ({ text }) => text || '-' },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: APPLY_STATUS_COLOR[record.status] }, () => APPLY_STATUS_LABEL[record.status] || '-')
  },
  { title: '申请时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 360,
    customRender: ({ record }) =>
      h(
        'div',
        {
          class:
            'flex flex-nowrap items-center justify-center gap-2 whitespace-nowrap px-1'
        },
        [
          h(
            Button,
            {
              size: 'small',
              class: 'inline-flex shrink-0 items-center gap-1',
              onClick: () =>
                router.push({ path: '/admin/oms/apply/applyDetail', query: { id: record.id } })
            },
            {
              icon: () => h(EyeOutlined),
              default: () => '详情'
            }
          ),
          ...(record.status === 0
            ? [
                h(
                  Button,
                  {
                    size: 'small',
                    type: 'primary',
                    class: 'inline-flex shrink-0 items-center gap-1',
                    onClick: () => handleApprove(record)
                  },
                  {
                    icon: () => h(CheckOutlined),
                    default: () => '通过'
                  }
                ),
                h(
                  Button,
                  {
                    size: 'small',
                    danger: true,
                    class: 'inline-flex shrink-0 items-center gap-1',
                    onClick: () => handleReject(record)
                  },
                  {
                    icon: () => h(CloseOutlined),
                    default: () => '拒绝'
                  }
                )
              ]
            : []),
          ...(record.status === 1
            ? [
                h(
                  Button,
                  {
                    size: 'small',
                    type: 'primary',
                    style: { backgroundColor: '#059669', borderColor: '#059669' },
                    class: 'inline-flex shrink-0 items-center gap-1',
                    onClick: () => handleConfirmReceipt(record)
                  },
                  {
                    icon: () => h(CheckOutlined),
                    default: () => '确认收货'
                  }
                )
              ]
            : [])
        ]
      )
  }
]

const handleSearch = () => {
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  searchApplyNo.value = ''
  applyStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

// 通过按钮入参：UpdateOmsOrderReturnApplyStatusReqVO { id, status }
const handleApprove = async (record) => {
  const rsp = await updateOrderReturnApplyStatus({ id: record.id, status: 1 })
  if (!rsp?.success) {
    message.error(rsp?.message || '更新状态失败')
    return
  }
  message.success('已通过')
  await fetchList()
}

const handleReject = async (record) => {
  const rsp = await updateOrderReturnApplyStatus({ id: record.id, status: 3 })
  if (!rsp?.success) {
    message.error(rsp?.message || '更新状态失败')
    return
  }
  message.success('已拒绝')
  await fetchList()
}

const handleConfirmReceipt = async (record) => {
  const rsp = await updateOrderReturnApplyStatus({ id: record.id, status: 2 })
  if (!rsp?.success) {
    message.error(rsp?.message || '确认收货失败')
    return
  }
  message.success('已确认收货，退货完成')
  await fetchList()
}

const fetchList = async () => {
  const reqVO = { current: current.value, size: size.value }
  if (searchApplyNo.value.trim()) reqVO.orderSn = searchApplyNo.value.trim()
  if (applyStatus.value !== undefined && applyStatus.value !== null) reqVO.status = Number(applyStatus.value)

  const rsp = await fetchOrderReturnApplyList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取退货申请列表失败')
    return
  }
  allRows.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>
