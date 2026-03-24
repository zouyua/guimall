<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="原因类型">
          <a-input v-model:value="searchKeyword" placeholder="请输入关键词" class="w-56" allow-clear />
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
        <a-button type="primary" class="flex items-center gap-1" @click="openModal()">
          <PlusOutlined />
          新增原因
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
      :title="editingId ? '编辑原因' : '新增原因'"
      ok-text="保存"
      cancel-text="取消"
      destroy-on-close
      @ok="handleModalOk"
    >
      <a-form :model="modalForm" layout="vertical" class="mt-2">
        <a-form-item label="原因文案" required>
          <a-input v-model:value="modalForm.label" placeholder="如：商品破损" allow-clear />
        </a-form-item>
        <a-form-item label="排序">
          <a-input-number v-model:value="modalForm.sort" :min="0" class="w-full" />
        </a-form-item>
        <a-form-item label="是否启用">
          <a-switch v-model:checked="modalForm.enabled" />
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, reactive, onMounted, watch } from 'vue'
import { Button, Popconfirm, Tag, message, Switch } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import {
  createOrderReturnReason,
  deleteOrderReturnReasons,
  fetchOrderReturnReasonList,
  updateOrderReturnReason,
  updateOrderReturnReasonStatus
} from '@/api/admin/orderReturnReason'

const searchKeyword = ref('')

const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

const modalVisible = ref(false)
const editingId = ref(null)
const modalForm = reactive({
  label: '',
  sort: 0,
  enabled: true
})

const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: '原因文案', dataIndex: 'name', align: 'center' },
  { title: '排序', dataIndex: 'sort', align: 'center', width: 100 },
  {
    title: '状态',
    align: 'center',
    width: 180,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(Tag, { color: Number(record.status) === 1 ? 'success' : 'default' }, () =>
          Number(record.status) === 1 ? '启用' : '停用'
        ),
        h(Switch, {
          checked: Number(record.status) === 1,
          size: 'small',
          onChange: async (checked) => handleToggleStatus(record, checked)
        })
      ])
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
            title: '确定删除该原因吗？',
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
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  searchKeyword.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const openModal = (record) => {
  if (record) {
    editingId.value = record.id
    modalForm.label = record.name
    modalForm.sort = record.sort
    modalForm.enabled = Number(record.status) === 1
  } else {
    editingId.value = null
    modalForm.label = ''
    modalForm.sort = (allRows.value.length ? Math.max(...allRows.value.map((r) => Number(r.sort || 0))) : 0) + 1
    modalForm.enabled = true
  }
  modalVisible.value = true
}

// 新增/编辑按钮入参与后端 VO 对齐
const handleModalOk = async () => {
  if (!modalForm.label?.trim()) {
    message.warning('请输入原因文案')
    return
  }
  const payload = {
    name: modalForm.label.trim(),
    sort: Number(modalForm.sort || 0),
    status: modalForm.enabled ? 1 : 0
  }
  if (editingId.value) {
    const rsp = await updateOrderReturnReason(editingId.value, payload)
    if (!rsp?.success) {
      message.error(rsp?.message || '更新失败')
      return
    }
    message.success('更新成功')
  } else {
    const rsp = await createOrderReturnReason(payload)
    if (!rsp?.success) {
      message.error(rsp?.message || '新增失败')
      return
    }
    message.success('新增成功')
  }
  modalVisible.value = false
  await fetchList()
}

// 删除按钮入参：List<Long> ids
const handleDelete = async (id) => {
  const rsp = await deleteOrderReturnReasons([id])
  if (!rsp?.success) {
    message.error(rsp?.message || '删除失败')
    return
  }
  message.success('删除成功')
  await fetchList()
}

const handleToggleStatus = async (record, checked) => {
  const rsp = await updateOrderReturnReasonStatus({
    id: record.id,
    status: checked ? 1 : 0
  })
  if (!rsp?.success) {
    message.error(rsp?.message || '状态更新失败')
    return
  }
  record.status = checked ? 1 : 0
  message.success('状态已更新')
}

const fetchList = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchKeyword.value.trim()) reqVO.name = searchKeyword.value.trim()

  const rsp = await fetchOrderReturnReasonList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取退货原因列表失败')
    return
  }
  allRows.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>
