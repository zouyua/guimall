<template>
  <div class="min-h-screen bg-stone-50 font-sans pb-20">
    <!-- 顶部导航 (共享样式) -->
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
           <router-link to="/cart" class="text-stone-500 hover:text-emerald-600 transition-colors flex items-center">
             <a-badge :count="cartStore.cartCount" :offset="[-2, 2]" size="small">
               <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                 <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" />
               </svg>
             </a-badge>
             购物车
           </router-link>
           <div class="w-px h-4 bg-stone-200"></div>
           <router-link to="/" class="text-stone-500 hover:text-emerald-600">首页</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-xl mx-auto px-4 mt-12">
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-12">
        <!-- 左侧：图片展示 -->
        <div class="lg:col-span-6">
          <div class="bg-white rounded-[3rem] overflow-hidden shadow-xl border border-stone-100 sticky top-24">
            <img :src="product.pic" class="w-full aspect-square object-cover transition-transform duration-700 hover:scale-105" />
            <div v-if="product.albumPics" class="flex p-4 gap-4 overflow-x-auto">
              <img v-for="pic in product.albumPics.split(',')" :key="pic" :src="pic"
                class="w-20 h-20 rounded-2xl object-cover cursor-pointer border-2 hover:border-emerald-500 transition-all"
                @click="product.pic = pic" />
            </div>
          </div>
        </div>

        <!-- 右侧：详情与购买 -->
        <div class="lg:col-span-6 space-y-8">
          <div>
            <div class="flex items-center space-x-2 mb-4">
              <span class="bg-emerald-100 text-emerald-700 px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider">桂林特色</span>
              <span class="bg-orange-100 text-orange-700 px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider">助农直供</span>
            </div>
            <h1 class="text-4xl font-black text-stone-900 mb-4 leading-tight">{{ product.name }}</h1>
            <p class="text-lg text-stone-500">{{ product.subTitle }}</p>
          </div>

          <div class="bg-emerald-50 p-8 rounded-[2.5rem] border border-emerald-100 relative overflow-hidden">
             <div class="absolute right-0 top-0 w-32 h-32 bg-emerald-100/50 rounded-full translate-x-10 -translate-y-10 blur-2xl"></div>
             <div class="relative z-10">
               <div class="flex items-baseline space-x-3 mb-2">
                 <span class="text-stone-500 font-bold">价格</span>
                 <!-- 划线市场价 -->
                 <span class="text-stone-400 text-lg line-through" v-if="product.marketPrice">¥{{ product.marketPrice }}</span>
                 <!-- 当前展示价：SKU选中时用SKU价，否则用商品促销价/售价 -->
                 <span class="text-4xl font-black text-emerald-600">
                   ¥{{ selectedSku ? (selectedSku.promotionPrice || selectedSku.price) : (product.promotionPrice || product.price) }}
                 </span>
                 <span class="text-stone-400 text-sm">/ {{ product.unit || '件' }}</span>
               </div>
               <!-- 促销标签 -->
               <div class="flex gap-2 flex-wrap">
                 <span v-if="product.promotionPrice" class="bg-red-100 text-red-600 text-xs font-bold px-3 py-1 rounded-full">限时促销</span>
                 <span v-if="product.marketPrice && product.price" class="bg-orange-100 text-orange-600 text-xs font-bold px-3 py-1 rounded-full">
                   省 ¥{{ (product.marketPrice - (product.promotionPrice || product.price)).toFixed(2) }}
                 </span>
                 <span class="bg-emerald-100 text-emerald-700 text-xs font-bold px-3 py-1 rounded-full">产地直供</span>
               </div>
             </div>
          </div>

          <!-- SKU 选择 -->
          <div v-if="product.skus && product.skus.length > 0" class="space-y-6">
             <h3 class="font-bold text-stone-800 text-lg">规格选择</h3>
             <div class="flex flex-wrap gap-3">
               <button v-for="sku in product.skus" :key="sku.id"
                 @click="selectSku(sku)"
                 :class="selectedSku?.id === sku.id ? 'bg-emerald-600 text-white border-emerald-600' : 'bg-white text-stone-600 border-stone-200 hover:border-emerald-500'"
                 class="px-6 py-3 rounded-2xl border-2 font-bold transition-all text-sm">
                 {{ formatSku(sku) }}
               </button>
             </div>
             <p v-if="selectedSku" class="text-sm text-stone-400">
               库存：<span :class="selectedSku.stock > 10 ? 'text-emerald-600' : 'text-red-500'" class="font-bold">{{ selectedSku.stock > 0 ? selectedSku.stock + ' ' + (product.unit || '件') : '暂时缺货' }}</span>
             </p>
          </div>

          <!-- 商品参数 -->
          <div v-if="parsedParams.length > 0" class="bg-white p-6 rounded-3xl border border-stone-100 shadow-sm">
            <h3 class="font-bold text-stone-800 text-lg mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
              商品参数
            </h3>
            <div class="divide-y divide-stone-100 rounded-2xl overflow-hidden border border-stone-100">
              <div v-for="(param, idx) in parsedParams" :key="idx" class="flex">
                <div class="w-32 shrink-0 bg-stone-50 px-4 py-3 text-sm font-medium text-stone-500">{{ param.key }}</div>
                <div class="flex-1 px-4 py-3 text-sm text-stone-800">{{ param.value || '-' }}</div>
              </div>
            </div>
          </div>

          <!-- 农户信息 -->
          <div class="bg-white p-6 rounded-3xl border border-stone-100 shadow-sm flex items-center justify-between">
            <div class="flex items-center space-x-4">
              <div class="w-14 h-14 bg-stone-100 rounded-full flex items-center justify-center overflow-hidden border-2 border-emerald-50">
                <img :src="'https://api.dicebear.com/7.x/avataaars/svg?seed=' + product.farmerName" class="w-full h-full object-cover" />
              </div>
              <div>
                <p class="text-xs text-stone-400 font-bold uppercase tracking-widest">提供者</p>
                <h4 class="font-bold text-stone-800">{{ product.farmerName || '签约农户' }}</h4>
              </div>
            </div>
            <button @click="goTrace" class="flex items-center text-emerald-600 font-bold text-sm hover:underline">
               查看完整溯源档案
               <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
            </button>
          </div>

          <!-- 购买行为 -->
          <div class="pt-8 flex flex-col sm:flex-row gap-4">
            <div class="flex items-center bg-white rounded-2xl border border-stone-200 p-2 sm:w-32 justify-between">
               <button @click="quantity > 1 && quantity--" class="w-8 h-8 flex items-center justify-center text-stone-400 hover:text-emerald-600">-</button>
               <span class="font-bold text-stone-800">{{ quantity }}</span>
               <button @click="quantity++" class="w-8 h-8 flex items-center justify-center text-stone-400 hover:text-emerald-600">+</button>
            </div>
            <button @click="handleAddCart" class="flex-1 bg-emerald-600 text-white py-4 rounded-2xl font-black text-lg shadow-lg shadow-emerald-200 hover:bg-emerald-700 hover:-translate-y-1 transition-all">加入购物车</button>
          </div>

          <!-- 详情介绍 -->
          <div class="pt-12 border-t border-stone-200">
             <h3 class="text-2xl font-black text-stone-900 mb-6">产品故事</h3>
             <div class="prose prose-stone max-w-none text-stone-500 leading-relaxed" v-html="product.detailHtml || product.description">
             </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 底部固定栏 (移动端) -->
    <div class="lg:hidden fixed bottom-0 left-0 right-0 bg-white/80 backdrop-blur-lg border-t border-stone-200 p-4 flex gap-4 z-50">
       <button @click="goTrace" class="w-14 h-14 bg-emerald-50 text-emerald-600 rounded-2xl flex items-center justify-center">
         <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
       </button>
       <button class="flex-1 bg-emerald-600 text-white font-black rounded-2xl">立即购买</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getProductDetail } from '@/api/frontend/product'
