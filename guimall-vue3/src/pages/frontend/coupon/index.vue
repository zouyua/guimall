<template>
  <div class="min-h-screen bg-gradient-to-br from-stone-50 to-stone-100">
    <!-- 导航栏 -->
    <nav class="bg-white/90 backdrop-blur-md border-b border-stone-200 sticky top-0 z-50">
      <div class="max-w-screen-xl mx-auto px-4 py-3">
        <div class="flex items-center justify-between">
          <!-- Logo -->
          <router-link to="/" class="flex items-center space-x-2">
            <div class="w-8 h-8 bg-emerald-600 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
            </div>
            <span class="text-xl font-black text-emerald-900">桂Mall</span>
          </router-link>

          <!-- 主导航 -->
          <ul class="hidden md:flex items-center space-x-8 font-semibold">
            <li><router-link to="/" class="text-stone-600 hover:text-emerald-600 transition-colors">首页</router-link></li>
            <li><router-link to="/category" class="text-stone-600 hover:text-emerald-600 transition-colors">全部分类</router-link></li>
            <li><router-link to="/coupon-center" class="text-emerald-600 transition-colors">领券中心</router-link></li>
            <li><router-link to="/support" class="text-stone-600 hover:text-emerald-600 transition-colors">助农专区</router-link></li>
          </ul>

          <!-- 右侧功能区 -->
          <div class="flex items-center space-x-3">
            <!-- 已登录 -->
            <template v-if="memberLoggedIn">
              <router-link to="/user/coupons" class="text-sm text-stone-600 hover:text-emerald-600 transition-colors">我的优惠券</router-link>
              <router-link to="/cart" class="text-stone-600 hover:text-emerald-600 transition-colors">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" /></svg>
              </router-link>
              <router-link to="/member/center" class="flex items-center gap-1.5 text-sm text-stone-600 hover:text-emerald-600 transition-colors">
                <img :src="memberAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + memberNickname" class="w-6 h-6 rounded-full border border-emerald-100" />
                <span>{{ memberNickname }}</span>
              </router-link>
            </template>

            <!-- 未登录 -->
            <template v-else>
              <button @click="$router.push('/member/login')" class="text-sm text-stone-600 hover:text-emerald-600 transition-colors">登录</button>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <!-- 页面标题 -->
    <div class="max-w-screen-xl mx-auto px-4 py-12">
      <div class="text-center mb-12">
        <h1 class="text-4xl font-black text-stone-800 mb-4">领券中心</h1>
        <p class="text-stone-500">精选优惠券，领取即用，省钱购物</p>
      </div>

      <!-- 优惠券列表 -->
      <a-spin :spinning="loading">
        <div v-if="couponList.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="coupon in couponList" :key="coupon.id"
               class="bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all overflow-hidden border border-stone-200">
            <div class="flex">
              <!-- 左侧金额区域 -->
              <div class="w-32 bg-gradient-to-br from-emerald-500 to-emerald-600 text-white flex flex-col items-center justify-center p-4">
                <div class="text-3xl font-black">¥{{ coupon.amount }}</div>
                <div class="text-xs mt-1 opacity-90">
                  {{ coupon.minAmount > 0 ? `满${coupon.minAmount}可用` : '无门槛' }}
                </div>
              </div>

              <!-- 右侧信息区域 -->
              <div class="flex-1 p-4 flex flex-col justify-between">
                <div>
                  <h3 class="font-bold text-stone-800 mb-2">{{ coupon.name }}</h3>
                  <div class="text-xs text-stone-500 space-y-1">
                    <div>有效期：{{ formatDate(coupon.startTime) }} 至 {{ formatDate(coupon.endTime) }}</div>
                    <div v-if="coupon.publishCount > 0">
                      剩余：{{ coupon.remainCount }} / {{ coupon.publishCount }}
                    </div>
                    <div>每人限领：{{ coupon.perLimit }}张</div>
                  </div>
                </div>

                <div class="mt-4">
                  <a-button
                    v-if="!coupon.received"
                    type="primary"
                    size="small"
                    :loading="coupon.receiving"
                    :disabled="isOutOfStock(coupon)"
                    @click="handleReceive(coupon)"
                    class="w-full"
                  >
                    {{ isOutOfStock(coupon) ? '已抢光' : '立即领取' }}
                  </a-button>
                  <a-button v-else size="small" disabled class="w-full">
                    已领取
                  </a-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <a-empty v-else description="暂无可领取的优惠券" class="my-20" />
      </a-spin>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getAvailableCouponList, receiveCoupon } from '@/api/frontend/coupon'
import { isMemberLoggedIn, getMemberInfo, getMemberId } from '@/composables/member'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const couponList = ref([])

// 会员登录状态
const memberLoggedIn = ref(isMemberLoggedIn())
const memberNickname = ref('')
const memberAvatar = ref('')

// 初始化会员状态
const initMemberStatus = () => {
  memberLoggedIn.value = isMemberLoggedIn()
  if (memberLoggedIn.value) {
    const info = getMemberInfo()
    memberNickname.value = info?.nickname || info?.username || '会员'
    memberAvatar.value = info?.icon || ''
  }
}

// 加载可领取的优惠券列表
const loadCouponList = async () => {
  const memberId = getMemberId()
  if (!memberId) {
    message.warning('请先登录')
    router.push('/member/login')
    return
  }

  loading.value = true
  try {
    const res = await getAvailableCouponList(memberId)
    if (res.success) {
      couponList.value = (res.data || []).map(item => ({
        ...item,
        receiving: false,
        received: false
      }))
    }
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 领取优惠券
const handleReceive = async (coupon) => {
  const memberId = getMemberId()
  if (!memberId) {
    message.warning('请先登录')
    return
  }

  coupon.receiving = true
  try {
    const res = await receiveCoupon({
      couponId: coupon.id,
      memberId: memberId
    })
    if (res.success) {
      message.success('领取成功')
      coupon.received = true
      coupon.receiveCount++
    } else {
      message.error(res.message || '领取失败')
    }
  } catch (e) {
    message.error('领取失败')
  } finally {
    coupon.receiving = false
  }
}

// 判断是否已抢光
const isOutOfStock = (coupon) => {
  return coupon.remainCount !== undefined && coupon.remainCount <= 0
}

// 格式化日期
const formatDate = (dateStr) => {
  return dayjs(dateStr).format('YYYY-MM-DD')
}

onMounted(() => {
  initMemberStatus()
  loadCouponList()
})
</script>

<style scoped>
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
}
</style>
