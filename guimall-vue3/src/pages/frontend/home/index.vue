<template>
  <div class="min-h-screen bg-stone-50 font-sans">
    <!-- 导航栏 -->
    <nav class="bg-white/90 backdrop-blur-md border-b border-stone-200 sticky top-0 z-50">
      <div class="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
        <a href="/" class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-emerald-600 rounded-xl flex items-center justify-center shadow-lg shadow-emerald-200">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <span class="text-2xl font-black tracking-tighter text-emerald-900 uppercase">Guimall</span>
        </a>

        <div class="flex items-center md:order-2 space-x-4">
          <div class="relative hidden md:block">
            <input type="text" v-model="queryParams.keyword" @keyup.enter="handleSearch"
              class="block w-64 p-2 pl-4 text-sm text-stone-900 border border-stone-200 rounded-full bg-stone-100 focus:ring-emerald-500 focus:border-emerald-500 transition-all"
              placeholder="搜索桂林特色农产品...">
          </div>
          <button @click="$router.push('/login')" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">登录</button>
          <button @click="$router.push('/admin')" class="bg-emerald-600 text-white px-6 py-2 rounded-full font-bold hover:bg-emerald-700 transition-all shadow-md">管理端</button>
        </div>

        <div class="items-center justify-between hidden w-full md:flex md:w-auto md:order-1">
          <ul class="flex flex-col p-4 md:p-0 mt-4 font-bold md:flex-row md:space-x-12 md:mt-0">
            <li><router-link to="/" exact class="block py-2 text-stone-600 hover:text-emerald-600 transition-colors relative group" exact-active-class="text-emerald-600 nav-active">首页<span class="absolute bottom-0 left-0 w-0 h-0.5 bg-emerald-500 transition-all group-hover:w-full"></span></router-link></li>
            <li><router-link to="/category" class="block py-2 text-stone-600 hover:text-emerald-600 transition-colors relative group" active-class="text-emerald-600 nav-active">全部分类<span class="absolute bottom-0 left-0 w-0 h-0.5 bg-emerald-500 transition-all group-hover:w-full"></span></router-link></li>
            <li><router-link to="/support" class="block py-2 text-stone-600 hover:text-emerald-600 transition-colors relative group" active-class="text-emerald-600 nav-active">助农专区<span class="absolute bottom-0 left-0 w-0 h-0.5 bg-emerald-500 transition-all group-hover:w-full"></span></router-link></li>
            <li><router-link to="/about" class="block py-2 text-stone-600 hover:text-emerald-600 transition-colors relative group" active-class="text-emerald-600 nav-active">关于我们<span class="absolute bottom-0 left-0 w-0 h-0.5 bg-emerald-500 transition-all group-hover:w-full"></span></router-link></li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Banner 区域 (Ant Design Carousel) -->
    <header class="max-w-screen-xl mx-auto px-4 mt-6">
      <a-carousel autoplay class="rounded-[2.5rem] overflow-hidden shadow-2xl">
        <div v-for="adv in advertises" :key="adv.id" class="relative h-[450px]">
          <img :src="adv.pic" class="w-full h-full object-cover" />
          <div class="absolute inset-0 bg-gradient-to-r from-black/60 to-transparent flex items-center px-12">
            <div class="max-w-lg text-white space-y-4">
              <h2 class="text-5xl font-black">{{ adv.name }}</h2>
              <button class="bg-emerald-600 px-8 py-3 rounded-xl font-bold hover:bg-emerald-700 transition-all">了解详情</button>
            </div>
          </div>
        </div>
      </a-carousel>
    </header>

    <!-- 分类展示 (真实数据) -->
    <section class="max-w-screen-xl mx-auto px-4 mt-12">
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6">
        <div v-for="cat in categories" :key="cat.id"
             @click="handleCategoryClick(cat.id)"
             :class="queryParams.categoryId === cat.id ? 'bg-emerald-600 text-white shadow-emerald-200' : 'bg-white text-stone-800'"
             class="p-6 rounded-3xl shadow-xl hover:shadow-2xl hover:-translate-y-2 transition-all text-center cursor-pointer border border-stone-100 group">
          <div class="w-16 h-16 mx-auto bg-stone-50 rounded-2xl flex items-center justify-center mb-4 group-hover:bg-emerald-50 transition-colors">
            <img :src="cat.icon" class="w-10 h-10 object-contain group-hover:scale-110 transition-transform" />
          </div>
          <h3 class="font-bold">{{ cat.name }}</h3>
        </div>
      </div>
    </section>

    <!-- 推荐模块: 新品 & 人气 -->
    <section class="max-w-screen-xl mx-auto px-4 mt-20 grid lg:grid-cols-2 gap-8">
       <div class="bg-emerald-900 rounded-[3rem] p-10 text-white relative overflow-hidden group">
          <div class="relative z-10">
             <span class="text-emerald-400 font-bold tracking-widest uppercase text-sm">New Arrivals</span>
             <div class="flex items-end justify-between mt-2 mb-6">
               <h2 class="text-4xl font-black">本周新品推荐</h2>
               <router-link to="/category" class="text-emerald-400 hover:text-emerald-200 text-sm font-bold flex items-center gap-1 transition-colors">
                 查看全部
                 <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
               </router-link>
             </div>
             <!-- 可拖动滚动容器 -->
             <div ref="newScrollRef" class="flex gap-4 overflow-x-auto pb-3 scroll-smooth cursor-grab active:cursor-grabbing select-none" style="scrollbar-width: thin; scrollbar-color: rgba(255,255,255,0.2) transparent;">
                <div v-for="p in newProducts" :key="p.productId" @click="viewDetail(p.productId)"
                     class="flex-none w-[160px] bg-white/10 backdrop-blur-md rounded-[2rem] p-4 cursor-pointer hover:bg-white/20 transition-all">
                   <img :src="p.pic" class="w-full aspect-square object-cover rounded-2xl mb-3" />
                   <p class="font-bold truncate text-sm">{{ p.name }}</p>
                   <p class="text-emerald-400 font-black">¥{{ p.price }}</p>
                </div>
                <div v-if="!newProducts.length" class="flex-none w-full text-center text-emerald-300/50 py-8">暂无新品</div>
             </div>
          </div>
          <div class="absolute -right-20 -bottom-20 w-64 h-64 bg-emerald-800 rounded-full blur-3xl opacity-50 group-hover:scale-110 transition-transform duration-1000"></div>
       </div>
       <div class="bg-orange-50 rounded-[3rem] p-10 border border-orange-100 relative overflow-hidden group">
          <div class="relative z-10">
             <span class="text-orange-600 font-bold tracking-widest uppercase text-sm">Best Sellers</span>
             <div class="flex items-end justify-between mt-2 mb-6">
               <h2 class="text-4xl font-black text-stone-900">农友最爱人气王</h2>
               <router-link to="/category?sortType=1" class="text-orange-400 hover:text-orange-600 text-sm font-bold flex items-center gap-1 transition-colors">
                 查看全部
                 <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
               </router-link>
             </div>
             <div ref="recScrollRef" class="flex gap-4 overflow-x-auto pb-3 scroll-smooth cursor-grab active:cursor-grabbing select-none" style="scrollbar-width: thin; scrollbar-color: rgba(0,0,0,0.1) transparent;">
                <div v-for="p in recommendProducts" :key="p.productId" @click="viewDetail(p.productId)"
                     class="flex-none w-[160px] bg-white rounded-[2rem] p-4 shadow-sm cursor-pointer hover:shadow-xl transition-all">
                   <img :src="p.pic" class="w-full aspect-square object-cover rounded-2xl mb-3" />
                   <p class="font-bold text-stone-800 truncate text-sm">{{ p.name }}</p>
                   <p class="text-orange-600 font-black">¥{{ p.price }}</p>
                </div>
                <div v-if="!recommendProducts.length" class="flex-none w-full text-center text-stone-300 py-8">暂无推荐</div>
             </div>
          </div>
       </div>
    </section>

    <!-- 商品列表 + 分页 -->
    <section class="max-w-screen-xl mx-auto px-4 py-24">
      <div class="flex items-end justify-between mb-12">
        <div>
          <h2 class="text-4xl font-black text-stone-900 mb-4">全部鲜货</h2>
          <div class="w-20 h-2 bg-emerald-500 rounded-full"></div>
        </div>
        <div class="flex gap-2">
           <button v-for="(f, idx) in ['综合', '销量', '价格']" :key="f"
             @click="queryParams.sortType = idx; loadData()"
             :class="queryParams.sortType === idx ? 'bg-emerald-600 text-white border-emerald-600' : 'bg-white border-stone-200'"
             class="px-6 py-2 rounded-full text-sm font-bold border hover:border-emerald-500 transition-all">{{ f }}</button>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8 mb-16">
        <div v-for="item in productList" :key="item.id"
             class="bg-white rounded-[2.5rem] overflow-hidden shadow-sm hover:shadow-2xl transition-all duration-500 border border-stone-100 group">
          <div class="relative overflow-hidden aspect-square">
            <img :src="item.pic" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
            <div class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-3">
               <button @click="viewDetail(item.id)" class="w-12 h-12 bg-white rounded-full flex items-center justify-center text-stone-900 hover:bg-emerald-500 hover:text-white transition-all">
                 <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/><path d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
               </button>
               <button @click="goTrace(item.id)" class="w-12 h-12 bg-emerald-600 rounded-full flex items-center justify-center text-white hover:bg-emerald-700 transition-all shadow-lg" title="查看溯源">
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
              <button class="w-12 h-12 bg-stone-100 rounded-2xl flex items-center justify-center text-stone-600 hover:bg-emerald-600 hover:text-white transition-all">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M12 4v16m8-8H4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页组件 (Ant Design) -->
      <div class="flex justify-center">
        <a-pagination
          v-model:current="queryParams.current"
          :total="total"
          :pageSize="queryParams.size"
          show-less-items
          @change="handlePageChange"
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
            <li><a href="#" class="hover:text-emerald-400 transition-colors">助农政策</a></li>
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
import {
  getProductList,
  getCategoryTree,
  getHomeAdvertises,
  getNewProducts,
  getRecommendProducts
} from '@/api/frontend/product'

