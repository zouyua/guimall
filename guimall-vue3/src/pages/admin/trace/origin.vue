<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="产地名称">
          <a-input v-model:value="searchName" placeholder="请输入产地名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="所在地区">
          <a-select v-model:value="region" placeholder="请选择地区" class="!w-44" allow-clear>
            <a-select-option value="广西">广西</a-select-option>
            <a-select-option value="江西">江西</a-select-option>
            <a-select-option value="云南">云南</a-select-option>
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
        <a-button type="primary" class="flex items-center gap-1" @click="handleAdd">
          <PlusOutlined />
          新增产地
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
import { Button, Popconfirm, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')
const region = ref()

const allRows = ref([
  {
    id: 1,
    name: '桂林砂糖橘基地',
    region: '广西',
    address: '广西桂林市临桂区',
    productCount: 3,
    createTime: '2026-03-01'
  },
  {
    id: 2,
    name: '赣南脐橙园',
    region: '江西',
    address: '江西赣州市',
    productCount: 2,
    createTime: '2026-03-05'
  },
  {
    id: 3,
    name: '高原蔬菜合作社',
    region: '云南',
    address: '云南昆明周边',
    productCount: 5,
    createTime: '2026-03-08'
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchName.value.trim()) {
    list = list.filter((r) => r.name.includes(searchName.value.trim()))
  }
  if (region.value) {
    list = list.filter((r) => r.region === region.value)
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
    title: '产地名称',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '地区',
    dataIndex: 'region',
    align: 'center'
  },
  {
    title: '详细地址',
    dataIndex: 'address',
    align: 'center'
  },
  {
    title: '关联商品数',
    dataIndex: 'productCount',
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center'
  },
  {
    title: '操作',
    align: 'center',
    width: 180,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () => handleEdit(record)
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该产地吗？',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  class: 'flex items-center gap-1'
                },
                {
                  icon: () => h(DeleteOutlined),
                  default: () => '删除'
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
  region.value = undefined
  current.value = 1
}

const handleAdd = () => {
  router.push('/admin/trace/origin/add')
}

const handleEdit = (record) => {
  router.push({ path: '/admin/trace/origin/update', query: { id: String(record.id) } })
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
  message.success('已删除（演示）')
}
</script>
