<template>
  <div class="p-2 box">

    <!-- 查询条件 -->
    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="商品名称">
          <a-input v-model:value="searchProductName" placeholder="请输入商品名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="商品分类">
          <a-select v-model:value="categoryId" placeholder="请选择分类" class="!w-44" allow-clear>
            <a-select-option :value="1">水果</a-select-option>
            <a-select-option :value="2">蔬菜</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="上架状态">
          <a-select v-model:value="publishStatus" placeholder="请选择状态" class="!w-44" allow-clear>
            <a-select-option :value="1">上架</a-select-option>
            <a-select-option :value="0">下架</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <!-- 商品列表 -->
    <a-card :bordered="false">

      <!-- 新增 -->
      <div class="mb-4">
        <a-button type="primary" class="flex items-center gap-1" @click="router.push('/admin/pms/product/add')">
          <PlusOutlined />
          新增商品
        </a-button>
      </div>

      <!-- 表格 -->
      <a-table :dataSource="pagedData" :columns="columns" :pagination="false" rowKey="id" bordered class="w-full" />

      <!-- 分页 -->
      <div class="mt-6 flex justify-center">
        <a-pagination v-model:current="current" v-model:pageSize="size" :total="total" show-size-changer
          :pageSizeOptions="[10, 20, 50]" :show-total="(total) => `共 ${total} 条`" />
      </div>

    </a-card>

  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'

import {
  Image,
  Switch,
  Button,
  Popconfirm
} from 'ant-design-vue'

import {
  PlusOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

/* 查询条件 */
const searchProductName = ref('')
const categoryId = ref()
const publishStatus = ref()


/* 假数据 */
const allProducts = ref([
  {
    id: 1,
    name: '砂糖橘',
    pic: 'https://picsum.photos/80',
    categoryName: '水果',
    farmerName: '张三',
    price: 12.5,
    stock: 100,
    sale: 200,
    publishStatus: true,
    newStatus: true,
    recommendStatus: false,
    createTime: '2026-03-15'
  },
  {
    id: 2,
    name: '脐橙',
    pic: 'https://picsum.photos/81',
    categoryName: '水果',
    farmerName: '李四',
    price: 18.9,
    stock: 80,
    sale: 120,
    publishStatus: false,
    newStatus: false,
    recommendStatus: true,
    createTime: '2026-03-16'
  }
])


/* 表格列 */
const columns = [

  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },

  {
    title: '商品图片',
    dataIndex: 'pic',
    align: 'center',
    customRender: ({ text }) => {
      return h(
        'div',
        { class: 'flex justify-center' },
        [
          h(Image, {
            src: text,
            width: 60,
            preview: true,
            class: 'rounded border'
          })
        ]
      )
    }
  },

  {
    title: '商品名称',
    dataIndex: 'name',
    align: 'center'
  },

  {
    title: '分类',
    dataIndex: 'categoryName',
    align: 'center'
  },

  {
    title: '农户',
    dataIndex: 'farmerName',
    align: 'center'
  },

  {
    title: '价格',
    dataIndex: 'price',
    align: 'center',
    customRender: ({ text }) => `¥ ${text}`
  },

  {
    title: '库存',
    dataIndex: 'stock',
    align: 'center'
  },

  {
    title: '销量',
    dataIndex: 'sale',
    align: 'center'
  },

  {
    title: '状态',
    align: 'center',
    width: 160,
    customRender: ({ record }) => {
      return h('div', { class: 'flex flex-col items-center gap-3 py-1' }, [
        h('div', { class: 'flex items-center justify-center gap-2' }, [
          h('span', { class: 'text-sm text-gray-600 w-10 text-right shrink-0' }, '上架'),
          h(Switch, {
            checked: record.publishStatus,
            class: 'product-status-switch',
            onChange: (checked) => {
              record.publishStatus = checked
            }
          })
        ]),
        h('div', { class: 'flex items-center justify-center gap-2' }, [
          h('span', { class: 'text-sm text-gray-600 w-10 text-right shrink-0' }, '新品'),
          h(Switch, {
            checked: record.newStatus,
            class: 'product-status-switch',
            onChange: (checked) => {
              record.newStatus = checked
            }
          })
        ]),
        h('div', { class: 'flex items-center justify-center gap-2' }, [
          h('span', { class: 'text-sm text-gray-600 w-10 text-right shrink-0' }, '推荐'),
          h(Switch, {
            checked: record.recommendStatus,
            class: 'product-status-switch',
            onChange: (checked) => {
              record.recommendStatus = checked
            }
          })
        ])
      ])
    }
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
    customRender: ({ record }) => {

      return h('div', { class: 'flex items-center justify-center gap-2' }, [

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
          Button,
          {
            size: 'small',
            class: 'flex items-center gap-1',
            onClick: () => handleView(record)
          },
          {
            icon: () => h(EyeOutlined),
            default: () => '查看'
          }
        ),

        h(
          Popconfirm,
          {
            title: '确定删除该商品吗？',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () => h(
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
  }

]


/* 分页 */
const current = ref(1)
const size = ref(10)

const categoryIdToName = { 1: '水果', 2: '蔬菜' }

const filteredProducts = computed(() => {

  let list = allProducts.value

  if (searchProductName.value) {
    list = list.filter(p => p.name.includes(searchProductName.value))
  }

  if (categoryId.value != null) {
    const name = categoryIdToName[categoryId.value]
    if (name) {
      list = list.filter(p => p.categoryName === name)
    }
  }

  if (publishStatus.value === 1) {
    list = list.filter(p => p.publishStatus === true)
  }
  if (publishStatus.value === 0) {
    list = list.filter(p => p.publishStatus === false)
  }

  return list

})

const total = computed(() => filteredProducts.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  const end = start + size.value
  return filteredProducts.value.slice(start, end)
})


/* 方法 */
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
  allProducts.value = allProducts.value.filter(p => p.id !== id)
}

const handleEdit = (record) => {
  router.push({ path: '/admin/pms/product/update', query: { id: record.id } })
}

const handleView = (record) => {
  router.push({ path: '/admin/pms/product/detail', query: { id: record.id } })
}

</script>

<style scoped>
:deep(.product-status-switch.ant-switch) {
  transform: scale(1.12);
  transform-origin: center center;
}
</style>