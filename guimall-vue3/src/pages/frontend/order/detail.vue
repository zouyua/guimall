<template>
  <div class="min-h-screen bg-stone-50 font-sans">
    <!-- 导航栏 -->
    <nav class="bg-white border-b border-stone-200 p-4 sticky top-0 z-50">
      <div class="max-w-screen-xl mx-auto flex items-center justify-between">
        <router-link to="/" class="flex items-center space-x-2">
          <div class="w-8 h-8 bg-emerald-600 rounded-lg flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <span class="text-xl font-bold text-emerald-900">GUIMALL</span>
        </router-link>
        <div class="flex space-x-8 font-bold text-stone-600">
          <router-link to="/" class="hover:text-emerald-600" active-class="text-emerald-600">首页</router-link>
          <router-link to="/my-orders" class="hover:text-emerald-600" active-class="text-emerald-600">我的订单</router-link>
          <router-link to="/cart" class="hover:text-emerald-600" active-class="text-emerald-600">购物车</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto px-4 py-12">
      <a-spin :spinning="loading" size="large">
        <div v-if="orderDetail" class="space-y-6">
          <!-- 订单状态 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <div class="flex items-center justify-between">
              <div>
                <h1 class="text-2xl font-black text-stone-900 mb-2">订单详情</h1>
                <p class="text-stone-400">订单编号：<span class="font-mono font-bold text-stone-600">{{ orderDetail.orderSn }}</span></p>
              </div>
              <span :class="statusConfig[orderDetail.status]?.color || 'bg-stone-100 text-stone-600'"
                class="px-6 py-2 rounded-full text-sm font-bold">
                {{ statusConfig[orderDetail.status]?.label || '未知' }}
              </span>
            </div>
          </div>

          <!-- 收货信息 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6 flex items-center gap-2">
              <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
                <path d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
              收货信息
            </h2>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div><span class="text-stone-400">收货人：</span><span class="font-bold">{{ orderDetail.receiverName }}</span></div>
              <div><span class="text-stone-400">联系电话：</span><span class="font-bold">{{ orderDetail.receiverPhone }}</span></div>
              <div class="col-span-2"><span class="text-stone-400">收货地址：</span><span class="font-bold">{{ orderDetail.receiverDetailAddress }}</span></div>
            </div>
          </div>

          <!-- 商品信息 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6 flex items-center gap-2">
              <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
              商品信息
            </h2>
            <div class="space-y-4">
              <div v-for="item in orderDetail.items" :key="item.id"
                class="flex items-center gap-4 p-4 rounded-2xl bg-stone-50">
                <img :src="item.productPic" class="w-20 h-20 rounded-xl object-cover border border-stone-200" />
                <div class="flex-1">
                  <h3 class="font-bold text-stone-800">{{ item.productName }}</h3>
                  <p class="text-xs text-stone-400 mt-1">{{ item.productAttr || '默认规格' }}</p>
                </div>
                <div class="text-right">
                  <p class="text-emerald-600 font-bold">¥{{ item.productPrice }}</p>
                  <p class="text-xs text-stone-400 mt-1">× {{ item.productQuantity }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 费用明细 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6 flex items-center gap-2">
              <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
              费用明细
            </h2>
            <div class="space-y-3 text-sm">
              <div class="flex justify-between">
                <span class="text-stone-400">商品总额</span>
                <span class="font-bold">¥{{ orderDetail.totalAmount }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-stone-400">运费</span>
                <span class="font-bold">¥{{ orderDetail.freightAmount || 0 }}</span>
              </div>
              <div v-if="orderDetail.couponAmount && orderDetail.couponAmount > 0" class="flex justify-between text-emerald-600">
                <span>优惠券（{{ orderDetail.couponName || '优惠券' }}）</span>
                <span class="font-bold">-¥{{ orderDetail.couponAmount }}</span>
              </div>
              <div class="border-t border-stone-200 pt-3 flex justify-between items-center">
                <span class="text-stone-600 font-bold">实付金额</span>
                <span class="text-2xl font-black text-emerald-600">¥{{ orderDetail.payAmount }}</span>
              </div>
            </div>
          </div>

          <!-- 订单备注 -->
          <div v-if="orderDetail.note" class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-4">订单备注</h2>
            <p class="text-stone-600">{{ orderDetail.note }}</p>
          </div>

          <!-- 操作按钮 -->
          <div class="flex justify-center gap-4">
            <button @click="$router.back()" class="bg-stone-100 text-stone-600 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all">
              返回
            </button>
            <!-- 待付款 -->
            <template v-if="orderDetail.status === 0">
              <button @click="handleCancelOrder"
                class="bg-stone-100 text-stone-600 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all">
                取消订单
              </button>
              <button @click="$router.push(`/pay?orderSn=${orderDetail.orderSn}&payAmount=${orderDetail.payAmount}`)"
                class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200">
                去支付
              </button>
            </template>
            <!-- 已支付（待发货/已发货/已完成）-->
            <button v-else-if="orderDetail.status >= 1 && orderDetail.status <= 3" @click="handleReturnApply"
              class="bg-orange-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-orange-700 transition-all shadow-lg shadow-orange-200">
              申请退货
            </button>
          </div>
        </div>
      </a-spin>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { getOrderDetail } from '@/api/frontend/order'
import { cancelOrder } from '@/api/frontend/orderReturn'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'

const route = useRoute()
const router = useRouter()

// 登录检查
if (!isMemberLoggedIn()) {
  message.warning('请先登录')
  router.push('/member/login')
}

const memberId = getMemberId()
const orderId = route.query.id

const loading = ref(false)
const orderDetail = ref(null)

const statusConfig = {
  0: { label: '待付款', color: 'bg-orange-100 text-orange-600' },
  1: { label: '待发货', color: 'bg-blue-100 text-blue-600' },
  2: { label: '已发货', color: 'bg-purple-100 text-purple-600' },
  3: { label: '已完成', color: 'bg-emerald-100 text-emerald-600' },
  4: { label: '已关闭', color: 'bg-stone-100 text-stone-600' },
  5: { label: '无效订单', color: 'bg-red-100 text-red-600' }
}

const loadOrderDetail = async () => {
  if (!orderId) {
    message.error('订单ID不能为空')
    router.back()
    return
  }

  loading.value = true
  try {
    const res = await getOrderDetail(orderId, memberId)
    if (res.success) {
      orderDetail.value = res.data
    } else {
      message.error(res.message || '加载订单详情失败')
      router.back()
    }
  } catch (e) {
    message.error('加载订单详情失败')
    router.back()
  } finally {
    loading.value = false
  }
}

const handleCancelOrder = () => {
  Modal.confirm({
    title: '确认取消订单？',
    content: '取消后订单将关闭，如已使用优惠券将自动退回',
    okText: '确认取消',
    cancelText: '我再想想',
    onOk: async () => {
      try {
        const res = await cancelOrder({ orderId: orderDetail.value.id, memberId })
        if (res.success) {
          message.success('订单已取消')
          router.push('/my-orders')
        } else {
          message.error(res.message || '取消失败')
        }
      } catch (e) {
        message.error('取消失败')
      }
    }
  })
}

const handleReturnApply = () => {
  router.push(`/order/return?orderId=${orderDetail.value.id}`)
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped>
:deep(.ant-spin-container) {
  min-height: 400px;
}
</style>
