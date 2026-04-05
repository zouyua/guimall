<template>
  <div class="min-h-screen bg-gradient-to-br from-emerald-50 via-stone-50 to-emerald-50 font-sans flex flex-col">
    <!-- 顶部导航 -->
    <nav class="bg-white/80 backdrop-blur-md border-b border-stone-200">
      <div class="max-w-screen-xl flex items-center justify-between mx-auto p-4">
        <router-link to="/" class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-emerald-600 rounded-xl flex items-center justify-center shadow-lg shadow-emerald-200">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <span class="text-2xl font-black tracking-tighter text-emerald-900 uppercase">Guimall</span>
        </router-link>
        <router-link to="/" class="text-stone-500 hover:text-emerald-600 transition-colors font-medium flex items-center">
          <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M10 19l-7-7m0 0l7-7m-7 7h18" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/></svg>
          返回首页
        </router-link>
      </div>
    </nav>

    <!-- 主体内容 -->
    <div class="flex-1 flex items-center justify-center px-4 py-12">
      <div class="w-full max-w-md">
        <!-- 卡片 -->
        <div class="bg-white rounded-[2.5rem] shadow-2xl shadow-emerald-100/50 border border-stone-100 overflow-hidden">
          <!-- 顶部装饰 -->
          <div class="bg-gradient-to-r from-emerald-600 to-emerald-500 px-8 pt-10 pb-8 relative overflow-hidden">
            <div class="absolute right-0 top-0 w-40 h-40 bg-emerald-400/30 rounded-full translate-x-16 -translate-y-16 blur-2xl"></div>
            <div class="absolute left-0 bottom-0 w-32 h-32 bg-emerald-700/20 rounded-full -translate-x-10 translate-y-10 blur-2xl"></div>
            <div class="relative z-10">
              <h1 class="text-3xl font-black text-white mb-1">
                {{ activeTab === 'login' ? '欢迎回来' : '加入我们' }}
              </h1>
              <p class="text-emerald-100 text-sm">
                {{ activeTab === 'login' ? '登录您的 GUIMALL 账号，畅享桂林好物' : '注册 GUIMALL 账号，开启山水好物之旅' }}
              </p>
            </div>
          </div>

          <!-- 标签切换 -->
          <div class="flex border-b border-stone-100">
            <button @click="activeTab = 'login'; clearMessages()"
              :class="activeTab === 'login' ? 'text-emerald-600 border-b-2 border-emerald-600 bg-emerald-50/50' : 'text-stone-400 hover:text-stone-600'"
              class="flex-1 py-4 font-bold text-sm transition-all">
              会员登录
            </button>
            <button @click="activeTab = 'register'; clearMessages()"
              :class="activeTab === 'register' ? 'text-emerald-600 border-b-2 border-emerald-600 bg-emerald-50/50' : 'text-stone-400 hover:text-stone-600'"
              class="flex-1 py-4 font-bold text-sm transition-all">
              新会员注册
            </button>
          </div>

          <!-- 表单区域 -->
          <div class="p-8">
            <!-- 成功消息 -->
            <div v-if="successMsg" class="mb-6 bg-emerald-50 border border-emerald-200 text-emerald-700 px-4 py-3 rounded-2xl text-sm font-medium flex items-center">
              <svg class="w-5 h-5 mr-2 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
              {{ successMsg }}
            </div>

            <!-- 错误消息 -->
            <div v-if="errorMsg" class="mb-6 bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-2xl text-sm font-medium flex items-center">
              <svg class="w-5 h-5 mr-2 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
              {{ errorMsg }}
            </div>

            <!-- 登录表单 -->
            <form v-if="activeTab === 'login'" @submit.prevent="handleLogin" class="space-y-5">
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">用户名</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                  </span>
                  <input v-model="loginForm.username" type="text" placeholder="请输入用户名"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">密码</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" /></svg>
                  </span>
                  <input v-model="loginForm.password" type="password" placeholder="请输入密码"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <button type="submit" :disabled="loginLoading"
                class="w-full bg-emerald-600 text-white py-4 rounded-2xl font-black text-lg shadow-lg shadow-emerald-200 hover:bg-emerald-700 hover:-translate-y-0.5 transition-all disabled:opacity-60 disabled:cursor-not-allowed">
                <span v-if="loginLoading" class="flex items-center justify-center">
                  <svg class="animate-spin w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  登录中...
                </span>
                <span v-else>登 录</span>
              </button>
            </form>

            <!-- 注册表单 -->
            <form v-else @submit.prevent="handleRegister" class="space-y-5">
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">用户名</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                  </span>
                  <input v-model="registerForm.username" type="text" placeholder="请输入用户名"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">密码</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" /></svg>
                  </span>
                  <input v-model="registerForm.password" type="password" placeholder="请输入密码"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">确认密码</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" /></svg>
                  </span>
                  <input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">昵称</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" /></svg>
                  </span>
                  <input v-model="registerForm.nickname" type="text" placeholder="请输入昵称"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-bold text-stone-600 mb-2">手机号</label>
                <div class="relative">
                  <span class="absolute left-4 top-1/2 -translate-y-1/2 text-stone-400">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" /></svg>
                  </span>
                  <input v-model="registerForm.phone" type="text" placeholder="请输入手机号"
                    class="w-full pl-12 pr-4 py-3.5 bg-stone-50 border border-stone-200 rounded-2xl text-stone-800 placeholder-stone-400 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all" />
                </div>
              </div>
              <button type="submit" :disabled="registerLoading"
                class="w-full bg-emerald-600 text-white py-4 rounded-2xl font-black text-lg shadow-lg shadow-emerald-200 hover:bg-emerald-700 hover:-translate-y-0.5 transition-all disabled:opacity-60 disabled:cursor-not-allowed">
                <span v-if="registerLoading" class="flex items-center justify-center">
                  <svg class="animate-spin w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  注册中...
                </span>
                <span v-else>注 册</span>
              </button>
            </form>
          </div>
        </div>

        <!-- 底部提示 -->
        <p class="text-center text-stone-400 text-sm mt-8">
          桂林山水助农电商平台 &copy; GUIMALL
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { setMemberToken } from '@/composables/cookie'
import { setMemberInfo } from '@/composables/member'
import { memberLogin, memberRegister } from '@/api/frontend/member'

