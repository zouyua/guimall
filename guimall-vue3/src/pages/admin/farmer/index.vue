<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="农户姓名">
          <a-input v-model:value="searchName" placeholder="请输入姓名" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="手机号">
          <a-input v-model:value="searchPhone" placeholder="请输入手机号" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="状态">
          <a-select v-model:value="status" placeholder="请选择" class="!w-40" allow-clear>
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
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
        <a-button type="primary" class="flex items-center gap-1" @click="router.push('/admin/farmer/add')">
          <PlusOutlined />
          新增农户
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

    <!-- 删除拦截详情弹窗：展示该农户已绑定的全部商品 -->
    <a-modal
      v-model:open="bindingModalOpen"
      :title="`删除拦截：${bindingFarmerName || '当前农户'} 已绑定商品`"
      :footer="null"
      width="920px"
      destroy-on-close
    >
      <p class="mb-3 text-sm text-orange-600">
        当前农户已绑定 {{ bindingRows.length }} 个商品，请先解除绑定后再删除。
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
import { Image, Button, Popconfirm, Tag, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { fetchFarmerList, deleteFarmer } from '@/api/admin/farmer'
import { getProductBindingsByFarmerId } from '@/api/admin/traceProductOrigin'

const router = useRouter()

const searchName = ref('')
const searchPhone = ref('')
const status = ref()

const allFarmers = ref([])
const total = ref(0)
const bindingModalOpen = ref(false)
const bindingRows = ref([])
const bindingFarmerName = ref('')

const current = ref(1)
const size = ref(10)

const statusLabel = (s) => {
  if (Number(s) === 1) return { text: '启用', color: 'success' }
  return { text: '禁用', color: 'default' }
}

const pagedData = computed(() => allFarmers.value)
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
    title: '产地',
    dataIndex: 'originName',
    align: 'center',
    customRender: ({ text }) => text || '-'
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
    title: '头像',
    dataIndex: 'avatar',
    align: 'center',
    customRender: ({ text }) =>
      h('div', { class: 'flex justify-center' }, [
        h(Image, {
          src: text,
          width: 48,
          height: 48,
          preview: true,
          class: 'rounded-full object-cover border'
        })
      ])
  },
  {
    title: '农户姓名',
    dataIndex: 'name',
    align: 'center'
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    align: 'center'
  },
  {
    title: '所在地区',
    align: 'center',
    customRender: ({ record }) => {
      const p = record.province || ''
      const c = record.city || ''
      const r = record.region || ''
      return [p, c, r].filter(Boolean).join(' / ') || '-'
    }
  },
  {
    title: '农场名称',
    dataIndex: 'farmName',
    align: 'center'
  },
  {
    title: '状态',
    align: 'center',
    customRender: ({ record }) => {
      const { text, color } = statusLabel(record.status)
      return h(Tag, { color }, () => text)
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
    customRender: ({ record }) =>
      h('div', { class: 'flex items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'flex items-center gap-1',
            onClick: () =>
              router.push({ path: '/admin/farmer/update', query: { id: record.id } })
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该农户吗？',
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
  searchPhone.value = ''
  status.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

// 删除按钮入参校验：
// 1) 先按 farmerId 查询是否存在绑定商品（/admin/trace/productOrigin/farmer/{farmerId}）
// 2) 无绑定才调用 /admin/farmer/{id} 删除
const handleDelete = async (id) => {
  try {
    const bindRsp = await getProductBindingsByFarmerId(id)
    if (!bindRsp?.success) {
      message.error(bindRsp?.message || '删除前校验失败')
      return
    }
    const bindings = bindRsp?.data || []
    if (bindings.length > 0) {
      bindingRows.value = bindings
      const row = allFarmers.value.find((it) => it.id === id)
      bindingFarmerName.value = row?.name || ''
      bindingModalOpen.value = true
      return
    }
  } catch (err) {
    console.error('删除农户前校验失败', err)
    message.error('删除前校验接口调用失败，请确认后端服务已发布最新接口')
    return
  }

  const rsp = await deleteFarmer(id)
  if (!rsp?.success) {
    message.error(rsp?.message || '删除农户失败')
    return
  }
  message.success('删除成功')
  if (allFarmers.value.length === 1 && current.value > 1) {
    current.value--
    return
  }
  await fetchList()
}

const fetchList = async () => {
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()
  if (searchPhone.value.trim()) reqVO.phone = searchPhone.value.trim()
  if (status.value === 0 || status.value === 1) reqVO.status = status.value

  const rsp = await fetchFarmerList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取农户列表失败')
    return
  }
  allFarmers.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>
