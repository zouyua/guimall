<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">领取记录</span>
        <span v-if="couponTitle" class="text-sm text-gray-500">（{{ couponTitle }}）</span>
      </div>
    </a-card>

    <a-card :bordered="false">
      <a-table
        :dataSource="pagedData"
        :columns="columns"
        :pagination="false"
        rowKey="id"
        bordered
        class="w-full"
      />

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
import { ref, computed, h, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Tag } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const couponTitle = ref('')

const mockHistoryByCoupon = {
  1: [
    { id: 'h1', member: '用户A', phone: '138****0001', receiveTime: '2026-03-10 09:12:00', useStatus: '未使用' },
    { id: 'h2', member: '用户B', phone: '139****0002', receiveTime: '2026-03-11 14:20:00', useStatus: '已使用' },
    { id: 'h3', member: '用户C', phone: '136****0003', receiveTime: '2026-03-12 11:05:00', useStatus: '未使用' }
  ],
  2: [
    { id: 'h4', member: '用户D', phone: '135****0004', receiveTime: '2026-03-08 18:00:00', useStatus: '已过期' }
  ],
  3: []
}

const allRows = ref([])

onMounted(() => {
  const cid = Number(route.query.couponId) || 1
  couponTitle.value = route.query.name || ''
  allRows.value = mockHistoryByCoupon[cid] ? [...mockHistoryByCoupon[cid]] : []
})

const current = ref(1)
const size = ref(10)

const total = computed(() => allRows.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  return allRows.value.slice(start, start + size.value)
})

const useColor = {
  未使用: 'processing',
  已使用: 'success',
  已过期: 'default'
}

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '领取用户', dataIndex: 'member', align: 'center' },
  { title: '手机号', dataIndex: 'phone', align: 'center' },
  { title: '领取时间', dataIndex: 'receiveTime', align: 'center' },
  {
    title: '使用状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: useColor[record.useStatus] || 'default' }, () => record.useStatus)
  }
]

const goBack = () => {
  router.push('/admin/sms/coupon')
}
</script>
