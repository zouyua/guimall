<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="商品分类">
          <a-select v-model:value="searchCategoryId" placeholder="全部分类" class="!w-44" allow-clear>
            <a-select-option v-for="item in categoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
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
        <a-button type="primary" class="flex items-center gap-1" @click="handleAdd">
          <PlusOutlined />
          新增记录类型
        </a-button>
      </div>

      <a-table :dataSource="filteredData" :columns="columns" :pagination="false" rowKey="id" bordered class="w-full" />

    </a-card>

    <!-- 新增 / 编辑弹窗 -->
    <a-modal
      v-model:open="modalOpen"
      :title="editingRecord ? '编辑记录类型' : '新增记录类型'"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      :confirmLoading="submitLoading"
    >
      <a-form ref="modalFormRef" :model="modalForm" :rules="modalRules" layout="horizontal" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="商品分类" name="categoryId" required>
          <a-select v-model:value="modalForm.categoryId" placeholder="请选择分类" class="w-full">
            <a-select-option v-for="item in categoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="类型名称" name="typeName" required>
          <a-input v-model:value="modalForm.typeName" placeholder="请输入记录类型名称" allow-clear />
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number v-model:value="modalForm.sort" :min="0" :precision="0" class="w-full" />
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
// 溯源记录类型管理页
// 职责：查询/新增/编辑/删除 溯源记录类型
import { ref, computed, h, onMounted } from 'vue'
import { Button, Popconfirm, Tag, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { getAllRecordTypes, createRecordType, updateRecordType, deleteRecordType } from '@/api/admin/traceRecordType'
import { fetchProductCategoryOptions } from '@/api/admin/productCategory'

// 查询条件
const searchCategoryId = ref()

// 分类选项
const categoryOptions = ref([])
// 分类 id → name 映射
const categoryNameMap = computed(() => {
  const map = {}
  categoryOptions.value.forEach(item => {
    map[item.id] = item.name
  })
  return map
})

// 所有记录类型
const allData = ref([])

// 按分类筛选
const filteredData = computed(() => {
  if (searchCategoryId.value === undefined || searchCategoryId.value === null) {
    return allData.value
  }
  return allData.value.filter(item => item.categoryId === searchCategoryId.value)
})

// 表格列
const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => index + 1
  },
  {
    title: '商品分类',
    align: 'center',
    customRender: ({ record }) =>
      h(Tag, { color: 'blue' }, () => categoryNameMap.value[record.categoryId] || `分类ID: ${record.categoryId}`)
  },
  {
    title: '类型名称',
    dataIndex: 'typeName',
    align: 'center'
  },
  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center',
    width: 80
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 180
  },
  {
    title: '操作',
    align: 'center',
    width: 200,
    customRender: ({ record }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            onClick: () => handleEdit(record)
          },
          () => [h(EditOutlined), ' 编辑']
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该记录类型吗？',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(Button, { size: 'small', danger: true }, () => [h(DeleteOutlined), ' 删除'])
          }
        )
      ])
  }
]

// ---- 弹窗 ----
const modalOpen = ref(false)
const editingRecord = ref(null) // null = 新增
const submitLoading = ref(false)
const modalFormRef = ref()
const modalForm = ref({
  categoryId: undefined,
  typeName: '',
  sort: 0
})
const modalRules = {
  categoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
}

const handleAdd = () => {
  editingRecord.value = null
  modalForm.value = { categoryId: undefined, typeName: '', sort: 0 }
  modalOpen.value = true
}

const handleEdit = (record) => {
  editingRecord.value = record
  modalForm.value = {
    categoryId: record.categoryId,
    typeName: record.typeName,
    sort: record.sort ?? 0
  }
  modalOpen.value = true
}

const handleModalCancel = () => {
  modalOpen.value = false
  editingRecord.value = null
}

const handleModalOk = async () => {
  try {
    await modalFormRef.value?.validate()
  } catch (e) {
    return
  }
  submitLoading.value = true
  try {
    if (editingRecord.value) {
      // 编辑
      const rsp = await updateRecordType({
        id: editingRecord.value.id,
        categoryId: modalForm.value.categoryId,
        typeName: modalForm.value.typeName.trim(),
        sort: modalForm.value.sort
      })
      if (!rsp?.success) {
        message.error(rsp?.message || '更新失败')
        return
      }
      message.success('更新成功')
    } else {
      // 新增
      const rsp = await createRecordType({
        categoryId: modalForm.value.categoryId,
        typeName: modalForm.value.typeName.trim(),
        sort: modalForm.value.sort
      })
      if (!rsp?.success) {
        message.error(rsp?.message || '新增失败')
        return
      }
      message.success('新增成功')
    }
    modalOpen.value = false
    editingRecord.value = null
    await fetchList()
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  const rsp = await deleteRecordType(id)
  if (!rsp?.success) {
    message.error(rsp?.message || '删除失败')
    return
  }
  message.success('删除成功')
  await fetchList()
}

const handleSearch = () => {
  // 前端筛选，无需重新请求
}

const handleReset = () => {
  searchCategoryId.value = undefined
}

const fetchList = async () => {
  const rsp = await getAllRecordTypes()
  if (!rsp?.success) {
    message.error(rsp?.message || '获取记录类型列表失败')
    return
  }
  allData.value = rsp.data || []
}

const loadCategoryOptions = async () => {
  const res = await fetchProductCategoryOptions()
  if (res?.success) {
    categoryOptions.value = res.data || []
  }
}

onMounted(async () => {
  await Promise.all([loadCategoryOptions(), fetchList()])
})
</script>
