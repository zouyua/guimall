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

        <a-form-item label="产地">
          <a-select v-model:value="searchOriginId" placeholder="请选择产地" class="!w-48" allow-clear>
            <a-select-option v-for="o in originOptions" :key="o.id" :value="o.id">{{ o.originName }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="农户商品关联">

      <a-alert
        class="mb-4"
        type="info"
        show-icon
        banner
        message="商品的农户关联在商品添加/编辑时设置（选择「关联农户」），此页面仅展示当前关联关系。农户的产地关联请在农户编辑页面设置。"
      />

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
import { ref, computed, h, onMounted } from 'vue'
import { Tag } from 'ant-design-vue'
import { fetchProductList } from '@/api/admin/product'
import { fetchFarmerList } from '@/api/admin/farmer'
import { fetchTraceOriginOptions } from '@/api/admin/traceOrigin'

const searchName = ref('')
const searchFarmer = ref('')
const searchOriginId = ref()
const originOptions = ref([])

const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

// 农户 id → originNames 映射（从农户列表接口获取）
const farmerOriginMap = ref({})

const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '商品名称',
    dataIndex: 'name',
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
    align: 'center',
    customRender: ({ record }) => {
      const names = record.originNames || []
      if (names.length === 0) return '—'
      return h('div', { class: 'flex flex-wrap justify-center gap-1' },
        names.map(name => h(Tag, { color: 'green' }, () => name))
      )
    }
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) =>
      h(
        Tag,
        { color: record.farmerName ? 'success' : 'default' },
        () => (record.farmerName ? '已关联' : '未关联')
      )
  }
]

const handleSearch = async () => {
  current.value = 1
  await fetchRows()
}

const handleReset = () => {
  searchName.value = ''
  searchFarmer.value = ''
  searchOriginId.value = undefined
  current.value = 1
  fetchRows()
}

const fetchRows = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()

  const rsp = await fetchProductList(reqVO)
  if (!rsp?.success) return

  let rows = (rsp.data || []).map(item => {
    // 从 farmerOriginMap 中查找该农户关联的产地
    const origins = item.farmerId ? (farmerOriginMap.value[item.farmerId] || []) : []
    return { ...item, originNames: origins }
  })

  // 前端过滤农户名称
  if (searchFarmer.value.trim()) {
    rows = rows.filter(r =>
      (r.farmerName || '').includes(searchFarmer.value.trim())
    )
  }

  // 前端过滤产地
  if (searchOriginId.value) {
    const selectedOrigin = originOptions.value.find(o => o.id === searchOriginId.value)
    if (selectedOrigin) {
      rows = rows.filter(r =>
        (r.originNames || []).includes(selectedOrigin.originName)
      )
    }
  }

  allRows.value = rows
  total.value = (searchFarmer.value.trim() || searchOriginId.value) ? rows.length : (rsp.total || rows.length)
}

// 加载所有农户及其关联产地（用于映射到商品列表）
const loadFarmerOrigins = async () => {
  const rsp = await fetchFarmerList({ current: 1, size: 9999 })
  if (!rsp?.success) return

  const map = {}
  for (const farmer of (rsp.data || [])) {
    map[farmer.id] = farmer.originNames || []
  }
  farmerOriginMap.value = map
}

onMounted(async () => {
  const originRsp = await fetchTraceOriginOptions()
  if (originRsp?.success) originOptions.value = originRsp.data || []

  await loadFarmerOrigins()
  await fetchRows()
})
</script>
