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
            <button @click="toggleManualInput"
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
              <div class="col-span-3">
                <label class="block text-sm font-bold text-stone-500 mb-2">省/市/区</label>
                <a-cascader
                  v-model:value="selectedRegion"
                  :options="regionData"
                  placeholder="请选择省/市/区"
                  @change="handleRegionChange"
                  :show-search="{ filter }"
                  size="large"
                  class="w-full !rounded-xl"
                  :field-names="{ label: 'label', value: 'value', children: 'children' }"
                />
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
              <div v-for="coupon in availableCoupons" :key="coupon.couponId || coupon.id"
                @click="toggleCoupon(coupon)"
                :class="selectedCouponId === (coupon.couponId || coupon.id)
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
                <div :class="selectedCouponId === (coupon.couponId || coupon.id) ? 'bg-orange-500' : 'bg-stone-200'"
                  class="w-5 h-5 rounded-full flex items-center justify-center transition-colors shrink-0">
                  <svg v-if="selectedCouponId === (coupon.couponId || coupon.id)" class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"/>
                  </svg>
                </div>
              </div>
            </div>
          </div>

          <!-- 积分抵扣 -->
          <div v-if="memberIntegration > 0" class="bg-white rounded-3xl border border-stone-100 shadow-sm p-8">
            <h2 class="text-xl font-black text-stone-900 mb-6 flex items-center">
              <span class="w-8 h-8 bg-purple-100 rounded-xl flex items-center justify-center mr-3">
                <svg class="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </span>
              积分抵扣
              <span class="ml-2 text-sm font-normal text-purple-500">（当前{{ memberIntegration }}积分）</span>
            </h2>

            <div class="flex items-center justify-between">
              <div class="flex items-center gap-3">
                <a-switch
                  :checked="integrationEnabled"
                  @change="toggleIntegration"
                  class="!bg-stone-300"
                  :class="{ '!bg-purple-500': integrationEnabled }"
                />
                <span class="text-stone-600">
                  使用积分抵扣
                  <span class="text-xs text-stone-400 ml-1">（{{ INTEGRATION_TO_YUAN_RATIO }}积分 = 1元）</span>
                </span>
              </div>
              <div v-if="integrationEnabled" class="flex items-center gap-3">
                <a-input-number
                  :value="useIntegration"
                  @change="onIntegrationChange"
                  :min="0"
                  :max="maxIntegration"
                  :step="100"
                  size="small"
                  class="!w-28"
                />
                <span class="text-purple-600 font-bold text-sm">
                  抵扣 ¥{{ integrationDiscount.toFixed(2) }}
                </span>
              </div>
            </div>

            <div v-if="maxIntegration === 0 && memberIntegration > 0" class="mt-3 text-xs text-stone-400">
              当前订单金额不足以使用积分抵扣
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
              <div v-if="integrationDiscount > 0" class="flex justify-between text-purple-500">
                <span>积分抵扣（{{ useIntegration }}积分）</span>
                <span class="font-bold">-¥{{ integrationDiscount.toFixed(2) }}</span>
              </div>
              <div v-if="memberLevelDiscount > 0" class="flex justify-between text-amber-600">
                <span>会员折扣（{{ memberLevel?.name }}）</span>
                <span class="font-bold">-¥{{ memberLevelDiscount.toFixed(2) }}</span>
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
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { submitOrder } from '@/api/frontend/order'
import { getAddressList, getMemberInfoApi, getCurrentMemberLevel } from '@/api/frontend/member'
import { getOrderAvailableCoupons } from '@/api/frontend/coupon'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'
import regionData from '@/utils/regionData'

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

// 积分抵扣
const memberIntegration = ref(0) // 会员当前积分余额
const useIntegration = ref(0)    // 本单使用积分数
const integrationEnabled = ref(false) // 是否启用积分抵扣
const INTEGRATION_TO_YUAN_RATIO = 100 // 100积分 = 1元

// 会员等级折扣
const memberLevel = ref(null) // 当前会员等级信息
const memberLevelDiscount = computed(() => {
  if (!memberLevel.value || !memberLevel.value.discount || memberLevel.value.discount >= 100) return 0
  const total = parseFloat(totalAmount.value) || 0
  return parseFloat((total * (100 - memberLevel.value.discount) / 100).toFixed(2))
})

const integrationDiscount = computed(() => {
  return useIntegration.value / INTEGRATION_TO_YUAN_RATIO
})

// 积分可抵扣的最大金额（不超过 商品总额 - 优惠券抵扣 - 会员折扣，且不超过会员积分余额对应金额）
const maxIntegration = computed(() => {
  const total = parseFloat(totalAmount.value) || 0
  const discount = couponDiscount.value || 0
  const levelDiscount = memberLevelDiscount.value || 0
  const remaining = total - discount - levelDiscount
  if (remaining <= 0.01) return 0
  // 最多可抵扣到只剩0.01元
  const maxDeductYuan = remaining - 0.01
  const maxByBalance = memberIntegration.value
  const maxByAmount = Math.floor(maxDeductYuan * INTEGRATION_TO_YUAN_RATIO)
  return Math.min(maxByBalance, maxByAmount)
})

const toggleIntegration = (checked) => {
  integrationEnabled.value = checked
  if (checked) {
    // 默认使用全部可用积分
    useIntegration.value = maxIntegration.value
  } else {
    useIntegration.value = 0
  }
}

