<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="类型名称">
          <a-input v-model:value="searchName" placeholder="请输入类型名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="关联分类">
          <a-select v-model:value="categoryId" placeholder="请选择分类" class="!w-44" allow-clear>
            <a-select-option :value="1">水果</a-select-option>
            <a-select-option :value="2">蔬菜</a-select-option>
            <a-select-option :value="3">粮油副食</a-select-option>
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
        <a-button type="primary" class="flex items-center gap-1" @click="goAdd">
          <PlusOutlined />
          新增商品类型
        </a-button>
      </div>

      <div class="mb-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回
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

  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined, ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchName = ref('')
const categoryId = ref()

const allRows = ref([
  {
    id: 1,
    name: '规格',
    categoryName: '水果',
    inputType: '多选',
    attrCount: 3,
    sort: 1,
    createTime: '2026-03-10'
  },
  {
    id: 2,
    name: '产地',
    categoryName: '水果',
    inputType: '唯一文本',
    attrCount: 1,
    sort: 2,
    createTime: '2026-03-10'
  },
  {
    id: 3,
    name: '包装',
    categoryName: '蔬菜',
    inputType: '单选',
    attrCount: 2,
    sort: 1,
    createTime: '2026-03-12'
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchName.value.trim()) {
    list = list.filter((r) => r.name.includes(searchName.value.trim()))
  }
  if (categoryId.value != null) {
    const map = { 1: '水果', 2: '蔬菜', 3: '粮油副食' }
    list = list.filter((r) => r.categoryName === map[categoryId.value])
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
  {
    title: '类型名称',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '关联分类',
    dataIndex: 'categoryName',
    align: 'center'
  },
  {
    title: '录入方式',
    dataIndex: 'inputType',
    align: 'center'
  },
  {
    title: '属性项数',
    dataIndex: 'attrCount',
    align: 'center'
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
    width: 200,
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
  current.value = 1
}

const handleReset = () => {
  searchName.value = ''
  categoryId.value = undefined
  current.value = 1
}

const goBack = () => {
  router.push('/admin/pms/productAttr')
}

const goAdd = () => {
  router.push('/admin/pms/productAttr/addProductAttr')
}

const handleEdit = (record) => {
  router.push({ path: '/admin/pms/productAttr/updateProductAttr', query: { id: record.id } })
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
}
</script>
