<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="退货单号">
          <a-input v-model:value="searchApplyNo" placeholder="请输入退货单号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="处理状态">
          <a-select v-model:value="applyStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option value="待审核">待审核</a-select-option>
            <a-select-option value="已通过">已通过</a-select-option>
            <a-select-option value="已拒绝">已拒绝</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <div class="font-semibold mb-4">退货申请</div>

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
import { EyeOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchApplyNo = ref('')
const applyStatus = ref()

const statusColor = {
  待审核: 'processing',
  已通过: 'success',
  已拒绝: 'error'
}

const allRows = ref([
  { id: 1, applyNo: 'TH20260320001', orderNo: 'GM202603180015', memberName: '用户C', refundAmount: 45, reason: '商品破损', status: '待审核', createTime: '2026-03-20 14:00:00' },
  { id: 2, applyNo: 'TH20260319002', orderNo: 'GM202603170002', memberName: '用户D', refundAmount: 32.8, reason: '不想要了', status: '已通过', createTime: '2026-03-19 10:20:00' },
  { id: 3, applyNo: 'TH20260318003', orderNo: 'GM202603160099', memberName: '用户E', refundAmount: 88, reason: '发错货', status: '已拒绝', createTime: '2026-03-18 16:45:00' }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchApplyNo.value.trim()) {
    list = list.filter((r) => r.applyNo.includes(searchApplyNo.value.trim()))
  }
  if (applyStatus.value) {
    list = list.filter((r) => r.status === applyStatus.value)
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
  { title: '退货单号', dataIndex: 'applyNo', align: 'center' },
  { title: '原订单号', dataIndex: 'orderNo', align: 'center' },
  { title: '申请人', dataIndex: 'memberName', align: 'center' },
  {
    title: '退款金额',
    dataIndex: 'refundAmount',
    align: 'center',
    customRender: ({ text }) => `¥ ${text}`
  },
  { title: '退货原因', dataIndex: 'reason', align: 'center', ellipsis: true },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: statusColor[record.status] }, () => record.status)
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
          ...(record.status === '待审核'
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
            : [])
        ]
      )
  }
]

const handleSearch = () => {
  current.value = 1
}

const handleReset = () => {
  searchApplyNo.value = ''
  applyStatus.value = undefined
  current.value = 1
}

const handleApprove = (record) => {
  console.log('通过退货', record)
  record.status = '已通过'
}

const handleReject = (record) => {
  console.log('拒绝退货', record)
  record.status = '已拒绝'
}
</script>
