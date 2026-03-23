<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">角色管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        管理可登录本后台的<strong>账号</strong>（含正式管理员、运营人员，也可能是<strong>测试账号</strong>等）。
        在「管理员管理」中维护角色类型后，可在此为账号<strong>分配角色</strong>（操作列「分配角色」，即绑定管理员侧定义的角色）。
      </p>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="登录账号">
          <a-input v-model:value="searchUsername" placeholder="请输入登录账号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="状态">
          <a-select v-model:value="searchStatus" placeholder="请选择" class="!w-36" allow-clear>
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
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
          新增账号
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
      :title="editingId ? '编辑账号' : '新增账号'"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="520px"
      @ok="handleModalOk"
    >
      <a-form :model="modalForm" layout="vertical" class="mt-2">
        <a-form-item label="登录账号" required>
          <a-input v-model:value="modalForm.username" placeholder="英文或数字" :disabled="!!editingId" allow-clear />
        </a-form-item>
        <a-form-item :label="editingId ? '新密码（留空不修改）' : '登录密码'" :required="!editingId">
          <a-input-password v-model:value="modalForm.password" placeholder="至少 6 位" allow-clear />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input v-model:value="modalForm.nickname" placeholder="显示名称" allow-clear />
        </a-form-item>
        <a-form-item label="角色" required>
          <a-select v-model:value="modalForm.roleId" placeholder="请在「管理员管理」中先维护角色" class="w-full" allow-clear>
            <a-select-option v-for="r in roleOptions" :key="r.id" :value="r.id">{{ r.name }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="modalForm.status">
            <a-radio :value="1">正常</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:open="assignModalVisible"
      title="分配角色"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="480px"
      @ok="handleAssignOk"
    >
      <a-form layout="vertical" class="mt-2">
        <a-form-item label="登录账号">
          <a-input :value="assignRecord?.username" disabled />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input :value="assignRecord?.nickname" disabled />
        </a-form-item>
        <a-form-item label="角色" required>
          <a-select v-model:value="assignForm.roleId" placeholder="请选择要分配的角色" class="w-full" allow-clear>
            <a-select-option v-for="r in roleOptions" :key="r.id" :value="r.id">{{ r.name }}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, reactive } from 'vue'
import { Button, Popconfirm, Switch, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined, TeamOutlined } from '@ant-design/icons-vue'

const searchUsername = ref('')
const searchStatus = ref()

const roleOptions = [
  { id: 1, name: '超级管理员' },
  { id: 2, name: '订单管理员' },
  { id: 3, name: '商品管理员' },
  { id: 4, name: '营销管理员' },
  { id: 5, name: '测试账号' }
]

