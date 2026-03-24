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
  Button,
  Popconfirm
} from 'ant-design-vue'

import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined
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

</script>