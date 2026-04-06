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
      <div class="max-w-2xl mx-auto">
        <!-- 订单信息卡片 -->
        <div class="bg-white rounded-3xl border border-stone-100 shadow-xl p-10 text-center">
          <!-- 图标 -->
          <div class="w-24 h-24 bg-emerald-50 rounded-full flex items-center justify-center mx-auto mb-8">
            <svg class="w-12 h-12 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
            </svg>
          </div>

          <h1 class="text-2xl font-black text-stone-900 mb-2">订单支付</h1>
          <p class="text-stone-400 mb-8">请在规定时间内完成支付</p>

          <!-- 订单详情 -->
          <div class="bg-stone-50 rounded-2xl p-6 mb-8">
            <div class="flex justify-between items-center mb-3">
              <span class="text-stone-500">订单编号</span>
              <span class="font-bold text-stone-800 font-mono">{{ orderSn }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-stone-500">支付金额</span>
              <span class="text-3xl font-black text-emerald-600">¥{{ payAmount }}</span>
            </div>
          </div>

          <!-- 倒计时 -->
          <div class="mb-8" v-if="countdown > 0">
            <p class="text-sm text-stone-400 mb-2">支付剩余时间</p>
            <p class="text-2xl font-black text-orange-500 font-mono">{{ formatCountdown }}</p>
          </div>
          <div class="mb-8" v-else>
            <p class="text-red-500 font-bold">订单已超时，请重新下单</p>
          </div>

          <!-- 支付按钮 -->
          <button @click="handleAlipay"
            :disabled="paying || countdown <= 0"
            class="w-full bg-blue-600 text-white py-4 rounded-2xl font-black text-lg shadow-lg hover:bg-blue-700 hover:-translate-y-0.5 transition-all disabled:opacity-60 disabled:cursor-not-allowed flex items-center justify-center space-x-3">
            <svg class="w-6 h-6" viewBox="0 0 24 24" fill="currentColor">
              <path d="M20.5 7.28V5c0-1.1-.9-2-2-2h-15c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h15c1.1 0 2-.9 2-2v-2.28c.6-.35 1-.98 1-1.72V9c0-.74-.4-1.38-1-1.72zM20 9v6h-7V9h7zM3.5 5h15v2h-8.5c-1.1 0-2 .9-2 2v6c0 1.1.9 2 2 2h8.5v2h-15V5z"/>
              <circle cx="16" cy="12" r="1.5"/>
            </svg>
            <span v-if="paying">正在跳转支付宝...</span>
            <span v-else>支付宝支付</span>
          </button>

          <!-- 等待状态提示 -->
          <div class="mt-6 p-4 bg-amber-50 border border-amber-100 rounded-2xl" v-if="paying">
            <div class="flex items-center justify-center space-x-2 text-amber-600">
              <svg class="animate-spin w-5 h-5" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span class="font-bold">等待支付中，请在支付宝页面完成付款</span>
            </div>
            <p class="text-sm text-amber-500 mt-2">支付完成后页面将自动跳转</p>
          </div>

          <p class="text-xs text-stone-400 mt-6">如遇问题请联系客服：400-888-9999</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { createAlipay, getRemainingTime } from '@/api/frontend/order'

const route = useRoute()
const router = useRouter()

const orderSn = ref(route.query.orderSn || '')
const payAmount = ref(route.query.payAmount || '0.00')
const paying = ref(false)

// 倒计时（从服务端获取剩余秒数）
const countdown = ref(0)
let timer = null

const formatCountdown = computed(() => {
  const minutes = Math.floor(countdown.value / 60)
  const seconds = countdown.value % 60
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
})

const startCountdown = () => {
  timer = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      clearInterval(timer)
    }
  }, 1000)
}

const handleAlipay = async () => {
  if (!orderSn.value) {
    message.error('订单号无效')
    return
  }
  paying.value = true
  try {
    const res = await createAlipay(orderSn.value)
    if (res.success && res.data) {
      // 支付宝返回的是HTML表单字符串，需要插入DOM并自动提交
      const div = document.createElement('div')
      div.innerHTML = res.data
      document.body.appendChild(div)
      const form = div.querySelector('form')
      if (form) {
        form.submit()
      } else {
        // 如果没有form标签，尝试在新窗口打开
        const newWin = window.open('', '_blank')
        if (newWin) {
          newWin.document.write(res.data)
          newWin.document.close()
        }
      }
    } else {
      message.error(res.message || '支付发起失败')
      paying.value = false
    }
  } catch (e) {
    message.error('支付发起失败，请重试')
    paying.value = false
  }
}

onMounted(async () => {
  if (!orderSn.value) {
    message.warning('无效的支付页面')
    router.push('/my-orders')
    return
  }
  try {
    const res = await getRemainingTime(orderSn.value)
    if (res.success && res.data > 0) {
      countdown.value = res.data
    }
  } catch (e) {
    // 请求失败时兜底使用 30 分钟
    countdown.value = 30 * 60
  }
  startCountdown()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>