const onIntegrationChange = (val) => {
  const v = parseInt(val) || 0
  if (v < 0) {
    useIntegration.value = 0
  } else if (v > maxIntegration.value) {
    useIntegration.value = maxIntegration.value
  } else {
    useIntegration.value = v
  }
}

const loadMemberIntegration = async () => {
  if (!memberId) return
  try {
    const res = await getMemberInfoApi(memberId)
    if (res.success && res.data) {
      memberIntegration.value = res.data.integration || 0
    }
  } catch (e) {}
}

const loadMemberLevel = async () => {
  if (!memberId) return
  try {
    const res = await getCurrentMemberLevel(memberId)
    if (res.success && res.data) {
      memberLevel.value = res.data
    }
  } catch (e) {}
}

// 优惠券变化时，重新校正积分使用量
watch(couponDiscount, () => {
  if (integrationEnabled.value) {
    if (useIntegration.value > maxIntegration.value) {
      useIntegration.value = maxIntegration.value
    }
  }
})

const toggleCoupon = (coupon) => {
  console.log('toggleCoupon 被调用, coupon:', coupon)
  const couponId = coupon.couponId || coupon.id
  if (selectedCouponId.value === couponId) {
    // 取消选择
    selectedCouponId.value = null
    couponDiscount.value = 0
    console.log('取消选择优惠券')
  } else {
    selectedCouponId.value = couponId
    couponDiscount.value = parseFloat(coupon.amount) || 0
    console.log('选择优惠券, ID:', selectedCouponId.value, '折扣:', couponDiscount.value)
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

const selectedRegion = ref([])

// 级联选择器搜索过滤
const filter = (inputValue, path) => {
  return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1)
}

// 处理省市区选择变化
const handleRegionChange = (value) => {
  if (value && value.length === 3) {
    address.value.receiverProvince = value[0]
    address.value.receiverCity = value[1]
    address.value.receiverRegion = value[2]
  }
}

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
  // 同步级联选择器的值
  selectedRegion.value = [addr.province, addr.city, addr.region]
}

// 切换手动输入
const toggleManualInput = () => {
  showManualInput.value = !showManualInput.value
  if (!showManualInput.value) {
    // 收起时，如果有已保存地址，自动选中第一个或默认地址
    if (savedAddresses.value.length > 0) {
      const defaultAddr = savedAddresses.value.find(a => a.isDefault === 1)
      if (defaultAddr) {
        selectAddress(defaultAddr)
      } else {
        selectAddress(savedAddresses.value[0])
      }
    }
  } else {
    // 展开时，清空选中的地址ID
    selectedAddressId.value = null
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

// 计算商品总金额（所有商品单价*数量之和，保留两位小数）
const totalAmount = computed(() => {
  return orderItems.value
    .reduce((sum, item) => sum + item.price * item.quantity, 0)
    .toFixed(2)
})

//计算应付金额
const payAmount = computed(() => {
  const total = parseFloat(totalAmount.value) || 0
  const discount = couponDiscount.value || 0
  const intDiscount = integrationDiscount.value || 0
  const levelDiscount = memberLevelDiscount.value || 0
  const result = total - discount - intDiscount - levelDiscount
  // 确保最低支付金额为0.01元
  return result > 0.01 ? result.toFixed(2) : '0.01'
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

  console.log('=== 提交订单前状态检查 ===')
  console.log('selectedCouponId.value:', selectedCouponId.value)
  console.log('couponDiscount.value:', couponDiscount.value)
  console.log('totalAmount:', totalAmount.value)
  console.log('payAmount:', payAmount.value)

  submitting.value = true
  try {
    // 判断是否从购物车结算（如果商品有 id 字段，说明是购物车项）
    const isFromCart = orderItems.value.some(item => item.id)

    const data = {
      memberId,
      ...address.value,
      items: orderItems.value.map(item => ({
        productId: item.productId,
        productSkuId: item.productSkuId,
        productName: item.productName,
        productPic: item.productPic,
        productAttr: item.productAttr,
        price: item.price,
        quantity: item.quantity,
        cartItemId: item.id // 购物车项ID，直接购买时为 undefined
      })),
      totalAmount: parseFloat(totalAmount.value),
      freightAmount: 0,
      payAmount: parseFloat(payAmount.value),
      fromCart: isFromCart // 根据是否有购物车ID判断
    }

    // 只有选择了优惠券才添加 couponId
    if (selectedCouponId.value) {
      data.couponId = selectedCouponId.value
      console.log('已选择优惠券ID:', selectedCouponId.value)
    } else {
      console.log('未选择优惠券')
      // 如果没有选择优惠券，但有折扣，说明数据不一致，重置折扣
      if (couponDiscount.value > 0) {
        console.warn('检测到优惠券折扣但未选择优惠券ID，重置折扣')
        couponDiscount.value = 0
        message.warning('优惠券状态异常，已重置，请重新选择')
        return
      }
    }

    // 积分抵扣
    if (useIntegration.value > 0) {
      data.useIntegration = useIntegration.value
      console.log('使用积分:', useIntegration.value, '抵扣金额:', integrationDiscount.value)
    }

    console.log('提交订单数据:', JSON.stringify(data, null, 2))
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
  // 加载会员积分余额
  loadMemberIntegration()
  // 加载会员等级信息
  loadMemberLevel()

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
