<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <!-- 查询条件：字段与后端 FindOriginPageListReqVO 保持一致 -->
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="产地名称">
          <a-input v-model:value="searchName" placeholder="请输入产地名称" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="省">
          <a-input v-model:value="searchProvince" placeholder="请输入省" class="w-44" allow-clear />
        </a-form-item>

        <a-form-item label="市">
          <a-input v-model:value="searchCity" placeholder="请输入市" class="w-44" allow-clear />
        </a-form-item>

        <a-form-item label="区/县">
          <a-input v-model:value="searchRegion" placeholder="请输入区/县" class="w-44" allow-clear />
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
        <a-button type="primary" class="flex items-center gap-1" @click="handleAdd">
          <PlusOutlined />
          新增产地
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

    <!-- 删除拦截详情弹窗：展示该产地下已绑定的全部商品 -->
    <a-modal
      v-model:open="bindingModalOpen"
      :title="`删除拦截：${bindingOriginName || '当前产地'} 已绑定商品`"
      :footer="null"
      width="920px"
      destroy-on-close
    >
      <p class="mb-3 text-sm text-orange-600">
        该产地已绑定 {{ bindingRows.length }} 个商品，请先在商品产地绑定模块解除关联后再删除。
      </p>
      <a-table
        :dataSource="bindingRows"
        :columns="bindingColumns"
        :pagination="{ pageSize: 8, showSizeChanger: true, pageSizeOptions: ['8', '20', '50'] }"
        rowKey="relationId"
        bordered
        size="small"
      />
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Popconfirm, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import {
  fetchTraceOriginList,
  deleteTraceOrigin
} from '@/api/admin/traceOrigin'
import { getProductBindingsByOriginId } from '@/api/admin/traceProductOrigin'

const router = useRouter()

const searchName = ref('')
const searchProvince = ref('')
const searchCity = ref('')
const searchRegion = ref('')

const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)
const pagedData = computed(() => allRows.value)

const bindingModalOpen = ref(false)
const bindingRows = ref([])
const bindingOriginName = ref('')

const bindingColumns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => index + 1
  },
  {
    title: '商品ID',
    dataIndex: 'productId',
    align: 'center',
    width: 100
  },
  {
    title: '商品名称',
    dataIndex: 'productName',
    align: 'center'
  },
  {
    title: '农户',
    dataIndex: 'farmerName',
    align: 'center',
    customRender: ({ record }) => record?.farmerName || '-'
  },
  {
    title: '绑定时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 190
  }
]

const columns = [
  {
    title: '序号',
    align: 'center',
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '产地名称',
    dataIndex: 'originName',
    align: 'center'
  },
  {
    title: '省',
    dataIndex: 'province',
    align: 'center'
  },
  {
    title: '市',
    dataIndex: 'city',
    align: 'center'
  },
  {
    title: '区/县',
    dataIndex: 'region',
    align: 'center'
  },
  {
    title: '产地简介',
    dataIndex: 'description',
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
    width: 180,
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
            title: '确定删除该产地吗？',
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
  searchProvince.value = ''
  searchCity.value = ''
  searchRegion.value = ''
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleAdd = () => {
  router.push('/admin/trace/origin/add')
}

const handleEdit = (record) => {
  router.push({ path: '/admin/trace/origin/update', query: { id: String(record.id) } })
}

// 操作按钮“删除”入参：后端 DeleteMapping("/{id}")，仅需 id
// 处理策略：
// 1) 先调用“按产地查询绑定商品”接口（后端：GET /admin/trace/productOrigin/origin/{originId}）
// 2) 若已绑定则弹窗拦截，展示全部绑定明细，不执行删除
// 3) 若拦截接口调用异常（如后端未发布新接口/网关未同步），给出明确提示，避免前端直接报“请求错误”
const handleDelete = async (id) => {
  try {
    // 删除前拦截：先检查是否已被商品绑定，避免误删
    const bindRsp = await getProductBindingsByOriginId(id)
    if (!bindRsp?.success) {
      message.error(bindRsp?.message || '删除前校验失败，请稍后重试')
      return
    }
    const bindings = bindRsp?.data || []
    if (bindings.length > 0) {
      bindingRows.value = bindings
      const row = allRows.value.find((it) => it.id === id)
      bindingOriginName.value = row?.originName || ''
      bindingModalOpen.value = true
      return
    }
  } catch (err) {
    // 这里常见于：后端未重启/接口路径未发布/网关未同步
    // 保留强拦截原则，直接终止删除，避免在校验缺失的情况下误删数据。
    console.error('删除前绑定校验接口调用失败:', err)
    message.error('删除前校验接口调用失败，请确认后端已发布 /admin/trace/productOrigin/origin/{originId} 并重启服务')
    return
  }

  try {
    const rsp = await deleteTraceOrigin(id)
    if (!rsp?.success) {
      message.error(rsp?.message || '删除产地失败')
      return
    }
    message.success('删除成功')
    if (allRows.value.length === 1 && current.value > 1) {
      current.value--
      return
    }
    await fetchList()
  } catch (err) {
    console.error('删除产地请求失败:', err)
    message.error('删除请求失败，请检查后端服务与接口权限配置')
  }
}

// 查询按钮入参与后端 FindOriginPageListReqVO 一一对应
const fetchList = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.originName = searchName.value.trim()
  if (searchProvince.value.trim()) reqVO.province = searchProvince.value.trim()
  if (searchCity.value.trim()) reqVO.city = searchCity.value.trim()
  if (searchRegion.value.trim()) reqVO.region = searchRegion.value.trim()

  const rsp = await fetchTraceOriginList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取产地列表失败')
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
