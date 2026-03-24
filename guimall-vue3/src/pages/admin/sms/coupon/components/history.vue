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
// 优惠券领取记录页
// 职责：按 couponId 分页查看领取记录
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Tag, message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { fetchCouponHistoryList } from '@/api/admin/couponHistory'

const router = useRouter()
const route = useRoute()

// 页面抬头展示信息
const couponTitle = ref('')

// 列表数据与分页状态
const allRows = ref([])
const total = ref(0)
const couponId = ref(undefined)

onMounted(() => {
  // 从路由获取 couponId，作为列表查询条件
  const cid = Number(route.query.couponId)
  if (cid) couponId.value = cid
  couponTitle.value = route.query.name || ''
  fetchList()
})

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => {
  return allRows.value
})

const useColor = {
  0: 'processing',
  1: 'success',
  2: 'default'
}

const useLabel = {
  0: '未使用',
  1: '已使用',
  2: '已过期'
}

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '领取用户', dataIndex: 'memberNickname', align: 'center' },
  { title: '优惠码', dataIndex: 'couponCode', align: 'center' },
  { title: '领取时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '使用状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: useColor[Number(record.useStatus)] || 'default' }, () => useLabel[Number(record.useStatus)] || '-')
  }
]

const fetchList = async () => {
  // 与后端 FindSmsCouponHistoryPageListReqVO 对齐
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (couponId.value) reqVO.couponId = couponId.value
  const rsp = await fetchCouponHistoryList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取领取记录失败')
    return
  }
  allRows.value = rsp.data || []
  total.value = rsp.total || 0
}

const goBack = () => {
  router.push('/admin/sms/coupon')
}

watch([current, size], () => {
  // 翻页/切换页大小自动请求
  fetchList()
})
</script>
