<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="订单编号">
          <a-input v-model:value="searchOrderNo" placeholder="请输入订单号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="收货人">
          <a-input v-model:value="searchReceiver" placeholder="请输入姓名" class="w-48" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center justify-between">
        <div class="flex items-center gap-4">
          <a-button class="flex items-center gap-1" @click="goBack">
            <ArrowLeftOutlined />
            返回订单列表
          </a-button>
          <span class="text-xl font-bold">发货管理</span>
        </div>
      </div>
    </a-card>

    <a-card :bordered="false">
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

    <LogisticsDialog ref="logisticsRef" @submit="handleDeliverSubmit" />
  </div>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag, message } from 'ant-design-vue'
import { ArrowLeftOutlined, SendOutlined } from '@ant-design/icons-vue'
import LogisticsDialog from './logisticsDialog.vue'
import { fetchOrderList, deliverOrder, getOrderDetail } from '@/api/admin/order'

const router = useRouter()
const logisticsRef = ref()

const searchOrderNo = ref('')
const searchReceiver = ref('')

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
  { title: '订单编号', dataIndex: 'orderSn', align: 'center' },
  { title: '收货人', dataIndex: 'receiverName', align: 'center' },
  { title: '联系电话', dataIndex: 'receiverPhone', align: 'center' },
  { title: '收货地址', dataIndex: 'address', align: 'center', ellipsis: true },
  {
    title: '状态',
    align: 'center',
    customRender: () => h(Tag, { color: 'blue' }, () => '待发货')
  },
  { title: '下单时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 140,
    customRender: ({ record }) =>
      h(
        Button,
        {
          size: 'small',
          type: 'primary',
          class: 'flex items-center gap-1 mx-auto',
          onClick: () => logisticsRef.value?.open(record)
        },
        {
          icon: () => h(SendOutlined),
          default: () => '发货'
        }
      )
  }
]

const handleSearch = () => {
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  searchOrderNo.value = ''
  searchReceiver.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const goBack = () => {
  router.push('/admin/oms/order')
}

// 发货按钮入参与后端 DeliverOmsOrderReqVO 对齐：{ id, deliveryCompany, deliverySn }
const handleDeliverSubmit = async (payload) => {
  const reqVO = {
    id: payload.order.id,
    deliveryCompany: payload.deliveryCompany,
    deliverySn: payload.deliverySn
  }
  const rsp = await deliverOrder(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '发货失败')
    throw new Error('deliver failed')
  }
  message.success('发货成功')
  await fetchList()
}

const fetchList = async () => {
  // 发货列表只查待发货(1)
  const reqVO = {
    current: current.value,
    size: size.value,
    status: 1
  }
  if (searchOrderNo.value.trim()) reqVO.orderSn = searchOrderNo.value.trim()

  const rsp = await fetchOrderList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取待发货订单失败')
    return
  }

  const baseRows = rsp.data || []
  const detailRows = await Promise.all(
    baseRows.map(async (it) => {
      try {
        const dRsp = await getOrderDetail(it.id)
        const d = dRsp?.success ? dRsp.data : null
        return {
          ...it,
          receiverName: d?.receiverName || '',
          receiverPhone: d?.receiverPhone || '',
          address: d?.receiverDetailAddress || ''
        }
      } catch {
        return { ...it, receiverName: '', receiverPhone: '', address: '' }
      }
    })
  )

  let list = detailRows
  if (searchReceiver.value.trim()) {
    const kw = searchReceiver.value.trim()
    list = list.filter((it) => (it.receiverName || '').includes(kw))
  }
  allRows.value = list
  total.value = rsp.total || list.length
}

onMounted(() => {
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>
