<template>
  <div class="min-h-screen bg-stone-50 font-sans">
    <!-- 导航栏 -->
    <nav class="bg-white/90 backdrop-blur-md border-b border-stone-200 sticky top-0 z-50">
      <div class="max-w-screen-xl flex items-center justify-between mx-auto p-4">
        <a href="/" class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-emerald-600 rounded-xl flex items-center justify-center shadow-lg shadow-emerald-200">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <span class="text-2xl font-black tracking-tighter text-emerald-900 uppercase">Guimall</span>
        </a>
        <div class="flex items-center space-x-4">
          <router-link to="/" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">首页</router-link>
          <router-link to="/my-orders" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">我的订单</router-link>
          <router-link to="/cart" class="text-stone-600 hover:text-emerald-600 font-medium transition-colors">购物车</router-link>
        </div>
      </div>
    </nav>

    <!-- 主内容区 -->
    <div class="max-w-screen-xl mx-auto px-4 py-8">
      <div class="flex gap-8">

        <!-- 左侧用户卡片 -->
        <div class="w-72 shrink-0">
          <div class="bg-white rounded-3xl shadow-sm border border-stone-100 p-8 text-center sticky top-24">
            <!-- 头像 -->
            <div class="relative inline-block group">
              <a-upload
                :show-upload-list="false"
                :custom-request="handleAvatarUpload"
                accept="image/*"
              >
                <div class="w-28 h-28 rounded-full mx-auto overflow-hidden border-4 border-emerald-100 cursor-pointer group-hover:border-emerald-300 transition-colors relative">
                  <img :src="memberInfo.icon || defaultAvatar" class="w-full h-full object-cover" />
                  <div class="absolute inset-0 bg-black/30 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                    <CameraOutlined class="text-white text-xl" />
                  </div>
                </div>
              </a-upload>
            </div>
            <h3 class="mt-4 text-xl font-bold text-stone-800">{{ memberInfo.nickname || '用户' }}</h3>
            <p class="text-stone-400 text-sm mt-1">{{ memberInfo.phone || '暂无手机号' }}</p>
            <div class="mt-6 space-y-2">
              <div
                v-for="tab in tabs" :key="tab.key"
                @click="activeTab = tab.key"
                :class="activeTab === tab.key
                  ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                  : 'bg-stone-50 text-stone-600 border-transparent hover:bg-stone-100'"
                class="flex items-center gap-3 px-4 py-3 rounded-xl cursor-pointer border transition-all font-medium"
              >
                <component :is="tab.icon" />
                <span>{{ tab.label }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧内容区 -->
        <div class="flex-1 min-w-0">

          <!-- 个人资料 -->
          <div v-show="activeTab === 'profile'" class="bg-white rounded-3xl shadow-sm border border-stone-100 p-8">
            <h2 class="text-xl font-bold text-stone-800 mb-6 flex items-center gap-2">
              <UserOutlined class="text-emerald-600" />
              个人资料
            </h2>
            <a-form :model="profileForm" layout="horizontal" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
              <a-form-item label="昵称">
                <a-input v-model:value="profileForm.nickname" placeholder="请输入昵称" />
              </a-form-item>
              <a-form-item label="手机号">
                <a-input v-model:value="profileForm.phone" placeholder="请输入手机号" />
              </a-form-item>
              <a-form-item label="性别">
                <a-select v-model:value="profileForm.gender" placeholder="请选择">
                  <a-select-option :value="0">保密</a-select-option>
                  <a-select-option :value="1">男</a-select-option>
                  <a-select-option :value="2">女</a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item label="所在城市">
                <a-input v-model:value="profileForm.city" placeholder="请输入城市" />
              </a-form-item>
              <a-form-item label="职业">
                <a-input v-model:value="profileForm.job" placeholder="请输入职业" />
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 4 }">
                <a-button type="primary" @click="handleSaveProfile" class="bg-emerald-600 hover:bg-emerald-700 border-emerald-600">
                  保存修改
                </a-button>
              </a-form-item>
            </a-form>
          </div>

          <!-- 收货地址 -->
          <div v-show="activeTab === 'address'" class="bg-white rounded-3xl shadow-sm border border-stone-100 p-8">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-bold text-stone-800 flex items-center gap-2">
                <EnvironmentOutlined class="text-emerald-600" />
                收货地址
              </h2>
              <a-button type="primary" @click="openAddressModal(null)" class="bg-emerald-600 hover:bg-emerald-700 border-emerald-600">
                <PlusOutlined /> 新增地址
              </a-button>
            </div>

            <div v-if="addressList.length === 0" class="text-center py-16 text-stone-400">
              <EnvironmentOutlined class="text-4xl mb-4 block" />
              <p>暂无收货地址，请添加</p>
            </div>

            <div v-else class="space-y-4">
              <div v-for="addr in addressList" :key="addr.id"
                :class="addr.defaultStatus ? 'border-emerald-200 bg-emerald-50/50' : 'border-stone-100'"
                class="border rounded-2xl p-5 flex items-start justify-between transition-all hover:shadow-md">
                <div class="flex-1">
                  <div class="flex items-center gap-3 mb-2">
                    <span class="font-bold text-stone-800">{{ addr.name }}</span>
                    <span class="text-stone-500">{{ addr.phone }}</span>
                    <span v-if="addr.isDefault" class="bg-emerald-100 text-emerald-700 text-xs font-bold px-2 py-0.5 rounded-full">默认</span>
                  </div>
                  <p class="text-stone-500 text-sm">{{ addr.province }}{{ addr.city }}{{ addr.region }}{{ addr.detailAddress }}</p>
                </div>
                <div class="flex items-center gap-2 shrink-0 ml-4">
                  <a-button v-if="!addr.isDefault" size="small" @click="handleSetDefault(addr.id)">设为默认</a-button>
                  <a-button size="small" @click="openAddressModal(addr)">
                    <EditOutlined />
                  </a-button>
                  <a-popconfirm title="确定删除该地址吗？" @confirm="handleDeleteAddress(addr.id)">
                    <a-button size="small" danger>
                      <DeleteOutlined />
                    </a-button>
                  </a-popconfirm>
                </div>
              </div>
            </div>
          </div>

          <!-- 修改密码 -->
          <div v-show="activeTab === 'password'" class="bg-white rounded-3xl shadow-sm border border-stone-100 p-8">
            <h2 class="text-xl font-bold text-stone-800 mb-6 flex items-center gap-2">
              <LockOutlined class="text-emerald-600" />
              修改密码
            </h2>
            <a-form :model="passwordForm" layout="horizontal" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
              <a-form-item label="当前密码" required>
                <a-input-password v-model:value="passwordForm.oldPassword" placeholder="请输入当前密码" />
              </a-form-item>
              <a-form-item label="新密码" required>
                <a-input-password v-model:value="passwordForm.newPassword" placeholder="请输入新密码（至少6位）" />
              </a-form-item>
              <a-form-item label="确认密码" required>
                <a-input-password v-model:value="passwordForm.confirmPassword" placeholder="请再次输入新密码" />
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 4 }">
                <a-button type="primary" @click="handleChangePassword" class="bg-emerald-600 hover:bg-emerald-700 border-emerald-600">
                  修改密码
                </a-button>
              </a-form-item>
            </a-form>
          </div>

        </div>
      </div>
    </div>

    <!-- 新增/编辑地址弹窗 -->
    <a-modal
      v-model:open="addressModalVisible"
      :title="editingAddress ? '编辑地址' : '新增地址'"
      @ok="handleSaveAddress"
      :okButtonProps="{ class: 'bg-emerald-600 hover:bg-emerald-700 border-emerald-600' }"
    >
      <a-form :model="addressForm" layout="vertical" class="mt-4">
        <a-form-item label="收货人" required>
          <a-input v-model:value="addressForm.name" placeholder="请输入收货人姓名" />
        </a-form-item>
        <a-form-item label="手机号" required>
          <a-input v-model:value="addressForm.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="所在区域" required>
          <a-cascader
            v-model:value="addressRegionValue"
            :options="regionData"
            placeholder="请选择省/市/区"
            style="width: 100%"
            @change="onAddressRegionChange"
          />
        </a-form-item>
        <a-form-item label="详细地址" required>
          <a-textarea v-model:value="addressForm.detailAddress" :rows="2" placeholder="请输入详细地址（街道、门牌号等）" />
        </a-form-item>
        <a-form-item label="邮政编码">
          <a-input v-model:value="addressForm.postCode" placeholder="邮编（选填）" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Footer -->
    <footer class="bg-stone-900 text-stone-400 py-12 mt-16">
      <div class="max-w-screen-xl mx-auto px-4 text-center">
        <span class="text-lg font-black tracking-tighter text-white">GUI<span class="text-emerald-500">MALL</span></span>
        <p class="mt-2 text-sm">广西桂林助农电商平台</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  UserOutlined,
  EnvironmentOutlined,
  LockOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  CameraOutlined
} from '@ant-design/icons-vue'
import {
  getMemberInfoApi,
  updateMemberInfo,
  updateMemberPassword,
  getAddressList,
  addAddress,
  updateAddress,
  deleteAddress,
  setDefaultAddress
} from '@/api/frontend/member'
import { uploadFile } from '@/api/admin/upload'
import { getMemberInfo, isMemberLoggedIn, setMemberInfo } from '@/composables/member'
import regionData from '@/utils/regionData'

