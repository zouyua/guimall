<template>
  <div>
    <!-- 查询条件 -->
    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <!-- 商品名称 -->
        <a-form-item label="商品名称">
          <a-input
            v-model:value="searchProductName"
            placeholder="请输入商品名称"
            class="w-52"
          />
        </a-form-item>

        <!-- 商品分类 -->
        <a-form-item label="商品分类">
          <a-select
            v-model:value="categoryId"
            placeholder="请选择分类"
            class="w-40"
            allow-clear
          >
            <a-select-option :value="1">水果</a-select-option>
            <a-select-option :value="2">蔬菜</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 上架状态 -->
        <a-form-item label="上架状态">
          <a-select
            v-model:value="publishStatus"
            placeholder="请选择状态"
            class="w-40"
            allow-clear
          >
            <a-select-option :value="1">上架</a-select-option>
            <a-select-option :value="0">下架</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 按钮 -->
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <!-- 表格 -->
    <a-card :bordered="false">
      <!-- 新增按钮 -->
      <div class="mb-5">
        <a-button type="primary">新增</a-button>
      </div>

      <!-- 商品列表 -->
      <a-table
        :dataSource="pagedData"
        :columns="columns"
        :pagination="false"
        rowKey="id"
        bordered
      />

      <!-- 分页 -->
      <div class="mt-6 flex justify-center">
        <a-pagination
          v-model:current="current"
          v-model:pageSize="size"
          :total="total"
          show-size-changer
          :pageSizeOptions="[10, 20, 50]"
          show-total="(total) => `共 ${total} 条`"
        />
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { computed, h, ref } from 'vue'

// ================= 查询条件 =================
const searchProductName = ref('')
const categoryId = ref(undefined)
const publishStatus = ref(undefined)

// ================= 假数据（后续替换为接口） =================
const allProducts = ref([
  {
    id: 1,
    name: '砂糖橘',
    categoryId: 1,
    categoryName: '水果',
    farmerName: '张三',
    price: 12.5,
    stock: 100,
    publishStatus: 1,
    sale: 200,
    createTime: '2026-03-15 12:00:00'
  },
  {
    id: 2,
    name: '脐橙',
    categoryId: 1,
    categoryName: '水果',
    farmerName: '李四',
    price: 18.9,
    stock: 80,
    publishStatus: 0,
    sale: 120,
    createTime: '2026-03-16 09:30:00'
  },
  {
    id: 3,
    name: '西红柿',
    categoryId: 2,
    categoryName: '蔬菜',
    farmerName: '王五',
    price: 6.8,
    stock: 260,
    publishStatus: 1,
    sale: 340,
    createTime: '2026-03-17 14:10:00'
  }
])

// ================= 表格列 =================
const columns = [
  {
    title: '商品名称',
    dataIndex: 'name'
  },
  {
    title: '分类',
    dataIndex: 'categoryName'
  },
  {
    title: '农户',
    dataIndex: 'farmerName'
  },
  {
    title: '价格',
    dataIndex: 'price'
  },
  {
    title: '库存',
    dataIndex: 'stock'
  },
  {
    title: '销量',
    dataIndex: 'sale'
  },
  {
    title: '状态',
    dataIndex: 'publishStatus',
    customRender: ({ text }) => (text === 1 ? '上架' : '下架')
  },
  {
    title: '创建时间',
    dataIndex: 'createTime'
  },
  {
    title: '操作',
    key: 'action',
    customRender: ({ record }) => {
      return h(
        'a',
        {
          style: 'cursor:pointer',
          onClick: () => {
            handleDelete(record.id)
          }
        },
        '删除'
      )
    }
  }
]

// ================= 分页 =================
const current = ref(1)
const size = ref(10)
const filteredProducts = computed(() => {
  let list = allProducts.value

  if (searchProductName.value) {
    list = list.filter((p) => p.name.includes(searchProductName.value))
  }

  if (categoryId.value !== undefined && categoryId.value !== null) {
    list = list.filter((p) => p.categoryId === categoryId.value)
  }

  if (publishStatus.value !== undefined && publishStatus.value !== null) {
    list = list.filter((p) => p.publishStatus === publishStatus.value)
  }

  return list
})

const total = computed(() => filteredProducts.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  const end = start + size.value
  return filteredProducts.value.slice(start, end)
})

// ================= 方法 =================
const handleSearch = () => {
  current.value = 1
}

const handleReset = () => {
  searchProductName.value = ''
  categoryId.value = undefined
  publishStatus.value = undefined
  current.value = 1
}

const handleDelete = (id) => {
  allProducts.value = allProducts.value.filter((p) => p.id !== id)
  current.value = 1
}
</script>