const allRows = ref([
  {
    id: 1,
    username: 'admin',
    nickname: '系统超管',
    roleId: 1,
    roleName: '超级管理员',
    status: 1,
    createTime: '2026-01-01 10:00:00'
  },
  {
    id: 2,
    username: 'order_mgr',
    nickname: '张订单',
    roleId: 2,
    roleName: '订单管理员',
    status: 1,
    createTime: '2026-03-01 09:30:00'
  },
  {
    id: 3,
    username: 'product_mgr',
    nickname: '李商品',
    roleId: 3,
    roleName: '商品管理员',
    status: 1,
    createTime: '2026-03-05 11:00:00'
  },
  {
    id: 4,
    username: 'mkt_mgr',
    nickname: '王营销',
    roleId: 4,
    roleName: '营销管理员',
    status: 1,
    createTime: '2026-03-08 15:20:00'
  },
  {
    id: 5,
    username: 'test_uat',
    nickname: '联调测试',
    roleId: 5,
    roleName: '测试账号',
    status: 1,
    createTime: '2026-03-12 09:00:00'
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchUsername.value.trim()) {
    list = list.filter((r) => r.username.includes(searchUsername.value.trim()))
  }
  if (searchStatus.value === 0 || searchStatus.value === 1) {
    list = list.filter((r) => r.status === searchStatus.value)
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
  username: '',
  password: '',
  nickname: '',
  roleId: undefined,
  status: 1
})

const assignModalVisible = ref(false)
const assignRecord = ref(null)
const assignForm = reactive({
  roleId: undefined
})

const resetModalForm = () => {
  modalForm.username = ''
  modalForm.password = ''
  modalForm.nickname = ''
  modalForm.roleId = undefined
  modalForm.status = 1
}

const openModal = (record) => {
  editingId.value = record?.id ?? null
  if (record) {
    modalForm.username = record.username
    modalForm.password = ''
    modalForm.nickname = record.nickname
    modalForm.roleId = record.roleId
    modalForm.status = record.status
  } else {
    resetModalForm()
  }
  modalVisible.value = true
}

const openAssignModal = (record) => {
  assignRecord.value = record
  assignForm.roleId = record.roleId
  assignModalVisible.value = true
}

const handleAssignOk = () => {
  if (assignForm.roleId == null) {
    message.warning('请选择角色')
    return
  }
  const row = assignRecord.value
  if (row) {
    const roleName = roleOptions.find((r) => r.id === assignForm.roleId)?.name ?? ''
    row.roleId = assignForm.roleId
    row.roleName = roleName
    message.success('分配成功（演示）')
  }
  assignModalVisible.value = false
}

const handleModalOk = () => {
  if (!modalForm.username?.trim()) {
    message.warning('请输入登录账号')
    return
  }
  if (!editingId.value && !modalForm.password?.trim()) {
    message.warning('请设置登录密码')
    return
  }
  if (!modalForm.roleId) {
    message.warning('请选择角色')
    return
  }
  const roleName = roleOptions.find((r) => r.id === modalForm.roleId)?.name ?? ''
  if (editingId.value) {
    const row = allRows.value.find((r) => r.id === editingId.value)
    if (row) {
      row.nickname = modalForm.nickname
      row.roleId = modalForm.roleId
      row.roleName = roleName
      row.status = modalForm.status
    }
    message.success('已保存（演示）')
  } else {
    const id = Math.max(0, ...allRows.value.map((r) => r.id)) + 1
    allRows.value.push({
      id,
      username: modalForm.username.trim(),
      nickname: modalForm.nickname || modalForm.username,
      roleId: modalForm.roleId,
      roleName,
      status: modalForm.status,
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
    })
    message.success('已新增（演示）')
  }
  modalVisible.value = false
}

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '登录账号', dataIndex: 'username', align: 'center' },
  { title: '昵称', dataIndex: 'nickname', align: 'center' },
  { title: '角色', dataIndex: 'roleName', align: 'center' },
  {
    title: '状态',
    align: 'center',
    width: 100,
    customRender: ({ record }) =>
      h('div', { class: 'flex justify-center py-1' }, [
        h(Switch, {
          checked: record.status === 1,
          checkedChildren: '正常',
          unCheckedChildren: '禁用',
          class: 'ums-admin-status-switch',
          onChange: (checked) => {
            record.status = checked ? 1 : 0
          }
        })
      ])
  },
  { title: '创建时间', dataIndex: 'createTime', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 300,
    customRender: ({ record }) =>
      h('div', { class: 'flex flex-wrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            class: 'inline-flex shrink-0 items-center gap-1',
            onClick: () => openAssignModal(record)
          },
          {
            icon: () => h(TeamOutlined),
            default: () => '分配角色'
          }
        ),
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'inline-flex shrink-0 items-center gap-1',
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
            title: '确定删除该账号吗？',
            disabled: record.username === 'admin',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: record.username === 'admin',
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
  searchUsername.value = ''
  searchStatus.value = undefined
  current.value = 1
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
  if (pagedData.value.length === 0 && current.value > 1) current.value--
  message.success('已删除（演示）')
}
</script>

<style scoped>
:deep(.ums-admin-status-switch.ant-switch) {
  transform: scale(1.1);
  transform-origin: center center;
}
</style>
