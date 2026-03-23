<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="农户姓名">
          <a-input v-model:value="searchName" placeholder="请输入姓名" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="手机号">
          <a-input v-model:value="searchPhone" placeholder="请输入手机号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="认证状态">
          <a-select v-model:value="certStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="1">已认证</a-select-option>
            <a-select-option :value="0">待审核</a-select-option>
            <a-select-option :value="2">未通过</a-select-option>
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
        <a-button type="primary" class="flex items-center gap-1" @click="router.push('/admin/farmer/add')">
          <PlusOutlined />
          新增农户
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
import { Image, Button, Popconfirm, Tag } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')
const searchPhone = ref('')
const certStatus = ref()

const allFarmers = ref([
  {
    id: 1,
    name: '张三',
    phone: '13800138001',
    region: '广西桂林',
    avatar: 'https://picsum.photos/seed/f1/80',
    certStatus: 1,
    productCount: 5,
    createTime: '2026-03-01'
  },
  {
    id: 2,
    name: '李四',
    phone: '13900139002',
    region: '江西赣州',
    avatar: 'https://picsum.photos/seed/f2/80',
    certStatus: 1,
    productCount: 3,
    createTime: '2026-03-05'
  },
  {
    id: 3,
    name: '王五',
    phone: '13700137003',
    region: '云南昆明',
    avatar: 'https://picsum.photos/seed/f3/80',
    certStatus: 0,
    productCount: 0,
    createTime: '2026-03-12'
  },
  {
    id: 4,
    name: '赵六',
    phone: '13600136004',
    region: '四川成都',
    avatar: 'https://picsum.photos/seed/f4/80',
    certStatus: 2,
    productCount: 1,
    createTime: '2026-03-14'
  }
])

const current = ref(1)
const size = ref(10)

const certLabel = (s) => {
  if (s === 1) return { text: '已认证', color: 'success' }
  if (s === 0) return { text: '待审核', color: 'processing' }
  return { text: '未通过', color: 'error' }
}

const filteredList = computed(() => {
  let list = allFarmers.value
  if (searchName.value.trim()) {
    list = list.filter((f) => f.name.includes(searchName.value.trim()))
  }
  if (searchPhone.value.trim()) {
    list = list.filter((f) => f.phone.includes(searchPhone.value.trim()))
  }
  if (certStatus.value != null) {
    list = list.filter((f) => f.certStatus === certStatus.value)
  }
  return list
})

const total = computed(() => filteredList.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  return filteredList.value.slice(start, start + size.value)
})

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '头像',
    dataIndex: 'avatar',
    align: 'center',
    customRender: ({ text }) =>
      h('div', { class: 'flex justify-center' }, [
        h(Image, {
          src: text,
          width: 48,
          height: 48,
          preview: true,
          class: 'rounded-full object-cover border'
        })
      ])
  },
  {
    title: '农户姓名',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    align: 'center'
  },
  {
    title: '所在地区',
    dataIndex: 'region',
    align: 'center'
  },
  {
    title: '认证状态',
    align: 'center',
    customRender: ({ record }) => {
      const { text, color } = certLabel(record.certStatus)
      return h(Tag, { color }, () => text)
    }
  },
  {
    title: '关联商品数',
    dataIndex: 'productCount',
    align: 'center'
  },
  {
    title: '注册时间',
    dataIndex: 'createTime',
    align: 'center'
  },
  {
    title: '操作',
    align: 'center',
    width: 200,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () =>
              router.push({ path: '/admin/farmer/update', query: { id: record.id } })
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该农户吗？',
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
  searchPhone.value = ''
  certStatus.value = undefined
  current.value = 1
}

const handleDelete = (id) => {
  allFarmers.value = allFarmers.value.filter((f) => f.id !== id)
  if (pagedData.value.length === 0 && current.value > 1) {
    current.value--
  }
}
</script>
