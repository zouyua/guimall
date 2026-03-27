<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">商品详情</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-descriptions bordered :column="2" size="middle" class="max-w-4xl">
        <a-descriptions-item label="商品名称" :span="2">{{ detail.name }}</a-descriptions-item>
        <a-descriptions-item label="分类">{{ detail.categoryName }}</a-descriptions-item>
        <a-descriptions-item label="关联农户">{{ detail.farmerName }}</a-descriptions-item>
        <a-descriptions-item label="销售价格">¥ {{ detail.price }}</a-descriptions-item>
        <a-descriptions-item label="库存">
          {{ displayStock }}<span v-if="displayUnit">&nbsp;{{ displayUnit }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="销量">{{ detail.sale }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="上架">{{ detail.publishStatus ? '是' : '否' }}</a-descriptions-item>
        <a-descriptions-item label="新品">{{ detail.newStatus ? '是' : '否' }}</a-descriptions-item>
        <a-descriptions-item label="推荐">{{ detail.recommendStatus ? '是' : '否' }}</a-descriptions-item>
        <a-descriptions-item label="主图" :span="2">
          <a-image v-if="detail.pic" :src="detail.pic" :width="120" class="rounded border" />
          <span v-else class="text-gray-400">暂无</span>
        </a-descriptions-item>
        <a-descriptions-item label="商品描述" :span="2">
          {{ detail.description || '—' }}
        </a-descriptions-item>
      </a-descriptions>

      <div class="mt-6 flex justify-center">
        <a-button type="primary" @click="goEdit">编辑商品</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getProductDetail } from '@/api/admin/product'
import { getFarmerDetail } from '@/api/admin/farmer'

const router = useRouter()
const route = useRoute()

const detail = reactive({
  id: null,
  name: '',
  pic: '',
  categoryName: '',
  farmerName: '',
  price: 0,
  stock: 0,
  sale: 0,
  unit: '斤',
  publishStatus: 0,
  newStatus: false,
  recommendStatus: false,
  createTime: '',
  description: ''
})

// 库存展示：保证为数字；单位若缺失/异常（如乱码或问号）则不展示，避免页面出现“?”。
const displayStock = computed(() => {
  const n = Number(detail.stock)
  return Number.isFinite(n) ? n : 0
})

const displayUnit = computed(() => {
  const unit = String(detail.unit ?? '').trim()
  if (!unit || unit === 'null' || unit === 'undefined') return ''
  if (/[?�]/.test(unit)) return ''
  return unit
})

const loadDetail = async () => {
  const id = Number(route.query.id)
  if (!id || Number.isNaN(id)) return

  const rsp = await getProductDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取商品详情失败')
    return
  }

  Object.assign(detail, {
    id: rsp.data.id,
    name: rsp.data.name ?? '',
    pic: rsp.data.pic ?? '',
    categoryName: rsp.data.categoryName ?? '',
    farmerName: rsp.data.farmerName ?? rsp.data?.farmer?.name ?? '',
    price: rsp.data.price ?? 0,
    stock: rsp.data.stock ?? 0,
    sale: rsp.data.sale ?? 0,
    unit: rsp.data.unit ?? '斤',
    publishStatus: rsp.data.publishStatus ?? 0,
    createTime: rsp.data.createTime ?? '',
    description: rsp.data.description ?? ''
  })

  // 兼容：部分后端详情接口未返回 farmerName，仅返回 farmerId
  if (!detail.farmerName) {
    const farmerId = rsp.data.farmerId ?? rsp.data?.farmer?.id
    if (farmerId != null) {
      const farmerRsp = await getFarmerDetail(farmerId)
      if (farmerRsp?.success && farmerRsp?.data) {
        detail.farmerName = farmerRsp.data.name ?? farmerRsp.data.realName ?? ''
      }
    }
  }
}

onMounted(() => {
  loadDetail()
})

const goBack = () => {
  router.push('/admin/pms/product')
}

const goEdit = () => {
  router.push({ path: '/admin/pms/product/update', query: { id: detail.id } })
}
</script>
