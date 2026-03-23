<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="订单编号">
          <a-input v-model:value="searchOrderNo" placeholder="请输入订单号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="订单状态">
          <a-select v-model:value="orderStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option value="待付款">待付款</a-select-option>
            <a-select-option value="待发货">待发货</a-select-option>
            <a-select-option value="已发货">已发货</a-select-option>
            <a-select-option value="已完成">已完成</a-select-option>
            <a-select-option value="已关闭">已关闭</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <div class="mb-4 flex flex-wrap justify-between items-center gap-3">
        <div class="font-semibold">订单列表</div>
        <a-button @click="router.push('/admin/oms/order/deliverOrderList')">
          发货管理
        </a-button>
      </div>

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
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag } from 'ant-design-vue'
import { EyeOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchOrderNo = ref('')
const orderStatus = ref()

const statusColor = {
  待付款: 'orange',
  待发货: 'blue',
  已发货: 'cyan',
  已完成: 'success',
  已关闭: 'default'
}

const allOrders = ref([
  { id: 1, orderNo: 'GM202603200001', memberName: '用户A', totalAmount: 128.5, status: '待付款', payType: '微信', createTime: '2026-03-20 10:12:00' },
  { id: 2, orderNo: 'GM202603190088', memberName: '用户B', totalAmount: 56, status: '待发货', payType: '支付宝', createTime: '2026-03-19 15:30:22' },
  { id: 3, orderNo: 'GM202603180015', memberName: '用户C', totalAmount: 199, status: '已发货', payType: '微信', createTime: '2026-03-18 09:05:11' },
  { id: 4, orderNo: 'GM202603170002', memberName: '用户D', totalAmount: 32.8, status: '已完成', payType: '微信', createTime: '2026-03-17 20:18:00' },
  { id: 5, orderNo: 'GM202603160099', memberName: '用户E', totalAmount: 88, status: '已关闭', payType: '—', createTime: '2026-03-16 11:00:00' }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allOrders.value
  if (searchOrderNo.value.trim()) {
    list = list.filter((o) => o.orderNo.includes(searchOrderNo.value.trim()))
  }
  if (orderStatus.value) {
    list = list.filter((o) => o.status === orderStatus.value)
  }
  return list
})

const total = computed(() => filtered.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  return filtered.value.slice(start, start + size.value)
})

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '订单编号', dataIndex: 'orderNo', align: 'center' },
  { title: '下单用户', dataIndex: 'memberName', align: 'center' },
  {
    title: '订单金额',
    dataIndex: 'totalAmount',
    align: 'center',
    customRender: ({ text }) => `¥ ${text}`
  },
  {
    title: '支付方式',
    dataIndex: 'payType',
    align: 'center'
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: statusColor[record.status] || 'default' }, () => record.status)
  },
  { title: '下单时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 120,
    customRender: ({ record }) =>
      h(
        Button,
        {
          size: 'small',
          type: 'primary',
          class: 'flex items-center gap-1 mx-auto',
          onClick: () =>
            router.push({ path: '/admin/oms/order/orderDetail', query: { id: record.id } })
        },
        {
          icon: () => h(EyeOutlined),
          default: () => '详情'
        }
      )
  }
]

const handleSearch = () => {
  current.value = 1
}

const handleReset = () => {
  searchOrderNo.value = ''
  orderStatus.value = undefined
  current.value = 1
}
</script>
