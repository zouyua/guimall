<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="订单编号">
          <a-input v-model:value="searchOrderNo" placeholder="请输入订单号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="订单状态">
          <a-select v-model:value="orderStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="0">待付款</a-select-option>
            <a-select-option :value="1">待发货</a-select-option>
            <a-select-option :value="2">已发货</a-select-option>
            <a-select-option :value="3">已完成</a-select-option>
            <a-select-option :value="4">已关闭</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="订单列表">
      <template #extra>
        <a-button @click="router.push('/admin/oms/order/deliverOrderList')">
          发货管理
        </a-button>
      </template>

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

    <!-- 订单备注弹窗 -->
    <a-modal
      v-model:open="remarkModalOpen"
      title="订单备注"
      ok-text="保存备注"
      cancel-text="取消"
      :confirmLoading="remarkLoading"
      destroy-on-close
      @ok="submitRemark"
    >
      <a-form layout="vertical">
        <a-form-item label="订单编号">
          <a-input :value="remarkTargetOrder?.orderSn || ''" disabled />
        </a-form-item>
        <a-form-item label="备注内容">
          <a-textarea v-model:value="remarkText" :rows="4" placeholder="请输入备注（可留空）" allow-clear />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 关闭订单弹窗 -->
    <a-modal
      v-model:open="closeModalOpen"
      title="关闭订单"
      ok-text="确认关闭"
      cancel-text="取消"
      :confirmLoading="closeLoading"
      destroy-on-close
      @ok="submitClose"
    >
      <a-form layout="vertical">
        <a-form-item label="订单编号">
          <a-input :value="closeTargetOrder?.orderSn || ''" disabled />
        </a-form-item>
        <a-form-item label="关闭原因（选填）">
          <a-textarea v-model:value="closeNote" :rows="3" placeholder="请输入关闭原因（可留空）" allow-clear />
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag, Popconfirm, Tooltip, message } from 'ant-design-vue'
import { EyeOutlined, EditOutlined, CloseCircleOutlined, InfoCircleOutlined } from '@ant-design/icons-vue'
import { fetchOrderList, closeOrder, remarkOrder } from '@/api/admin/order'

const router = useRouter()

const searchOrderNo = ref('')
const orderStatus = ref()

const ORDER_STATUS_LABEL = {
  0: '待付款',
  1: '待发货',
  2: '已发货',
  3: '已完成',
  4: '已关闭',
  5: '无效订单'
}
const ORDER_STATUS_COLOR = { 0: 'orange', 1: 'blue', 2: 'cyan', 3: 'success', 4: 'default', 5: 'default' }

const allOrders = ref([])
const total = ref(0)
const remarkModalOpen = ref(false)
const closeModalOpen = ref(false)
const remarkLoading = ref(false)
const closeLoading = ref(false)
const remarkTargetOrder = ref(null)
const closeTargetOrder = ref(null)
const remarkText = ref('')
const closeNote = ref('')

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => allOrders.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '订单编号', dataIndex: 'orderSn', align: 'center' },
  { title: '下单用户', dataIndex: 'memberUsername', align: 'center' },
  {
    title: '订单金额',
    dataIndex: 'payAmount',
    align: 'center',
    customRender: ({ text }) => `¥ ${text}`
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: ORDER_STATUS_COLOR[record.status] || 'default' }, () => ORDER_STATUS_LABEL[record.status] || '-')
  },
  {
    title: '备注',
    align: 'center',
    width: 90,
    customRender: ({ record }) => {
      const note = (record.note || '').trim()
      if (!note) return '-'
      return h(
        Tooltip,
        { title: note },
        {
          default: () =>
            h('span', { class: 'inline-flex cursor-pointer items-center text-amber-500' }, [
              h(InfoCircleOutlined)
            ])
        }
      )
    }
  },
  { title: '下单时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 300,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () =>
              router.push({ path: '/admin/oms/order/orderDetail', query: { id: record.id } })
          },
          {
            icon: () => h(EyeOutlined),
            default: () => '详情'
          }
        ),
        h(
          Button,
          {
            size: 'small',
            class: 'flex items-center gap-1',
            onClick: () => openRemark(record)
          },
          {
            icon: () => h(EditOutlined),
            default: () => '备注'
          }
        ),
        h(
          Popconfirm,
          {
            title: canCloseOrder(record) ? '确定关闭该订单吗？' : '当前状态不允许关闭订单',
            disabled: !canCloseOrder(record),
            onConfirm: () => openClose(record)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: !canCloseOrder(record),
                  class: 'flex items-center gap-1'
                },
                {
                  icon: () => h(CloseCircleOutlined),
                  default: () => '关闭订单'
                }
              )
          }
        )
      ])
  }
]

const handleSearch = () => {
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  searchOrderNo.value = ''
  orderStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

// 查询按钮入参与后端 FindOmsOrderPageListReqVO 对齐
const fetchList = async () => {
  const reqVO = { current: current.value, size: size.value }
  if (searchOrderNo.value.trim()) reqVO.orderSn = searchOrderNo.value.trim()
  if (orderStatus.value !== undefined && orderStatus.value !== null) reqVO.status = Number(orderStatus.value)

  const rsp = await fetchOrderList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取订单列表失败')
    return
  }
  allOrders.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  fetchList()
})

watch([current, size], () => {
  fetchList()
})

const canCloseOrder = (record) => Number(record.status) === 0 || Number(record.status) === 1

const openRemark = (record) => {
  remarkTargetOrder.value = record
  remarkText.value = record.note || ''
  remarkModalOpen.value = true
}

// 备注按钮入参与后端 RemarkOmsOrderReqVO 对齐：{ id, note }
const submitRemark = async () => {
  if (!remarkTargetOrder.value?.id) return
  remarkLoading.value = true
  try {
    const reqVO = {
      id: remarkTargetOrder.value.id,
      note: remarkText.value?.trim() || ''
    }
    const rsp = await remarkOrder(reqVO)
    if (!rsp?.success) {
      message.error(rsp?.message || '备注失败')
      return
    }
    message.success('备注已保存')
    remarkModalOpen.value = false
    await fetchList()
  } finally {
    remarkLoading.value = false
  }
}

const openClose = (record) => {
  closeTargetOrder.value = record
  closeNote.value = ''
  closeModalOpen.value = true
}

// 关闭按钮入参与后端 CloseOmsOrderReqVO 对齐：{ id, note }
const submitClose = async () => {
  if (!closeTargetOrder.value?.id) return
  closeLoading.value = true
  try {
    const reqVO = {
      id: closeTargetOrder.value.id,
      note: closeNote.value?.trim() || ''
    }
    const rsp = await closeOrder(reqVO)
    if (!rsp?.success) {
      message.error(rsp?.message || '关闭订单失败')
      return
    }
    message.success('订单已关闭')
    closeModalOpen.value = false
    await fetchList()
  } finally {
    closeLoading.value = false
  }
}
</script>
