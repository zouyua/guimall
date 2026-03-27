<template>
  <div class="p-2 box">

    <!-- 查询条件 -->
    <a-card :bordered="false" class="mb-5">

      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <!-- 订单编号 -->
        <a-form-item label="订单编号">
          <a-input
            v-model:value="searchOrderNo"
            placeholder="请输入订单号"
            class="w-56"
            allow-clear
          />
        </a-form-item>

        <!-- 订单状态 -->
        <a-form-item label="订单状态">
          <a-select
            v-model:value="orderStatus"
            placeholder="请选择"
            class="!w-40"
            allow-clear
          >
            <a-select-option :value="0">待付款</a-select-option>
            <a-select-option :value="1">待发货</a-select-option>
            <a-select-option :value="2">已发货</a-select-option>
            <a-select-option :value="3">已完成</a-select-option>
            <a-select-option :value="4">已关闭</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 查询按钮 -->
        <a-form-item>
          <a-button type="primary" @click="handleSearch">
            查询
          </a-button>

          <a-button class="ml-2" @click="handleReset">
            重置
          </a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <!-- 表格 -->
    <a-card :bordered="false" title="订单列表">

      <template #extra>
        <a-button @click="router.push('/admin/oms/order/deliverOrderList')">
          发货管理
        </a-button>
      </template>

      <a-table
        :dataSource="allOrders"
        :columns="columns"
        :pagination="false"
        rowKey="id"
        bordered
      />

      <!-- 分页 -->
      <div class="mt-6 flex justify-center">

        <a-pagination
          v-model:current="current"
          v-model:pageSize="size"
          :total="total"
          show-size-changer
          :pageSizeOptions="[10,20,50]"
          :show-total="(t)=>`共 ${t} 条`"
        />

      </div>

    </a-card>

  </div>
</template>

<script setup>

/*
  =====================================
  引入依赖
  =====================================
*/

import { ref, onMounted, watch, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Tag, Popconfirm, Tooltip, message } from 'ant-design-vue'
import {
  EyeOutlined,
  EditOutlined,
  CloseCircleOutlined,
  InfoCircleOutlined
} from '@ant-design/icons-vue'

import {
  fetchOrderList,
  closeOrder,
  remarkOrder
} from '@/api/admin/order'

const router = useRouter()

/*
  =====================================
  查询条件
  =====================================
*/

// 订单编号
const searchOrderNo = ref('')

// 订单状态
const orderStatus = ref()

/*
  =====================================
  分页参数
  =====================================
*/

const current = ref(1) // 当前页
const size = ref(10) // 每页数量
const total = ref(0) // 总数量

/*
  =====================================
  表格数据
  =====================================
*/

const allOrders = ref([])

/*
  =====================================
  订单状态字典
  =====================================
*/

const ORDER_STATUS_LABEL = {
  0: '待付款',
  1: '待发货',
  2: '已发货',
  3: '已完成',
  4: '已关闭',
  5: '无效订单'
}

const ORDER_STATUS_COLOR = {
  0: 'orange',
  1: 'blue',
  2: 'cyan',
  3: 'success',
  4: 'default',
  5: 'default'
}

/*
  =====================================
  表格列定义
  =====================================
*/

const columns = [

  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) =>
      (current.value - 1) * size.value + index + 1
  },

  {
    title: '订单编号',
    dataIndex: 'orderSn',
    align: 'center'
  },

  {
    title: '下单用户',
    dataIndex: 'memberUsername',
    align: 'center'
  },

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
      h(
        Tag,
        { color: ORDER_STATUS_COLOR[record.status] || 'default' },
        () => ORDER_STATUS_LABEL[record.status] || '-'
      )
  },

  {
    title: '备注',
    align: 'center',
    customRender: ({ record }) => {

      const note = (record.note || '').trim()

      if (!note) return '-'

      return h(
        Tooltip,
        { title: note },
        {
          default: () =>
            h(
              'span',
              { class: 'text-amber-500 cursor-pointer' },
              [h(InfoCircleOutlined)]
            )
        }
      )
    }
  },

  {
    title: '下单时间',
    dataIndex: 'createTime',
    align: 'center'
  },

  {
    title: '操作',
    align: 'center',
    width: 260,
    customRender: ({ record }) =>
      h('div', { class: 'flex gap-2 justify-center' }, [

        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            onClick: () =>
              router.push({
                path: '/admin/oms/order/orderDetail',
                query: { id: record.id }
              })
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
            title: '确定关闭该订单吗？',
            onConfirm: () => submitClose(record)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true
                },
                {
                  icon: () => h(CloseCircleOutlined),
                  default: () => '关闭'
                }
              )
          }
        )

      ])
  }
]

/*
  =====================================
  查询按钮
  =====================================
*/

const handleSearch = () => {

  // 查询时回到第一页
  current.value = 1

  // 请求接口
  fetchList()
}

/*
  =====================================
  重置按钮
  =====================================
*/

const handleReset = () => {

  searchOrderNo.value = ''
  orderStatus.value = undefined

  current.value = 1

  fetchList()
}

/*
  =====================================
  请求订单列表接口
  =====================================
*/

const fetchList = async () => {

  // 构造请求参数
  const reqVO = {
    current: current.value,
    size: size.value
  }

  // 订单编号
  if (searchOrderNo.value.trim()) {
    reqVO.orderSn = searchOrderNo.value.trim()
  }

  // 状态
  if (orderStatus.value !== undefined) {
    reqVO.status = orderStatus.value
  }

  try {

    const rsp = await fetchOrderList(reqVO)

    if (!rsp?.success) {
      message.error(rsp?.message || '获取订单列表失败')
      return
    }

    // 设置表格数据
    allOrders.value = rsp.data || []

    // 设置总数量
    total.value = rsp.total || 0

  } catch (err) {

    message.error('请求订单接口失败')

  }
}

/*
  =====================================
  页面初始化
  =====================================
*/

onMounted(() => {

  fetchList()

})

/*
  =====================================
  分页监听
  =====================================
*/

watch([current, size], () => {

  fetchList()

})

/*
  =====================================
  备注订单
  =====================================
*/

const openRemark = async (record) => {

  const text = prompt('请输入备注')

  if (text === null) return

  const rsp = await remarkOrder({
    id: record.id,
    note: text
  })

  if (rsp?.success) {
    message.success('备注成功')
    fetchList()
  }

}

/*
  =====================================
  关闭订单
  =====================================
*/

const submitClose = async (record) => {

  const rsp = await closeOrder({
    id: record.id
  })

  if (rsp?.success) {
    message.success('订单已关闭')
    fetchList()
  }

}

</script>