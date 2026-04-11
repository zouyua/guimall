<template>
  <div class="min-h-screen bg-stone-50 font-sans">
    <!-- 顶部导航 -->
    <nav class="bg-white/90 backdrop-blur-md border-b border-stone-200 sticky top-0 z-50">
      <div class="max-w-screen-xl flex items-center justify-between mx-auto p-4">
        <router-link to="/" class="flex items-center space-x-3">
          <div class="w-8 h-8 bg-emerald-600 rounded-lg flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
          </div>
          <span class="text-xl font-bold tracking-tight text-emerald-900 uppercase">Guimall</span>
        </router-link>
        <div class="flex items-center space-x-6">
          <router-link to="/cart" class="text-stone-500 hover:text-emerald-600 transition-colors font-medium flex items-center">
            <a-badge :count="cartStore.cartCount" :offset="[-2, 2]" size="small">
              <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" />
              </svg>
            </a-badge>
            购物车
          </router-link>
          <div class="w-px h-4 bg-stone-200"></div>
          <router-link to="/" class="text-stone-500 hover:text-emerald-600 transition-colors font-medium">首页</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto px-4 py-12">
      <!-- 页面标题 -->
      <div class="mb-10">
        <h1 class="text-3xl font-black text-stone-900 mb-2">我的订单</h1>
        <div class="w-16 h-1.5 bg-emerald-500 rounded-full"></div>
      </div>

      <!-- 状态筛选标签 -->
      <div class="flex flex-wrap gap-3 mb-8">
        <button v-for="tab in tabs" :key="tab.value"
          @click="activeTab = tab.value; currentPage = 1; loadOrders()"
          :class="activeTab === tab.value
            ? 'bg-emerald-600 text-white border-emerald-600 shadow-lg shadow-emerald-200'
            : 'bg-white text-stone-600 border-stone-200 hover:border-emerald-400'"
          class="px-6 py-2.5 rounded-full text-sm font-bold border transition-all">
          {{ tab.label }}
        </button>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-32">
        <a-spin size="large" />
      </div>

      <!-- 空订单 -->
      <div v-else-if="orderList.length === 0" class="flex flex-col items-center justify-center py-32">
        <div class="w-32 h-32 bg-stone-100 rounded-full flex items-center justify-center mb-6">
          <svg class="w-16 h-16 text-stone-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
        </div>
        <p class="text-xl text-stone-400 font-bold mb-6">暂无订单</p>
        <router-link to="/"
          class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200">
          去逛逛
        </router-link>
      </div>

      <!-- 订单列表 -->
      <div v-else class="space-y-6">
        <div v-for="order in orderList" :key="order.id"
          class="bg-white rounded-3xl border border-stone-100 shadow-sm hover:shadow-lg transition-all overflow-hidden">
          <!-- 订单头部 -->
          <div class="flex flex-col sm:flex-row sm:items-center justify-between px-8 py-5 bg-stone-50/50 border-b border-stone-100">
            <div class="flex items-center space-x-6">
              <span class="text-sm text-stone-400">
                订单编号：<span class="font-mono font-bold text-stone-600">{{ order.orderSn }}</span>
              </span>
              <span class="text-sm text-stone-400">{{ order.createTime }}</span>
            </div>
            <span :class="statusConfig[order.status]?.color || 'bg-stone-100 text-stone-600'"
              class="px-4 py-1.5 rounded-full text-xs font-bold mt-2 sm:mt-0 inline-block w-fit">
              {{ statusConfig[order.status]?.label || '未知' }}
            </span>
          </div>

          <!-- 订单商品 -->
          <div class="px-8 py-6">
            <!-- 会员等级订单 -->
            <div v-if="order.orderType === 1" class="flex items-center space-x-4 py-3">
              <div class="w-16 h-16 rounded-xl bg-amber-50 border border-amber-200 flex items-center justify-center flex-shrink-0">
                <svg class="w-8 h-8 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M5 3l3.057-3L12 4.5 15.943 0 19 3l-2 6.5L19 21H5l2-11.5L5 3z" />
                </svg>
              </div>
              <div class="flex-1 min-w-0">
                <h4 class="font-bold text-stone-800 text-sm">会员等级开通</h4>
                <p class="text-xs text-amber-500 mt-0.5">{{ order.note || '会员服务' }}</p>
              </div>
              <div class="text-right flex-shrink-0">
                <p class="text-emerald-600 font-bold text-sm">¥{{ order.payAmount }}</p>
              </div>
            </div>
            <!-- 普通商品订单 -->
            <template v-else>
              <div v-for="item in order.items" :key="item.id"
                class="flex items-center space-x-4 py-3 first:pt-0">
                <img :src="item.productPic" class="w-16 h-16 rounded-xl object-cover border border-stone-100 flex-shrink-0" />
                <div class="flex-1 min-w-0">
                  <h4 class="font-bold text-stone-800 truncate text-sm">{{ item.productName }}</h4>
                  <p class="text-xs text-stone-400 mt-0.5">{{ item.productAttr || '默认规格' }}</p>
                </div>
                <div class="text-right flex-shrink-0">
                  <p class="text-emerald-600 font-bold text-sm">¥{{ item.productPrice }}</p>
                  <p class="text-xs text-stone-400">× {{ item.productQuantity }}</p>
                </div>
              </div>
            </template>
          </div>

          <!-- 订单底部 -->
          <div class="flex flex-col sm:flex-row items-center justify-between px-8 py-5 border-t border-stone-100">
            <div class="text-stone-500 text-sm mb-3 sm:mb-0">
              <template v-if="order.orderType === 1">
                <span class="inline-flex items-center px-2 py-0.5 rounded-full bg-amber-50 text-amber-600 text-xs font-bold">会员服务</span>
              </template>
              <template v-else>
                共 <span class="font-bold text-stone-800">{{ order.items?.length || 0 }}</span> 件商品
              </template>
            </div>
            <div class="flex items-center space-x-6">
              <span class="text-stone-600">
                实付：<span class="text-xl font-black text-emerald-600">¥{{ order.payAmount }}</span>
              </span>
              <!-- 待付款 → 去支付 + 取消订单 -->
              <template v-if="order.status === 0">
                <button @click="handleCancelOrder(order.id)"
                  class="bg-stone-100 text-stone-600 px-6 py-2.5 rounded-xl font-bold hover:bg-stone-200 transition-all text-sm">
                  取消订单
                </button>
                <button @click="$router.push(`/pay?orderSn=${order.orderSn}&payAmount=${order.payAmount}`)"
                  class="bg-emerald-600 text-white px-6 py-2.5 rounded-xl font-bold hover:bg-emerald-700 transition-all shadow-md text-sm">
                  去支付
                </button>
              </template>
              <!-- 已支付（待发货/已发货/已完成）→ 查看详情 + 申请退货 -->
              <template v-else-if="order.status >= 1 && order.status <= 3">
                <button @click="viewOrderDetail(order.id)"
                  class="bg-stone-100 text-stone-600 px-6 py-2.5 rounded-xl font-bold hover:bg-stone-200 transition-all text-sm">
                  查看详情
                </button>
                <!-- 已发货 → 确认收货 -->
                <button v-if="order.status === 2" @click="handleConfirmReceipt(order.id)"
                  class="bg-emerald-600 text-white px-6 py-2.5 rounded-xl font-bold hover:bg-emerald-700 transition-all shadow-md text-sm">
                  确认收货
                </button>
                <button @click="handleReturnApply(order.id)"
                  class="bg-orange-100 text-orange-600 px-6 py-2.5 rounded-xl font-bold hover:bg-orange-200 transition-all text-sm">
                  申请退货
                </button>
              </template>
              <!-- 其他状态 → 查看详情 -->
              <button v-else @click="viewOrderDetail(order.id)"
                class="bg-stone-100 text-stone-600 px-6 py-2.5 rounded-xl font-bold hover:bg-stone-200 transition-all text-sm">
                查看详情
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > 0" class="flex justify-center mt-12">
        <a-pagination
          v-model:current="currentPage"
          :total="total"
          :pageSize="pageSize"
          show-less-items
          @change="handlePageChange"
        />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { getOrderList, confirmReceipt } from '@/api/frontend/order'
