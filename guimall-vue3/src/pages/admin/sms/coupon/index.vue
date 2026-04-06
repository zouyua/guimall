<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="优惠券名称">
          <a-input v-model:value="searchName" placeholder="请输入名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <!-- 新增 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="router.push('/admin/sms/coupon/add')">
          <PlusOutlined />
          新增优惠券
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
// 优惠券管理列表页
// 职责：查询/分页/删除/跳转新增编辑/查看领取记录
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, UnorderedListOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { deleteCoupon, fetchCouponList } from '@/api/admin/coupon'

const router = useRouter()

// 查询条件
const searchName = ref('')

// 列表数据与分页状态
const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '优惠券名称', dataIndex: 'name', align: 'center', ellipsis: true },
  {
    title: '优惠类型',
    align: 'center',
    width: 120,
    customRender: () => '满减券'
  },
  { title: '优惠金额', dataIndex: 'amount', align: 'center', width: 100 },
  { title: '发行数量', dataIndex: 'count', align: 'center', width: 100 },
  { title: '已领取', dataIndex: 'receiveCount', align: 'center', width: 90 },
  {
    title: '有效期',
    align: 'center',
    width: 200,
    customRender: ({ record }) => `${String(record.startTime || '').slice(0, 10)} ~ ${String(record.endTime || '').slice(0, 10)}`
  },
  {
    title: '操作',
    align: 'center',
    width: 280,
    customRender: ({ record }) =>
      h(
        'div',
        { class: 'flex flex-nowrap items-center justify-center gap-2' },
        [
          h(
            Button,
            {
              size: 'small',
              onClick: () =>
                router.push({
                  path: '/admin/sms/coupon/history',
                  query: { couponId: record.id, name: record.name }
                })
            },
            () => [h(UnorderedListOutlined), ' 领取记录']
          ),
          h(
            Button,
            {
              size: 'small',
              type: 'primary',
              onClick: () =>
                router.push({ path: '/admin/sms/coupon/update', query: { id: record.id } })
            },
            () => [h(EditOutlined), ' 编辑']
          ),
          h(
            Popconfirm,
            {
              title: '确定删除该优惠券吗？',
              onConfirm: () => handleDelete(record.id)
            },
            {
              default: () =>
                h(
                  Button,
                  {
                    size: 'small',
                    danger: true
                  },
                  () => [h(DeleteOutlined), ' 删除']
                )
            }
          )
        ]
      )
  }
]

const handleSearch = () => {
  // 查询时重置到第 1 页，避免页码越界
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  // 重置所有查询条件，并重新拉取列表
  searchName.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleDelete = async (id) => {
  // 删除按钮入参严格校验：id 必须存在
  if (!id) {
    message.warning('优惠券ID不能为空')
    return
  }
  const rsp = await deleteCoupon(id)
  if (!rsp?.success) {
    message.error(rsp?.message || '删除失败')
    return
  }
  message.success('删除成功')
  await fetchList()
}

const fetchList = async () => {
  // 与后端 FindSmsCouponPageListReqVO 对齐
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()
  // 仅保留满减功能，固定传 type=0
  reqVO.type = 0
  const rsp = await fetchCouponList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取优惠券列表失败')
    return
  }
  allRows.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  // 页面首次进入加载
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>