const router = useRouter()
const route = useRoute()

const activeTab = ref('login')
const successMsg = ref('')
const errorMsg = ref('')
const loginLoading = ref(false)
const registerLoading = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  phone: ''
})

const clearMessages = () => {
  successMsg.value = ''
  errorMsg.value = ''
}

const handleLogin = async () => {
  clearMessages()

  if (!loginForm.value.username) {
    errorMsg.value = '请输入用户名'
    return
  }
  if (!loginForm.value.password) {
    errorMsg.value = '请输入密码'
    return
  }

  loginLoading.value = true
  try {
    const res = await memberLogin({
      username: loginForm.value.username,
      password: loginForm.value.password
    })
    if (res.success) {
      // 存储 token
      if (res.data.token) {
        setMemberToken(res.data.token)
      }
      // 存储会员信息
      setMemberInfo({
        memberId: res.data.memberId,
        nickname: res.data.nickname || res.data.username || loginForm.value.username
      })
      // 跳转到之前的页面或首页
      const redirect = route.query.redirect || '/'
      router.push(redirect)
    } else {
      errorMsg.value = res.message || '登录失败，请检查用户名和密码'
    }
  } catch (e) {
    errorMsg.value = '登录失败，请稍后重试'
  } finally {
    loginLoading.value = false
  }
}

const handleRegister = async () => {
  clearMessages()

  if (!registerForm.value.username) {
    errorMsg.value = '请输入用户名'
    return
  }
  if (!registerForm.value.password) {
    errorMsg.value = '请输入密码'
    return
  }
  if (registerForm.value.password.length < 6) {
    errorMsg.value = '密码长度不能少于6位'
    return
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    errorMsg.value = '两次输入的密码不一致'
    return
  }
  if (!registerForm.value.nickname) {
    errorMsg.value = '请输入昵称'
    return
  }
  if (!registerForm.value.phone) {
    errorMsg.value = '请输入手机号'
    return
  }
  if (!/^1[3-9]\d{9}$/.test(registerForm.value.phone)) {
    errorMsg.value = '请输入正确的手机号'
    return
  }

  registerLoading.value = true
  try {
    const res = await memberRegister({
      username: registerForm.value.username,
      password: registerForm.value.password,
      nickname: registerForm.value.nickname,
      phone: registerForm.value.phone
    })
    if (res.success) {
      // 注册成功，切换到登录标签
      successMsg.value = '注册成功！请使用新账号登录'
      activeTab.value = 'login'
      // 预填用户名
      loginForm.value.username = registerForm.value.username
      loginForm.value.password = ''
      // 清空注册表单
      registerForm.value = {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        phone: ''
      }
    } else {
      errorMsg.value = res.message || '注册失败，请稍后重试'
    }
  } catch (e) {
    errorMsg.value = '注册失败，请稍后重试'
  } finally {
    registerLoading.value = false
  }
}
</script>

<style scoped>
/* 输入框获得焦点时的动画 */
input:focus {
  transform: translateY(-1px);
}
</style>
