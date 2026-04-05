<template>
  <div class="bg-white h-[64px] flex pr-4 border-b border-slate-200">

    <!-- 左侧折叠按钮(收缩、展开) -->
    <div class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
      @click="handleMenuWidth">
      <!-- 菜单展开时 -->
      <MenuFoldOutlined v-if="menuStore.menuWidth === '200px'" class="text-lg" />
      <!-- 菜单收缩时 -->
      <MenuUnfoldOutlined v-else class="text-lg" />
    </div>

    <!-- 右侧区域 -->
    <div class="ml-auto flex items-center">

      <!-- 前往前台 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>前往前台</span>
        </template>
        <div class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
          @click="goFrontend">
          <HomeOutlined class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 点击刷新按钮 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>刷新</span>
        </template>
        <div class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
          @click="handleRefresh">
          <ReloadOutlined class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 全屏按钮 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>全屏</span>
        </template>
        <div class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
          @click="toggle">
          <FullscreenOutlined v-if="!isFullscreen" class="text-lg" />
          <FullscreenExitOutlined v-else class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 用户下拉 -->
      <a-dropdown v-model:open="visible">
        <a class="flex items-center justify-center text-gray-700 text-sm px-2 h-[64px] hover:bg-gray-200"
          @click.prevent>
          <!-- 用户头像 -->
          <a-avatar class="mr-2" :size="25" :src="avatar" />
          {{ userStore.userInfo.username }}
          <DownOutlined class="ml-1 text-xs" />
        </a>

        <!-- 下拉菜单 -->
        <template #overlay>
          <a-menu @click="handleMenuClick">
            <a-menu-item key="1">修改密码</a-menu-item>
            <a-menu-item key="2">退出登录</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>

      <!-- 修改密码 -->
<a-modal
  v-model:open="open"
  title="修改密码"
  :maskClosable="false"
  :keyboard="false"
  @ok="handleSubmit"
>

  <a-form
    ref="formRef"
    :model="form"
    :rules="rules"
    layout="vertical"
  >

    <a-form-item label="用户名" name="username">
      <a-input v-model:value="form.username" disabled />
    </a-form-item>

    <a-form-item label="密码" name="password">
      <a-input-password
        v-model:value="form.password"
        placeholder="请输入密码"
      />
    </a-form-item>

    <a-form-item label="确认密码" name="rePassword">
      <a-input-password
        v-model:value="form.rePassword"
        placeholder="请确认密码"
      />
    </a-form-item>

  </a-form>

</a-modal>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref,watch } from 'vue'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined,
  DownOutlined,
  ReloadOutlined,
  HomeOutlined
} from '@ant-design/icons-vue'

// 引入图片
import avatar from '@/assets/avatar_01.png'

//引入useFullscreen
import { useFullscreen } from '@vueuse/core'
import { useMenuStore } from '@/stores/menu'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { showConfirm, showMessage } from '@/composables/util'

import { updateAdminPassword } from '@/api/admin/user'

//router
const router = useRouter()

//引入了用户 store
const userStore = useUserStore()
//引入了菜单 store
const menuStore = useMenuStore()

//icon点击事件，调用 store 中的方法切换菜单宽度
const handleMenuWidth = () => {
  //动态设置菜单的宽度大小
  menuStore.handleMenuWidth()
}

//刷新页面
const handleRefresh = () => location.reload()

//跳转前台首页（新窗口）
const goFrontend = () => window.open('/', '_blank')

//isFullscreen表示当前是否全屏；toggle用于动态切换全屏、非全屏
const { isFullscreen, toggle } = useFullscreen()

//下拉菜单是否显示
const visible = ref(false)

//修改密码对话框是否显示
const open = ref(false)

//表单引用
const formRef = ref(null)

// 表单数据
const form = reactive({
  username: userStore.userInfo.username || '',
  password: '',
  rePassword: ''
})

/**
 * 监听 Pinia store中某个值的变化
 */
watch(
  () => userStore.userInfo.username,
  (newValue,oldValue) => {
    //在这里处理变化后的值
    console.log('新值：', newValue);
    console.log('旧值：', oldValue);

    //可以在这里执行任何需要的逻辑
    //重新将新的值，设置回form对象中
    form.username = newValue
  }
)

/**
 * 下拉菜单点击
 */
const handleMenuClick = (e) => {

  if (e.key === '1') {
    // 修改密码
    open.value = true
  }

  if (e.key === '2') {
    // 退出登录
    showConfirm('确定要退出登录吗？', () => {

      userStore.logout()

      showMessage('退出登录成功')

      router.push('/login')

    })
  }

  visible.value = false
}

/**
 * 表单校验规则
 */
const rules = {

  username: [
    {
      required: true,
      message: '用户名不能为空',
      trigger: 'blur'
    }
  ],

  password: [
    {
      required: true,
      message: '密码不能为空',
      trigger: 'blur'
    }
  ],

  rePassword: [
    {
      required: true,
      message: '确认密码不能为空',
      trigger: 'blur'
    },
    {
      validator(_, value) {

        if (value !== form.password) {
          return Promise.reject('两次密码不一致')
        }

        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

/**
 * 提交修改密码
 */
const handleSubmit = () => {

  formRef.value
    .validate()
    .then(() => {

      updateAdminPassword(form).then((res) => {

        if (res.success) {

          showMessage('密码修改成功，请重新登录')

          userStore.logout()

          open.value = false

          router.push('/login')

        } else {

          showMessage(res.message, 'error')

        }

      })

    })
    .catch(() => {
      console.log('表单校验失败')
    })
}
</script>