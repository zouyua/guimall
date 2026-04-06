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
          <input type="text" v-model="queryParams.keyword" @keyup.enter="handleSearch"
            class="w-48 p-2 pl-4 text-sm text-stone-900 border border-stone-200 rounded-full bg-stone-100 focus:ring-emerald-500 focus:border-emerald-500 transition-all"
            placeholder="搜索桂林特色农产品...">
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

    <main class="max-w-screen-xl mx-auto px-4 py-12 flex gap-8">
      <!-- 左侧分类筛选 -->
      <aside class="w-64 shrink-0 hidden lg:block">
        <div class="bg-white rounded-3xl p-6 shadow-sm border border-stone-100 sticky top-24">
          <h2 class="text-xl font-black text-stone-900 mb-6 flex items-center">
            <svg class="w-5 h-5 mr-2 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M4 6h16M4 12h16M4 18h7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
            商品分类
          </h2>
          <div class="space-y-4">
            <!-- 一级分类 -->
            <div>
              <div v-for="cat in categories" :key="cat.id" class="mb-1">
                <button
                  @click="handleParentCategoryClick(cat)"
                  :class="(parentCategoryId === cat.id || queryParams.categoryId === cat.id || (cat.children && cat.children.some(sub => sub.id === queryParams.categoryId))) ? 'bg-emerald-100 text-emerald-800 font-bold' : 'text-stone-600 hover:bg-stone-50'"
                  class="w-full text-left px-4 py-3 rounded-xl transition-all flex items-center justify-between group"
                >
                  <div class="flex items-center">
                    <img v-if="cat.icon" :src="cat.icon" class="w-5 h-5 mr-3 object-contain" />
                    {{ cat.name }}
                  </div>
                  <svg v-if="cat.children && cat.children.length"
                       class="w-4 h-4 transition-transform"
                       :class="parentCategoryId === cat.id ? 'rotate-90' : ''"
                       fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
                  </svg>
                </button>

                <!-- 二级分类 (展开) -->
                <div v-if="parentCategoryId === cat.id && cat.children && cat.children.length" class="ml-6 mt-1 space-y-1">
                  <button
                    @click="handleSubCategoryClick(cat.id)"
                    :class="queryParams.categoryId === cat.id ? 'text-emerald-600 font-bold' : 'text-stone-500 hover:text-emerald-500'"
                    class="w-full text-left px-4 py-2 text-sm transition-all"
                  >
                    全部 {{ cat.name }}
                  </button>
                  <button
                    v-for="sub in cat.children"
                    :key="sub.id"
                    @click="handleSubCategoryClick(sub.id)"
                    :class="queryParams.categoryId === sub.id ? 'text-emerald-600 font-bold' : 'text-stone-500 hover:text-emerald-500'"
                    class="w-full text-left px-4 py-2 text-sm transition-all flex items-center"
                  >
                    <span class="w-1 h-1 bg-stone-300 rounded-full mr-2" :class="queryParams.categoryId === sub.id ? 'bg-emerald-500' : ''"></span>
                    {{ sub.name }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </aside>

      <!-- 右侧商品列表 -->
      <div class="flex-1">
        <!-- 排序工具栏 -->
        <div class="bg-white rounded-3xl p-4 mb-8 shadow-sm border border-stone-100 flex items-center justify-between">
          <div class="flex gap-2">
            <button v-for="(f, idx) in ['综合', '销量', '价格']" :key="f"
               @click="queryParams.sortType = idx; syncUrl()"
               :class="queryParams.sortType === idx ? 'bg-emerald-600 text-white shadow-lg shadow-emerald-100' : 'bg-stone-50 text-stone-600'"
               class="px-6 py-2 rounded-full text-sm font-bold transition-all">{{ f }}</button>
          </div>
          <div class="text-sm text-stone-400">
            共找到 <span class="text-emerald-600 font-bold">{{ total }}</span> 件农产好物
          </div>
        </div>

        <!-- 列表容器 -->
        <div class="relative min-h-[600px] bg-white rounded-[3rem] overflow-hidden">
          <a-spin :spinning="loading" class="category-spin" size="large">
            <div class="p-8">
              <div v-if="productList.length > 0" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8 mb-12">
                <div v-for="item in productList" :key="item.id"
                     class="bg-white rounded-[2.5rem] overflow-hidden shadow-sm hover:shadow-2xl transition-all duration-500 border border-stone-100 group">
                  <div class="relative overflow-hidden aspect-square">
                    <img :src="item.pic" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
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
                      <button class="w-12 h-12 bg-stone-100 rounded-2xl flex items-center justify-center text-stone-600 hover:bg-emerald-600 hover:text-white transition-all">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M12 4v16m8-8H4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 空状态 -->
              <div v-else-if="!loading" class="py-20 text-center">
                <div class="w-24 h-24 bg-stone-50 rounded-full flex items-center justify-center mx-auto mb-6 text-stone-300">
                  <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                </div>
                <h3 class="text-2xl font-bold text-stone-900 mb-2">暂无相关商品</h3>
                <p class="text-stone-400 mb-8">换个搜索词或分类试试吧</p>
                <button @click="resetQuery" class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold">查看全部</button>
              </div>
            </div>
          </a-spin>
        </div>

        <!-- 分页 -->
        <div class="flex justify-center mt-12">
          <a-pagination
            v-model:current="queryParams.current"
            :total="total"
            :pageSize="queryParams.size"
            show-less-items
            @change="loadData"
          />
        </div>
      </div>
    </main>

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
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductList, getCategoryTree } from '@/api/frontend/product'
import { isMemberLoggedIn, getMemberInfo } from '@/composables/member'
import { removeMemberInfo } from '@/composables/member'
import { removeMemberToken } from '@/composables/cookie'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

