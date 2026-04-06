<template>
  <div class="p-2 box">

    <!-- 查询条件 -->
    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="分类名称">
          <a-input v-model:value="searchName" placeholder="请输入分类名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>


    <!-- 分类管理 -->
    <div class="flex gap-4">

      <!-- 左侧分类树 -->
      <a-card :bordered="false" class="w-64">

        <div class="font-semibold mb-3">
          商品分类
        </div>

        <a-tree :tree-data="categoryTree" default-expand-all :show-icon="false" @select="handleTreeSelect" />

      </a-card>


      <!-- 右侧分类表格 -->
      <a-card :bordered="false" class="flex-1">

        <!-- 新增 -->
        <div class="mb-4">
          <a-button type="primary" class="flex items-center gap-1" @click="handleAdd">
            <PlusOutlined />
            新增分类
          </a-button>
        </div>

        <!-- 表格 -->
        <a-table :dataSource="pagedData" :columns="columns" :pagination="false" rowKey="id" bordered />

        <!-- 分页 -->
        <div class="mt-6 flex justify-center">
          <a-pagination v-model:current="current" v-model:pageSize="size" :total="total" show-size-changer
            :pageSizeOptions="[10, 20, 50]" :show-total="(t) => `共 ${t} 条`" />
        </div>

      </a-card>

    </div>

  </div>

  <!-- 参数定义管理弹窗 -->
  <a-modal
    v-model:open="paramModalVisible"
    :title="`参数模板管理 - ${paramModalCategoryName}`"
    width="640px"
    :footer="null"
    destroy-on-close
  >
    <a-alert
      class="mb-4"
      type="info"
      show-icon
      banner
      message="管理该分类下的参数模板，商品编辑时将自动加载这些参数供填写。"
    />

    <!-- 添加参数表单 -->
    <div class="flex items-center gap-2 mb-4">
      <a-input
        v-model:value="newParamName"
        placeholder="参数名（如：保质期、产地）"
        class="flex-1"
        @pressEnter="handleCreateParam"
      />
      <a-input-number
        v-model:value="newParamSort"
        placeholder="排序"
        :min="0"
        style="width: 100px"
      />
      <a-button type="primary" @click="handleCreateParam" :disabled="!newParamName.trim()">
        添加
      </a-button>
    </div>

    <!-- 参数列表 -->
    <a-table
      :dataSource="paramDefinitions"
      :columns="paramDefColumns"
      rowKey="id"
      :pagination="false"
      bordered
      size="small"
      :loading="paramLoading"
    />
  </a-modal>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  fetchProductCategoryList,
  fetchProductCategoryTree,
  deleteProductCategory,
  fetchProductCategoryOptions
} from '@/api/admin/productCategory'

import {
  fetchParamDefinitions,
  createParamDefinition,
  updateParamDefinition,
  deleteParamDefinition
} from '@/api/admin/paramDefinition'

import {
  Button,
  Popconfirm,
  Input,
  InputNumber
} from 'ant-design-vue'

import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  UnorderedListOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

/* ================= 查询条件 ================= */

const searchName = ref('')
const selectedTree = ref(null)


/* ================= 分类树数据 & 表格数据 ================= */
const categoryTree = ref([])
const allCategories = ref([])
const categoryNameMap = ref({})


/* ================= 分页 ================= */

const current = ref(1)
const size = ref(10)


/* ================= 分页数据 ================= */
const total = ref(0)
const pagedData = computed(() => allCategories.value)


/* ================= 表格列 ================= */