const router = useRouter()
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/128/3135/3135715.png'

// 标签页
const tabs = [
  { key: 'profile', label: '个人资料', icon: UserOutlined },
  { key: 'address', label: '收货地址', icon: EnvironmentOutlined },
  { key: 'password', label: '修改密码', icon: LockOutlined }
]
const activeTab = ref('profile')

// 会员信息
const memberInfo = ref({})
const memberId = ref(null)

// 个人资料表单
const profileForm = reactive({
  nickname: '',
  phone: '',
  gender: 0,
  city: '',
  job: ''
})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 地址列表
const addressList = ref([])

// 地址弹窗
const addressModalVisible = ref(false)
const editingAddress = ref(null)
const addressForm = reactive({
  name: '',
  phone: '',
  province: '',
  city: '',
  region: '',
  detailAddress: '',
  postCode: ''
})

// 地址弹窗省市区级联
const addressRegionValue = ref([])

const onAddressRegionChange = (value) => {
  if (value && value.length >= 2) {
    addressForm.province = value[0]
    addressForm.city = value[1]
    addressForm.region = value[2] || ''
  } else {
    addressForm.province = ''
    addressForm.city = ''
    addressForm.region = ''
  }
}

// 初始化
const initData = async () => {
  if (!isMemberLoggedIn()) {
    message.warning('请先登录')
    router.push('/member/login')
    return
  }
  const info = getMemberInfo()
  memberId.value = info?.memberId || info?.id
  memberInfo.value = info || {}

  // 加载详细信息
  if (memberId.value) {
    try {
      const res = await getMemberInfoApi(memberId.value)
      if (res.success && res.data) {
        memberInfo.value = res.data
        profileForm.nickname = res.data.nickname || ''
        profileForm.phone = res.data.phone || ''
        profileForm.gender = res.data.gender || 0
        profileForm.city = res.data.city || ''
        profileForm.job = res.data.job || ''
      }
    } catch (e) {
      // 使用本地缓存数据
      profileForm.nickname = info?.nickname || ''
      profileForm.phone = info?.phone || ''
    }
    loadAddresses()
  }
}