import { cancelOrder } from '@/api/frontend/orderReturn'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'
import { useCartStore } from '@/stores/cart'

const router = useRouter()

// 登录检查
if (!isMemberLoggedIn()) {
  message.warning('请先登录')
  router.push('/member/login?redirect=/my-orders')
}

const memberId = getMemberId()
const cartStore = useCartStore()

const tabs = [
  { label: '全部', value: -1 },
  { label: '待付款', value: 0 },
  { label: '待发货', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已完成', value: 3 }
]

const statusConfig = {
  0: { label: '待付款', color: 'bg-orange-100 text-orange-600' },
  1: { label: '待发货', color: 'bg-blue-100 text-blue-600' },
  2: { label: '已发货', color: 'bg-purple-100 text-purple-600' },
  3: { label: '已完成', color: 'bg-emerald-100 text-emerald-600' },
  4: { label: '已关闭', color: 'bg-stone-100 text-stone-500' }
}

const activeTab = ref(-1)
const loading = ref(false)
const orderList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const loadOrders = async () => {
  loading.value = true
  try {
    const data = {
      memberId,
      current: currentPage.value,
      size: pageSize.value
    }
    if (activeTab.value >= 0) {
      data.status = activeTab.value
    }
    const res = await getOrderList(data)
    if (res.success) {
      orderList.value = res.data || []
      total.value = res.total || 0
    }
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadOrders()
}

const viewOrderDetail = (orderId) => {
  router.push(`/order/detail?id=${orderId}`)
}

const handleCancelOrder = (orderId) => {
  Modal.confirm({
    title: '确认取消订单？',
    content: '取消后订单将关闭，如已使用优惠券将自动退回',
    okText: '确认取消',
    cancelText: '我再想想',
    onOk: async () => {
      try {
        const res = await cancelOrder({ orderId, memberId })
        if (res.success) {
          message.success('订单已取消')
          loadOrders()
        } else {
          message.error(res.message || '取消失败')
        }
      } catch (e) {
        message.error('取消失败')
      }
    }
  })
}

const handleReturnApply = (orderId) => {
  router.push(`/order/return?orderId=${orderId}`)
}

const handleConfirmReceipt = (orderId) => {
  Modal.confirm({
    title: '确认收货？',
    content: '请确认您已收到商品，确认后订单将完成',
    okText: '确认收货',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await confirmReceipt(orderId, memberId)
        if (res.success) {
          message.success('已确认收货')
          loadOrders()
        } else {
          message.error(res.message || '确认收货失败')
        }
      } catch (e) {
        message.error('确认收货失败')
      }
    }
  })
}

onMounted(() => {
  loadOrders()
  cartStore.loadCartCount()
})
</script>

<style scoped>
:deep(.ant-pagination-item-active) {
  border-color: #059669;
}
:deep(.ant-pagination-item-active a) {
  color: #059669;
}
:deep(.ant-pagination-item:hover) {
  border-color: #059669;
}
:deep(.ant-pagination-item:hover a) {
  color: #059669;
}
</style>