const router = useRouter()

const queryParams = ref({
  keyword: '',
  current: 1,
  size: 10,
  categoryId: null,
  sortType: 0
})

const total = ref(0)
const advertises = ref([])
const categories = ref([])
const newProducts = ref([])
const recommendProducts = ref([])
const productList = ref([])

const newScrollRef = ref(null)
const recScrollRef = ref(null)

const setupDragScroll = (el) => {
  if (!el) return
  let isDown = false, startX = 0, scrollLeft = 0
  el.addEventListener('mousedown', e => {
    isDown = true
    startX = e.pageX - el.offsetLeft
    scrollLeft = el.scrollLeft
    el.style.cursor = 'grabbing'
  })
  el.addEventListener('mouseleave', () => { isDown = false; el.style.cursor = 'grab' })
  el.addEventListener('mouseup', () => { isDown = false; el.style.cursor = 'grab' })
  el.addEventListener('mousemove', e => {
    if (!isDown) return
    e.preventDefault()
    el.scrollLeft = scrollLeft - (e.pageX - el.offsetLeft - startX)
  })
}

const loadData = async () => {
  // 加载主商品列表
  const productRes = await getProductList(queryParams.value)
  if (productRes.success) {
    productList.value = productRes.data
    total.value = productRes.total
  }
}

