<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="类型名称">
          <a-input v-model:value="searchName" placeholder="请输入类型名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false">

      <div class="mb-4 flex flex-wrap justify-between items-center gap-3">
        <a-button type="primary" class="flex items-center gap-1" @click="goAdd">
          <PlusOutlined />
          新增商品类型
        </a-button>
      </div>

      <a-table
        :dataSource="pagedData"
        :columns="columns"
        :pagination="false"
        rowKey="id"
        bordered
        class="w-full"
        :expandedRowKeys="expandedRowKeys"
        @expand="onExpand"
      >
        <template #expandedRowRender="{ record }">
          <div class="p-3 bg-gray-50 rounded-md border border-gray-100">
            <a-tabs
              :activeKey="attrCacheOf(record.id).activeTab"
              @update:activeKey="(k) => (attrCacheOf(record.id).activeTab = k)"
            >
              <a-tab-pane key="0" tab="规格">
                <div class="mb-3 flex items-center justify-between gap-3">
                  <a-button type="primary" class="flex items-center gap-1" @click="openAttrModalForCreate(record, 0)">
                    <PlusOutlined />
                    新增规格
                  </a-button>
                </div>
                <a-table
                  :dataSource="attrCacheOf(record.id).specRows"
                  :columns="attrColumns"
                  :pagination="false"
                  rowKey="id"
                  bordered
                  size="small"
                />
              </a-tab-pane>

              <a-tab-pane key="1" tab="参数">
                <div class="mb-3 flex items-center justify-between gap-3">
                  <div class="text-sm text-gray-500">
                    该类型下的参数属性（type=1）
                  </div>
                  <a-button type="primary" class="flex items-center gap-1" @click="openAttrModalForCreate(record, 1)">
                    <PlusOutlined />
                    新增参数
                  </a-button>
                </div>
                <a-table
                  :dataSource="attrCacheOf(record.id).paramRows"
                  :columns="attrColumns"
                  :pagination="false"
                  rowKey="id"
                  bordered
                  size="small"
                />
              </a-tab-pane>
            </a-tabs>
          </div>
        </template>
      </a-table>

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
      v-model:open="attrModalOpen"
      :title="attrModalTitle"
      :confirmLoading="attrModalLoading"
      :destroyOnClose="true"
      @ok="submitAttrModal"
    >
      <a-form
        ref="attrFormRef"
        :model="attrForm"
        :rules="attrRules"
        layout="vertical"
      >
        <a-form-item label="属性名称" name="name" :required="true">
          <a-input v-model:value="attrForm.name" placeholder="请输入属性名称" allow-clear />
        </a-form-item>

        <a-form-item label="属性类型" name="type" :required="true">
          <a-radio-group v-model:value="attrForm.type">
            <a-radio :value="0">规格</a-radio>
            <a-radio :value="1">参数</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-row :gutter="[16, 0]">
          <a-col :span="12">
            <a-form-item label="选择类型" name="selectType">
              <a-select v-model:value="attrForm.selectType" placeholder="请选择">
                <a-select-option :value="0">唯一</a-select-option>
                <a-select-option :value="1">单选</a-select-option>
                <a-select-option :value="2">多选</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="输入类型" name="inputType">
              <a-select v-model:value="attrForm.inputType" placeholder="请选择">
                <a-select-option :value="0">手动</a-select-option>
                <a-select-option :value="1">列表</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item
          label="可选值列表"
          name="inputList"
          :required="attrForm.inputType === 1"
          help="输入类型为“列表”时必填，使用英文逗号分隔，例如：小份,中份,大份"
        >
          <a-textarea v-model:value="attrForm.inputList" :rows="3" placeholder="例如：小份,中份,大份" allow-clear />
        </a-form-item>

        <a-row :gutter="[16, 0]">
          <a-col :span="12">
            <a-form-item label="排序" name="sort">
              <a-input-number v-model:value="attrForm.sort" class="w-full" :min="0" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="搜索类型" name="searchType">
              <a-select v-model:value="attrForm.searchType" placeholder="请选择">
                <a-select-option :value="0">不搜索</a-select-option>
                <a-select-option :value="1">关键字搜索</a-select-option>
                <a-select-option :value="2">范围搜索</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="[16, 0]">
          <a-col :span="8">
            <a-form-item label="筛选类型" name="filterType">
              <a-select v-model:value="attrForm.filterType" placeholder="请选择">
                <a-select-option :value="0">不筛选</a-select-option>
                <a-select-option :value="1">普通筛选</a-select-option>
                <a-select-option :value="2">颜色筛选</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="是否关联" name="relatedStatus">
              <a-select v-model:value="attrForm.relatedStatus" placeholder="请选择">
                <a-select-option :value="0">不关联</a-select-option>
                <a-select-option :value="1">关联</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="支持手动新增" name="handAddStatus">
              <a-select v-model:value="attrForm.handAddStatus" placeholder="请选择">
                <a-select-option :value="0">不支持</a-select-option>
                <a-select-option :value="1">支持</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, message } from 'ant-design-vue'
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons-vue'
import {
  fetchProductAttrCategoryList,
  deleteProductAttrCategory
} from '@/api/admin/productAttrCategory'
import {
  fetchProductAttrByCategoryId,
  createProductAttr,
  updateProductAttr,
  deleteProductAttr
} from '@/api/admin/productAttr'

