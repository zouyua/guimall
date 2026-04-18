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
      <!-- 页面标题 -->
      <div class="mb-10">
        <h1 class="text-3xl font-black text-stone-900 mb-2">我的购物车</h1>
        <div class="w-16 h-1.5 bg-emerald-500 rounded-full"></div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-32">
        <a-spin size="large" />
      </div>

      <!-- 空购物车 -->
      <div v-else-if="cartList.length === 0" class="flex flex-col items-center justify-center py-32">
        <div class="w-40 h-40 bg-stone-100 rounded-full flex items-center justify-center mb-8">
          <svg class="w-20 h-20 text-stone-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" />
          </svg>
        </div>
        <p class="text-xl text-stone-400 font-bold mb-6">购物车空空如也</p>
        <router-link to="/"
          class="bg-emerald-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200">
          去逛逛
        </router-link>
      </div>

      <!-- 购物车列表 -->
      <div v-else>
        <!-- 表头 -->
        <div class="hidden md:grid grid-cols-12 gap-4 px-8 py-4 text-sm font-bold text-stone-400 uppercase tracking-wider">
          <div class="col-span-1 flex items-center">
            <a-checkbox v-model:checked="selectAll" @change="handleSelectAll" />
          </div>
          <div class="col-span-5">商品信息</div>
          <div class="col-span-2 text-center">单价</div>
          <div class="col-span-2 text-center">数量</div>
          <div class="col-span-1 text-center">小计</div>
          <div class="col-span-1 text-center">操作</div>
        </div>

        <!-- 购物车项 -->
        <div v-for="item in cartList" :key="item.id"
          class="bg-white rounded-3xl border border-stone-100 shadow-sm mb-4 p-6 md:p-8 hover:shadow-lg transition-all">
          <div class="grid grid-cols-12 gap-4 items-center">
            <!-- 选择框 -->
            <div class="col-span-12 md:col-span-1 flex items-center">
              <a-checkbox v-model:checked="item.selected" @change="calcTotal" />
            </div>

            <!-- 商品信息 -->
            <div class="col-span-12 md:col-span-5 flex items-center space-x-4">
              <img :src="item.productPic" class="w-24 h-24 rounded-2xl object-cover border border-stone-100 flex-shrink-0" />
              <div class="min-w-0">
                <h3 class="font-bold text-stone-800 truncate cursor-pointer hover:text-emerald-600 transition-colors"
                  @click="$router.push(`/product/detail?id=${item.productId}`)">
                  {{ item.productName }}
                </h3>
                <p class="text-sm text-stone-400 mt-1 truncate">{{ item.productAttr || '默认规格' }}</p>
              </div>
            </div>

            <!-- 单价 -->
            <div class="col-span-4 md:col-span-2 text-center">
              <span class="text-emerald-600 font-black text-lg">¥{{ item.price }}</span>
            </div>

            <!-- 数量 -->
            <div class="col-span-4 md:col-span-2 flex justify-center">
              <div class="flex items-center bg-stone-100 rounded-xl overflow-hidden">
                <button @click="changeQuantity(item, -1)"
                  class="w-9 h-9 flex items-center justify-center text-stone-500 hover:bg-stone-200 transition-colors font-bold">
                  -
                </button>
                <span class="w-12 text-center font-bold text-stone-800 text-sm">{{ item.quantity }}</span>
                <button @click="changeQuantity(item, 1)"
                  class="w-9 h-9 flex items-center justify-center text-stone-500 hover:bg-stone-200 transition-colors font-bold">
                  +
                </button>
              </div>
            </div>

            <!-- 小计 -->
            <div class="col-span-2 md:col-span-1 text-center">
              <span class="text-emerald-600 font-black">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
            </div>

            <!-- 删除 -->
            <div class="col-span-2 md:col-span-1 text-center">
              <button @click="handleDelete(item)"
                class="text-stone-400 hover:text-red-500 transition-colors">
                <svg class="w-5 h-5 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- 底部结算栏 -->
        <div class="bg-white rounded-3xl border border-stone-100 shadow-xl p-6 md:p-8 mt-8 sticky bottom-4">
          <div class="flex flex-col md:flex-row items-center justify-between gap-4">
            <div class="flex items-center space-x-6">
              <a-checkbox v-model:checked="selectAll" @change="handleSelectAll">
                <span class="font-bold text-stone-600">全选</span>
              </a-checkbox>
              <button @click="handleDeleteSelected" class="text-stone-400 hover:text-red-500 transition-colors text-sm font-medium">
                删除选中
              </button>
            </div>
            <div class="flex items-center space-x-8">
              <div class="text-right">
                <p class="text-sm text-stone-400">
                  已选 <span class="text-emerald-600 font-bold">{{ selectedCount }}</span> 件商品
                </p>
                <p class="text-stone-800">
                  合计：<span class="text-3xl font-black text-emerald-600">¥{{ totalPrice }}</span>
                </p>
              </div>
              <button @click="goCheckout"
                :disabled="selectedCount === 0"
                :class="selectedCount === 0 ? 'bg-stone-300 cursor-not-allowed' : 'bg-emerald-600 hover:bg-emerald-700 shadow-lg shadow-emerald-200 hover:-translate-y-0.5'"
                class="text-white px-12 py-4 rounded-2xl font-black text-lg transition-all">
                去结算
              </button>
            </div>
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
import { getCartList, updateCartQuantity, deleteCartItems } from '@/api/frontend/cart'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'

