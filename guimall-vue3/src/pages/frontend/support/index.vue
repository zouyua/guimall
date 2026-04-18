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
          <router-link to="/category" class="hover:text-emerald-600" active-class="text-emerald-600">商品分类</router-link>
          <router-link to="/coupon-center" class="hover:text-emerald-600" active-class="text-emerald-600">领券中心</router-link>
          <router-link to="/support" class="hover:text-emerald-600" active-class="text-emerald-600">助农专区</router-link>
          <router-link to="/about" class="hover:text-emerald-600" active-class="text-emerald-600">关于我们</router-link>
        </div>
        <div class="flex items-center space-x-4">
          <input type="text" v-model="keyword" @keyup.enter="handleSearch"
            class="w-48 p-2 pl-4 text-sm text-stone-900 border border-stone-200 rounded-full bg-stone-100 focus:ring-emerald-500 focus:border-emerald-500 transition-all"
            placeholder="搜索助农农产品...">
          <!-- 已登录 -->
          <template v-if="memberLoggedIn">
            <router-link to="/cart" class="text-stone-600 hover:text-emerald-600 transition-colors">
              <a-badge :count="cartStore.cartCount" :offset="[-2, 2]" size="small">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" /></svg>
              </a-badge>
            </router-link>
            <MemberBadge :nickname="memberNickname" :avatar="memberAvatar" />
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

    <!-- Hero 区 -->
    <header class="relative bg-emerald-900 text-white overflow-hidden">
      <div class="absolute inset-0">
        <img src="https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1600&q=80"
             class="w-full h-full object-cover opacity-20" />
        <div class="absolute inset-0 bg-gradient-to-r from-emerald-900 via-emerald-900/90 to-transparent"></div>
      </div>
      <div class="relative max-w-screen-xl mx-auto px-4 py-24">
        <div class="max-w-2xl">
          <span class="inline-block bg-emerald-500/30 border border-emerald-400/50 text-emerald-300 text-sm font-bold px-4 py-1.5 rounded-full mb-6 uppercase tracking-widest">Agriculture Aid Zone</span>
          <h1 class="text-6xl font-black mb-6 leading-tight">桂林助农<br/><span class="text-emerald-400">· 诚信溯源</span></h1>
          <p class="text-xl text-emerald-100/70 leading-relaxed mb-10">连接桂林深山农户与千家万户餐桌。每一笔购买，都是对一位农户最直接的支持。平台承诺全程区块链溯源，让爱心消费看得见、信得过。</p>
          <div class="flex gap-4">
            <button @click="scrollToProducts" class="bg-emerald-500 hover:bg-emerald-400 text-white px-8 py-3 rounded-2xl font-bold transition-all shadow-lg shadow-emerald-900/50">选购助农好物</button>
            <button @click="scrollToFarmers" class="border border-emerald-400/50 text-emerald-300 hover:bg-emerald-800 px-8 py-3 rounded-2xl font-bold transition-all">认识农户朋友</button>
          </div>
        </div>
      </div>
    </header>

    <!-- 帮扶数据统计 -->
    <section class="bg-white border-b border-stone-100">
      <div class="max-w-screen-xl mx-auto px-4 py-12 grid grid-cols-2 md:grid-cols-4 gap-8">
        <div v-for="stat in stats" :key="stat.label" class="text-center">
          <p class="text-4xl font-black text-emerald-600 mb-2">{{ stat.value }}</p>
          <p class="text-stone-500 font-bold">{{ stat.label }}</p>
        </div>
      </div>
    </section>

    <!-- 签约农户 -->
    <section id="farmers" class="max-w-screen-xl mx-auto px-4 mt-20">
      <div class="flex items-end justify-between mb-10">
        <div>
          <h2 class="text-4xl font-black text-stone-900 mb-2">签约帮扶农户</h2>
          <div class="w-16 h-2 bg-emerald-500 rounded-full"></div>
        </div>
        <span class="text-stone-400 font-bold text-sm uppercase tracking-widest">Our Farmers</span>
      </div>

      <div v-if="farmers.length" ref="farmerScrollRef"
           class="flex gap-6 overflow-x-auto pb-4 scroll-smooth"
           style="cursor:grab; scrollbar-width:thin; scrollbar-color:rgba(0,0,0,0.1) transparent;">
        <div v-for="farmer in farmers" :key="farmer.id"
             class="flex-none w-[280px] bg-white rounded-[2.5rem] p-8 border border-stone-100 shadow-sm hover:shadow-xl transition-all duration-500">
          <div class="flex items-start gap-4 mb-6">
            <img :src="farmer.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + farmer.name"
                 class="w-16 h-16 rounded-2xl border-2 border-emerald-100 object-cover" />
            <div class="min-w-0 min-h-[52px]">
              <h3 class="text-xl font-black text-stone-900">{{ farmer.name }}</h3>
              <p class="text-stone-400 text-sm leading-5 min-h-[40px] line-clamp-2">{{ farmer.city }}{{ farmer.region }}</p>
            </div>
          </div>
          <p class="text-sm text-stone-500 mb-4 line-clamp-2">{{ farmer.farmName }}</p>
          <div class="flex flex-wrap gap-2 mb-4">
            <span v-for="tag in (farmer.certType || '').split(',').filter(t => t)" :key="tag"
                  class="text-xs font-bold bg-emerald-50 text-emerald-700 px-3 py-1 rounded-full">
              {{ tag }}
            </span>
            <span v-if="!farmer.certType" class="text-xs font-bold bg-stone-50 text-stone-400 px-3 py-1 rounded-full">签约农户</span>
          </div>
          <p class="text-xs text-stone-400 font-bold">主营：{{ farmer.mainProduct || '暂无信息' }}</p>
        </div>
      </div>
      <div v-else class="py-12 text-center text-stone-300 font-bold">暂无签约农户信息</div>
    </section>

    <!-- 助农商品列表 -->
    <section id="products" class="max-w-screen-xl mx-auto px-4 mt-20 pb-24">
      <div class="flex items-end justify-between mb-10">
        <div>
          <h2 class="text-4xl font-black text-stone-900 mb-2">助农好物精选</h2>
          <div class="w-16 h-2 bg-emerald-500 rounded-full"></div>
        </div>
        <div class="flex gap-2">
          <button v-for="(f, idx) in ['综合', '销量', '价格']" :key="f"
            @click="sortType = idx; loadProducts()"
            :class="sortType === idx ? 'bg-emerald-600 text-white' : 'bg-white text-stone-600 border border-stone-200'"
            class="px-5 py-2 rounded-full text-sm font-bold transition-all hover:border-emerald-500">{{ f }}</button>
        </div>
      </div>

      <div class="relative min-h-[400px] bg-white rounded-[3rem] overflow-hidden">
        <a-spin :spinning="loading" class="support-spin" size="large">
          <div class="p-8">
            <div v-if="productList.length" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8 mb-12">
              <div v-for="item in productList" :key="item.id"
                   class="bg-white rounded-[2.5rem] overflow-hidden shadow-sm hover:shadow-2xl transition-all duration-500 border border-stone-100 group">
                <div class="relative overflow-hidden aspect-square">
                  <img :src="item.pic" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
                  <div class="absolute top-4 left-4 bg-emerald-600 text-white text-xs font-black px-3 py-1 rounded-full">助农直供</div>
                  <div class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-3">
                    <button @click="viewDetail(item.id)" class="w-12 h-12 bg-white rounded-full flex items-center justify-center text-stone-900 hover:bg-emerald-500 hover:text-white transition-all">
                      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/><path d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                    </button>
                    <button @click="goTrace(item.id)" class="w-12 h-12 bg-emerald-600 rounded-full flex items-center justify-center text-white hover:bg-emerald-700 transition-all shadow-lg">
                      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                    </button>
                  </div>
                </div>
                <div class="p-8">
                  <h3 class="text-xl font-bold text-stone-800 mb-2 truncate">{{ item.name }}</h3>
                  <div class="flex items-center justify-between">
                    <div>
                      <span class="text-xs text-stone-400 mr-1">¥</span>
                      <span class="text-3xl font-black text-emerald-600">{{ item.price }}</span>
                    </div>
                    <button @click.stop="viewDetail(item.id)" class="w-12 h-12 bg-stone-100 rounded-2xl flex items-center justify-center text-stone-600 hover:bg-emerald-600 hover:text-white transition-all">
                      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M12 4v16m8-8H4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div v-else-if="!loading" class="py-20 text-center">
              <p class="text-stone-400 font-bold text-xl">暂无助农商品</p>
              <router-link to="/category" class="mt-6 inline-block bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold">浏览全部商品</router-link>
            </div>
          </div>
        </a-spin>
      </div>

      <div class="flex justify-center mt-12">
        <a-pagination
          v-model:current="current"
          :total="total"
          :pageSize="pageSize"
          show-less-items
          @change="loadProducts"
        />
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-stone-900 text-stone-400 py-20">
      <div class="max-w-screen-xl mx-auto px-4 grid md:grid-cols-4 gap-12">
        <div class="col-span-2">
          <span class="text-2xl font-black tracking-tighter text-white mb-6 block">GUI<span class="text-emerald-500">MALL</span></span>
          <p class="max-w-sm">广西桂林助农电商平台，致力于通过数字化手段连接农户与消费者，建立透明、可信、高效的现代农业流通体系。</p>
        </div>
        <div>
          <h4 class="text-white font-bold mb-6 uppercase tracking-widest text-sm">快速链接</h4>
          <ul class="space-y-4">
            <li><router-link to="/support" class="hover:text-emerald-400 transition-colors">助农政策</router-link></li>
            <li><a href="#" class="hover:text-emerald-400 transition-colors">配送说明</a></li>
            <li><a href="#" class="hover:text-emerald-400 transition-colors">售后服务</a></li>
          </ul>
        </div>
        <div>
          <h4 class="text-white font-bold mb-6 uppercase tracking-widest text-sm">联系我们</h4>
          <p>地址：广西桂林市XX区助农科技园</p>
          <p>电话：400-888-9999</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAidAgricultureProducts, getSupportFarmers } from '@/api/frontend/product'
