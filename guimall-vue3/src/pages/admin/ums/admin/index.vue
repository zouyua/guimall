<template>
  <div class="p-2 box">

    <!-- <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">角色管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        管理可登录本后台的<strong>账号</strong>（含正式管理员、运营人员，也可能是<strong>测试账号</strong>等）。
        在「管理员管理」中维护角色类型后，可在此为账号<strong>分配角色</strong>（操作列「分配角色」，即绑定管理员侧定义的角色）。
      </p>
    </a-card> -->

    <!-- 搜索区：按“登录账号 / 状态”筛选管理员列表 -->
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

    <!-- 数据表格区：展示管理员分页列表 -->
    <a-card :bordered="false">

      <!-- 工具栏：新增按钮 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="openModal()">
          <PlusOutlined />
          新增账号
        </a-button>
      </div>

      <!-- 表格：列表展示 + 操作列（分配角色 / 编辑 / 删除） -->
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

    <!-- 新增/编辑弹窗：提交到后端 /admin/ums/admin/create 或 /update -->
    <a-modal
      v-model:open="modalVisible"
      :title="editingId ? '编辑账号' : '新增账号'"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="520px"
      @ok="handleModalOk"
    >
      <a-form ref="modalFormRef" :model="modalForm" :rules="modalRules" layout="vertical" class="mt-2">
        <a-form-item name="username" label="登录账号" :required="true">
          <a-input v-model:value="modalForm.username" placeholder="英文或数字" :disabled="!!editingId" allow-clear />
        </a-form-item>
        <a-form-item name="password" :label="editingId ? '新密码（留空不修改）' : '登录密码'" :required="!editingId">
          <a-input-password v-model:value="modalForm.password" placeholder="至少 6 位" allow-clear />
        </a-form-item>
        <a-form-item name="nickname" label="昵称" :required="true">
          <a-input v-model:value="modalForm.nickname" placeholder="显示名称" allow-clear />
        </a-form-item>
        <a-form-item name="roleId" label="角色" :required="true">
          <a-select v-model:value="modalForm.roleId" placeholder="请在「管理员管理」中先维护角色" class="w-full" allow-clear>
            <a-select-option v-for="r in roleOptions" :key="r.id" :value="r.id">{{ roleDisplay(r.name) }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="status" label="状态" :required="true">
          <a-radio-group v-model:value="modalForm.status">
            <a-radio :value="1">正常</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 分配角色弹窗：把账号 roleId 绑定到当前管理员 /admin/ums/admin/assignRole -->
    <a-modal
      v-model:open="assignModalVisible"
      title="分配角色"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="480px"
      @ok="handleAssignOk"
    >
      <a-form ref="assignFormRef" :model="assignForm" :rules="assignRules" layout="vertical" class="mt-2">
        <a-form-item label="登录账号">
          <a-input :value="assignRecord?.username" disabled />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input :value="assignRecord?.nickname" disabled />
        </a-form-item>
        <a-form-item name="roleId" label="角色" :required="true">
          <a-select v-model:value="assignForm.roleId" placeholder="请选择要分配的角色" class="w-full" allow-clear>
            <a-select-option v-for="r in roleOptions" :key="r.id" :value="r.id">{{ roleDisplay(r.name) }}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, reactive, onMounted, watch } from 'vue'
import { Button, Popconfirm, Switch, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined, TeamOutlined } from '@ant-design/icons-vue'
import {
  fetchUmsAdminList,
  createUmsAdmin,
  updateUmsAdmin,
  deleteUmsAdmins,
  fetchUmsRoleOptions,
  assignUmsAdminRole
} from '@/api/admin/umsAdmin'

const searchUsername = ref('')
const searchStatus = ref()

const roleOptions = ref([])

// 后端已做分页：这里直接存当前页数据
const allRows = ref([])

const current = ref(1)
const size = ref(10)

const total = ref(0)
const pagedData = computed(() => allRows.value)

// =========================
// 1) 数据加载：角色下拉
// =========================
const fetchRoles = async () => {
  const rsp = await fetchUmsRoleOptions()
  roleOptions.value = Array.isArray(rsp) ? rsp : rsp?.data || []
}

// =========================
// 角色名称展示映射（仅用于前端“识别”，不影响 roleId->roleCode 的提交逻辑）
// =========================
const ROLE_LABEL_MAP = {
  ROLE_ADMIN: '超级管理员',
  ROLE_ORDER: '订单管理员',
  ROLE_PRODUCT: '商品管理员',
  ROLE_MKT: '营销管理员',
  ROLE_VISITOR: '联调/UAT账号'
}

const roleDisplay = (roleCode) => ROLE_LABEL_MAP[roleCode] || roleCode || ''

