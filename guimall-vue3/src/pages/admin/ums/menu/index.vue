<template>
  <div class="p-2 box">

    <!-- <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">菜单管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        维护后台侧边栏菜单结构；其中「角色管理」「管理员管理」等与权限模块对应（静态演示）。
      </p>
    </a-card> -->

    <!-- 搜索区：按菜单名称筛选菜单分页列表 -->
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

    <!-- 数据表格区：展示菜单列表 + 显示/隐藏开关 + 编辑/删除操作 -->
    <a-card :bordered="false">

      <!-- 工具栏：新增菜单 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="router.push('/admin/ums/menu/add')">
          <PlusOutlined />
          新增菜单
        </a-button>
      </div>

      <!-- 表格：包含父级菜单、路由路径、图标、排序、显示状态开关与操作按钮 -->
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
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, Switch, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { fetchUmsMenuList, updateUmsMenu, deleteUmsMenus } from '@/api/admin/umsMenu'

const router = useRouter()

const searchName = ref('')

// 当前分页菜单数据源
const allRows = ref([])

const current = ref(1)
const size = ref(10)

const total = ref(0)
const pagedData = computed(() => allRows.value)

// 加载菜单列表（支持分页 + 菜单名称模糊）
const fetchMenus = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) {
    reqVO.name = searchName.value.trim()
  }

  const rsp = await fetchUmsMenuList(reqVO)
  if (rsp?.success) {
    allRows.value = rsp.data || []
    total.value = rsp.total || 0
    return
  }

  // 若后端返回失败，给出提示便于排查接口/鉴权/数据过滤问题
  console.log('fetchUmsMenuList failed:', rsp)
  message.error(rsp?.message || '获取菜单列表失败')
}

onMounted(() => {
  fetchMenus()
})

watch([current, size], () => {
  fetchMenus()
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
          onChange: async (checked) => {
            // 侧栏显示/隐藏开关：checked=true -> hidden=0
            const nextHidden = checked ? 0 : 1
            await updateUmsMenu({
              id: record.id,
              parentId: record.parentId,
              name: record.name,
              path: record.path,
              icon: record.icon,
              sort: record.sort,
              hidden: nextHidden
            })
            record.hidden = nextHidden
            message.success('状态已更新')
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
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchMenus()
}

const handleReset = () => {
  searchName.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchMenus()
}

const handleDelete = async (id) => {
  if (!id) return
  await deleteUmsMenus([id])
  message.success('已删除')

  if (allRows.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchMenus()
}
</script>

<style scoped>
:deep(.ums-menu-visible-switch.ant-switch) {
  transform: scale(1.08);
  transform-origin: center center;
}
</style>
