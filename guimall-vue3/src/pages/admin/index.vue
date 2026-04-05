<template>
  <div class="p-2 box admin-dashboard">
    <!-- 欢迎 -->
    <a-card :bordered="false" class="mb-5 dashboard-welcome">
      <div class="flex flex-wrap items-start justify-between gap-4">
        <div>
          <div class="text-lg font-semibold text-gray-900">
            {{ greeting }}，{{ displayName }}
          </div>
          <p class="mt-1 mb-0 text-sm text-gray-500">
            {{ todayText }} · 桂品商城管理后台工作台
          </p>
        </div>
      </div>
    </a-card>

    <!-- 销售概览 -->
    <div class="mb-2 text-sm font-medium text-gray-700">销售概览</div>
    <a-row :gutter="[16, 16]" class="mb-5">
      <a-col :xs="24" :sm="12" :lg="6" v-for="item in salesStatCards" :key="item.key">
        <a-card :bordered="false" class="stat-card stat-card--sales h-full" :body-style="{ padding: '20px 24px' }">
          <div class="flex items-start justify-between gap-3">
            <div class="min-w-0 flex-1">
              <div class="text-sm text-gray-500">{{ item.title }}</div>
              <div class="mt-2 text-2xl font-semibold tabular-nums text-gray-900">
                <template v-if="item.valueType === 'currency'">
                  <span class="text-lg font-medium text-gray-500">¥</span>{{ formatMoney(item.value) }}
                </template>
                <template v-else>
                  {{ item.value }}
                  <span v-if="item.suffix" class="text-base font-normal text-gray-400">{{ item.suffix }}</span>
                </template>
              </div>
              <div v-if="item.trend !== 0 && item.trendLabel" class="mt-2 text-xs" :class="item.trend >= 0 ? 'text-emerald-600' : 'text-rose-600'">
                {{ item.trend >= 0 ? '↑' : '↓' }} {{ Math.abs(item.trend) }}% {{ item.trendLabel }}
              </div>
            </div>
            <div
              class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg text-xl"
              :class="item.iconWrap"
            >
              <component :is="item.icon" />
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 近7日销售额（带坐标轴） -->
    <a-card :bordered="false" class="mb-5">
      <template #title>
        <span class="text-base font-medium">近7日销售额</span>
        <span class="ml-2 text-xs font-normal text-gray-400">（元）</span>
      </template>
      <div class="sales-chart-with-axis flex flex-col gap-2 sm:flex-row sm:items-stretch">
        <!-- 纵轴标题 -->
        <div
          class="sales-chart-y-title hidden shrink-0 items-center justify-center border-r border-gray-100 pr-2 sm:flex sm:w-8"
        >
          <span class="text-xs leading-tight text-gray-400 [writing-mode:vertical-rl]">销售额（元）</span>
        </div>
        <div class="min-w-0 flex-1">
          <div class="flex gap-2">
            <!-- Y 轴刻度（与横向网格线同一比例：上为 max，下为 0） -->
            <div
              class="sales-chart-y-ticks relative w-[3.25rem] shrink-0 text-right"
              :style="{ height: `${plotHeightPx}px` }"
            >
              <span
                v-for="(tick, i) in yAxisTicks"
                :key="i"
                class="absolute right-0 text-[11px] leading-none text-gray-500 tabular-nums"
                :style="{
                  top: `${(1 - tick.ratio) * 100}%`,
                  transform: 'translateY(-50%)'
                }"
              >
                {{ tick.label }}
              </span>
            </div>
            <!-- 绘图区：网格 + 柱 -->
            <div class="min-w-0 flex-1 overflow-x-auto">
              <div class="relative" :style="{ height: `${plotHeightPx}px` }">
                <!-- 横向网格线（与 Y 轴刻度对齐：0% 顶部为最大值，100% 底部为 0） -->
                <div class="pointer-events-none absolute inset-0">
                  <div
                    v-for="n in 5"
                    :key="n"
                    class="absolute left-0 right-0 border-t border-dashed border-gray-200"
                    :style="{ top: `${(n - 1) * 25}%` }"
                  />
                </div>
                <!-- 柱状图 -->
                <div
                  class="absolute inset-0 flex items-end justify-between gap-1.5 px-0.5 sm:gap-2 sm:px-2"
                >
                  <div
                    v-for="(d, i) in last7DaysSales"
                    :key="i"
                    class="flex h-full min-w-[36px] flex-1 flex-col justify-end sm:min-w-[44px] sm:max-w-[88px]"
                  >
                    <a-tooltip :title="`¥${formatMoney(d.amount)}`">
                      <div
                        class="sales-chart__bar w-full rounded-t-md bg-gradient-to-t from-blue-600 to-sky-400 transition-all hover:opacity-90"
                        :style="{ height: `${barHeightRatio(d.amount)}%` }"
                      />
                    </a-tooltip>
                  </div>
                </div>
              </div>
              <!-- X 轴：日期 -->
              <div class="mt-0 flex justify-between gap-1 border-t border-gray-200 pt-2 sm:gap-2">
                <span
                  v-for="(d, i) in last7DaysSales"
                  :key="i"
                  class="min-w-0 flex-1 text-center text-[11px] text-gray-600 sm:text-xs"
                >
                  {{ d.label }}
                </span>
              </div>
              <div class="mt-1 text-center text-[11px] text-gray-400">日期</div>
            </div>
          </div>
          <p class="mb-0 mt-2 text-center text-[11px] text-gray-400 sm:hidden">纵轴：销售额（元）</p>
        </div>
      </div>
    </a-card>

    <!-- 运营数据 -->
    <div class="mb-2 text-sm font-medium text-gray-700">运营数据</div>
    <a-row :gutter="[16, 16]" class="mb-5">
      <a-col :xs="24" :sm="12" :lg="8" v-for="item in operationStatCards" :key="item.key">
        <a-card :bordered="false" class="stat-card h-full" :body-style="{ padding: '20px 24px' }">
          <div class="flex items-start justify-between gap-3">
            <div>
              <div class="text-sm text-gray-500">{{ item.title }}</div>
              <div class="mt-2 text-2xl font-semibold tabular-nums text-gray-900">
                {{ item.value }}
                <span v-if="item.suffix" class="text-base font-normal text-gray-400">{{ item.suffix }}</span>
              </div>
              <div class="mt-2 text-xs text-gray-400">
                实时统计
              </div>
            </div>
            <div
              class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg text-xl"
              :class="item.iconWrap"
            >
              <component :is="item.icon" />
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="[16, 16]">
      <!-- 快捷入口 -->
      <a-col :xs="24" :lg="10">
        <a-card :bordered="false" title="快捷入口" class="h-full">
          <div class="grid grid-cols-2 gap-3 sm:grid-cols-3">
            <a-button
              v-for="link in quickLinks"
              :key="link.path"
              class="quick-link-btn !h-auto !py-3 !px-2"
              @click="router.push(link.path)"
            >
              <span class="flex flex-col items-center gap-2">
                <component :is="link.icon" class="text-lg text-blue-600" />
                <span class="text-xs">{{ link.label }}</span>
              </span>
            </a-button>
          </div>
        </a-card>
      </a-col>

      <!-- 待办概览 -->
      <a-col :xs="24" :lg="14">
        <a-card :bordered="false" title="待办概览" class="h-full">
          <a-list :data-source="todoItems" size="small" :split="false">
            <template #renderItem="{ item }">
              <a-list-item class="!px-0">
                <a-list-item-meta>
                  <template #title>
                    <span class="text-gray-800">{{ item.title }}</span>
                  </template>
                  <template #description>
                    <span class="text-xs text-gray-400">{{ item.hint }}</span>
                  </template>
                </a-list-item-meta>
                <template #actions>
                  <a-tag :color="item.tagColor">{{ item.count }} 项</a-tag>
                  <a-button type="link" size="small" class="!px-1" @click="router.push(item.path)">处理</a-button>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>

    <!-- 最近订单 -->
    <a-card :bordered="false" title="最近订单" class="mt-4">
      <template #extra>
        <a-button type="link" size="small" @click="router.push('/admin/oms/order')">查看全部</a-button>
      </template>
      <a-table
        :dataSource="recentOrders"
        :columns="orderColumns"
        :pagination="false"
        rowKey="id"
        size="small"
        bordered
        class="w-full"
      />
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, h, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { Tag } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import {
  ShoppingOutlined,
  CarOutlined,
  AppstoreOutlined,
  TeamOutlined,
  PlusOutlined,
  UnorderedListOutlined,
  GiftOutlined,
  UserOutlined,
  DollarOutlined,
  AccountBookOutlined,
  PayCircleOutlined
} from '@ant-design/icons-vue'
import { fetchOrderList } from '@/api/admin/order'
import { fetchProductList } from '@/api/admin/product'
import { fetchFarmerList } from '@/api/admin/farmer'

