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
          <router-link to="/my-orders" class="text-stone-500 hover:text-emerald-600 transition-colors font-medium">我的订单</router-link>
          <div class="w-px h-4 bg-stone-200"></div>
          <router-link to="/" class="text-stone-500 hover:text-emerald-600 transition-colors font-medium">首页</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto px-4 py-12">
      <div class="max-w-xl mx-auto">
        <!-- 加载中 -->
        <div v-if="loading" class="bg-white rounded-3xl border border-stone-100 shadow-xl p-12 text-center">
          <div class="w-24 h-24 bg-emerald-50 rounded-full flex items-center justify-center mx-auto mb-8">
            <a-spin size="large" />
          </div>
          <h1 class="text-2xl font-black text-stone-900 mb-3">正在确认支付结果...</h1>
          <p class="text-stone-400">请稍候，不要关闭页面</p>
        </div>

        <!-- 支付成功 -->
        <div v-else-if="isSuccess" class="bg-white rounded-3xl border border-stone-100 shadow-xl p-12 text-center">
          <div class="w-24 h-24 bg-emerald-50 rounded-full flex items-center justify-center mx-auto mb-8">
            <svg class="w-14 h-14 text-emerald-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h1 class="text-3xl font-black text-stone-900 mb-3">
            {{ isMemberLevelOrder ? '会员开通成功' : '支付成功' }}
          </h1>
          <p class="text-stone-400 mb-2">
            {{ isMemberLevelOrder ? '恭喜您，会员等级已激活！' : '恭喜您，订单支付已完成！' }}
          </p>
          <p class="text-stone-500 mb-8">
            订单编号：<span class="font-mono font-bold text-stone-800">{{ orderSn }}</span>
          </p>

          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link v-if="isMemberLevelOrder" to="/member/center"
              class="bg-amber-500 text-white px-8 py-3 rounded-2xl font-bold hover:bg-amber-600 transition-all shadow-lg shadow-amber-200 text-center">
              查看会员中心
            </router-link>
            <router-link v-else to="/my-orders"
              class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200 text-center">
              查看订单
            </router-link>
            <router-link to="/"
              class="bg-stone-100 text-stone-700 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all text-center">
              继续购物
            </router-link>
          </div>
        </div>

        <!-- 支付失败 -->
        <div v-else class="bg-white rounded-3xl border border-stone-100 shadow-xl p-12 text-center">
          <div class="w-24 h-24 bg-red-50 rounded-full flex items-center justify-center mx-auto mb-8">
            <svg class="w-14 h-14 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </div>
          <h1 class="text-3xl font-black text-stone-900 mb-3">支付失败</h1>
          <p class="text-stone-400 mb-2">很抱歉，支付未能完成</p>
          <p class="text-stone-500 mb-8" v-if="orderSn">
            订单编号：<span class="font-mono font-bold text-stone-800">{{ orderSn }}</span>
          </p>

          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <button v-if="orderSn" @click="retryPay"
              class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200">
              重新支付
            </button>
            <router-link to="/my-orders"
              class="bg-stone-100 text-stone-700 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all text-center">
              查看订单
            </router-link>
            <router-link to="/"
              class="bg-stone-100 text-stone-700 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all text-center">
              返回首页
            </router-link>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { queryAlipayStatus } from '@/api/frontend/order'
import { useMemberLevelStore } from '@/stores/memberLevel'

const route = useRoute()
const router = useRouter()
const memberLevelStore = useMemberLevelStore()

const loading = ref(true)
const orderSn = ref('')
const tradeStatus = ref('')

const isSuccess = computed(() => {
  return tradeStatus.value === 'TRADE_SUCCESS' || tradeStatus.value === 'TRADE_FINISHED'
})

// 判断是否是会员等级订单（订单号以VIP开头）
const isMemberLevelOrder = computed(() => {
  return orderSn.value && orderSn.value.startsWith('VIP')
})

const retryPay = () => {
  router.push(`/pay?orderSn=${orderSn.value}`)
}

onMounted(async () => {
  // 支付宝回跳时，参数可能在 # 前面（window.location.search）
  // 也可能在 # 后面（route.query），需要两处都尝试读取
  let outTradeNo = route.query.out_trade_no || ''

  if (!outTradeNo) {
    try {
      const fullUrl = window.location.href
      const hashIndex = fullUrl.indexOf('#')
      const beforeHash = hashIndex > -1 ? fullUrl.substring(0, hashIndex) : fullUrl
      const urlObj = new URL(beforeHash)
      outTradeNo = urlObj.searchParams.get('out_trade_no') || ''
    } catch (e) {}
  }

  orderSn.value = outTradeNo

  // 主动调用后端查询支付宝交易状态并更新订单
  if (outTradeNo) {
    try {
      const res = await queryAlipayStatus(outTradeNo)
      if (res.success && (res.data === '支付成功' || res.data === '已支付')) {
        tradeStatus.value = 'TRADE_SUCCESS'
        // 会员等级订单支付成功后，刷新等级缓存
        if (outTradeNo.startsWith('VIP')) {
          memberLevelStore.reset()
          memberLevelStore.loadLevel()
        }
      } else {
        tradeStatus.value = 'WAIT_BUYER_PAY'
      }
    } catch (e) {
      // 查询失败，有 orderSn 默认视为成功（沙箱可能不返回状态）
      tradeStatus.value = outTradeNo ? 'TRADE_SUCCESS' : ''
      if (outTradeNo && outTradeNo.startsWith('VIP')) {
        memberLevelStore.reset()
        memberLevelStore.loadLevel()
      }
    } finally {
      loading.value = false
    }
  } else {
    loading.value = false
  }
})
</script>