const router = useRouter()

// 登录检查
if (!isMemberLoggedIn()) {
  message.warning('请先登录')
  router.push('/member/login?redirect=/cart')
}

const memberId = getMemberId()

const loading = ref(false)
const cartList = ref([])

const selectAll = ref(false)

const selectedCount = computed(() => cartList.value.filter(i => i.selected).length)

const totalPrice = computed(() => {
  return cartList.value
    .filter(i => i.selected)
    .reduce((sum, i) => sum + i.price * i.quantity, 0)
    .toFixed(2)
})

const handleSelectAll = () => {
  cartList.value.forEach(item => {
    item.selected = selectAll.value
  })
}

const calcTotal = () => {
  selectAll.value = cartList.value.length > 0 && cartList.value.every(i => i.selected)
}

// 加载购物车列表：请求后端接口，设置 loading 状态，初始化每项为选中
const loadCart = async () => {
  loading.value = true // 开始加载，显示 loading 动画
  try {
    const res = await getCartList(memberId) // 调用接口获取当前用户的购物车列表
    if (res.success) { // 如果请求成功
      // 把每个购物车项都加上 selected: true 字段，表示默认全选
      cartList.value = (res.data || []).map(item => ({ ...item, selected: true }))
      // 如果购物车有商品，则全选按钮为 true，否则为 false
      selectAll.value = cartList.value.length > 0
    }
  } finally {
    loading.value = false // 加载结束，隐藏 loading 动画
  }
}

const changeQuantity = async (item, delta) => {
  const newQty = item.quantity + delta
  if (newQty < 1) return
  try {
    const res = await updateCartQuantity(memberId, item.id, newQty)
    if (res.success) {
      item.quantity = newQty
    }
  } catch (e) {
    message.error('修改数量失败')
  }
}

const handleDelete = async (item) => {
  try {
    const res = await deleteCartItems({ memberId, ids: [item.id] })
    if (res.success) {
      cartList.value = cartList.value.filter(i => i.id !== item.id)
      message.success('已删除')
      calcTotal()
    }
  } catch (e) {
    message.error('删除失败')
  }
}

const handleDeleteSelected = async () => {
  const selectedIds = cartList.value.filter(i => i.selected).map(i => i.id)
  if (selectedIds.length === 0) {
    message.warning('请先选择商品')
    return
  }
  try {
    const res = await deleteCartItems({ memberId, ids: selectedIds })
    if (res.success) {
      cartList.value = cartList.value.filter(i => !i.selected)
      message.success('已删除选中商品')
      calcTotal()
    }
  } catch (e) {
    message.error('删除失败')
  }
}

// 结算操作：筛选已选中的商品，校验后存入 sessionStorage 并跳转到结算页
const goCheckout = () => {
  const selectedItems = cartList.value.filter(i => i.selected) // 获取所有被选中的购物车项
  if (selectedItems.length === 0) { // 如果没有选中商品
    message.warning('请选择要结算的商品') // 提示用户
    return
  }
  // 将选中的购物车项ID存入本地存储对象sessionStorage
  sessionStorage.setItem('checkoutItems', JSON.stringify(selectedItems))
  router.push('/checkout')
}

// 组件挂载后自动加载购物车数据
onMounted(() => {
  loadCart()
})
</script>

<style scoped>
:deep(.ant-checkbox-checked .ant-checkbox-inner) {
  background-color: #059669;
  border-color: #059669;
}
:deep(.ant-checkbox-wrapper:hover .ant-checkbox-inner),
:deep(.ant-checkbox:hover .ant-checkbox-inner) {
  border-color: #059669;
}
</style>
