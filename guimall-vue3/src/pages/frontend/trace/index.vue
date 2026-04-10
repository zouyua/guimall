<template>
  <div class="min-h-screen bg-[#FDFCF8] text-[#2D3436] font-sans pb-20">
    <!-- Navigation (Matches existing Index.vue style) -->
    <nav class="bg-white/90 backdrop-blur-md border-b border-stone-200 sticky top-0 z-50">
      <div class="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
        <router-link to="/" class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-emerald-600 rounded-xl flex items-center justify-center shadow-lg shadow-emerald-200">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <span class="text-2xl font-black tracking-tighter text-emerald-900 uppercase">Guimall</span>
        </router-link>

        <div class="flex items-center md:order-2 space-x-4">
          <!-- 已登录：显示会员信息 -->
          <template v-if="memberLoggedIn">
            <MemberBadge :nickname="memberNickname" :avatar="memberAvatar" />
            <router-link to="/cart" class="text-stone-600 hover:text-emerald-600 transition-colors flex items-center">
              <a-badge :count="cartStore.cartCount" :offset="[-2, 2]" size="small">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" /></svg>
              </a-badge>
            </router-link>
            <button @click="handleLogout" class="text-stone-500 hover:text-red-500 font-medium transition-colors">退出</button>
          </template>
          <!-- 未登录：显示登录按钮 -->
          <template v-else>
            <button @click="$router.push('/member/login')" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">登录</button>
          </template>
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

    <main class="max-w-screen-xl mx-auto px-4 mt-8">
      <!-- Hero Section: Product & Farmer -->
      <a-spin :spinning="loading">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
          <div class="lg:col-span-7 space-y-6">
            <div class="relative group overflow-hidden rounded-[2.5rem] shadow-2xl border-4 border-white">
              <img :src="product.pic || 'https://via.placeholder.com/800x400?text=Guimall+Traceability'" :alt="product.name" class="w-full h-[450px] object-cover transition-transform duration-700 group-hover:scale-105" />
              <div class="absolute top-6 left-6 bg-emerald-600 text-white px-5 py-2 rounded-2xl text-sm font-bold shadow-lg">
                地标认证农产品
              </div>
            </div>

            <div class="bg-white p-10 rounded-[2.5rem] shadow-sm border border-stone-100">
              <div class="flex items-start justify-between mb-4">
                <div>
                  <h1 class="text-4xl font-black text-stone-900 mb-2">{{ product.name }}</h1>
                  <p class="text-lg text-emerald-600 font-bold mb-6">溯源编码：TRC-{{ productId }}-{{ product.id || 'N/A' }}</p>
                </div>
                <div class="text-right">
                  <p class="text-xs text-stone-400 mb-1 uppercase tracking-widest">溯源查询次数</p>
                  <p class="text-3xl font-black text-emerald-600">{{ scanCount }}</p>
                </div>
              </div>

              <div class="flex flex-wrap gap-4 mb-8">
                <span v-for="tag in ['正宗原产', '无农药残留', '手工采摘', '全程质检']" :key="tag" class="flex items-center text-sm font-bold text-emerald-700 bg-emerald-50 px-4 py-2 rounded-xl">
                  <svg class="w-4 h-4 mr-1.5" fill="currentColor" viewBox="0 0 20 20"><path d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"/></svg>
                  {{ tag }}
                </span>
              </div>

              <div class="border-t border-stone-100 pt-8 flex items-center justify-between">
                <div class="flex items-center space-x-4">
                  <img :src="farmer.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Farmer'" class="w-16 h-16 rounded-2xl border-2 border-emerald-100 p-0.5" />
                  <div>
                    <h3 class="text-xl font-black text-stone-900">{{ farmer.name }}</h3>
                    <p class="text-stone-500 font-medium">{{ farmer.farmName }} · 签约种植户</p>
                  </div>
                </div>
                <div class="flex gap-2">
                   <button @click="showContact = true" class="px-6 py-3 bg-stone-100 text-stone-600 rounded-2xl font-bold hover:bg-stone-200 transition-all">联系农户</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Sidebar: Origin Detail -->
          <div class="lg:col-span-5 space-y-6">
            <div class="bg-emerald-900 text-emerald-50 p-10 rounded-[2.5rem] shadow-xl relative overflow-hidden group">
              <div class="absolute -right-10 -top-10 w-40 h-40 bg-emerald-800 rounded-full opacity-50 group-hover:scale-110 transition-transform duration-1000"></div>
              <div class="relative z-10">
                <div class="flex items-center space-x-2 mb-6">
                  <svg class="w-6 h-6 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/><path d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
                  <h2 class="text-xl font-bold uppercase tracking-widest">Origin Archive</h2>
                </div>
                <h3 class="text-4xl font-black mb-6">{{ origin.originName || '桂林核心产区' }}</h3>
                <p class="text-emerald-100/70 leading-relaxed mb-8 text-lg">
                  {{ origin.description || '该产地位于桂林漓江流域核心保护区，环境优美，水质清澈，常年云雾缭绕，是得天独厚的绿色农产品培育基。' }}
                </p>
                <div class="space-y-4">
                  <div v-for="(val, label) in originInfoMap" :key="label"
                       class="flex justify-between items-center py-3 border-b border-emerald-800/50">
                    <span class="text-emerald-400 font-bold">{{ label }}</span>
                    <span class="font-bold">{{ val || '-' }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Certification Badges -->
            <div class="bg-white p-8 rounded-[2.5rem] shadow-sm border border-stone-100">
               <h4 class="text-stone-400 font-bold text-xs uppercase tracking-widest mb-6">官方权威认证</h4>
               <div class="grid grid-cols-2 gap-6">
                  <div v-for="cert in certifications" :key="cert.name" class="flex items-center gap-3">
                     <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
                        <img :src="cert.icon" class="w-6 h-6" />
                     </div>
                     <span class="text-sm font-bold text-stone-700">{{ cert.name }}</span>
                  </div>
               </div>
            </div>
          </div>
        </div>
      </a-spin>

      <!-- Lifecycle Timeline -->
      <div class="mt-24">
        <div class="flex items-center justify-between mb-16">
          <div>
            <h2 class="text-4xl font-black text-stone-900 mb-2">成长轨迹</h2>
            <div class="w-16 h-2 bg-emerald-500 rounded-full"></div>
          </div>
          <div class="text-right">
             <span class="px-4 py-2 bg-stone-100 rounded-xl text-stone-400 font-bold text-sm uppercase tracking-widest">Growth Timeline</span>
          </div>
        </div>

        <div class="relative">
          <!-- Timeline Line -->
          <div class="absolute left-10 md:left-1/2 top-0 bottom-0 w-1.5 bg-stone-100 transform -translate-x-1/2 hidden md:block"></div>

          <div v-if="traceRecords.length > 0" class="space-y-16">
            <div v-for="(record, index) in traceRecords" :key="record.id"
                 class="relative flex flex-col md:flex-row items-start md:items-center"
                 :class="index % 2 === 0 ? 'md:flex-row-reverse' : ''">

              <!-- Timeline Marker -->
              <div class="absolute left-10 md:left-1/2 w-8 h-8 bg-white rounded-full border-4 border-emerald-500 shadow-xl shadow-emerald-100 transform -translate-x-1/2 z-10 top-2 md:top-auto flex items-center justify-center">
                 <div class="w-2 h-2 bg-emerald-500 rounded-full"></div>
              </div>

              <div class="md:w-1/2 pl-20 md:pl-0" :class="index % 2 === 0 ? 'md:pl-16' : 'md:pr-16 text-right'">
                <div class="bg-white p-8 rounded-[2.5rem] shadow-sm border border-stone-100 hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 group">
                  <div class="flex items-center gap-3 mb-4" :class="index % 2 !== 0 ? 'justify-end' : ''">
                    <span class="px-3 py-1 rounded-lg bg-emerald-50 text-emerald-600 text-xs font-black uppercase tracking-wider">
                      {{ record.recordType }}
                    </span>
                    <span class="text-sm text-stone-300 font-bold font-mono">{{ record.recordTime }}</span>
                  </div>
                  <h4 class="text-2xl font-black text-stone-800 mb-2">{{ record.title }}</h4>
                  <p v-if="record.farmerName" class="text-sm text-emerald-600 font-semibold mb-4">操作人：{{ record.farmerName }}</p>
                  <p class="text-stone-500 leading-relaxed text-lg mb-6">{{ record.content }}</p>

                  <div v-if="record.pic" class="overflow-hidden rounded-3xl aspect-video bg-stone-50">
                    <img :src="record.pic" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" />
                  </div>
                </div>
              </div>
              <div class="md:w-1/2 hidden md:block"></div>
            </div>
          </div>

          <!-- Empty Records Placeholder -->
          <div v-else class="py-20 text-center bg-white rounded-[3rem] border-2 border-dashed border-stone-100">
             <div class="w-20 h-20 bg-stone-50 rounded-full flex items-center justify-center mx-auto mb-6">
                <svg class="w-10 h-10 text-stone-200" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
             </div>
             <p class="text-stone-400 font-bold">成长数据采集中，敬请期待...</p>
          </div>
        </div>
      </div>

      <!-- Certification Footer (Replacing old one) -->
      <div class="mt-32 grid md:grid-cols-3 gap-8">
         <div class="md:col-span-2 bg-emerald-900 rounded-[3rem] p-12 text-white flex flex-col md:flex-row items-center gap-10">
            <div class="w-32 h-32 bg-emerald-800 rounded-3xl flex items-center justify-center shrink-0">
               <svg class="w-16 h-16 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
            </div>
            <div>
               <h3 class="text-3xl font-black mb-4">区块链溯源技术保障</h3>
               <p class="text-emerald-200/60 text-lg leading-relaxed">每一份农产品都拥有唯一的数字身份 ID。通过区块链分布式账本技术，确保从田间到餐桌的全生命周期数据不可篡改，实现真正的诚信溯源。</p>
            </div>
         </div>
         <div class="bg-orange-50 rounded-[3rem] p-10 border border-orange-100 flex flex-col justify-center">
            <h4 class="text-orange-900 font-black text-xl mb-4">扫码验证正品</h4>
            <p class="text-orange-700/70 mb-8">您可以扫描包装上的 QR 码，实时核对产品批次与农残检测报告。</p>
            <div class="w-24 h-24 bg-white p-2 rounded-2xl mx-auto shadow-sm border border-orange-100">
               <img v-if="qrcodeUrl" :src="qrcodeUrl" class="w-full h-full object-contain" alt="溯源二维码" />
               <div v-else class="w-full h-full bg-stone-50 rounded-lg flex items-center justify-center">
                  <span class="text-xs text-stone-300">暂无</span>
               </div>
            </div>
         </div>
      </div>
    </main>

    <!-- Footer (Simplified) -->
    <footer class="mt-20 py-12 text-center text-stone-300 text-sm font-bold uppercase tracking-[0.2em]">
      &copy; 2026 GUIMALL · Guangxi Guilin Agriculture Empowerment Platform
    </footer>

    <!-- 联系农户弹窗 -->
    <div v-if="showContact" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm" @click.self="showContact = false">
      <div class="bg-white rounded-[2.5rem] p-10 shadow-2xl w-full max-w-md mx-4 relative">
        <button @click="showContact = false" class="absolute top-6 right-6 w-10 h-10 bg-stone-100 rounded-xl flex items-center justify-center text-stone-400 hover:bg-stone-200 transition-all">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M6 18L18 6M6 6l12 12" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
        </button>
        <div class="flex items-center gap-4 mb-8">
          <img :src="farmer.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Farmer'" class="w-16 h-16 rounded-2xl border-2 border-emerald-100" />
          <div>
            <h3 class="text-2xl font-black text-stone-900">{{ farmer.name }}</h3>
            <p class="text-stone-400">{{ farmer.farmName }}</p>
          </div>
        </div>
        <div class="space-y-4">
          <div v-if="farmer.phone" class="flex items-center gap-4 p-5 bg-emerald-50 rounded-2xl">
            <div class="w-12 h-12 bg-emerald-600 rounded-xl flex items-center justify-center shrink-0">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
            </div>
            <div>
              <p class="text-xs text-stone-400 font-bold uppercase tracking-widest mb-0.5">联系电话</p>
              <a :href="'tel:' + farmer.phone" class="text-2xl font-black text-emerald-600 hover:text-emerald-700">{{ farmer.phone }}</a>
            </div>
          </div>
          <div v-else class="p-5 bg-stone-50 rounded-2xl text-center text-stone-400 font-bold">
            暂未提供联系方式
          </div>
          <p class="text-xs text-stone-300 text-center">点击电话号码可直接拨打 · 平台已对农户资质进行审核</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTraceDetail } from '@/api/frontend/product'