// 头像上传
const handleAvatarUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      memberInfo.value.icon = res.data
      const updateRes = await updateMemberInfo({ id: memberId.value, icon: res.data })
      if (updateRes.success) {
        // 同步更新本地存储
        const localInfo = getMemberInfo()
        if (localInfo) {
          localInfo.icon = res.data
          setMemberInfo(localInfo)
        }
        message.success('头像更新成功')
      }
      onSuccess(res)
    } else {
      message.error(res.message || '上传失败')
      onError(new Error(res.message))
    }
  } catch (e) {
    message.error('上传失败')
    onError(e)
  }
}

// 保存个人资料
const handleSaveProfile = async () => {
  if (!profileForm.nickname.trim()) {
    message.warning('昵称不能为空')
    return
  }
  try {
    const res = await updateMemberInfo({
      id: memberId.value,
      nickname: profileForm.nickname.trim(),
      phone: profileForm.phone?.trim() || null,
      gender: profileForm.gender,
      city: profileForm.city?.trim() || null,
      job: profileForm.job?.trim() || null
    })
    if (res.success) {
      message.success('资料更新成功')
      // 同步更新本地存储
      const localInfo = getMemberInfo()
      if (localInfo) {
        localInfo.nickname = profileForm.nickname.trim()
        localInfo.phone = profileForm.phone?.trim() || localInfo.phone
        setMemberInfo(localInfo)
      }
      memberInfo.value.nickname = profileForm.nickname.trim()
      memberInfo.value.phone = profileForm.phone?.trim() || memberInfo.value.phone
    }
  } catch (e) {
    message.error('更新失败')
  }
}

