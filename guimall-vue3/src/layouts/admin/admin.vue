<template>
  <!-- 外层布局必须撑满屏幕 -->
  <a-layout style="min-height: 100vh">

    <!-- 左侧菜单栏 -->

    <a-layout-sider :width="menuStore.menuWidth" :collapsed="menuStore.menuWidth === '64px'" theme="dark"
      :trigger="null" class="transition-all duration-300">

      <div class="h-[64px] flex items-center justify-center overflow-hidden">
        <img v-if="menuStore.menuWidth === '200px'" src="@/assets/guimall-logo.png"
          class="w-full h-full object-cover" />
        <img v-else src="@/assets/guimall-logo-mini.png" class="w-full h-full object-cover" />
      </div>
      <AdminMenu />
    </a-layout-sider>

    <!-- 右侧主区域 -->
    <a-layout>

      <!-- 顶部 Header -->
      <a-layout-header class="header">
        <AdminHeader />
      </a-layout-header>

      <!-- 内容区 -->
      <a-layout-content class="content">

        <!-- 标签导航栏 -->
        <AdminTagList />

        <!-- 主页面 -->
        <!-- <div class="main">
          <router-view />
         </div> -->

        <div class="main">
          <!-- 主内容（根据路由动态展示不同页面） -->
          <router-view v-slot="{ Component }">
            <Transition name="fade">
              <!-- max 指定最多缓存 20 个组件 -->
              <KeepAlive :max="20">
                <component :is="Component"></component>
              </KeepAlive>
            </Transition>

          </router-view>
        </div>

      </a-layout-content>

      <!-- 底部 Footer -->
      <a-layout-footer class="footer">
        <AdminFooter />
      </a-layout-footer>

    </a-layout>
  </a-layout>
</template>

<script setup>
import { onMounted } from 'vue'
//引入组件
import AdminMenu from './components/AdminMenu.vue'
import AdminHeader from './components/AdminHeader.vue'
import AdminTagList from './components/AdminTagList.vue'
import AdminFooter from './components/AdminFooter.vue'

import { useMenuStore } from '@/stores/menu'
import { useUserStore } from '@/stores/user'

const menuStore = useMenuStore()
const userStore = useUserStore()

// 在组件挂载时获取用户信息
onMounted(() => {
  userStore.setUserInfo()
})
</script>

<style scoped>
/* 顶部 */
.header {
  height: 64px;
  padding: 0;
  background: #e2efeb;
  color: white;
  line-height: 64px;
}

/* 内容区 */
.content {
  background: #f5f5f5;
  padding: 0;
}

/* router-view 主体 */
.main {
  background: white;
  min-height: calc(100vh - 64px - 70px);
  padding: 16px;
}

/* 内容区域过渡动画：淡入淡出效果 */
/* 刚开始进入时 */
.fade-enter-from {
    /* 透明度 */
    opacity: 0;
}

/* 刚开始结束 */
.fade-enter-to {
    opacity: 1;
}

/* 刚开始离开 */
.fade-leave-from {
  opacity: 1;
}

/* 离开已结束 */
.fade-leave-to {
  opacity: 0;
}

/* 离开进行中 */
.fade-leave-active {
    transition: all 0.3s;
}

/* 进入进行中 */
.fade-enter-active {
    transition: all 0.3s;
    transition-delay: 0.3s;
}

/* 底部 */
.footer {
  height: 70px;
  background: #0e7490;
  color: white;
  text-align: center;
  line-height: 70px;
  padding: 0;
}
</style>