const router = useRouter()

const searchName = ref('')
const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)
const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '类型名称',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '规格数量',
    dataIndex: 'attributeCount',
    align: 'center'
  },
  {
    title: '参数数量',
    dataIndex: 'paramCount',
    align: 'center'
  },
  {
    title: '操作',
    align: 'center',
    width: 170,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () => handleEdit(record)
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该类型吗？',
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
  searchName.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const goAdd = () => {
  router.push('/admin/pms/productAttr/addProductAttr')
}

const handleEdit = (record) => {
  router.push({ path: '/admin/pms/productAttr/updateProductAttr', query: { id: record.id } })
}

const expandedRowKeys = ref([])
const selectedCategory = ref(null)
const attrCache = ref({})

const attrCacheOf = (categoryId) => {
  const key = String(categoryId)
  if (!attrCache.value[key]) {
    attrCache.value[key] = {
      activeTab: '0',
      specRows: [],
      paramRows: [],
      loading: false
    }
  }
  return attrCache.value[key]
}

const attrColumns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => index + 1
  },
  {
    title: '属性名称',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '输入类型',
    align: 'center',
    customRender: ({ record }) => (record.inputType === 1 ? '列表' : '手动')
  },
  {
    title: '选择类型',
    align: 'center',
    customRender: ({ record }) => {
      if (record.selectType === 1) return '单选'
      if (record.selectType === 2) return '多选'
      return '唯一'
    }
  },
  {
    title: '可选值列表',
    dataIndex: 'inputList',
    align: 'center',
    ellipsis: true
  },
  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center',
    width: 80
  },
  {
    title: '操作',
    align: 'center',
    width: 160,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () => openAttrModalForEdit(record)
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该属性吗？',
            onConfirm: () => handleDeleteAttr(record.id)
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

const onExpand = async (expanded, record) => {
  if (!record?.id) return
  if (expanded) {
    expandedRowKeys.value = [record.id]
    selectedCategory.value = record
    await fetchAttrRows(record.id)
  } else {
    expandedRowKeys.value = []
    if (selectedCategory.value?.id === record.id) selectedCategory.value = null
  }
}

const fetchAttrRows = async (categoryId) => {
  if (!categoryId) return
  const c = attrCacheOf(categoryId)
  c.loading = true
  try {
    const [specRsp, paramRsp] = await Promise.all([
      fetchProductAttrByCategoryId(categoryId, 0),
      fetchProductAttrByCategoryId(categoryId, 1)
    ])

    if (!specRsp?.success) {
      message.error(specRsp?.message || '获取规格属性明细失败')
      return
    }
    if (!paramRsp?.success) {
      message.error(paramRsp?.message || '获取参数属性明细失败')
      return
    }

    c.specRows = specRsp.data || []
    c.paramRows = paramRsp.data || []
  } finally {
    c.loading = false
  }
}

const attrModalOpen = ref(false)
const attrModalLoading = ref(false)
const attrModalMode = ref('create')
const attrModalTitle = computed(() => (attrModalMode.value === 'edit' ? '编辑属性' : '新增属性'))

const attrFormRef = ref()
const attrForm = ref({
  id: undefined,
  productAttributeCategoryId: undefined,
  name: '',
  selectType: 0,
  inputType: 0,
  inputList: '',
  sort: 0,
  filterType: 0,
  searchType: 0,
  relatedStatus: 0,
  handAddStatus: 1,
  type: 0
})

const attrRules = {
  name: [{ required: true, message: '请输入属性名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择属性类型', trigger: 'change' }],
  inputList: [
    {
      validator: async (_rule, value) => {
        if (attrForm.value.inputType === 1) {
          const v = (value ?? '').trim()
          if (!v) return Promise.reject(new Error('输入类型为“列表”时，请填写可选值列表'))
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

const openAttrModalForCreate = (categoryRecord, type) => {
  if (!categoryRecord?.id) return
  selectedCategory.value = categoryRecord
  attrModalMode.value = 'create'
  attrForm.value = {
    id: undefined,
    productAttributeCategoryId: categoryRecord.id,
    name: '',
    selectType: 0,
    inputType: 0,
    inputList: '',
    sort: 0,
    filterType: 0,
    searchType: 0,
    relatedStatus: 0,
    handAddStatus: 1,
    type
  }
  attrModalOpen.value = true
}

const openAttrModalForEdit = (record) => {
  const categoryId = record.productAttributeCategoryId
  if (!categoryId) return
  if (!selectedCategory.value?.id || selectedCategory.value.id !== categoryId) {
    selectedCategory.value = { id: categoryId, name: '' }
  }
  attrModalMode.value = 'edit'
  attrForm.value = {
    id: record.id,
    productAttributeCategoryId: categoryId,
    name: record.name ?? '',
    selectType: record.selectType ?? 0,
    inputType: record.inputType ?? 0,
    inputList: record.inputList ?? '',
    sort: record.sort ?? 0,
    filterType: record.filterType ?? 0,
    searchType: record.searchType ?? 0,
    relatedStatus: record.relatedStatus ?? 0,
    handAddStatus: record.handAddStatus ?? 1,
    type: record.type ?? 0
  }
  attrModalOpen.value = true
}

const submitAttrModal = async () => {
  if (!attrFormRef.value) return
  try {
    await attrFormRef.value.validate()
  } catch {
    return
  }
  if (!selectedCategory.value?.id) return

  attrModalLoading.value = true
  try {
    const payload = {
      ...attrForm.value,
      productAttributeCategoryId: selectedCategory.value.id
    }
    let rsp
    if (attrModalMode.value === 'edit') {
      rsp = await updateProductAttr(payload)
    } else {
      rsp = await createProductAttr(payload)
    }
    if (!rsp?.success) {
      message.error(rsp?.message || '保存失败')
      return
    }
    message.success('保存成功')
    attrModalOpen.value = false
    await fetchAttrRows(selectedCategory.value.id)
    await fetchList()
  } finally {
    attrModalLoading.value = false
  }
}

const handleDeleteAttr = async (id) => {
  const rsp = await deleteProductAttr(id)
  if (!rsp?.success) {
    message.error(rsp?.message || '删除失败')
    return
  }
  message.success('删除成功')
  if (selectedCategory.value?.id) {
    await fetchAttrRows(selectedCategory.value.id)
  }
  await fetchList()
}

const handleDelete = async (id) => {
  await deleteProductAttrCategory(id)
  message.success('删除成功')
  if (allRows.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchList()
}

const fetchList = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()

  const rsp = await fetchProductAttrCategoryList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取商品类型列表失败')
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
