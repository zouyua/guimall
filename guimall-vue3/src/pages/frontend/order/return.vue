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
          <router-link to="/my-orders" class="hover:text-emerald-600" active-class="text-emerald-600">我的订单</router-link>
        </div>
      </div>
    </nav>

    <main class="max-w-screen-lg mx-auto px-4 py-12">
      <a-spin :spinning="loading" size="large">
        <div v-if="orderDetail" class="space-y-6">
          <!-- 页面标题 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h1 class="text-2xl font-black text-stone-900 mb-2">申请退货</h1>
            <p class="text-stone-400">订单编号：<span class="font-mono font-bold text-stone-600">{{ orderDetail.orderSn }}</span></p>
          </div>

          <!-- 订单商品 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6">退货商品</h2>
            <div class="space-y-4">
              <div v-for="item in orderDetail.items" :key="item.id"
                class="flex items-center gap-4 p-4 rounded-2xl bg-stone-50">
                <img :src="item.productPic" class="w-20 h-20 rounded-xl object-cover border border-stone-200" />
                <div class="flex-1">
                  <h3 class="font-bold text-stone-800">{{ item.productName }}</h3>
                  <p class="text-xs text-stone-400 mt-1">{{ item.productAttr || '默认规格' }}</p>
                </div>
                <div class="text-right">
                  <p class="text-emerald-600 font-bold">¥{{ item.productPrice }}</p>
                  <p class="text-xs text-stone-400 mt-1">× {{ item.productQuantity }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 退货表单 -->
          <div class="bg-white rounded-3xl p-8 shadow-sm border border-stone-100">
            <h2 class="text-lg font-bold text-stone-900 mb-6">退货信息</h2>
            <a-form :model="formData" layout="vertical">
              <a-form-item label="退货原因" required>
                <a-select v-model:value="formData.reason" placeholder="请选择退货原因" size="large">
                  <a-select-option v-for="reason in returnReasons" :key="reason.id" :value="reason.name">
                    {{ reason.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>

              <a-form-item label="问题描述">
                <a-textarea v-model:value="formData.description" placeholder="请详细描述退货原因（选填）"
                  :rows="4" :maxlength="500" show-count />
              </a-form-item>

              <a-form-item label="退货金额" required>
                <a-input v-model:value="formData.returnAmount" size="large" prefix="¥" readonly
                  class="font-bold text-emerald-600" />
              </a-form-item>

              <a-form-item label="退货人姓名" required>
                <a-input v-model:value="formData.returnName" placeholder="请输入退货人姓名" size="large" />
              </a-form-item>

              <a-form-item label="退货人电话" required>
                <a-input v-model:value="formData.returnPhone" placeholder="请输入退货人电话" size="large" />
              </a-form-item>

              <a-form-item label="证明图片（选填）">
                <p class="text-xs text-stone-400 mb-2">如有商品质量问题，请上传相关图片</p>
                <a-input v-model:value="formData.proofPics" placeholder="图片URL，多张用逗号分隔" size="large" />
              </a-form-item>
            </a-form>
          </div>

          <!-- 操作按钮 -->
          <div class="flex justify-center gap-4">
            <button @click="$router.back()" class="bg-stone-100 text-stone-600 px-8 py-3 rounded-2xl font-bold hover:bg-stone-200 transition-all">
              取消
            </button>
            <button @click="handleSubmit" :disabled="submitting"
              class="bg-orange-600 text-white px-8 py-3 rounded-2xl font-bold hover:bg-orange-700 transition-all shadow-lg shadow-orange-200 disabled:opacity-50">
              {{ submitting ? '提交中...' : '提交申请' }}
            </button>
          </div>
        </div>
      </a-spin>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getOrderDetail } from '@/api/frontend/order'
import { getReturnReasons, createReturnApply } from '@/api/frontend/orderReturn'
import { getMemberId, isMemberLoggedIn } from '@/composables/member'

const route = useRoute()
const router = useRouter()

// 登录检查
if (!isMemberLoggedIn()) {
  message.warning('请先登录')
  router.push('/member/login')
}

const memberId = getMemberId()
const orderId = route.query.orderId

const loading = ref(false)
const submitting = ref(false)
const orderDetail = ref(null)
const returnReasons = ref([])

const formData = ref({
  reason: undefined,
  description: '',
  returnAmount: '',
  returnName: '',
  returnPhone: '',
  proofPics: ''
})

const loadOrderDetail = async () => {
  if (!orderId) {
    message.error('订单ID不能为空')
    router.back()
    return
  }

  loading.value = true
  try {
    const res = await getOrderDetail(orderId, memberId)
    if (res.success) {
      orderDetail.value = res.data
      // 默认退货金额为实付金额
      formData.value.returnAmount = res.data.payAmount
      formData.value.returnName = res.data.receiverName
      formData.value.returnPhone = res.data.receiverPhone
    } else {
      message.error(res.message || '加载订单详情失败')
      router.back()
    }
  } catch (e) {
    message.error('加载订单详情失败')
    router.back()
  } finally {
    loading.value = false
  }
}

const loadReturnReasons = async () => {
  try {
    const res = await getReturnReasons()
    if (res.success) {
      returnReasons.value = res.data || []
    }
  } catch (e) {
    console.error('加载退货原因失败', e)
  }
}

const handleSubmit = async () => {
  if (!formData.value.reason) {
    message.warning('请选择退货原因')
    return
  }
  if (!formData.value.returnName) {
    message.warning('请输入退货人姓名')
    return
  }
  if (!formData.value.returnPhone) {
    message.warning('请输入退货人电话')
    return
  }

  submitting.value = true
  try {
    const data = {
      orderId: orderDetail.value.id,
      memberId,
      productId: null, // 整单退货，不指定商品
      reason: formData.value.reason,
      description: formData.value.description,
      returnAmount: parseFloat(formData.value.returnAmount),
      returnName: formData.value.returnName,
      returnPhone: formData.value.returnPhone,
      proofPics: formData.value.proofPics
    }

    const res = await createReturnApply(data)
    if (res.success) {
      message.success('退货申请已提交，请等待审核')
      router.push('/my-orders')
    } else {
      message.error(res.message || '提交失败')
    }
  } catch (e) {
    message.error('提交失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadOrderDetail()
  loadReturnReasons()
})
</script>

<style scoped>
:deep(.ant-spin-container) {
  min-height: 400px;
}
</style>
