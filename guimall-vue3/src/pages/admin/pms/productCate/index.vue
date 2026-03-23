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

import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'

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


/* ================= 分类树数据 ================= */

const categoryTree = ref([
  {
    title: '水果',
    key: 1,
    children: [
      {
        title: '柑橘类',
        key: 2,
        children: [
          { title: '砂糖橘', key: 3 },
          { title: '脐橙', key: 4 },
          { title: '沃柑', key: 5 }
        ]
      },
      {
        title: '热带水果',
        key: 6,
        children: [
          { title: '芒果', key: 7 },
          { title: '香蕉', key: 8 }
        ]
      }
    ]
  },

  {
    title: '蔬菜',
    key: 9,
    children: [
      {
        title: '叶菜类',
        key: 10,
        children: [
          { title: '生菜', key: 11 },
          { title: '菠菜', key: 12 }
        ]
      }
    ]
  },

  {
    title: '粮油副食',
    key: 13,
    children: [
      { title: '大米', key: 14 },
      { title: '食用油', key: 15 },
      { title: '蜂蜜', key: 16 }
    ]
  }
])


/* ================= 分类表格数据 ================= */

const allCategories = ref([

  { id: 1, name: '水果', parentName: '一级分类', sort: 1, createTime: '2026-03-10' },
  { id: 2, name: '柑橘类', parentName: '水果', sort: 2, createTime: '2026-03-10' },
  { id: 3, name: '砂糖橘', parentName: '柑橘类', sort: 3, createTime: '2026-03-11' },
  { id: 4, name: '脐橙', parentName: '柑橘类', sort: 4, createTime: '2026-03-11' },
  { id: 5, name: '沃柑', parentName: '柑橘类', sort: 5, createTime: '2026-03-11' },

  { id: 6, name: '热带水果', parentName: '水果', sort: 6, createTime: '2026-03-12' },
  { id: 7, name: '芒果', parentName: '热带水果', sort: 7, createTime: '2026-03-12' },
  { id: 8, name: '香蕉', parentName: '热带水果', sort: 8, createTime: '2026-03-12' },

  { id: 9, name: '蔬菜', parentName: '一级分类', sort: 9, createTime: '2026-03-13' },
  { id: 10, name: '叶菜类', parentName: '蔬菜', sort: 10, createTime: '2026-03-13' },
  { id: 11, name: '生菜', parentName: '叶菜类', sort: 11, createTime: '2026-03-13' },

  { id: 12, name: '粮油副食', parentName: '一级分类', sort: 12, createTime: '2026-03-15' },
  { id: 13, name: '大米', parentName: '粮油副食', sort: 13, createTime: '2026-03-15' },
  { id: 14, name: '食用油', parentName: '粮油副食', sort: 14, createTime: '2026-03-15' }

])


/* ================= 分页 ================= */

const current = ref(1)
const size = ref(10)


/* ================= 数据过滤 ================= */

const filteredData = computed(() => {

  let list = allCategories.value

  const keyword = searchName.value.trim()

  if (keyword) {
    list = list.filter(c => c.name.includes(keyword))
  }

  if (selectedTree.value) {
    list = list.filter(c => c.id === selectedTree.value)
  }

  return list

})


const total = computed(() => filteredData.value.length)


const pagedData = computed(() => {

  const start = (current.value - 1) * size.value
  const end = start + size.value

  return filteredData.value.slice(start, end)

})


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
    dataIndex: 'parentName',
    align: 'center'
  },

  {
    title: '分类级别',
    align: 'center',
    customRender: ({ record }) => {
      if (record.parentName === '一级分类') {
        return '一级分类'
      }
      return '二级分类'
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
  current.value = 1
}

const handleReset = () => {
  searchName.value = ''
  selectedTree.value = null
  current.value = 1
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
  selectedTree.value = keys[0]
}

/* 删除 */
const handleDelete = (id) => {

  allCategories.value =
    allCategories.value.filter(c => c.id !== id)

  if (pagedData.value.length === 1 && current.value > 1) {
    current.value--
  }

}

</script>