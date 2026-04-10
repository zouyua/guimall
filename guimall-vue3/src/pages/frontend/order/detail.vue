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

          <!-- 订单时间线 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6 flex items-center gap-2">
              <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
              订单进度
            </h2>
            <div class="relative">
              <!-- 时间线轴 -->
              <div class="absolute left-4 top-0 bottom-0 w-0.5 bg-stone-200"></div>
              <!-- 下单 -->
              <div class="relative flex items-start gap-4 pb-6">
                <div class="w-8 h-8 rounded-full flex items-center justify-center z-10 shrink-0 bg-emerald-500 text-white">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M5 13l4 4L19 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                </div>
                <div>
                  <p class="font-bold text-stone-800">提交订单</p>
                  <p class="text-sm text-stone-400 mt-0.5">{{ orderDetail.createTime }}</p>
                </div>
              </div>
              <!-- 支付 -->
              <div class="relative flex items-start gap-4 pb-6">
                <div :class="orderDetail.payTime ? 'bg-emerald-500 text-white' : 'bg-stone-200 text-stone-400'"
                  class="w-8 h-8 rounded-full flex items-center justify-center z-10 shrink-0">
                  <svg v-if="orderDetail.payTime" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M5 13l4 4L19 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                  <span v-else class="text-xs font-bold">2</span>
                </div>
                <div>
                  <p :class="orderDetail.payTime ? 'text-stone-800' : 'text-stone-400'" class="font-bold">付款成功</p>
                  <p v-if="orderDetail.payTime" class="text-sm text-stone-400 mt-0.5">{{ orderDetail.payTime }}</p>
                  <p v-else class="text-sm text-stone-300 mt-0.5">等待付款</p>
                </div>
              </div>
              <!-- 发货 -->
              <div class="relative flex items-start gap-4 pb-6">
                <div :class="orderDetail.deliveryTime ? 'bg-emerald-500 text-white' : 'bg-stone-200 text-stone-400'"
                  class="w-8 h-8 rounded-full flex items-center justify-center z-10 shrink-0">
                  <svg v-if="orderDetail.deliveryTime" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M5 13l4 4L19 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                  <span v-else class="text-xs font-bold">3</span>
                </div>
                <div>
                  <p :class="orderDetail.deliveryTime ? 'text-stone-800' : 'text-stone-400'" class="font-bold">商家发货</p>
                  <template v-if="orderDetail.deliveryTime">
                    <p class="text-sm text-stone-400 mt-0.5">{{ orderDetail.deliveryTime }}</p>
                    <p v-if="orderDetail.deliveryCompany || orderDetail.deliverySn" class="text-sm text-stone-500 mt-1">
                      <span v-if="orderDetail.deliveryCompany">{{ orderDetail.deliveryCompany }}</span>
                      <span v-if="orderDetail.deliverySn" class="font-mono ml-1">{{ orderDetail.deliverySn }}</span>
                    </p>
                  </template>
                  <p v-else class="text-sm text-stone-300 mt-0.5">等待发货</p>
                </div>
              </div>
              <!-- 确认收货 -->
              <div class="relative flex items-start gap-4">
                <div :class="orderDetail.receiveTime ? 'bg-emerald-500 text-white' : 'bg-stone-200 text-stone-400'"
                  class="w-8 h-8 rounded-full flex items-center justify-center z-10 shrink-0">
                  <svg v-if="orderDetail.receiveTime" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M5 13l4 4L19 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                  <span v-else class="text-xs font-bold">4</span>
                </div>
                <div>
                  <p :class="orderDetail.receiveTime ? 'text-stone-800' : 'text-stone-400'" class="font-bold">确认收货</p>
                  <p v-if="orderDetail.receiveTime" class="text-sm text-stone-400 mt-0.5">{{ orderDetail.receiveTime }}</p>
                  <p v-else class="text-sm text-stone-300 mt-0.5">等待收货</p>
                </div>
              </div>
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

          <!-- 物流信息 -->
          <div v-if="orderDetail.deliveryCompany || orderDetail.deliverySn" class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6 flex items-center gap-2">
              <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path d="M13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10a1 1 0 001 1h1m8-1a1 1 0 01-1 1H9m4-1V8a1 1 0 011-1h2.586a1 1 0 01.707.293l3.414 3.414a1 1 0 01.293.707V16a1 1 0 01-1 1h-1m-6-1a1 1 0 001 1h1M5 17a2 2 0 104 0m-4 0a2 2 0 114 0m6 0a2 2 0 104 0m-4 0a2 2 0 114 0" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
              </svg>
              物流信息
            </h2>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div><span class="text-stone-400">物流公司：</span><span class="font-bold">{{ orderDetail.deliveryCompany || '—' }}</span></div>
              <div><span class="text-stone-400">物流单号：</span><span class="font-mono font-bold">{{ orderDetail.deliverySn || '—' }}</span></div>
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
              <div v-if="orderDetail.integrationAmount && orderDetail.integrationAmount > 0" class="flex justify-between text-purple-600">
                <span>积分抵扣（{{ orderDetail.useIntegration || 0 }}积分）</span>
                <span class="font-bold">-¥{{ orderDetail.integrationAmount }}</span>
              </div>
              <div v-if="orderDetail.promotionAmount && orderDetail.promotionAmount > 0" class="flex justify-between text-amber-600">
                <span>会员折扣</span>
                <span class="font-bold">-¥{{ orderDetail.promotionAmount }}</span>
              </div>
              <div v-if="otherDiscount > 0 && !(orderDetail.couponAmount > 0) && !(orderDetail.promotionAmount > 0)" class="flex justify-between text-emerald-600">
                <span>优惠</span>
                <span class="font-bold">-¥{{ otherDiscount }}</span>
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
            <button @click="$router.push('/my-orders')" class="bg-stone-100 text-stone-600 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all">
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
            <!-- 已发货 → 确认收货 + 申请退货 -->
            <template v-else-if="orderDetail.status === 2">
              <button @click="handleConfirmReceipt"
                class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200">
                确认收货
              </button>
              <button @click="handleReturnApply"
                class="bg-orange-100 text-orange-600 px-8 py-3 rounded-2xl font-bold hover:bg-orange-200 transition-all">
                申请退货
              </button>
            </template>
            <!-- 待发货/已完成 → 申请退货 -->
            <template v-else-if="orderDetail.status === 1 || orderDetail.status === 3">
              <button @click="handleReturnApply"
                class="bg-orange-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-orange-700 transition-all shadow-lg shadow-orange-200">
                申请退货
              </button>
            </template>
          </div>
        </div>
      </a-spin>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { getOrderDetail, confirmReceipt } from '@/api/frontend/order'
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

// 计算未归类的优惠差额（总额 + 运费 - 优惠券 - 促销 - 积分抵扣 - 实付）
const otherDiscount = computed(() => {
  if (!orderDetail.value) return 0
  const total = parseFloat(orderDetail.value.totalAmount) || 0
  const freight = parseFloat(orderDetail.value.freightAmount) || 0
  const coupon = parseFloat(orderDetail.value.couponAmount) || 0
  const promotion = parseFloat(orderDetail.value.promotionAmount) || 0
  const integration = parseFloat(orderDetail.value.integrationAmount) || 0
  const pay = parseFloat(orderDetail.value.payAmount) || 0
  const diff = total + freight - coupon - promotion - integration - pay
  return diff > 0.001 ? parseFloat(diff.toFixed(2)) : 0
})

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

const handleConfirmReceipt = () => {
  Modal.confirm({
    title: '确认收货？',
    content: '请确认您已收到商品，确认后订单将完成',
    okText: '确认收货',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await confirmReceipt(orderDetail.value.id, memberId)
        if (res.success) {
          message.success('已确认收货')
          loadOrderDetail()
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
  loadOrderDetail()
})
</script>

<style scoped>
:deep(.ant-spin-container) {
  min-height: 400px;
}
</style>
