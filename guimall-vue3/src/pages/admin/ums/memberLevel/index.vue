<template>
  <div class="p-2 box">
    <!-- 搜索区域 -->
    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-input v-model:value="searchForm.name" placeholder="等级名称" allow-clear
          style="width: 180px" @pressEnter="handleSearch" />
        <a-select v-model:value="searchForm.status" placeholder="状态" allow-clear
          style="width: 120px">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">禁用</a-select-option>
        </a-select>
        <a-button type="primary" @click="handleSearch">
          <SearchOutlined /> 搜索
        </a-button>
        <a-button @click="handleReset">
          <ReloadOutlined /> 重置
        </a-button>
        <a-button type="primary" @click="openAddModal" style="margin-left: auto">
          <PlusOutlined /> 新增等级
        </a-button>
      </div>
    </a-card>

    <!-- 列表区域 -->
    <a-card :bordered="false">
      <a-table
        :dataSource="levelList"
        :columns="columns"
        :pagination="false"
        :loading="loading"
        rowKey="id"
        bordered
        class="w-full"
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

    <!-- 新增/编辑等级弹窗 -->
    <a-modal
      v-model:open="formVisible"
      :title="formData.id ? '编辑等级' : '新增等级'"
      @ok="handleFormSubmit"
      :confirmLoading="formLoading"
      :destroyOnClose="true"
      width="520px"
    >
      <a-form layout="vertical" class="py-2">
        <a-form-item label="等级名称" required>
          <a-input v-model:value="formData.name" placeholder="如：银卡会员" />
        </a-form-item>
        <a-form-item label="等级序号" required>
          <a-input-number v-model:value="formData.level" :min="0" :max="999"
            placeholder="序号越大等级越高" style="width: 100%" />
        </a-form-item>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="开通价格（元）" required>
            <a-input-number v-model:value="formData.price" :min="0" :precision="2"
              placeholder="0.00" style="width: 100%" />
          </a-form-item>
          <a-form-item label="折扣率（%）" required>
            <a-input-number v-model:value="formData.discount" :min="1" :max="100"
              placeholder="如 95 表示9.5折" style="width: 100%" />
          </a-form-item>
        </div>
        <a-form-item label="备注">
          <a-textarea v-model:value="formData.note" :rows="2" placeholder="可选" />
        </a-form-item>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="是否默认等级">
            <a-switch v-model:checked="formDefaultChecked" />
          </a-form-item>
          <a-form-item label="启用状态">
            <a-switch v-model:checked="formStatusChecked" />
          </a-form-item>
        </div>
      </a-form>
    </a-modal>

    <!-- 调整会员等级弹窗 -->
    <a-modal
      v-model:open="adjustVisible"
      title="调整会员等级"
      @ok="handleAdjustSubmit"
      :confirmLoading="adjustLoading"
      :destroyOnClose="true"
      width="480px"
    >
      <div class="space-y-4 py-2">
        <a-form layout="vertical">
          <a-form-item label="会员ID" required>
            <a-input-number v-model:value="adjustForm.memberId" :min="1"
              placeholder="请输入会员ID" style="width: 100%" />
          </a-form-item>
          <a-form-item label="目标等级" required>
            <a-select v-model:value="adjustForm.levelId" placeholder="请选择目标等级"
              style="width: 100%">
              <a-select-option v-for="lv in enabledLevels" :key="lv.id" :value="lv.id">
                {{ lv.name }}（等级{{ lv.level }}，{{ lv.discount }}%折扣）
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, h, computed, watch, onMounted } from 'vue'
import { Button, Tag, message, Modal } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  DeleteOutlined,
  PlusOutlined,
  UserSwitchOutlined
} from '@ant-design/icons-vue'
import {
  fetchMemberLevelList,
  fetchAllEnabledLevels,
  addMemberLevel,
  updateMemberLevel,
  deleteMemberLevel,
  adjustMemberLevel
} from '@/api/admin/memberLevel'

// ========== 等级列表 ==========
const loading = ref(false)
const levelList = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)

