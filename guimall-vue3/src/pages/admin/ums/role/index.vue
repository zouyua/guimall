<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">管理员管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        维护后台<strong>角色定义</strong>（如超级管理员、各业务管理员，也可包含<strong>测试账号</strong>等非生产角色）。
        在「角色管理」中为账号绑定此处配置的角色；点击<strong>分配菜单</strong>可设置该角色可见的后台菜单。
      </p>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="角色名称">
          <a-input v-model:value="searchName" placeholder="如：订单管理员" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="openModal()">
          <PlusOutlined />
          新增角色
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

    <a-modal
      v-model:open="modalVisible"
      :title="editingId ? '编辑角色' : '新增角色'"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="520px"
      @ok="handleModalOk"
    >
      <a-form :model="modalForm" layout="vertical" class="mt-2">
        <a-form-item label="角色名称" required>
          <a-input v-model:value="modalForm.name" placeholder="如：订单管理员、测试账号" allow-clear />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="modalForm.remark" :rows="3" placeholder="该角色负责的业务范围说明" allow-clear />
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined, MenuOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')

const allRows = ref([
  { id: 1, name: '超级管理员', remark: '全部后台菜单与操作权限', userCount: 1 },
  { id: 2, name: '订单管理员', remark: '订单、发货、退货与售后相关', userCount: 1 },
  { id: 3, name: '商品管理员', remark: '商品、分类、农户与产地等', userCount: 1 },
  { id: 4, name: '营销管理员', remark: '优惠券、推荐位与轮播图等', userCount: 1 },
  { id: 5, name: '测试账号', remark: '联调/UAT，仅开放必要菜单', userCount: 1 }
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

const modalVisible = ref(false)
const editingId = ref(null)
const modalForm = reactive({
  name: '',
  remark: ''
})

const openModal = (record) => {
  editingId.value = record?.id ?? null
  if (record) {
    modalForm.name = record.name
    modalForm.remark = record.remark
  } else {
    modalForm.name = ''
    modalForm.remark = ''
  }
  modalVisible.value = true
}

const handleModalOk = () => {
  if (!modalForm.name?.trim()) {
    message.warning('请输入角色名称')
    return
  }
  if (editingId.value) {
    const row = allRows.value.find((r) => r.id === editingId.value)
    if (row) {
      row.name = modalForm.name.trim()
      row.remark = modalForm.remark || ''
    }
    message.success('已保存（演示）')
  } else {
    const id = Math.max(0, ...allRows.value.map((r) => r.id)) + 1
    allRows.value.push({
      id,
      name: modalForm.name.trim(),
      remark: modalForm.remark || '',
      userCount: 0
    })
    message.success('已新增（演示）')
  }
  modalVisible.value = false
}

const goAlloc = (record) => {
  router.push({ path: '/admin/ums/role/allocMenu', query: { roleId: record.id, roleName: record.name } })
}

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '角色名称', dataIndex: 'name', align: 'center' },
  { title: '描述', dataIndex: 'remark', align: 'center', ellipsis: true },
  { title: '已绑定账号数', dataIndex: 'userCount', align: 'center', width: 120 },
  {
    title: '操作',
    align: 'center',
    width: 280,
    customRender: ({ record }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            class: 'inline-flex shrink-0 items-center gap-1',
            onClick: () => goAlloc(record)
          },
          {
            icon: () => h(MenuOutlined),
            default: () => '分配菜单'
          }
        ),
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'inline-flex shrink-0 items-center gap-1',
            disabled: record.id === 1,
            onClick: () => openModal(record)
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该角色吗？',
            disabled: record.id === 1,
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: record.id === 1,
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
