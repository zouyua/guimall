<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">管理员管理</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        维护后台<strong>角色定义</strong>（如超级管理员、各业务管理员，也可包含<strong>测试账号</strong>等非生产角色）。
        在「角色管理」中为账号绑定此处配置的角色；点击<strong>分配菜单</strong>可设置该角色可见的后台菜单。
      </p>
    </a-card>

    <!-- 搜索区：按“角色名称”筛选角色列表 -->
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

    <!-- 数据表格区：展示角色分页列表 -->
    <a-card :bordered="false">

      <!-- 工具栏：新增角色 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="openModal()">
          <PlusOutlined />
          新增角色
        </a-button>
      </div>

      <!-- 表格：显示角色信息 + 操作列（分配菜单/编辑/删除） -->
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

    <!-- 新增/编辑弹窗：提交到 /admin/ums/role/create 或 /admin/ums/role/update -->
    <a-modal
      v-model:open="modalVisible"
      :title="editingId ? '编辑角色' : '新增角色'"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      width="520px"
      @ok="handleModalOk"
    >
      <a-form ref="modalFormRef" :model="modalForm" :rules="modalRules" layout="vertical" class="mt-2">
        <a-form-item name="name" label="角色名称" :required="true">
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
import { ref, computed, h, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined, MenuOutlined } from '@ant-design/icons-vue'
import { fetchUmsRoleList, createUmsRole, updateUmsRole, deleteUmsRoles } from '@/api/admin/umsRole'

const router = useRouter()

const searchName = ref('')

// 当前页数据（后端已分页）
const allRows = ref([])

const current = ref(1)
const size = ref(10)

const total = ref(0)
const pagedData = computed(() => allRows.value)

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

// 把“用户在搜索框输入的内容”转换成后端能模糊匹配的 role code（name 字段）。
// 后端 role 列表的 name 实际是 ROLE_*，不包含中文。
const normalizeRoleQueryToCode = (query) => {
  const q = (query ?? '').trim()
  if (!q) return ''

  // 1) 用户直接输入 ROLE_*：原样透传
  if (q.startsWith('ROLE_')) return q

  // 2) 用户输入中文标签/片段：尽量映射到唯一/最匹配的 ROLE_ code
  const matchedCodes = []
  for (const [code, label] of Object.entries(ROLE_LABEL_MAP)) {
    if (!label) continue
    // label 包含用户输入（如“订单”->“订单管理员”）
    if (label.includes(q)) {
      matchedCodes.push(code)
      continue
    }
    // 用户输入包含 label（较少发生，但也支持，如“超级管理员”）
    if (q.includes(label)) {
      matchedCodes.push(code)
      continue
    }
  }

  if (matchedCodes.length === 0) return q
  // 匹配多项时取第一项，保证查询仍能生效（你若希望“多项同时匹配”，可以后续升级为下拉多选）
  return matchedCodes[0]
}

// 新增/编辑角色弹窗状态
const modalVisible = ref(false)
const editingId = ref(null)
const modalFormRef = ref(null)
const modalForm = reactive({
  name: '',
  remark: ''
})

const modalRules = computed(() => {
  return {
    name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
  }
})

// 加载角色列表（按当前页 + 搜索条件）
const fetchRolesList = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) {
    reqVO.name = normalizeRoleQueryToCode(searchName.value.trim())
  }

  const rsp = await fetchUmsRoleList(reqVO)
  if (rsp?.success) {
    allRows.value = rsp.data || []
    total.value = rsp.total || 0
    return
  }

  // 若后端返回失败：给出提示便于排查
  console.log('fetchUmsRoleList failed:', rsp)
  message.error(rsp?.message || '获取角色列表失败')
}

onMounted(() => {
  fetchRolesList()
})

watch([current, size], () => {
  fetchRolesList()
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

// 提交新增/编辑角色
const handleModalOk = async () => {
  try {
    await modalFormRef.value?.validate()
  } catch (e) {
    return
  }

  const payload = {
    name: modalForm.name.trim(),
    remark: modalForm.remark || ''
  }

  if (editingId.value) {
    updateUmsRole({
      ...payload,
      id: editingId.value
    }).then(async () => {
      message.success('已保存')
      modalVisible.value = false
      await fetchRolesList()
    })
  } else {
    createUmsRole(payload).then(async () => {
      message.success('已新增')
      modalVisible.value = false
      await fetchRolesList()
    })
  }
}

const goAlloc = (record) => {
  // 跳转到「分配菜单权限」页面（传 roleId / roleName）
  router.push({ path: '/admin/ums/role/allocMenu', query: { roleId: record.id, roleName: record.name } })
}

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '角色名称',
    dataIndex: 'name',
    align: 'center',
    customRender: ({ record }) => roleDisplay(record.name)
  },
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
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchRolesList()
}

const handleReset = () => {
  searchName.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchRolesList()
}

const handleDelete = async (id) => {
  if (!id) return
  await deleteUmsRoles([id])
  message.success('已删除')

  if (allRows.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchRolesList()
}
</script>