const columns = [

  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) =>
      (current.value - 1) * size.value + index + 1
  },

  {
    title: '分类名称',
    dataIndex: 'name',
    align: 'center'
  },

  {
    title: '父分类',
    align: 'center',
    customRender: ({ record }) => {
      if (!record.parentId || Number(record.parentId) === 0) return '一级分类'
      return categoryNameMap.value[record.parentId] || '父级分类不存在'
    }
  },

  {
    title: '分类级别',
    align: 'center',
    customRender: ({ record }) => {
      return Number(record.level) === 0 ? '一级分类' : '二级分类'
    }
  },

  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center'
  },

  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center'
  },

  {
    title: '操作',
    align: 'center',
    customRender: ({ record }) => {
      return h('div', { class: 'flex justify-center items-center gap-2' }, [

        h(
          Button,
          {
            size: 'small',
            type: 'default',
            class: 'flex items-center',
            onClick: () => handleOpenParamModal(record)
          },
          {
            icon: () => h(UnorderedListOutlined),
            default: () => '参数模板'
          }
        ),

        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center',
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
            title: '确定删除该分类吗？',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  class: 'flex items-center'
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
  }
]


/* ================= 方法 ================= */

const handleSearch = () => {
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  searchName.value = ''
  selectedTree.value = null
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

/* 新增分类 */
const handleAdd = () => {
  router.push('/admin/pms/productCate/add')
}

/* 编辑分类 */
const handleEdit = (record) => {
  router.push({ path: '/admin/pms/productCate/update', query: { id: record.id } })
}

/* 树选择 */
const handleTreeSelect = (keys) => {
  selectedTree.value = keys?.[0] ? Number(keys[0]) : null
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

/* 删除 */
const handleDelete = async (id) => {
  await deleteProductCategory(id)
  message.success('删除成功')
  if (allCategories.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchTree()
  await fetchList()
}

const toTreeNodes = (list = []) => {
  return list.map((item) => ({
    title: item.name || item.title,
    key: String(item.id || item.key),
    children: toTreeNodes(item.children || [])
  }))
}

const fetchTree = async () => {
  const rsp = await fetchProductCategoryTree()
  const treeRaw = Array.isArray(rsp) ? rsp : rsp?.data || []
  categoryTree.value = toTreeNodes(treeRaw)
}

const fetchCategoryNameMap = async () => {
  const rsp = await fetchProductCategoryOptions()
  const rows = rsp?.data || []
  const nextMap = {}
  rows.forEach((item) => {
    if (item?.id != null) nextMap[item.id] = item.name || ''
  })
  categoryNameMap.value = nextMap
}

const fetchList = async () => {
  const reqVO = {
    pageNum: current.value,
    pageSize: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()
  // 通过树节点选择时，只筛选分类名（后端当前无 parentId 条件）
  if (selectedTree.value) {
    const selectedName = categoryNameMap.value[selectedTree.value]
    if (selectedName) reqVO.name = selectedName
  }

  const rsp = await fetchProductCategoryList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取分类列表失败')
    return
  }
  allCategories.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(async () => {
  await fetchCategoryNameMap()
  await fetchTree()
  await fetchList()
})

watch([current, size], () => {
  fetchList()
})

/* ================= 参数模板管理弹窗 ================= */

const paramModalVisible = ref(false)
const paramModalCategoryId = ref(null)
const paramModalCategoryName = ref('')
const paramDefinitions = ref([])
const paramLoading = ref(false)
const newParamName = ref('')
const newParamSort = ref(0)

// 正在编辑的参数行
const editingParamId = ref(null)
const editingParamName = ref('')
const editingParamSort = ref(0)

const handleOpenParamModal = async (record) => {
  paramModalCategoryId.value = record.id
  paramModalCategoryName.value = record.name
  paramModalVisible.value = true
  newParamName.value = ''
  newParamSort.value = 0
  editingParamId.value = null
  await loadParamDefinitions()
}

const loadParamDefinitions = async () => {
  paramLoading.value = true
  try {
    const rsp = await fetchParamDefinitions(paramModalCategoryId.value)
    paramDefinitions.value = rsp?.data || []
  } finally {
    paramLoading.value = false
  }
}

const handleCreateParam = async () => {
  const name = newParamName.value.trim()
  if (!name) return
  await createParamDefinition(paramModalCategoryId.value, {
    categoryId: paramModalCategoryId.value,
    paramName: name,
    sort: newParamSort.value || 0
  })
  message.success('添加成功')
  newParamName.value = ''
  newParamSort.value = 0
  await loadParamDefinitions()
}

const handleSaveEditParam = async (record) => {
  const name = editingParamName.value.trim()
  if (!name) {
    message.warn('参数名不能为空')
    return
  }
  await updateParamDefinition(record.id, {
    paramName: name,
    sort: editingParamSort.value || 0
  })
  message.success('修改成功')
  editingParamId.value = null
  await loadParamDefinitions()
}

const handleCancelEditParam = () => {
  editingParamId.value = null
}

const handleDeleteParamDef = async (id) => {
  await deleteParamDefinition(id)
  message.success('删除成功')
  await loadParamDefinitions()
}

const paramDefColumns = [
  {
    title: '参数名',
    dataIndex: 'paramName',
    width: '40%',
    customRender: ({ record }) => {
      if (editingParamId.value === record.id) {
        return h(Input, {
          value: editingParamName.value,
          onChange: e => (editingParamName.value = e.target.value),
          onPressEnter: () => handleSaveEditParam(record)
        })
      }
      return record.paramName
    }
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: '20%',
    align: 'center',
    customRender: ({ record }) => {
      if (editingParamId.value === record.id) {
        return h(InputNumber, {
          value: editingParamSort.value,
          min: 0,
          onChange: v => (editingParamSort.value = v)
        })
      }
      return record.sort
    }
  },
  {
    title: '操作',
    width: '40%',
    align: 'center',
    customRender: ({ record }) => {
      if (editingParamId.value === record.id) {
        return h('div', { class: 'flex justify-center gap-2' }, [
          h(Button, { size: 'small', type: 'primary', onClick: () => handleSaveEditParam(record) }, () => '保存'),
          h(Button, { size: 'small', onClick: handleCancelEditParam }, () => '取消')
        ])
      }
      return h('div', { class: 'flex justify-center gap-2' }, [
        h(Button, {
          size: 'small',
          onClick: () => {
            editingParamId.value = record.id
            editingParamName.value = record.paramName
            editingParamSort.value = record.sort
          }
        }, () => '编辑'),
        h(
          Popconfirm,
          { title: '确定删除该参数？', onConfirm: () => handleDeleteParamDef(record.id) },
          { default: () => h(Button, { size: 'small', danger: true }, () => '删除') }
        )
      ])
    }
  }
]

</script>