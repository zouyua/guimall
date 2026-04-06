<template>
  <div class="min-h-screen bg-stone-50">
    <nav class="bg-white border-b border-stone-200 p-4 sticky top-0 z-50">
      <div class="max-w-screen-xl mx-auto flex items-center justify-between">
        <router-link to="/" class="flex items-center space-x-2">
          <div class="w-8 h-8 bg-emerald-600 rounded-lg flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
          </div>
          <span class="text-xl font-bold text-emerald-900">GUIMALL</span>
        </router-link>
        <div class="flex space-x-8 font-bold text-stone-600">
          <router-link to="/" class="hover:text-emerald-600" active-class="text-emerald-600">首页</router-link>
          <router-link to="/category" class="hover:text-emerald-600" active-class="text-emerald-600">商品分类</router-link>
          <router-link to="/coupon-center" class="hover:text-emerald-600" active-class="text-emerald-600">领券中心</router-link>
          <router-link to="/support" class="hover:text-emerald-600" active-class="text-emerald-600">助农专区</router-link>
          <router-link to="/about" class="hover:text-emerald-600" active-class="text-emerald-600">关于我们</router-link>
        </div>
        <div class="flex items-center space-x-4">
          <!-- 已登录 -->
          <template v-if="memberLoggedIn">
            <router-link to="/cart" class="text-stone-600 hover:text-emerald-600 transition-colors">
              <a-badge :count="cartStore.cartCount" :offset="[-2, 2]" size="small">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" /></svg>
              </a-badge>
            </router-link>
            <router-link to="/member/center" class="flex items-center gap-1.5 text-sm text-stone-600 hover:text-emerald-600 transition-colors">
              <img :src="memberAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + memberNickname" class="w-6 h-6 rounded-full border border-emerald-100" />
              <span>{{ memberNickname }}</span>
            </router-link>
            <button @click="handleLogout" class="text-sm text-stone-500 hover:text-red-500 transition-colors">退出</button>
          </template>
          <!-- 未登录 -->
          <template v-else>
            <button @click="$router.push('/member/login')" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">登录</button>
          </template>
          <button @click="$router.push('/admin')" class="bg-emerald-600 text-white px-6 py-2 rounded-full font-bold hover:bg-emerald-700 transition-all shadow-md">管理端</button>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto py-12 px-4">
      <div class="bg-white rounded-[2rem] p-12 shadow-sm border border-stone-100 min-h-[60vh] flex flex-col items-center justify-center text-center">
        <div class="w-24 h-24 bg-emerald-50 rounded-full flex items-center justify-center mb-8">
           <slot name="icon">
             <svg class="w-12 h-12 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 002-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
           </slot>
        </div>
        <h1 class="text-4xl font-black text-stone-900 mb-4">{{ title }}</h1>
        <p class="text-stone-400 max-w-md mx-auto mb-8">{{ description }}</p>
        <button @click="$router.push('/')" class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold shadow-lg shadow-emerald-200">回到首页</button>
      </div>
    </main>

    <footer class="py-12 text-center text-stone-400 text-sm">
      &copy; 2026 GUIMALL 桂林助农电商平台 · 振兴乡村 诚信溯源
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { isMemberLoggedIn, getMemberInfo, removeMemberInfo } from '@/composables/member'
import { removeMemberToken } from '@/composables/cookie'
import { useCartStore } from '@/stores/cart'

defineProps({
  title: String,
  description: String
})

const router = useRouter()
const cartStore = useCartStore()

const memberLoggedIn = ref(isMemberLoggedIn())
const memberNickname = ref('')
const memberAvatar = ref('')

const initMemberStatus = () => {
  memberLoggedIn.value = isMemberLoggedIn()
  if (memberLoggedIn.value) {
    const info = getMemberInfo()
    memberNickname.value = info?.nickname || info?.username || '会员'
    memberAvatar.value = info?.icon || ''
    cartStore.loadCartCount()
  }
}

const handleLogout = () => {
  removeMemberInfo()
  removeMemberToken()
  memberLoggedIn.value = false
  memberNickname.value = ''
  memberAvatar.value = ''
  cartStore.reset()
  router.push('/')
}

onMounted(() => {
  initMemberStatus()
})
</script>