const loadHomeData = async () => {
  // 加载轮播图 (type=0 WEB)
  const advRes = await getHomeAdvertises(0)
  if (advRes.success) advertises.value = advRes.data

  // 加载分类
  const categoryRes = await getCategoryTree()
  if (categoryRes.success) {
    categories.value = categoryRes.data.slice(0, 6).map(item => ({
      id: item.id,
      name: item.name,
      icon: item.icon || 'https://cdn-icons-png.flaticon.com/128/1625/1625099.png'
    }))
  }

  // 加载推荐和新品
  const newRes = await getNewProducts()
  if (newRes.success) {
    newProducts.value = newRes.data.map(p => ({
      ...p,
      id: p.productId,
      name: p.productName,
      pic: p.productPic
    }))
  }

  const recRes = await getRecommendProducts()
  if (recRes.success) {
    recommendProducts.value = recRes.data.map(p => ({
      ...p,
      id: p.productId,
      name: p.productName,
      pic: p.productPic
    }))
  }
}

const handlePageChange = (page) => {
  queryParams.value.current = page
  loadData()
}

const handleCategoryClick = (id) => {
  router.push(`/category?categoryId=${id}`)
}

const handleSearch = () => {
  router.push(`/category?keyword=${queryParams.value.keyword}`)
}

const viewDetail = (id) => {
  router.push(`/product/detail?id=${id}`)
}

const goTrace = (id) => {
  router.push(`/trace/${id}`)
}

onMounted(() => {
  loadHomeData().then(() => {
    setupDragScroll(newScrollRef.value)
    setupDragScroll(recScrollRef.value)
  })
  loadData()
})
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
:deep(.ant-carousel .slick-dots li button) {
  background: #10b981;
}
/* 顶栏导航选中时显示下划线 */
.nav-active span {
  width: 100% !important;
}
</style>
