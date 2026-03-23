<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">菜单管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        维护后台侧边栏菜单结构；其中「角色管理」「管理员管理」等与权限模块对应（静态演示）。
      </p>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="菜单名称">
          <a-input v-model:value="searchName" placeholder="请输入" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="router.push('/admin/ums/menu/add')">
          <PlusOutlined />
          新增菜单
        </a-button>
      </div>

      <a-table
        :dataSource="pagedData"
        :columns="columns"
        :pagination="false"
        rowKey="id"
        bordered
        class="w-full"
        :scroll="{ x: 1100 }"
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
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, Switch, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')

const allRows = ref([
  { id: 1, parentName: '—', name: '仪表盘', path: '/admin/index', icon: 'DashboardOutlined', sort: 0, hidden: 0 },
  { id: 2, parentName: '—', name: '商品模块', path: '', icon: 'AppstoreOutlined', sort: 1, hidden: 0 },
  { id: 3, parentName: '商品模块', name: '商品管理', path: '/admin/pms/product', icon: 'ShoppingOutlined', sort: 1, hidden: 0 },
  { id: 4, parentName: '商品模块', name: '商品分类', path: '/admin/pms/productCate', icon: 'ApartmentOutlined', sort: 2, hidden: 0 },
  { id: 5, parentName: '—', name: '权限模块', path: '', icon: 'SettingOutlined', sort: 9, hidden: 0 },
  { id: 6, parentName: '权限模块', name: '角色管理', path: '/admin/ums/admin', icon: 'UserOutlined', sort: 1, hidden: 0 },
  { id: 7, parentName: '权限模块', name: '管理员管理', path: '/admin/ums/role', icon: 'SafetyCertificateOutlined', sort: 2, hidden: 0 }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchName.value.trim()) {
    list = list.filter((r) => r.name.includes(searchName.value.trim()))
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
  { title: '上级菜单', dataIndex: 'parentName', align: 'center', width: 120 },
  { title: '菜单名称', dataIndex: 'name', align: 'center' },
  { title: '路由路径', dataIndex: 'path', align: 'center', ellipsis: true },
  { title: '图标组件', dataIndex: 'icon', align: 'center', width: 140,
    customRender: ({ text }) => h('span', { class: 'font-mono text-xs text-gray-600' }, text)
  },
  { title: '排序', dataIndex: 'sort', align: 'center', width: 80 },
  {
    title: '显示',
    align: 'center',
    width: 100,
    customRender: ({ record }) =>
      h('div', { class: 'flex justify-center' }, [
        h(Switch, {
          checked: record.hidden === 0,
          checkedChildren: '显示',
          unCheckedChildren: '隐藏',
          class: 'ums-menu-visible-switch',
          onChange: (checked) => {
            record.hidden = checked ? 0 : 1
          }
        })
      ])
  },
  {
    title: '操作',
    align: 'center',
    width: 160,
    fixed: 'right',
    customRender: ({ record }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'inline-flex shrink-0 items-center gap-1',
            onClick: () =>
              router.push({ path: '/admin/ums/menu/update', query: { id: record.id } })
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该菜单吗？',
            disabled: record.id <= 2,
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: record.id <= 2,
                  class: 'inline-flex shrink-0 items-center gap-1'
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
  current.value = 1
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
  if (pagedData.value.length === 0 && current.value > 1) current.value--
  message.success('已删除（演示）')
}
</script>

<style scoped>
:deep(.ums-menu-visible-switch.ant-switch) {
  transform: scale(1.08);
  transform-origin: center center;
}
</style>
