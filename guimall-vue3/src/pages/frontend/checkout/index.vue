<template>
  <div class="min-h-screen bg-stone-50 font-sans pb-20">
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
          <button @click="$router.back()" class="text-stone-500 flex items-center hover:text-emerald-600 transition-colors">
            <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M10 19l-7-7m0 0l7-7m-7 7h18" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
            返回
          </button>
          <div class="w-px h-4 bg-stone-200"></div>
          <router-link to="/" class="text-stone-500 hover:text-emerald-600 transition-colors">首页</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto px-4 py-12">
      <!-- 页面标题 -->
      <div class="mb-10">
        <h1 class="text-3xl font-black text-stone-900 mb-2">确认订单</h1>
        <div class="w-16 h-1.5 bg-emerald-500 rounded-full"></div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- 左侧：收货地址 + 商品列表 -->
        <div class="lg:col-span-2 space-y-8">
          <!-- 收货地址 -->
          <div class="bg-white rounded-3xl border border-stone-100 shadow-sm p-8">
            <h2 class="text-xl font-black text-stone-900 mb-6 flex items-center">
              <span class="w-8 h-8 bg-emerald-100 rounded-xl flex items-center justify-center mr-3">
                <svg class="w-4 h-4 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </span>
              收货信息
            </h2>

            <!-- 已有地址列表 -->
            <div v-if="savedAddresses.length > 0" class="space-y-3 mb-6">
              <div v-for="addr in savedAddresses" :key="addr.id"
                @click="selectAddress(addr)"
                :class="selectedAddressId === addr.id ? 'border-emerald-500 bg-emerald-50/50 ring-1 ring-emerald-200' : 'border-stone-200 hover:border-emerald-300'"
                class="border rounded-2xl p-4 cursor-pointer transition-all">
                <div class="flex items-center justify-between">
                  <div class="flex items-center gap-3">
                    <div :class="selectedAddressId === addr.id ? 'bg-emerald-600' : 'bg-stone-200'"
                      class="w-5 h-5 rounded-full flex items-center justify-center transition-colors">
                      <svg v-if="selectedAddressId === addr.id" class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"/>
                      </svg>
                    </div>
                    <div>
                      <span class="font-bold text-stone-800">{{ addr.name }}</span>
                      <span class="text-stone-500 ml-3">{{ addr.phone }}</span>
                      <span v-if="addr.isDefault" class="ml-2 bg-emerald-100 text-emerald-700 text-xs font-bold px-2 py-0.5 rounded-full">默认</span>
                    </div>
                  </div>
                </div>
                <p class="text-stone-500 text-sm mt-2 ml-8">{{ addr.province }}{{ addr.city }}{{ addr.region }} {{ addr.detailAddress }}</p>
              </div>
            </div>

            <!-- 使用新地址按钮 -->
            <button @click="showManualInput = !showManualInput"
              class="text-emerald-600 hover:text-emerald-700 font-bold text-sm mb-4 flex items-center gap-1">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/></svg>
              {{ showManualInput ? '收起手动输入' : '使用新地址' }}
            </button>

            <!-- 手动输入地址表单 -->
            <div v-show="showManualInput || savedAddresses.length === 0" class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">收货人</label>
                <a-input v-model:value="address.receiverName" placeholder="请输入收货人姓名"
                  size="large" class="!rounded-xl" />
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">联系电话</label>
                <a-input v-model:value="address.receiverPhone" placeholder="请输入手机号码"
                  size="large" class="!rounded-xl" />
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">省份</label>
                <a-input v-model:value="address.receiverProvince" placeholder="请输入省份"
                  size="large" class="!rounded-xl" />
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">城市</label>
                <a-input v-model:value="address.receiverCity" placeholder="请输入城市"
                  size="large" class="!rounded-xl" />
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">区/县</label>
                <a-input v-model:value="address.receiverRegion" placeholder="请输入区/县"
                  size="large" class="!rounded-xl" />
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-500 mb-2">详细地址</label>
                <a-input v-model:value="address.receiverDetailAddress" placeholder="请输入详细地址"
                  size="large" class="!rounded-xl" />
              </div>
            </div>
          </div>

          <!-- 商品列表 -->
          <div class="bg-white rounded-3xl border border-stone-100 shadow-sm p-8">
            <h2 class="text-xl font-black text-stone-900 mb-6 flex items-center">
              <span class="w-8 h-8 bg-emerald-100 rounded-xl flex items-center justify-center mr-3">
                <svg class="w-4 h-4 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
              </span>
              商品清单
            </h2>
            <div class="divide-y divide-stone-100">
              <div v-for="item in orderItems" :key="item.id"
                class="flex items-center space-x-4 py-5 first:pt-0 last:pb-0">
                <img :src="item.productPic" class="w-20 h-20 rounded-2xl object-cover border border-stone-100 flex-shrink-0" />
                <div class="flex-1 min-w-0">
                  <h4 class="font-bold text-stone-800 truncate">{{ item.productName }}</h4>
                  <p class="text-sm text-stone-400 mt-1">{{ item.productAttr || '默认规格' }}</p>
                </div>
                <div class="text-right flex-shrink-0">
                  <p class="text-emerald-600 font-black">¥{{ item.price }}</p>
                  <p class="text-sm text-stone-400">× {{ item.quantity }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 优惠券选择 -->
          <div class="bg-white rounded-3xl border border-stone-100 shadow-sm p-8">
            <h2 class="text-xl font-black text-stone-900 mb-6 flex items-center">
              <span class="w-8 h-8 bg-orange-100 rounded-xl flex items-center justify-center mr-3">
                <svg class="w-4 h-4 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
                </svg>
              </span>
              优惠券
              <span v-if="availableCoupons.length > 0" class="ml-2 text-sm font-normal text-orange-500">（{{ availableCoupons.length }}张可用）</span>
            </h2>

            <div v-if="availableCoupons.length === 0" class="text-stone-400 text-sm py-4">
              暂无可用优惠券
            </div>
            <div v-else class="space-y-3">
              <div v-for="coupon in availableCoupons" :key="coupon.id"
                @click="toggleCoupon(coupon)"
                :class="selectedCouponId === coupon.id
                  ? 'border-orange-400 bg-orange-50/60 ring-1 ring-orange-200'
                  : 'border-stone-200 hover:border-orange-300'"
                class="border rounded-2xl p-4 cursor-pointer transition-all flex items-center justify-between">
                <div class="flex items-center gap-4">
                  <div class="bg-orange-500 text-white rounded-xl px-4 py-2 text-center min-w-[80px]">
                    <span class="text-2xl font-black">¥{{ coupon.amount }}</span>
                  </div>
                  <div>
                    <p class="font-bold text-stone-800">{{ coupon.name }}</p>
                    <p class="text-xs text-stone-400 mt-1">满{{ coupon.minAmount }}元可用 · {{ coupon.note || '' }}</p>
                    <p class="text-xs text-stone-300 mt-0.5">{{ String(coupon.startTime || '').slice(0, 10) }} ~ {{ String(coupon.endTime || '').slice(0, 10) }}</p>
                  </div>
                </div>
                <div :class="selectedCouponId === coupon.id ? 'bg-orange-500' : 'bg-stone-200'"
                  class="w-5 h-5 rounded-full flex items-center justify-center transition-colors shrink-0">
                  <svg v-if="selectedCouponId === coupon.id" class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"/>
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：价格汇总 -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-3xl border border-stone-100 shadow-sm p-8 sticky top-24">
            <h2 class="text-xl font-black text-stone-900 mb-6">订单摘要</h2>

            <div class="space-y-4 mb-8">
              <div class="flex justify-between text-stone-500">
                <span>商品总额</span>
                <span class="font-bold text-stone-800">¥{{ totalAmount }}</span>
              </div>
              <div class="flex justify-between text-stone-500">
                <span>运费</span>
                <span class="font-bold text-emerald-600">免运费</span>
              </div>
              <div v-if="couponDiscount > 0" class="flex justify-between text-orange-500">
                <span>优惠券抵扣</span>
                <span class="font-bold">-¥{{ couponDiscount.toFixed(2) }}</span>
              </div>
              <div class="border-t border-stone-100 pt-4 flex justify-between">
                <span class="font-bold text-stone-800 text-lg">应付金额</span>
                <span class="text-2xl font-black text-emerald-600">¥{{ payAmount }}</span>
              </div>
            </div>

            <button @click="handleSubmit"
              :disabled="submitting"
              class="w-full bg-emerald-600 text-white py-4 rounded-2xl font-black text-lg shadow-lg shadow-emerald-200 hover:bg-emerald-700 hover:-translate-y-0.5 transition-all disabled:opacity-60 disabled:cursor-not-allowed">
              <span v-if="submitting" class="flex items-center justify-center">
                <svg class="animate-spin w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                提交中...
              </span>
              <span v-else>提交订单</span>
            </button>

            <p class="text-xs text-stone-400 text-center mt-4">提交订单即表示同意《购买协议》</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { submitOrder } from '@/api/frontend/order'
import { getAddressList } from '@/api/frontend/member'
import { getOrderAvailableCoupons } from '@/api/frontend/coupon'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'

const router = useRouter()

// 登录检查
if (!isMemberLoggedIn()) {
  message.warning('请先登录')
  router.push('/member/login?redirect=/checkout')
}

const memberId = getMemberId()

const submitting = ref(false)
const orderItems = ref([])
const savedAddresses = ref([])
const selectedAddressId = ref(null)
const showManualInput = ref(false)

// 优惠券
const availableCoupons = ref([])
const selectedCouponId = ref(null)
const couponDiscount = ref(0)

const toggleCoupon = (coupon) => {
  if (selectedCouponId.value === coupon.id) {
    // 取消选择
    selectedCouponId.value = null
    couponDiscount.value = 0
  } else {
    selectedCouponId.value = coupon.id
    couponDiscount.value = parseFloat(coupon.amount) || 0
  }
}

const loadAvailableCoupons = async () => {
  if (!memberId) return
  try {
    const res = await getOrderAvailableCoupons(memberId, totalAmount.value)
    if (res.success && res.data) {
      availableCoupons.value = res.data
    }
  } catch (e) {}
}

const address = ref({
  receiverName: '',
  receiverPhone: '',
  receiverProvince: '',
  receiverCity: '',
  receiverRegion: '',
  receiverDetailAddress: ''
})

// 选择已有地址
const selectAddress = (addr) => {
  selectedAddressId.value = addr.id
  showManualInput.value = false
  address.value = {
    receiverName: addr.name,
    receiverPhone: addr.phone,
    receiverProvince: addr.province,
    receiverCity: addr.city,
    receiverRegion: addr.region,
    receiverDetailAddress: addr.detailAddress
  }
}

// 加载已有地址列表
const loadAddresses = async () => {
  if (!memberId) return
  try {
    const res = await getAddressList(memberId)
    if (res.success && res.data) {
      savedAddresses.value = res.data
      // 自动选中默认地址
      const defaultAddr = res.data.find(a => a.isDefault === 1)
      if (defaultAddr) {
        selectAddress(defaultAddr)
      } else if (res.data.length > 0) {
        selectAddress(res.data[0])
      }
    }
  } catch (e) {}
}

const totalAmount = computed(() => {
  return orderItems.value
    .reduce((sum, item) => sum + item.price * item.quantity, 0)
    .toFixed(2)
})

const payAmount = computed(() => {
  const total = parseFloat(totalAmount.value) || 0
  const discount = couponDiscount.value || 0
  const result = total - discount
  return (result > 0 ? result : 0).toFixed(2)
})

const validateAddress = () => {
  const { receiverName, receiverPhone, receiverProvince, receiverCity, receiverRegion, receiverDetailAddress } = address.value
  if (!receiverName) { message.warning('请输入收货人姓名'); return false }
  if (!receiverPhone) { message.warning('请输入联系电话'); return false }
  if (!receiverProvince) { message.warning('请输入省份'); return false }
  if (!receiverCity) { message.warning('请输入城市'); return false }
  if (!receiverRegion) { message.warning('请输入区/县'); return false }
  if (!receiverDetailAddress) { message.warning('请输入详细地址'); return false }
  return true
}

const handleSubmit = async () => {
  if (!validateAddress()) return
  if (orderItems.value.length === 0) {
    message.warning('没有可结算的商品')
    return
  }

  submitting.value = true
  try {
    const data = {
      memberId,
      couponId: selectedCouponId.value,
      ...address.value,
      items: orderItems.value.map(item => ({
        productId: item.productId,
        productSkuId: item.productSkuId,
        productName: item.productName,
        productPic: item.productPic,
        productAttr: item.productAttr,
        price: item.price,
        quantity: item.quantity
      })),
      totalAmount: parseFloat(totalAmount.value),
      freightAmount: 0,
      payAmount: parseFloat(totalAmount.value)
    }

    const res = await submitOrder(data)
    if (res.success) {
      message.success('订单提交成功')
      // 清除sessionStorage中的结算数据
      sessionStorage.removeItem('checkoutItems')
      router.push(`/pay?orderSn=${res.data.orderSn}&payAmount=${res.data.payAmount}`)
    } else {
      message.error(res.message || '订单提交失败')
    }
  } catch (e) {
    message.error('订单提交失败，请重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  // 加载已保存的收货地址
  loadAddresses()

  // 从sessionStorage获取结算商品
  const stored = sessionStorage.getItem('checkoutItems')
  if (stored) {
    try {
      orderItems.value = JSON.parse(stored)
      // 商品加载完后，加载可用优惠券
      loadAvailableCoupons()
    } catch (e) {
      message.error('结算数据异常，请重新选择')
      router.push('/cart')
    }
  } else {
    message.warning('请先选择要结算的商品')
    router.push('/cart')
  }
})
</script>

<style scoped>
:deep(.ant-input) {
  border-radius: 0.75rem;
}
:deep(.ant-input:focus),
:deep(.ant-input-focused) {
  border-color: #059669;
  box-shadow: 0 0 0 2px rgba(5, 150, 105, 0.1);
}
:deep(.ant-input:hover) {
  border-color: #059669;
}
</style>