import { addCartItem } from '@/api/frontend/cart'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const id = route.query.id

const product = ref({})
const selectedSku = ref(null)
const quantity = ref(1)

// 商品参数（直接从 API 返回的数组读取）
const parsedParams = computed(() => {
  const params = product.value.productParams || []
  // 过滤掉空值的参数
  return params.filter(p => p.key && p.key.trim())
})

const loadDetail = async () => {
  const res = await getProductDetail(id)
  if (res.success) {
    product.value = res.data
    if (product.value.skus && product.value.skus.length > 0) {
      selectedSku.value = product.value.skus[0]
    }
  }
}

const formatSku = (sku) => {
  if (sku.specs && sku.specs.length > 0) {
    return sku.specs.map(s => s.specValue).join(' / ')
  }
  // 兼容旧 spData JSON 格式
  if (!sku.spData) return '默认规格'
  try {
    const arr = JSON.parse(sku.spData)
    return arr.map(item => item.value).join(' / ')
  } catch (e) {
    return sku.spData
  }
}

const selectSku = (sku) => {
  selectedSku.value = sku
  // SKU 有独立图片时切换主图
  if (sku.pic) {
    product.value = { ...product.value, pic: sku.pic }
  }
}

const goTrace = () => {
  router.push(`/trace/${id}`)
}

const handleAddCart = async () => {
  if (!isMemberLoggedIn()) {
    message.warning('请先登录')
    router.push(`/member/login?redirect=${encodeURIComponent(route.fullPath)}`)
    return
  }
  if (!selectedSku.value) {
    message.warning('请先选择商品规格')
    return
  }
  try {
    const data = {
      memberId: getMemberId(),
      productId: product.value.id,
      productSkuId: selectedSku.value.id,
      quantity: quantity.value,
      price: selectedSku.value.promotionPrice || selectedSku.value.price || product.value.price,
      productPic: product.value.pic,
      productName: product.value.name,
      productAttr: formatSku(selectedSku.value)
    }
    const res = await addCartItem(data)
    if (res.success) {
      message.success('已加入购物车')
      cartStore.loadCartCount()
    } else {
      message.error(res.message || '加入购物车失败')
    }
  } catch (e) {
    message.error('加入购物车失败')
  }
}

onMounted(() => {
  loadDetail()
  cartStore.loadCartCount()
})
</script>

<style scoped>
/* 可以在这里添加一些针对详情富文本的样式定制 */
:deep(img) {
  max-width: 100%;
  border-radius: 1.5rem;
  margin: 1rem 0;
}
</style>
