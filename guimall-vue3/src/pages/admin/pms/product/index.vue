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
            <a-select-option v-for="item in categoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
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
import { ref, computed, h, onMounted, onActivated, watch } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

//引入所需图标
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
import {
  fetchProductList,
  deleteProduct,
  publishProduct,
  unpublishProduct
} from '@/api/admin/product'
import { fetchProductCategoryTree } from '@/api/admin/productCategory'
import {
  fetchHomeNewProductList,
  createHomeNewProduct,
  deleteHomeNewProduct
} from '@/api/admin/homeNewProduct'
import {
  fetchHomeRecommendProductList,
  createHomeRecommendProduct,
  deleteHomeRecommendProduct
} from '@/api/admin/homeRecommendProduct'

const router = useRouter()

/* 分页查询参数 */
const searchProductName = ref('')
const categoryId = ref()
const publishStatus = ref()
const categoryOptions = ref([])
const categoryDescendantIdsMap = ref({})
const allProducts = ref([])
const total = ref(0)
const clientPaging = ref(false)

// 通过 productId 反查“新品推荐表 / 人气推荐表”主键，便于删除时走正确接口入参
const homeNewIdMap = ref({})
const homeRecommendIdMap = ref({})


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
    align: 'center',
    customRender: ({ text }) => {
      // Handle null, undefined, and ensure proper number display
      if (text === null || text === undefined) {
        return '0'
      }
      // Convert to number if it's a string, then back to string for display
      const numValue = typeof text === 'string' ? parseInt(text, 10) : text
      return isNaN(numValue) ? '0' : numValue.toString()
    }
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
            checked: Number(record.publishStatus) === 1,
            class: 'product-status-switch',
            onChange: async (checked) => {
              await handleTogglePublish(record, checked)
            }
          })
        ]),
        h('div', { class: 'flex items-center justify-center gap-2' }, [
          h('span', { class: 'text-sm text-gray-600 w-10 text-right shrink-0' }, '新品'),
          h(Switch, {
            checked: !!record.newStatus,
            class: 'product-status-switch',
            onChange: async (checked) => {
              await handleToggleNew(record, checked)
            }
          })
        ]),
        h('div', { class: 'flex items-center justify-center gap-2' }, [
          h('span', { class: 'text-sm text-gray-600 w-10 text-right shrink-0' }, '推荐'),
          h(Switch, {
            checked: !!record.recommendStatus,
            class: 'product-status-switch',
            onChange: async (checked) => {
              await handleToggleRecommend(record, checked)
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

const pagedData = computed(() => {
  if (!clientPaging.value) return allProducts.value
  const start = (current.value - 1) * size.value
  return allProducts.value.slice(start, start + size.value)
})

const fetchCategories = async () => {
  const rsp = await fetchProductCategoryTree()
  const tree = Array.isArray(rsp) ? rsp : rsp?.data || []

  // 下拉框仅展示一级分类
  categoryOptions.value = tree.map((n) => ({
    id: n.id,
    name: n.name,
    children: n.children || []
  }))

  // 构建：一级分类 -> 所有叶子分类ID（用于查询时展开到二级/多级）
  const map = {}
  const collectLeaves = (node) => {
    const children = node?.children || []
    if (!children.length) return [node.id]
    return children.flatMap(collectLeaves)
  }
  categoryOptions.value.forEach((n) => {
    if (n?.id != null) map[n.id] = collectLeaves(n)
  })
  categoryDescendantIdsMap.value = map
}

const fetchStatusRelations = async () => {
  const [newRsp, recommendRsp] = await Promise.all([
    fetchHomeNewProductList({ current: 1, size: 1000 }),
    fetchHomeRecommendProductList({ current: 1, size: 1000 })
  ])

  const nextNewMap = {}
  const nextRecommendMap = {}

  ;(newRsp?.data || []).forEach((item) => {
    if (item?.productId != null && item?.id != null) {
      nextNewMap[item.productId] = item.id
    }
  })
  ;(recommendRsp?.data || []).forEach((item) => {
    if (item?.productId != null && item?.id != null) {
      nextRecommendMap[item.productId] = item.id
    }
  })

  homeNewIdMap.value = nextNewMap
  homeRecommendIdMap.value = nextRecommendMap
}

const fetchProducts = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchProductName.value.trim()) reqVO.name = searchProductName.value.trim()
  if (publishStatus.value === 0 || publishStatus.value === 1) reqVO.publishStatus = publishStatus.value

  const selectedId = categoryId.value
  const descendantIds = selectedId != null ? (categoryDescendantIdsMap.value[selectedId] || [selectedId]) : []

  // 选了一级分类且存在子类：展开为多个分类并合并结果（后端只支持单 categoryId 精确过滤）
  if (selectedId != null && descendantIds.length > 1) {
    clientPaging.value = true

    const calls = descendantIds.map((cid) =>
      fetchProductList({
        ...reqVO,
        current: 1,
        size: 1000,
        categoryId: cid
      })
    )

    const rsps = await Promise.all(calls)
    const rows = []
    rsps.forEach((r) => {
      if (r?.success && Array.isArray(r.data)) rows.push(...r.data)
    })

    const uniq = new Map()
    rows.forEach((item) => {
      if (item?.id != null && !uniq.has(item.id)) uniq.set(item.id, item)
    })

    const merged = Array.from(uniq.values()).map((item) => ({
      ...item,
      stock: typeof item.stock === 'string' ? parseInt(item.stock, 10) : (item.stock || 0),
      newStatus: !!homeNewIdMap.value[item.id],
      recommendStatus: !!homeRecommendIdMap.value[item.id]
    }))

    allProducts.value = merged
    total.value = merged.length
    return
  }

  clientPaging.value = false
  if (selectedId != null) reqVO.categoryId = selectedId

  const rsp = await fetchProductList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取商品列表失败')
    return
  }

  allProducts.value = (rsp.data || []).map((item) => ({
    ...item,
    stock: typeof item.stock === 'string' ? parseInt(item.stock, 10) : (item.stock || 0),
    newStatus: !!homeNewIdMap.value[item.id],
    recommendStatus: !!homeRecommendIdMap.value[item.id]
  }))
  total.value = rsp.total || 0
}

/* 方法 */
const handleSearch = () => {
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchProducts()
}

const handleReset = () => {
  searchProductName.value = ''
  categoryId.value = undefined
  publishStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchProducts()
}

const handleDelete = async (id) => {
  await deleteProduct(id)
  message.success('删除成功')
  if (allProducts.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchStatusRelations()
  await fetchProducts()
}

const handleEdit = (record) => {
  router.push({ path: '/admin/pms/product/update', query: { id: record.id } })
}

const handleView = (record) => {
  router.push({ path: '/admin/pms/product/detail', query: { id: record.id } })
}

const handleTogglePublish = async (record, checked) => {
  if (!record?.id) return
  if (checked) {
    await publishProduct(record.id)
  } else {
    await unpublishProduct(record.id)
  }
  record.publishStatus = checked ? 1 : 0
  message.success('上架状态已更新')
}

const handleToggleNew = async (record, checked) => {
  if (!record?.id) return
  if (checked) {
    await createHomeNewProduct({
      productId: record.id,
      productName: record.name,
      recommendStatus: 1,
      sort: 0
    })
  } else {
    const relationId = homeNewIdMap.value[record.id]
    if (relationId) {
      await deleteHomeNewProduct(relationId)
    }
  }
  await fetchStatusRelations()
  record.newStatus = !!homeNewIdMap.value[record.id]
  message.success('新品状态已更新')
}

const handleToggleRecommend = async (record, checked) => {
  if (!record?.id) return
  if (checked) {
    await createHomeRecommendProduct({
      productId: record.id,
      productName: record.name,
      recommendStatus: 1,
      sort: 0
    })
  } else {
    const relationId = homeRecommendIdMap.value[record.id]
    if (relationId) {
      await deleteHomeRecommendProduct(relationId)
    }
  }
  await fetchStatusRelations()
  record.recommendStatus = !!homeRecommendIdMap.value[record.id]
  message.success('推荐状态已更新')
}

onMounted(async () => {
  await fetchCategories()
  await fetchStatusRelations()
  await fetchProducts()
})

// 从编辑/新增页面返回时自动刷新列表
onActivated(async () => {
  await fetchStatusRelations()
  await fetchProducts()
})

watch([current, size], () => {
  fetchProducts()
})

</script>

<style scoped>
:deep(.product-status-switch.ant-switch) {
  transform: scale(1.12);
  transform-origin: center center;
}
</style>