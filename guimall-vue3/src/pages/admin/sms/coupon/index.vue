<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="优惠券名称">
          <a-input v-model:value="searchName" placeholder="请输入名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="状态">
          <a-select v-model:value="searchStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option value="进行中">进行中</a-select-option>
            <a-select-option value="未开始">未开始</a-select-option>
            <a-select-option value="已结束">已结束</a-select-option>
            <a-select-option value="已停用">已停用</a-select-option>
          </a-select>
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
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, Tag } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, UnorderedListOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')
const searchStatus = ref()

const statusColor = {
  进行中: 'processing',
  未开始: 'default',
  已结束: 'default',
  已停用: 'error'
}

const allRows = ref([
  {
    id: 1,
    name: '新客满50减10',
    type: '满减',
    benefitText: '满50减10元',
    totalCount: 1000,
    received: 326,
    startTime: '2026-03-01 00:00:00',
    endTime: '2026-06-30 23:59:59',
    status: '进行中'
  },
  {
    id: 2,
    name: '果蔬88折券',
    type: '折扣',
    benefitText: '满30享8.8折',
    totalCount: 500,
    received: 88,
    startTime: '2026-04-01 00:00:00',
    endTime: '2026-05-01 23:59:59',
    status: '未开始'
  },
  {
    id: 3,
    name: '春节满减',
    type: '满减',
    benefitText: '满100减20元',
    totalCount: 200,
    received: 200,
    startTime: '2026-01-01 00:00:00',
    endTime: '2026-02-01 23:59:59',
    status: '已结束'
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchName.value.trim()) {
    list = list.filter((r) => r.name.includes(searchName.value.trim()))
  }
  if (searchStatus.value) {
    list = list.filter((r) => r.status === searchStatus.value)
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
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '优惠券名称', dataIndex: 'name', align: 'center', ellipsis: true },
  { title: '类型', dataIndex: 'type', align: 'center', width: 90 },
  { title: '优惠内容', dataIndex: 'benefitText', align: 'center' },
  { title: '发行数量', dataIndex: 'totalCount', align: 'center', width: 100 },
  { title: '已领取', dataIndex: 'received', align: 'center', width: 90 },
  {
    title: '有效期',
    align: 'center',
    width: 200,
    customRender: ({ record }) => `${record.startTime.slice(0, 10)} ~ ${record.endTime.slice(0, 10)}`
  },
  {
    title: '状态',
    align: 'center',
    width: 100,
    customRender: ({ record }) =>
      h(Tag, { color: statusColor[record.status] || 'default' }, () => record.status)
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
              class: 'inline-flex shrink-0 items-center gap-1',
              onClick: () =>
                router.push({
                  path: '/admin/sms/coupon/history',
                  query: { couponId: record.id, name: record.name }
                })
            },
            {
              icon: () => h(UnorderedListOutlined),
              default: () => '领取记录'
            }
          ),
          h(
            Button,
            {
              size: 'small',
              type: 'primary',
              class: 'inline-flex shrink-0 items-center gap-1',
              onClick: () =>
                router.push({ path: '/admin/sms/coupon/update', query: { id: record.id } })
            },
            {
              icon: () => h(EditOutlined),
              default: () => '编辑'
            }
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
                    danger: true,
                    class: 'inline-flex shrink-0 items-center gap-1'
                  },
                  {
                    icon: () => h(DeleteOutlined),
                    default: () => '删除'
                  }
                )
            }
          )
        ]
      )
  }
]

const handleSearch = () => {
  current.value = 1
}

const handleReset = () => {
  searchName.value = ''
  searchStatus.value = undefined
  current.value = 1
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
  if (pagedData.value.length === 0 && current.value > 1) current.value--
}
</script>
