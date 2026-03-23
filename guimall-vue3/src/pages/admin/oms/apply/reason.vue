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
import { ref, computed, h, reactive } from 'vue'
import { Button, Popconfirm, Tag } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const searchKeyword = ref('')

const allRows = ref([
  { id: 1, label: '商品破损', sort: 1, enabled: true },
  { id: 2, label: '不想要了', sort: 2, enabled: true },
  { id: 3, label: '发错货', sort: 3, enabled: true },
  { id: 4, label: '与描述不符', sort: 4, enabled: false }
])

const current = ref(1)
const size = ref(10)

const modalVisible = ref(false)
const editingId = ref(null)
const modalForm = reactive({
  label: '',
  sort: 0,
  enabled: true
})

const filtered = computed(() => {
  let list = allRows.value
  if (searchKeyword.value.trim()) {
    list = list.filter((r) => r.label.includes(searchKeyword.value.trim()))
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
  { title: '原因文案', dataIndex: 'label', align: 'center' },
  { title: '排序', dataIndex: 'sort', align: 'center', width: 100 },
  {
    title: '状态',
    align: 'center',
    width: 100,
    customRender: ({ record }) =>
      h(Tag, { color: record.enabled ? 'success' : 'default' }, () =>
        record.enabled ? '启用' : '停用'
      )
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
  current.value = 1
}

const handleReset = () => {
  searchKeyword.value = ''
  current.value = 1
}

const openModal = (record) => {
  if (record) {
    editingId.value = record.id
    modalForm.label = record.label
    modalForm.sort = record.sort
    modalForm.enabled = record.enabled
  } else {
    editingId.value = null
    modalForm.label = ''
    modalForm.sort = (allRows.value.length ? Math.max(...allRows.value.map((r) => r.sort)) : 0) + 1
    modalForm.enabled = true
  }
  modalVisible.value = true
}

const handleModalOk = () => {
  if (!modalForm.label?.trim()) return
  if (editingId.value) {
    const row = allRows.value.find((r) => r.id === editingId.value)
    if (row) {
      row.label = modalForm.label.trim()
      row.sort = modalForm.sort
      row.enabled = modalForm.enabled
    }
  } else {
    const nextId = Math.max(0, ...allRows.value.map((r) => r.id)) + 1
    allRows.value.push({
      id: nextId,
      label: modalForm.label.trim(),
      sort: modalForm.sort,
      enabled: modalForm.enabled
    })
  }
  modalVisible.value = false
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
}
</script>
