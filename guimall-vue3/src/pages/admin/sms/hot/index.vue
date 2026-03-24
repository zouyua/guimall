<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">人气推荐</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">维护首页「人气」区块展示的商品及排序（动态接口数据）。</p>
    </a-card>

    <a-card :bordered="false">

      <!-- 新增 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="openModal">
          <PlusOutlined />
          添加推荐商品
        </a-button>
      </div>

      <a-table :dataSource="list" :columns="columns" :pagination="false" rowKey="id" bordered class="w-full" />

    </a-card>

    <a-modal
      v-model:open="modalVisible"
      :title="editingId ? '编辑人气推荐' : '选择商品加入人气推荐'"
      ok-text="确定"
      cancel-text="取消"
      destroy-on-close
      @ok="handleAddOk"
    >
      <a-form layout="vertical" class="mt-2">
        <a-form-item label="商品" required>
          <a-select
            v-model:value="pickProductId"
            placeholder="请选择尚未推荐的商品"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option
              v-for="p in availablePool"
              :key="p.productId"
              :value="p.productId"
              :label="p.productName"
            >
              {{ p.productName }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
// 人气推荐列表页
// 职责：查询人气推荐列表、增删改、排序上移下移
import { ref, computed, h, onMounted } from 'vue'
import { Button, Popconfirm, message, Tag } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined, ArrowUpOutlined, ArrowDownOutlined, EditOutlined } from '@ant-design/icons-vue'
import {
  createHomeRecommendProduct,
  deleteHomeRecommendProduct,
  fetchHomeRecommendProductList,
  fetchHomeRecommendProductOptions,
  updateHomeRecommendProduct
} from '@/api/admin/homeRecommendProduct'

const list = ref([])
const productPool = ref([])
const modalVisible = ref(false)
const pickProductId = ref(undefined)
const editingId = ref(undefined)

const availablePool = computed(() => {
  // 可选商品：默认排除已推荐商品；编辑时保留当前记录可选
  const picked = new Set(list.value.map((r) => Number(r.productId)))
  return productPool.value.filter((p) => !picked.has(Number(p.productId)) || Number(p.id) === Number(editingId.value))
})

const openModal = (record) => {
  // record 存在时表示编辑，不存在表示新增
  if (record?.id) {
    editingId.value = Number(record.id)
    pickProductId.value = Number(record.productId)
  } else {
    editingId.value = undefined
    pickProductId.value = undefined
  }
  modalVisible.value = true
}

const handleAddOk = async () => {
  // 新增/编辑按钮入参严格校验
  if (!pickProductId.value) {
    message.warning('请选择商品')
    return
  }
  const option = productPool.value.find((x) => Number(x.productId) === Number(pickProductId.value))
  if (!option) {
    message.warning('商品选项不存在')
    return
  }
  const payload = {
    productId: Number(option.productId),
    productName: String(option.productName || '').trim(),
    recommendStatus: 1,
    sort: Number(option.sort || 0)
  }
  if (!payload.productName) {
    message.warning('商品名称不能为空')
    return
  }
  const rsp = editingId.value
    ? await updateHomeRecommendProduct(editingId.value, payload)
    : await createHomeRecommendProduct(payload)
  if (!rsp?.success) {
    message.error(rsp?.message || (editingId.value ? '更新失败' : '新增失败'))
    return
  }
  modalVisible.value = false
  message.success(editingId.value ? '更新成功' : '新增成功')
  await fetchList()
}

const move = async (index, dir) => {
  // 通过交换 sort 实现上移/下移
  const j = index + dir
  if (j < 0 || j >= list.value.length) return
  const cur = list.value[index]
  const target = list.value[j]
  if (!cur?.id || !target?.id) return
  const rsp1 = await updateHomeRecommendProduct(cur.id, {
    productId: Number(cur.productId),
    productName: String(cur.productName || '').trim(),
    recommendStatus: Number(cur.recommendStatus ?? 1),
    sort: Number(target.sort)
  })
  const rsp2 = await updateHomeRecommendProduct(target.id, {
    productId: Number(target.productId),
    productName: String(target.productName || '').trim(),
    recommendStatus: Number(target.recommendStatus ?? 1),
    sort: Number(cur.sort)
  })
  if (!rsp1?.success || !rsp2?.success) {
    message.error(rsp1?.message || rsp2?.message || '调整排序失败')
    return
  }
  await fetchList()
}

const remove = async (record) => {
  // 删除按钮入参校验：id 必须存在
  if (!record?.id) {
    message.warning('人气推荐ID不能为空')
    return
  }
  const rsp = await deleteHomeRecommendProduct(record.id)
  if (!rsp?.success) {
    message.error(rsp?.message || '移除失败')
    return
  }
  message.success('已移除')
  await fetchList()
}

const columns = [
  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center',
    width: 80
  },
  { title: '商品ID', dataIndex: 'productId', align: 'center', width: 100 },
  { title: '商品名称', dataIndex: 'productName', align: 'center' },
  {
    title: '推荐状态',
    dataIndex: 'recommendStatus',
    align: 'center',
    width: 100,
    customRender: ({ record }) => h(Tag, { color: Number(record.recommendStatus) === 1 ? 'success' : 'default' }, () => Number(record.recommendStatus) === 1 ? '推荐' : '不推荐')
  },
  {
    title: '操作',
    align: 'center',
    width: 280,
    customRender: ({ record, index }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'inline-flex items-center gap-1',
            onClick: () => openModal(record)
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
            disabled: index === 0,
            class: 'inline-flex items-center gap-1',
            onClick: () => move(index, -1)
          },
          {
            icon: () => h(ArrowUpOutlined),
            default: () => '上移'
          }
        ),
        h(
          Button,
          {
            size: 'small',
            disabled: index === list.value.length - 1,
            class: 'inline-flex items-center gap-1',
            onClick: () => move(index, 1)
          },
          {
            icon: () => h(ArrowDownOutlined),
            default: () => '下移'
          }
        ),
        h(
          Popconfirm,
          {
            title: '从人气推荐中移除？',
            onConfirm: () => remove(record)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  class: 'inline-flex items-center gap-1'
                },
                {
                  icon: () => h(DeleteOutlined),
                  default: () => '移除'
                }
              )
          }
        )
      ])
  }
]

const fetchList = async () => {
  // 拉取人气推荐分页数据（此处取较大 size 用于列表管理）
  const rsp = await fetchHomeRecommendProductList({ current: 1, size: 200 })
  if (!rsp?.success) {
    message.error(rsp?.message || '获取人气推荐列表失败')
    return
  }
  list.value = [...(rsp.data || [])].sort((a, b) => Number(a.sort || 0) - Number(b.sort || 0))
}

const fetchOptions = async () => {
  // 拉取可选商品下拉数据
  const rsp = await fetchHomeRecommendProductOptions()
  if (!rsp?.success) {
    message.error(rsp?.message || '获取商品选项失败')
    return
  }
  productPool.value = rsp.data || []
}

onMounted(async () => {
  await fetchOptions()
  await fetchList()
})
</script>
