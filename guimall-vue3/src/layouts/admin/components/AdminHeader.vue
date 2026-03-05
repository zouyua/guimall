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

      <!-- 点击刷新按钮 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>刷新</span>
        </template>
        <div
          class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
          @click="handleRefresh">
          <ReloadOutlined class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 全屏按钮 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>全屏</span>
        </template>
        <div
          class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
          @click="toggle">
          <FullscreenOutlined v-if="!isFullscreen" class="text-lg" />
          <FullscreenExitOutlined v-else class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 用户下拉 -->
      <a-dropdown v-model:open="visible">
        <a class="flex items-center justify-center text-gray-700 text-sm px-2 h-[64px] hover:bg-gray-200"
          @click.prevent>
          <a-avatar class="mr-2" :size="25" :src="avatar" />
          Admin
          <DownOutlined class="ml-1 text-xs" />
        </a>

        <template #overlay>
          <a-menu @click="handleMenuClick">
            <a-menu-item key="1">修改密码</a-menu-item>
            <a-menu-item key="2">退出登录</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined,
  DownOutlined,
  ReloadOutlined
} from '@ant-design/icons-vue'

// 引入图片
import avatar from '@/assets/avatar_01.png'

//引入useFullscreen
import { useFullscreen } from '@vueuse/core'

import { useMenuStore } from '@/stores/menu'

//引入了菜单 store
const menuStore = useMenuStore()

//icon点击事件，调用 store 中的方法切换菜单宽度
const handleMenuWidth = () => {
  //动态设置菜单的宽度大小
  menuStore.handleMenuWidth()
}

//刷新页面
const handleRefresh = () => location.reload()

//isFullscreen表示当前是否全屏；toggle用于动态切换全屏、非全屏
const { isFullscreen, toggle } = useFullscreen()

const visible = ref(false)

// JS 写法，不需要类型声明
const handleMenuClick = (e) => {
  if (e.key === '2') {
    visible.value = false
  }
}
</script>