const router = useRouter()
const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)

const displayName = computed(() => {
  const u = userInfo.value
  if (!u || typeof u !== 'object') return '管理员'
  return u.nickname || u.username || '管理员'
})

const hour = new Date().getHours()
const greeting = computed(() => {
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const todayText = computed(() => {
  const d = new Date()
  const w = ['日', '一', '二', '三', '四', '五', '六']
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${w[d.getDay()]}`
})

/** 金额展示 */
function formatMoney(n) {
  return Number(n).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

/** 柱状图绘图区高度（px），与左侧 Y 轴刻度对齐 */
const plotHeightPx = 192

/** 将原始最大值向上取整为「好看」的坐标上限，便于阅读 */
function niceChartMax(raw) {
  if (raw <= 0) return 10000
  const exp = Math.floor(Math.log10(raw))
  const base = 10 ** exp
  const n = raw / base
  const nice = n <= 1 ? 1 : n <= 2 ? 2 : n <= 5 ? 5 : 10
  return nice * base
}

function formatAxisY(n) {
  if (n === 0) return '0'
  if (n >= 10000) {
    const w = n / 10000
    return `${w % 1 === 0 ? w.toFixed(0) : w.toFixed(1)}万`
  }
  if (n >= 1000) {
    const k = n / 1000
    return `${k % 1 === 0 ? k.toFixed(0) : k.toFixed(1)}k`
  }
  return String(Math.round(n))
}

// ============ Reactive data (initially zero / empty) ============

const todaySales = ref(0)
const monthSales = ref(0)
const todayOrders = ref(0)
const pendingOrders = ref(0)
const productCount = ref(0)
const farmerCount = ref(0)

const salesStatCards = computed(() => [
  {
    key: 'todaySales',
    title: '今日销售额',
    value: todaySales.value,
    valueType: 'currency',
    trend: 0,
    trendLabel: '较昨日',
    icon: DollarOutlined,
    iconWrap: 'bg-rose-50 text-rose-600'
  },
  {
    key: 'todayOrders',
    title: '今日订单数',
    value: todayOrders.value,
    suffix: '单',
    valueType: 'count',
    trend: 0,
    trendLabel: '较昨日',
    icon: ShoppingOutlined,
    iconWrap: 'bg-blue-50 text-blue-600'
  },
  {
    key: 'monthSales',
    title: '本月销售额',
    value: monthSales.value,
    valueType: 'currency',
    trend: 0,
    trendLabel: '较上月同期',
    icon: AccountBookOutlined,
    iconWrap: 'bg-orange-50 text-orange-600'
  },
  {
    key: 'pending',
    title: '待处理订单',
    value: pendingOrders.value,
    suffix: '单',
    valueType: 'count',
    trend: 0,
    trendLabel: '',
    icon: PayCircleOutlined,
    iconWrap: 'bg-cyan-50 text-cyan-600'
  }
])

const operationStatCards = computed(() => [
  {
    key: 'products',
    title: '在售商品',
    value: productCount.value,
    suffix: '件',
    trend: 0,
    icon: AppstoreOutlined,
    iconWrap: 'bg-emerald-50 text-emerald-600'
  },
  {
    key: 'farmers',
    title: '签约农户',
    value: farmerCount.value,
    suffix: '户',
    trend: 0,
    icon: TeamOutlined,
    iconWrap: 'bg-violet-50 text-violet-600'
  },
  {
    key: 'members',
    title: '注册会员',
    value: '—',
    suffix: '',
    trend: 0,
    icon: UserOutlined,
    iconWrap: 'bg-amber-50 text-amber-600'
  }
])

/** 近7日销售额 */
const last7DaysSales = ref([])

const chartYMax = computed(() =>
  niceChartMax(Math.max(...last7DaysSales.value.map((d) => d.amount), 0))
)

const yAxisTicks = computed(() => {
  const max = chartYMax.value
  return [1, 0.75, 0.5, 0.25, 0].map((ratio) => ({
    ratio,
    value: max * ratio,
    label: formatAxisY(max * ratio)
  }))
})

/** 柱高占绘图区高度的比例（与左侧 Y 轴刻度一致） */
function barHeightRatio(amount) {
  if (chartYMax.value <= 0) return 0
  return Math.min(100, Math.max(0, (amount / chartYMax.value) * 100))
}

const quickLinks = [
  { label: '添加商品', path: '/admin/pms/product/add', icon: PlusOutlined },
  { label: '订单列表', path: '/admin/oms/order', icon: UnorderedListOutlined },
  { label: '发货管理', path: '/admin/oms/order/deliverOrderList', icon: CarOutlined },
  { label: '优惠券', path: '/admin/sms/coupon', icon: GiftOutlined },
  { label: '农户列表', path: '/admin/farmer', icon: UserOutlined },
  { label: '商品管理', path: '/admin/pms/product', icon: AppstoreOutlined }
]

const todoItems = ref([
  {
    title: '待发货订单',
    hint: '买家已付款，等待出库发货',
    count: 0,
    tagColor: 'orange',
    path: '/admin/oms/order/deliverOrderList'
  },
  {
    title: '待付款订单',
    hint: '等待买家付款',
    count: 0,
    tagColor: 'gold',
    path: '/admin/oms/order'
  },
  {
    title: '农户管理',
    hint: '已签约农户数',
    count: 0,
    tagColor: 'blue',
    path: '/admin/farmer'
  }
])

const ORDER_STATUS_LABEL = {
  0: '待付款',
  1: '待发货',
  2: '已发货',
  3: '已完成',
  4: '已关闭',
  5: '无效订单'
}
const ORDER_STATUS_COLOR = {
  0: 'orange',
  1: 'blue',
  2: 'cyan',
  3: 'success',
  4: 'default',
  5: 'default'
}

const recentOrders = ref([])

const orderColumns = [
  { title: '订单号', dataIndex: 'orderSn', align: 'center', width: 200 },
  { title: '买家', dataIndex: 'memberUsername', align: 'center', width: 120 },
  {
    title: '金额',
    dataIndex: 'payAmount',
    align: 'center',
    width: 110,
    customRender: ({ text }) => text != null ? `¥${formatMoney(text)}` : '—'
  },
  {
    title: '状态',
    dataIndex: 'status',
    align: 'center',
    width: 100,
    customRender: ({ text }) =>
      h(Tag, { color: ORDER_STATUS_COLOR[text] || 'default' }, () => ORDER_STATUS_LABEL[text] || '-')
  },
  { title: '下单时间', dataIndex: 'createTime', align: 'center' }
]

// ============ Data fetching ============

/** Format a Date to YYYY-MM-DD */
function fmtDate(d) {
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${dd}`
}

async function loadDashboardData() {
  const now = new Date()
  const todayStr = fmtDate(now)
  const monthStart = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-01`

  try {
    // Fetch a large page of orders for stats calculation
    const orderRes = await fetchOrderList({ current: 1, size: 500 })
    if (orderRes?.success && orderRes?.data) {
      const orders = orderRes.data

      // Today's stats
      const todayOrdersList = orders.filter(o => o.createTime && o.createTime.startsWith(todayStr))
      todayOrders.value = todayOrdersList.length
      todaySales.value = todayOrdersList.reduce((sum, o) => sum + (Number(o.payAmount) || 0), 0)

      // Month stats
      const monthOrders = orders.filter(o => o.createTime && o.createTime >= monthStart)
      monthSales.value = monthOrders.reduce((sum, o) => sum + (Number(o.payAmount) || 0), 0)

      // Pending orders (status 0=待付款, 1=待发货)
      const pendingList = orders.filter(o => o.status === 0 || o.status === 1)
      pendingOrders.value = pendingList.length

      // Todo items counts
      const waitShipCount = orders.filter(o => o.status === 1).length
      const waitPayCount = orders.filter(o => o.status === 0).length
      todoItems.value[0].count = waitShipCount
      todoItems.value[1].count = waitPayCount

      // Recent 5 orders
      recentOrders.value = orders.slice(0, 5)

      // Last 7 days chart
      const dayMap = {}
      for (let i = 6; i >= 0; i--) {
        const d = new Date(now)
        d.setDate(d.getDate() - i)
        const key = fmtDate(d)
        dayMap[key] = { label: `${d.getMonth() + 1}/${d.getDate()}`, amount: 0 }
      }
      orders.forEach(o => {
        if (o.createTime) {
          const dateKey = o.createTime.substring(0, 10)
          if (dayMap[dateKey]) {
            dayMap[dateKey].amount += Number(o.payAmount) || 0
          }
        }
      })
      last7DaysSales.value = Object.values(dayMap)
    }
  } catch (e) {
    console.warn('加载订单数据失败', e)
  }

  try {
    const productRes = await fetchProductList({ current: 1, size: 1 })
    if (productRes?.success) {
      productCount.value = productRes.total || 0
    }
  } catch (e) {
    console.warn('加载商品数据失败', e)
  }

  try {
    const farmerRes = await fetchFarmerList({ current: 1, size: 1 })
    if (farmerRes?.success) {
      farmerCount.value = farmerRes.total || 0
      todoItems.value[2].count = farmerRes.total || 0
    }
  } catch (e) {
    console.warn('加载农户数据失败', e)
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard-welcome {
  background: linear-gradient(135deg, #f8fafc 0%, #eff6ff 100%);
  border: 1px solid #e2e8f0;
}

.stat-card {
  border: 1px solid #f1f5f9;
  transition: box-shadow 0.2s ease, border-color 0.2s ease;
}

.stat-card:hover {
  border-color: #e2e8f0;
  box-shadow: 0 4px 14px rgba(15, 23, 42, 0.06);
}

.stat-card--sales {
  border-color: #ffe4e6;
  background: linear-gradient(180deg, #fffafb 0%, #ffffff 100%);
}

.sales-chart-with-axis {
  padding: 4px 0;
}

.sales-chart__bar {
  min-height: 2px;
}

.quick-link-btn {
  border-color: #e2e8f0;
}

.quick-link-btn:hover {
  border-color: #93c5fd;
  color: inherit;
}
</style>