// 会员登录状态
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

const queryParams = ref({
  keyword: route.query.keyword || '',
  current: 1,
  size: 9,
  categoryId: route.query.categoryId ? parseInt(route.query.categoryId) : null,
  sortType: 0
})

const total = ref(0)
const categories = ref([])
const productList = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getProductList(queryParams.value)
    if (res.success) {
      productList.value = res.data
      total.value = res.total
    }
  } finally {
    loading.value = false
  }
}

const parentCategoryId = ref(null)

const loadCategories = async () => {
  const res = await getCategoryTree()
  if (res.success) {
    categories.value = res.data // 直接使用原始树形结构
    // 如果 URL 中有 categoryId，尝试找到对应的父分类并展开
    if (queryParams.value.categoryId) {
      findParentAndExpand(queryParams.value.categoryId)
    }
  }
}

const findParentAndExpand = (id) => {
  categories.value.forEach(parent => {
    if (parent.id === id) {
      parentCategoryId.value = id
    } else if (parent.children && parent.children.some(sub => sub.id === id)) {
      parentCategoryId.value = parent.id
    }
  })
}

const handleParentCategoryClick = (cat) => {
  if (!cat) {
    parentCategoryId.value = null
    queryParams.value.categoryId = null
  } else {
    // 点击已展开的分类则折叠（只收起下拉，保持选中状态）
    if (parentCategoryId.value === cat.id) {
      parentCategoryId.value = null
    } else {
      parentCategoryId.value = cat.id
      queryParams.value.categoryId = cat.id
    }
  }
  queryParams.value.current = 1
  syncUrl()
}

const handleSubCategoryClick = (subId) => {
  queryParams.value.categoryId = subId
  queryParams.value.current = 1
  // loadData() // 这里不需要调用，syncUrl 触发路由变化，watch 会执行 loadData
  syncUrl()
}

const syncUrl = () => {
  router.replace({
    query: {
      ...route.query,
      categoryId: queryParams.value.categoryId || undefined,
      keyword: queryParams.value.keyword || undefined,
      _t: Date.now() // 添加时间戳强制触发 watch，如果参数没变但需要刷新
    }
  })
}

const handleSearch = () => {
  queryParams.value.current = 1
  // loadData() // 这里不需要调用，syncUrl 触发路由变化，watch 会执行 loadData
  syncUrl()
}

const resetQuery = () => {
  queryParams.value.keyword = ''
  queryParams.value.categoryId = null
  parentCategoryId.value = null
  queryParams.value.current = 1
  // loadData() // 这里不需要调用，syncUrl 触发路由变化，watch 会执行 loadData
  syncUrl()
}

const viewDetail = (id) => {
  router.push(`/product/detail?id=${id}`)
}

const goTrace = (id) => {
  router.push(`/trace/${id}`)
}

// 监听路由参数变化（处理从首页点击分类跳转过来的情况）
watch(() => route.query, (newQuery) => {
  queryParams.value.categoryId = newQuery.categoryId ? parseInt(newQuery.categoryId) : null
  queryParams.value.keyword = newQuery.keyword || ''
  loadData()
})

onMounted(() => {
  initMemberStatus()
  loadCategories()
  loadData()
})
</script>

<style scoped>
.category-spin :deep(.ant-spin-container) {
  min-height: 400px;
}
</style>
