<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="商品名称">
          <a-input v-model:value="searchName" placeholder="请输入商品名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="农户">
          <a-input v-model:value="searchFarmer" placeholder="请输入农户姓名" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="关联状态">
          <a-select v-model:value="linkStatus" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="1">已关联</a-select-option>
            <a-select-option :value="0">未关联</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="农户关联">

      <div class="mb-4 text-sm text-gray-500">
        <!-- 维护商品与供货农户的绑定关系，便于溯源与结算（动态接口数据）。 -->
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
      v-model:open="bindModalVisible"
      :title="editingRow?.linkStatus ? '更换关联农户' : '关联农户'"
      ok-text="确定"
      cancel-text="取消"
      :confirmLoading="bindLoading"
      destroy-on-close
      @ok="handleBindConfirm"
    >
      <a-form layout="vertical" class="mt-2">
        <a-form-item label="商品">
          <a-input :value="editingRow?.productName" disabled />
        </a-form-item>
        <a-form-item label="选择产地" required>
          <a-select
            v-model:value="bindOriginId"
            placeholder="请选择产地"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option v-for="o in originOptions" :key="o.id" :value="o.id" :label="o.originName">
              {{ o.originName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="选择农户" required>
          <a-select
            v-model:value="bindFarmerId"
            placeholder="请选择农户"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option v-for="f in farmerOptions" :key="f.id" :value="f.id" :label="f.name">
              {{ f.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, onMounted } from 'vue'
import { Button, Popconfirm, Tag, message } from 'ant-design-vue'
import { LinkOutlined, DisconnectOutlined } from '@ant-design/icons-vue'
import { fetchProductList } from '@/api/admin/product'
import { fetchFarmerOptions } from '@/api/admin/farmer'
import { fetchTraceOriginOptions } from '@/api/admin/traceOrigin'
import { bindProductOrigin, getProductOriginByProductId, unbindProductOrigin } from '@/api/admin/traceProductOrigin'

const searchName = ref('')
const searchFarmer = ref('')
const linkStatus = ref()

const farmerOptions = ref([])
const originOptions = ref([])

const bindModalVisible = ref(false)
const editingRow = ref(null)
const bindFarmerId = ref(undefined)
const bindOriginId = ref(undefined)
const bindLoading = ref(false)

const allRows = ref([])
const fullRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  return allRows.value.slice(start, start + size.value)
})

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '商品名称',
    dataIndex: 'productName',
    align: 'center'
  },
  {
    title: '关联农户',
    dataIndex: 'farmerName',
    align: 'center',
    customRender: ({ text }) => text || '—'
  },
  {
    title: '关联产地',
    dataIndex: 'originName',
    align: 'center',
    customRender: ({ text }) => text || '—'
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(
        Tag,
        { color: record.linkStatus ? 'success' : 'default' },
        () => (record.linkStatus ? '已关联' : '未关联')
      )
  },
  {
    title: '最近更新',
    dataIndex: 'updateTime',
    align: 'center'
  },
  {
    title: '操作',
    align: 'center',
    width: 220,
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            ghost: true,
            class: 'flex items-center gap-1',
            onClick: () => handleBind(record)
          },
          {
            icon: () => h(LinkOutlined),
            default: () => (record.linkStatus ? '更换农户' : '关联农户')
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定解除该商品的农户关联吗？',
            disabled: !record.linkStatus,
            onConfirm: () => handleUnbind(record.productId)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  disabled: !record.linkStatus,
                  class: 'flex items-center gap-1'
                },
                {
                  icon: () => h(DisconnectOutlined),
                  default: () => '解除'
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
  if (prev === 1) applyFilters()
}

const handleReset = () => {
  searchName.value = ''
  searchFarmer.value = ''
  linkStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchRows()
}

const handleBind = (record) => {
  editingRow.value = record
  bindFarmerId.value = record.farmerId ?? undefined
  bindOriginId.value = record.originId ?? undefined
  bindModalVisible.value = true
}

// 绑定按钮入参与后端 BindProductOriginReqVO 对齐：{ productId, originId, farmerId }
const handleBindConfirm = async () => {
  if (!bindOriginId.value) {
    message.warning('请选择产地')
    return
  }
  if (!bindFarmerId.value) {
    message.warning('请选择农户')
    return
  }
  const row = editingRow.value
  if (!row?.productId) return

  bindLoading.value = true
  try {
    const rsp = await bindProductOrigin({
      productId: row.productId,
      originId: bindOriginId.value,
      farmerId: bindFarmerId.value
    })
    if (!rsp?.success) {
      message.error(rsp?.message || '绑定失败')
      return
    }
    message.success(row.linkStatus ? '已更换关联农户' : '已关联农户')
    bindModalVisible.value = false
    await fetchRows()
  } finally {
    bindLoading.value = false
  }
}

// 解除按钮入参：DELETE /admin/trace/productOrigin/product/{productId}
const handleUnbind = async (productId) => {
  const rsp = await unbindProductOrigin(productId)
  if (!rsp?.success) {
    message.error(rsp?.message || '解除关联失败')
    return
  }
  message.success('已解除关联')
  await fetchRows()
}

const fetchOptions = async () => {
  const [farmerRsp, originRsp] = await Promise.all([
    fetchFarmerOptions(),
    fetchTraceOriginOptions()
  ])
  farmerOptions.value = farmerRsp?.data || []
  originOptions.value = originRsp?.data || []
}

const applyFilters = () => {
  let list = fullRows.value
  if (searchFarmer.value.trim()) {
    list = list.filter((r) => (r.farmerName || '').includes(searchFarmer.value.trim()))
  }
  if (linkStatus.value === 1 || linkStatus.value === 0) {
    list = list.filter((r) => r.linkStatus === linkStatus.value)
  }
  allRows.value = list
  total.value = list.length
}

const fetchRows = async () => {
  // 列表主查询入参：FindPmsProductPageListReqVO
  const reqVO = { current: 1, size: 500 }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()

  const productRsp = await fetchProductList(reqVO)
  if (!productRsp?.success) {
    message.error(productRsp?.message || '获取商品列表失败')
    return
  }
  const products = productRsp?.data || []

  const relationResults = await Promise.all(
    products.map(async (p) => {
      try {
        const relRsp = await getProductOriginByProductId(p.id)
        const rel = relRsp?.success ? relRsp.data : null
        return {
          id: p.id,
          productId: p.id,
          productName: p.name,
          // 关联页展示应以“绑定关系表(trace_product_origin)”为准，
          // 解除关联后不能再回退到商品表中的 farmer 字段，否则会出现“看起来仍已关联”的假象。
          farmerId: rel?.farmerId ?? null,
          farmerName: rel?.farmerName ?? '',
          originId: rel?.originId ?? null,
          originName: rel?.originName ?? '',
          linkStatus: rel?.farmerId ? 1 : 0,
          updateTime: rel?.createTime || '-'
        }
      } catch {
        return {
          id: p.id,
          productId: p.id,
          productName: p.name,
          // 未绑定/查询失败时统一视为未关联，避免误展示旧数据
          farmerId: null,
          farmerName: '',
          originId: null,
          originName: '',
          linkStatus: 0,
          updateTime: '-'
        }
      }
    })
  )

  fullRows.value = relationResults
  applyFilters()
}

onMounted(async () => {
  await fetchOptions()
  await fetchRows()
})
</script>