import { isMemberLoggedIn, removeMemberInfo, refreshMemberInfo } from '@/composables/member'
import { removeMemberToken } from '@/composables/cookie'
import { useCartStore } from '@/stores/cart'
import { useMemberLevelStore } from '@/stores/memberLevel'
import MemberBadge from '@/components/MemberBadge.vue'

const router = useRouter()
const cartStore = useCartStore()
const memberLevelStore = useMemberLevelStore()

// 会员登录状态
const memberLoggedIn = ref(isMemberLoggedIn())
const memberNickname = ref('')
const memberAvatar = ref('')

const initMemberStatus = async () => {
  memberLoggedIn.value = isMemberLoggedIn()
  if (memberLoggedIn.value) {
    const info = await refreshMemberInfo()
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
  memberLevelStore.reset()
  router.push('/')
}

// 搜索
const keyword = ref('')

// 农户
const farmers = ref([])
const farmerScrollRef = ref(null)

// 商品
const productList = ref([])
const loading = ref(false)
const total = ref(0)
const current = ref(1)
const pageSize = ref(9)
const sortType = ref(0)

// 统计数据（静态展示）
const stats = [
  { value: '120+', label: '签约农户' },
  { value: '8', label: '覆盖县区' },
  { value: '200+', label: '助农商品' },
  { value: '50,000+', label: '受益人次' }
]

const loadFarmers = async () => {
  const res = await getSupportFarmers()
  if (res.success) farmers.value = res.data
}

const loadProducts = async () => {
  loading.value = true
  try {
    const res = await getAidAgricultureProducts({
      current: current.value,
      size: pageSize.value,
      keyword: keyword.value || undefined,
      sortType: sortType.value
    })
    if (res.success) {
      productList.value = res.data
      total.value = res.total
    }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  current.value = 1
  loadProducts()
}

const viewDetail = (id) => router.push(`/product/detail?id=${id}`)
const goTrace = (id) => router.push(`/trace/${id}`)

const scrollToProducts = () => {
  document.querySelector('#products')?.scrollIntoView({ behavior: 'smooth' })
}

const scrollToFarmers = () => {
  document.querySelector('#farmers')?.scrollIntoView({ behavior: 'smooth' })
}

const setupDragScroll = (el) => {
  if (!el) return
  let isDown = false, startX = 0, scrollLeft = 0
  el.addEventListener('mousedown', e => { isDown = true; startX = e.pageX - el.offsetLeft; scrollLeft = el.scrollLeft; el.style.cursor = 'grabbing' })
  el.addEventListener('mouseleave', () => { isDown = false; el.style.cursor = 'grab' })
  el.addEventListener('mouseup', () => { isDown = false; el.style.cursor = 'grab' })
  el.addEventListener('mousemove', e => { if (!isDown) return; e.preventDefault(); el.scrollLeft = scrollLeft - (e.pageX - el.offsetLeft - startX) })
}

onMounted(() => {
  initMemberStatus()
  loadFarmers().then(() => setupDragScroll(farmerScrollRef.value))
  loadProducts()
})
</script>

<style scoped>
.support-spin :deep(.ant-spin-container) {
  min-height: 400px;
}
</style>