import { isMemberLoggedIn, getMemberInfo, removeMemberInfo } from '@/composables/member'
import { removeMemberToken } from '@/composables/cookie'
import { useCartStore } from '@/stores/cart'
import { useMemberLevelStore } from '@/stores/memberLevel'
import MemberBadge from '@/components/MemberBadge.vue'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const memberLevelStore = useMemberLevelStore()
const productId = route.params.productId

const loading = ref(false)

// 会员登录状态 (同首页逻辑)
const memberLoggedIn = ref(isMemberLoggedIn())
const memberNickname = ref('')
const memberAvatar = ref('')

const initMemberStatus = () => {
  memberLoggedIn.value = isMemberLoggedIn()
  if (memberLoggedIn.value) {
    const info = getMemberInfo()
    memberNickname.value = info?.nickname || info?.username || '会员'
    memberAvatar.value = info?.icon || ''
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
}

const product = ref({
  id: '',
  name: '加载中...',
  subTitle: '',
  pic: '',
  farmerId: ''
})

const farmer = ref({
  id: '',
  name: '加载中...',
  farmName: '',
  avatar: '',
  phone: ''
})

const showContact = ref(false)

const origin = ref({
  id: '',
  originName: '',
  description: '',
  province: '',
  city: '',
  region: '',
  altitude: '',
  sunshineHours: '',
  soilType: ''
})

const scanCount = ref(0)
const traceRecords = ref([])
const qrcodeUrl = ref('')

// 产地信息映射：只展示有值的字段，不再硬编码
const originInfoMap = computed(() => {
  const map = {}
  const loc = (origin.value.province || '') + (origin.value.city || '') + (origin.value.region || '')
  if (loc) map['地理位置'] = loc
  if (origin.value.altitude) map['海拔高度'] = origin.value.altitude
  if (origin.value.sunshineHours) map['日照时间'] = origin.value.sunshineHours
  if (origin.value.soilType) map['土壤成分'] = origin.value.soilType
  return map
})

const loadTraceDetail = async () => {
  loading.value = true
  try {
    const res = await getTraceDetail(productId)
    if (res.success) {
      const data = res.data
      // 映射后端数据到前端模型
      product.value = {
        id: data.productId,
        name: data.productName,
        subTitle: '产地直供 · 桂林特产',
        pic: data.productPic,
        farmerId: data.farmerId
      }

      farmer.value = {
        id: data.farmerId,
        name: data.farmerName || '签约农户',
        farmName: (data.originName || '桂林') + '助农基地',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + (data.farmerName || 'Farmer'),
        phone: data.farmerPhone || ''
      }

      origin.value = {
        id: data.originId,
        originName: data.originName,
        description: data.originDescription,
        province: data.province,
        city: data.city,
        region: data.region,
        altitude: data.altitude || '',
        sunshineHours: data.sunshineHours || '',
        soilType: data.soilType || ''
      }

      scanCount.value = data.scanCount || 0
      qrcodeUrl.value = data.qrcodeUrl || ''

      // 处理时间轴记录
      if (data.records && data.records.length > 0) {
        traceRecords.value = data.records.map((r, index) => ({
          id: index,
          recordType: r.recordTypeName || r.recordType || '记录',
          recordTime: r.recordTime || '2026-03-01',
          title: (r.recordTypeName || r.recordType || '记录') + '阶段',
          content: r.content || '暂无详细描述',
          pic: r.pic,
          farmerName: r.farmerName || data.farmerName || ''
        }))
      }
    }
  } catch (error) {
    console.error('Failed to load trace detail:', error)
  } finally {
    loading.value = false
  }
}

const certifications = [
  { name: '中国地理标志', icon: 'https://www.svgrepo.com/show/475456/medal.svg' },
  { name: '绿色食品认证', icon: 'https://www.svgrepo.com/show/475440/leaf.svg' },
  { name: '有机农产品', icon: 'https://www.svgrepo.com/show/475437/flower.svg' },
  { name: '助农帮扶', icon: 'https://www.svgrepo.com/show/475443/hand.svg' }
]

onMounted(() => {
  initMemberStatus()
  loadTraceDetail()
  if (memberLoggedIn.value) {
    cartStore.loadCartCount()
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=ZCOOL+XiaoWei&display=swap');

h1, h2, h3 {
  font-family: 'Inter', "Microsoft YaHei", sans-serif;
}

.font-serif-china {
  font-family: 'ZCOOL+XiaoWei', serif;
}

/* 顶栏导航选中时显示下划线 */
.nav-active span {
  width: 100% !important;
}
</style>