// 修改密码
const handleChangePassword = async () => {
  if (!passwordForm.oldPassword) {
    message.warning('请输入当前密码')
    return
  }
  if (!passwordForm.newPassword || passwordForm.newPassword.length < 6) {
    message.warning('新密码至少6位')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    message.warning('两次密码输入不一致')
    return
  }
  try {
    const res = await updateMemberPassword({
      memberId: memberId.value,
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    if (res.success) {
      message.success('密码修改成功')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } else {
      message.error(res.message || '密码修改失败')
    }
  } catch (e) {
    message.error('密码修改失败')
  }
}

// 加载地址列表
const loadAddresses = async () => {
  try {
    const res = await getAddressList(memberId.value)
    if (res.success) {
      addressList.value = res.data || []
    }
  } catch (e) {
    // 静默处理
  }
}

// 打开地址弹窗
const openAddressModal = (addr) => {
  editingAddress.value = addr
  if (addr) {
    addressForm.name = addr.name || ''
    addressForm.phone = addr.phone || ''
    addressForm.province = addr.province || ''
    addressForm.city = addr.city || ''
    addressForm.region = addr.region || ''
    addressForm.detailAddress = addr.detailAddress || ''
    addressForm.postCode = addr.postCode || ''
    addressRegionValue.value = [addr.province, addr.city, addr.region].filter(Boolean)
  } else {
    addressForm.name = ''
    addressForm.phone = ''
    addressForm.province = ''
    addressForm.city = ''
    addressForm.region = ''
    addressForm.detailAddress = ''
    addressForm.postCode = ''
    addressRegionValue.value = []
  }
  addressModalVisible.value = true
}

// 保存地址
const handleSaveAddress = async () => {
  if (!addressForm.name.trim() || !addressForm.phone.trim() || !addressForm.detailAddress.trim()) {
    message.warning('请填写完整地址信息')
    return
  }
  try {
    const data = {
      ...addressForm,
      memberId: memberId.value
    }
    let res
    if (editingAddress.value) {
      data.id = editingAddress.value.id
      res = await updateAddress(data)
    } else {
      res = await addAddress(data)
    }
    if (res.success) {
      message.success(editingAddress.value ? '地址更新成功' : '地址添加成功')
      addressModalVisible.value = false
      loadAddresses()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (e) {
    message.error('操作失败')
  }
}

// 删除地址
const handleDeleteAddress = async (id) => {
  try {
    const res = await deleteAddress(id)
    if (res.success) {
      message.success('地址已删除')
      loadAddresses()
    }
  } catch (e) {
    message.error('删除失败')
  }
}

// 设为默认
const handleSetDefault = async (id) => {
  try {
    const res = await setDefaultAddress(id, memberId.value)
    if (res.success) {
      message.success('已设为默认地址')
      loadAddresses()
    }
  } catch (e) {
    message.error('操作失败')
  }
}

onMounted(() => {
  initData()
})
</script>

<style scoped>
:deep(.ant-input),
:deep(.ant-input-password .ant-input),
:deep(.ant-select-selector),
:deep(.ant-input-affix-wrapper) {
  border-radius: 8px !important;
}

:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-input-affix-wrapper-focused),
:deep(.ant-select-focused .ant-select-selector) {
  border-color: #10b981 !important;
  box-shadow: 0 0 0 2px rgba(16, 185, 129, 0.15) !important;
}

:deep(.ant-btn-primary) {
  background-color: #059669 !important;
  border-color: #059669 !important;
}

:deep(.ant-btn-primary:hover) {
  background-color: #047857 !important;
  border-color: #047857 !important;
}

:deep(.ant-modal-header) {
  border-radius: 12px 12px 0 0;
}

:deep(.ant-upload.ant-upload-select-picture-card) {
  border-radius: 50%;
}
</style>