// =========================
// 2) 数据加载：管理员分页列表
// =========================
const fetchTableData = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }

  const username = searchUsername.value.trim()
  if (username) reqVO.username = username

  if (searchStatus.value === 0 || searchStatus.value === 1) {
    reqVO.status = searchStatus.value
  }

  const rsp = await fetchUmsAdminList(reqVO)
  if (rsp?.success) {
    allRows.value = rsp.data || []
    total.value = rsp.total || 0
    return
  }

  // 若后端返回 success=false，前端需要明确提示，便于你定位接口/鉴权/数据过滤问题
  console.log('fetchUmsAdminList failed:', rsp)
  message.error(rsp?.message || '获取管理员列表失败')
}

onMounted(async () => {
  await fetchRoles()
  await fetchTableData()
})

watch([current, size], async () => {
  await fetchTableData()
})

const modalVisible = ref(false)
const editingId = ref(null)
const modalFormRef = ref(null)
const modalForm = reactive({
  username: '',
  password: '',
  nickname: '',
  roleId: undefined,
  status: 1
})

const assignModalVisible = ref(false)
const assignRecord = ref(null)
const assignFormRef = ref(null)
const assignForm = reactive({
  roleId: undefined
})

// 新增/编辑管理员弹窗表单校验规则
const modalRules = computed(() => {
  return {
    username: [
      { required: true, message: '请输入登录账号', trigger: 'blur' }
    ],
    nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
    roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }],
    password: [
      {
        validator: (_, value) => {
          // 编辑时：新密码留空不修改
          if (editingId.value) {
            if (!value) return Promise.resolve()
            if (String(value).trim().length < 6) return Promise.reject(new Error('密码至少 6 位'))
            return Promise.resolve()
          }
          // 新增时：密码必填
          if (!value) return Promise.reject(new Error('请设置登录密码'))
          if (String(value).trim().length < 6) return Promise.reject(new Error('密码至少 6 位'))
          return Promise.resolve()
        },
        trigger: 'blur'
      }
    ]
  }
})

// 分配角色弹窗表单校验规则
const assignRules = {
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const resetModalForm = () => {
  modalForm.username = ''
  modalForm.password = ''
  modalForm.nickname = ''
  modalForm.roleId = undefined
  modalForm.status = 1
}

// 打开「新增/编辑账号」弹窗
// record 存在则为编辑，否则为新增
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

// 打开「分配角色」弹窗
const openAssignModal = (record) => {
  assignRecord.value = record
  assignForm.roleId = record.roleId
  assignModalVisible.value = true
}

// 提交「分配角色」
const handleAssignOk = async () => {
  try {
    await assignFormRef.value?.validate()
  } catch (e) {
    return
  }

  const row = assignRecord.value
  if (!row) return

  await assignUmsAdminRole({
    id: row.id,
    roleId: assignForm.roleId
  })

  const roleName = roleOptions.value.find((r) => r.id === assignForm.roleId)?.name ?? ''
  row.roleId = assignForm.roleId
  row.roleName = roleName
  message.success('分配成功')
  assignModalVisible.value = false
  await fetchTableData()
}

// 提交「新增/编辑账号」
const handleModalOk = async () => {
  try {
    await modalFormRef.value?.validate()
  } catch (e) {
    return
  }

  const payloadBase = {
    username: modalForm.username.trim(),
    nickname: modalForm.nickname.trim(),
    roleId: modalForm.roleId,
    status: modalForm.status
  }

  if (editingId.value) {
    await updateUmsAdmin({
      ...payloadBase,
      id: editingId.value,
      password: modalForm.password?.trim() ? modalForm.password.trim() : null
    })
    message.success('已保存')
    modalVisible.value = false
    await fetchTableData()
  } else {
    await createUmsAdmin({
      ...payloadBase,
      password: modalForm.password.trim()
    })
    message.success('已新增')
    modalVisible.value = false
    await fetchTableData()
  }
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
  {
    title: '角色',
    dataIndex: 'roleName',
    align: 'center',
    customRender: ({ record }) => roleDisplay(record.roleName)
  },
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
          onChange: async (checked) => {
            const nextStatus = checked ? 1 : 0
            await updateUmsAdmin({
              id: record.id,
              username: record.username,
              nickname: record.nickname,
              roleId: record.roleId,
              status: nextStatus,
              password: null
            })
            record.status = nextStatus
            message.success('状态已更新')
            await fetchTableData()
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
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchTableData()
}

const handleReset = () => {
  searchUsername.value = ''
  searchStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchTableData()
}

const handleDelete = async (id) => {
  if (!id) return
  await deleteUmsAdmins([id])
  message.success('已删除')
  if (allRows.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchTableData()
}
</script>

<style scoped>
:deep(.ums-admin-status-switch.ant-switch) {
  transform: scale(1.1);
  transform-origin: center center;
}
</style>
