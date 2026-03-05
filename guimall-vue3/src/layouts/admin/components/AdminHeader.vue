<template>
  <div class="bg-white h-[64px] flex pr-4 border-b border-slate-200">
    
    <!-- 左侧折叠按钮 -->
    <div
      class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 hover:bg-gray-200"
    >
      <MenuFoldOutlined class="text-lg" />
    </div>

    <!-- 右侧区域 -->
    <div class="ml-auto flex items-center">
      
      <!-- 全屏按钮 -->
      <a-tooltip placement="bottom">
        <template #title>
          <span>全屏</span>
        </template>
        <div
          class="w-[42px] h-[64px] flex cursor-pointer items-center justify-center text-gray-700 mr-2 hover:bg-gray-200"
        >
          <FullscreenOutlined class="text-lg" />
        </div>
      </a-tooltip>

      <!-- 用户下拉 -->
      <a-dropdown v-model:open="visible">
        <a
          class="flex items-center justify-center text-gray-700 text-sm px-2 h-[64px] hover:bg-gray-200"
          @click.prevent
        >
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
  FullscreenOutlined,
  DownOutlined
} from '@ant-design/icons-vue'

// 引入图片
import avatar from '@/assets/avatar_01.png'

const visible = ref(false)

// JS 写法，不需要类型声明
const handleMenuClick = (e) => {
  if (e.key === '2') {
    visible.value = false
  }
}
</script>