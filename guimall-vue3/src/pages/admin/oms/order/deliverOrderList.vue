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

    <a-card :bordered="false">

      <div class="mb-4 flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回订单列表
        </a-button>
        <span class="font-semibold">发货管理</span>
      </div>

      <p class="text-sm text-gray-500 mb-4">以下为待发货订单，填写物流信息后标记为已发货（静态演示）。</p>

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

    <LogisticsDialog ref="logisticsRef" />
  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag } from 'ant-design-vue'
import { ArrowLeftOutlined, SendOutlined } from '@ant-design/icons-vue'
import LogisticsDialog from './components/logisticsDialog.vue'

const router = useRouter()
const logisticsRef = ref()

const searchOrderNo = ref('')
const searchReceiver = ref('')

const allRows = ref([
  {
    id: 2,
    orderNo: 'GM202603190088',
    receiverName: '李四',
    receiverPhone: '13900139002',
    address: '江西省赣州市××区',
    status: '待发货',
    createTime: '2026-03-19 15:30:22'
  },
  {
    id: 6,
    orderNo: 'GM202603210033',
    receiverName: '王五',
    receiverPhone: '13700137003',
    address: '云南省昆明市××县',
    status: '待发货',
    createTime: '2026-03-21 08:20:00'
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchOrderNo.value.trim()) {
    list = list.filter((r) => r.orderNo.includes(searchOrderNo.value.trim()))
  }
  if (searchReceiver.value.trim()) {
    list = list.filter((r) => r.receiverName.includes(searchReceiver.value.trim()))
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
  current.value = 1
}

const handleReset = () => {
  searchOrderNo.value = ''
  searchReceiver.value = ''
  current.value = 1
}

const goBack = () => {
  router.push('/admin/oms/order')
}
</script>
