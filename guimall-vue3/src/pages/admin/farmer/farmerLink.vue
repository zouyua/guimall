<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="商品名称">
          <a-input v-model:value="searchName" placeholder="请输入商品名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="农户">
          <a-input v-model:value="searchFarmer" placeholder="请输入农户姓名" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="关联状态">
          <a-select v-model:value="linkStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="1">已关联</a-select-option>
            <a-select-option :value="0">未关联</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="农户关联">

      <div class="mb-4 text-sm text-gray-500">
        维护商品与供货农户的绑定关系，便于溯源与结算（静态演示数据）。
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

    <a-modal
      v-model:open="bindModalVisible"
      :title="editingRow?.linkStatus ? '更换关联农户' : '关联农户'"
      ok-text="确定"
      cancel-text="取消"
      destroy-on-close
      @ok="handleBindConfirm"
    >
      <a-form layout="vertical" class="mt-2">
        <a-form-item label="商品">
          <a-input :value="editingRow?.productName" disabled />
        </a-form-item>
        <a-form-item label="选择农户" required>
          <a-select
            v-model:value="bindFarmerName"
            placeholder="请选择农户"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option
              v-for="n in farmerOptions"
              :key="n"
              :value="n"
              :label="n"
            >
              {{ n }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { Button, Popconfirm, Tag, message } from 'ant-design-vue'
import { LinkOutlined, DisconnectOutlined } from '@ant-design/icons-vue'

const searchName = ref('')
const searchFarmer = ref('')
const linkStatus = ref()

const farmerOptions = ['张三', '李四', '王五', '赵六']

const bindModalVisible = ref(false)
const editingRow = ref(null)
const bindFarmerName = ref('')

const allRows = ref([
  { id: 1, productName: '砂糖橘', farmerName: '张三', linkStatus: 1, updateTime: '2026-03-15' },
  { id: 2, productName: '脐橙', farmerName: '李四', linkStatus: 1, updateTime: '2026-03-16' },
  { id: 3, productName: '生菜', farmerName: '', linkStatus: 0, updateTime: '—' },
  { id: 4, productName: '大米', farmerName: '王五', linkStatus: 1, updateTime: '2026-03-18' }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchName.value.trim()) {
    list = list.filter((r) => r.productName.includes(searchName.value.trim()))
  }
  if (searchFarmer.value.trim()) {
    list = list.filter((r) => r.farmerName.includes(searchFarmer.value.trim()))
  }
  if (linkStatus.value === 1) {
    list = list.filter((r) => r.linkStatus === 1)
  }
  if (linkStatus.value === 0) {
    list = list.filter((r) => r.linkStatus === 0)
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
  {
    title: '商品名称',
    dataIndex: 'productName',
    align: 'center'
  },
  {
    title: '关联农户',
    dataIndex: 'farmerName',
    align: 'center',
    customRender: ({ text }) => text || '—'
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(
        Tag,
        { color: record.linkStatus ? 'success' : 'default' },
        () => (record.linkStatus ? '已关联' : '未关联')
      )
  },
  {
    title: '最近更新',
    dataIndex: 'updateTime',
    align: 'center'
  },
  {
    title: '操作',
    align: 'center',
    width: 220,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            ghost: true,
            class: 'flex items-center gap-1',
            onClick: () => handleBind(record)
          },
          {
            icon: () => h(LinkOutlined),
            default: () => (record.linkStatus ? '更换农户' : '关联农户')
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定解除该商品的农户关联吗？',
            disabled: !record.linkStatus,
            onConfirm: () => handleUnbind(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: !record.linkStatus,
                  class: 'flex items-center gap-1'
                },
                {
                  icon: () => h(DisconnectOutlined),
                  default: () => '解除'
                }
              )
          }
        )
      ])
  }
]

const handleSearch = () => {
  current.value = 1
}

const handleReset = () => {
  searchName.value = ''
  searchFarmer.value = ''
  linkStatus.value = undefined
  current.value = 1
}

const handleBind = (record) => {
  editingRow.value = record
  bindFarmerName.value = record.farmerName || undefined
  bindModalVisible.value = true
}

const handleBindConfirm = () => {
  if (!bindFarmerName.value) {
    message.warning('请选择农户')
    return
  }
  const row = editingRow.value
  if (row) {
    const wasLinked = row.linkStatus === 1 && !!row.farmerName
    row.farmerName = bindFarmerName.value
    row.linkStatus = 1
    const d = new Date()
    const pad = (n) => String(n).padStart(2, '0')
    row.updateTime = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    message.success(wasLinked ? '已更换关联农户' : '已关联农户')
  }
  bindModalVisible.value = false
}

const handleUnbind = (id) => {
  const row = allRows.value.find((r) => r.id === id)
  if (row) {
    row.farmerName = ''
    row.linkStatus = 0
    row.updateTime = '2026-03-20'
  }
}
</script>
