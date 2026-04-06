<template>
  <!-- 外层布局必须撑满屏幕 -->
  <a-layout style="height: 100vh">

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
  overflow: hidden;
}

/* 内容区 */
.content {
  background: #f5f5f5;
  padding: 0;
}

/* router-view 主体 */
.main {
  background: #f5f5f5;
  height: calc(100vh - 64px - 70px);
  padding: 16px;
  overflow-y: auto;
}

/* 内容区域过渡动画：淡入淡出效果 */
.fade-enter-from { opacity: 0; }
.fade-enter-to { opacity: 1; }
.fade-leave-from { opacity: 1; }
.fade-leave-to { opacity: 0; }
.fade-leave-active { transition: all 0.3s; }
.fade-enter-active { transition: all 0.3s; transition-delay: 0.3s; }

/* 底部 */
.footer {
  height: 70px;
  background: #f5f5f5;
  color: white;
  text-align: center;
  line-height: 70px;
  padding: 0;
  overflow: hidden;
}
</style>

<!-- 全局样式：修复 Ant Design Vue 表格操作按钮图标对齐 -->
<style>
/* 修复 h() 渲染的 Button icon slot 不对齐问题 */
.ant-table .ant-btn {
  display: inline-flex !important;
  align-items: center !important;
  gap: 4px !important;
}
.ant-table .ant-btn > .anticon {
  display: inline-flex !important;
  align-items: center !important;
  line-height: 1 !important;
}
/* 修复操作列按钮组间距 */
.ant-table .ant-btn + .ant-btn {
  margin-left: 0 !important;
}
/* 表格表头居中对齐 */
.ant-table-thead > tr > th {
  text-align: center !important;
}
</style>