const searchForm = ref({
  name: '',
  status: undefined
})

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: 'ID', dataIndex: 'id', align: 'center', width: 70 },
  { title: '等级名称', dataIndex: 'name', align: 'center' },
  {
    title: '等级序号',
    dataIndex: 'level',
    align: 'center',
    width: 90,
    customRender: ({ record }) =>
      h('span', { style: { fontWeight: 'bold', color: '#8b5cf6' } }, record.level)
  },
  {
    title: '开通价格',
    dataIndex: 'price',
    align: 'center',
    width: 110,
    customRender: ({ record }) =>
      h('span', { style: { fontWeight: 'bold', color: '#059669' } }, '¥' + (record.price || 0))
  },
  {
    title: '折扣率',
    dataIndex: 'discount',
    align: 'center',
    width: 100,
    customRender: ({ record }) => {
      const d = record.discount || 100
      const label = d === 100 ? '无折扣' : (d / 10).toFixed(1) + '折'
      return h(Tag, { color: d < 100 ? 'orange' : 'default' }, () => label)
    }
  },
  {
    title: '默认',
    align: 'center',
    width: 80,
    customRender: ({ record }) =>
      h(Tag, { color: record.defaultStatus === 1 ? 'blue' : 'default' },
        () => record.defaultStatus === 1 ? '是' : '否')
  },
  {
    title: '状态',
    align: 'center',
    width: 80,
    customRender: ({ record }) =>
      h(Tag, { color: record.status === 1 ? 'success' : 'error' },
        () => record.status === 1 ? '启用' : '禁用')
  },
  { title: '备注', dataIndex: 'note', align: 'center', ellipsis: true },
  {
    title: '操作',
    align: 'center',
    width: 160,
    customRender: ({ record }) => {
      return h('div', { style: 'display:flex;gap:8px;justify-content:center' }, [
        h(Button, {
          type: 'primary',
          size: 'small',
          onClick: () => openEditModal(record)
        }, {
          default: () => [h(EditOutlined), ' 编辑']
        }),
        h(Button, {
          danger: true,
          size: 'small',
          onClick: () => handleDelete(record)
        }, {
          default: () => [h(DeleteOutlined), ' 删除']
        })
      ])
    }
  }
]

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      current: current.value,
      size: size.value,
      ...searchForm.value
    }
    Object.keys(params).forEach(k => {
      if (params[k] === '' || params[k] === undefined || params[k] === null) delete params[k]
    })
    const res = await fetchMemberLevelList(params)
    if (res?.success) {
      levelList.value = res.data || []
      total.value = res.total || 0
    } else {
      message.error(res?.message || '查询失败')
    }
  } catch (e) {
    message.error('查询失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  current.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.value = { name: '', status: undefined }
  current.value = 1
  fetchList()
}

watch([current, size], () => fetchList())

onMounted(() => {
  fetchList()
  loadEnabledLevels()
})

// ========== 新增 / 编辑 ==========
const formVisible = ref(false)
const formLoading = ref(false)
const formData = ref({})
const formDefaultChecked = computed({
  get: () => formData.value.defaultStatus === 1,
  set: (v) => { formData.value.defaultStatus = v ? 1 : 0 }
})
const formStatusChecked = computed({
  get: () => formData.value.status === 1,
  set: (v) => { formData.value.status = v ? 1 : 0 }
})

const openAddModal = () => {
  formData.value = {
    name: '',
    level: null,
    price: null,
    discount: 100,
    note: '',
    defaultStatus: 0,
    status: 1
  }
  formVisible.value = true
}

const openEditModal = (record) => {
  formData.value = { ...record }
  formVisible.value = true
}

const handleFormSubmit = async () => {
  if (!formData.value.name) { message.warning('请输入等级名称'); return }
  if (formData.value.level === null || formData.value.level === undefined) { message.warning('请输入等级序号'); return }
  if (formData.value.price === null || formData.value.price === undefined) { message.warning('请输入开通价格'); return }
  if (!formData.value.discount) { message.warning('请输入折扣率'); return }

  formLoading.value = true
  try {
    const api = formData.value.id ? updateMemberLevel : addMemberLevel
    const res = await api(formData.value)
    if (res?.success) {
      message.success(formData.value.id ? '编辑成功' : '新增成功')
      formVisible.value = false
      fetchList()
      loadEnabledLevels()
    } else {
      message.error(res?.message || '操作失败')
    }
  } catch (e) {
    message.error('操作失败')
  } finally {
    formLoading.value = false
  }
}

// ========== 删除 ==========
const handleDelete = (record) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定删除等级「${record.name}」吗？此操作不可恢复。`,
    okText: '确认删除',
    okType: 'danger',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await deleteMemberLevel(record.id)
        if (res?.success) {
          message.success('删除成功')
          fetchList()
          loadEnabledLevels()
        } else {
          message.error(res?.message || '删除失败')
        }
      } catch (e) {
        message.error('删除失败')
      }
    }
  })
}

// ========== 调整会员等级 ==========
const adjustVisible = ref(false)
const adjustLoading = ref(false)
const adjustForm = ref({ memberId: null, levelId: null })
const enabledLevels = ref([])

const loadEnabledLevels = async () => {
  try {
    const res = await fetchAllEnabledLevels()
    if (res?.success) {
      enabledLevels.value = res.data || []
    }
  } catch (e) {}
}

const openAdjustModal = () => {
  adjustForm.value = { memberId: null, levelId: null }
  adjustVisible.value = true
}

const handleAdjustSubmit = async () => {
  if (!adjustForm.value.memberId) { message.warning('请输入会员ID'); return }
  if (!adjustForm.value.levelId) { message.warning('请选择目标等级'); return }

  adjustLoading.value = true
  try {
    const res = await adjustMemberLevel(adjustForm.value)
    if (res?.success) {
      message.success('会员等级调整成功')
      adjustVisible.value = false
    } else {
      message.error(res?.message || '调整失败')
    }
  } catch (e) {
    message.error('调整失败')
  } finally {
    adjustLoading.value = false
  }
}
</script>
