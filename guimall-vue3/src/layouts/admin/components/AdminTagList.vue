<template>
  <!-- 左边：标签导航栏 -->
  <div
    class="flex h-[44px] top-[64px] px-3 right-0 z-50 items-center bg-white border-b border-gray-100 shadow-sm transition-all duration-300"
    :style="{ left: menuStore.menuWidth }">

     <!-- 左按钮 -->
  <span
    class="flex-shrink-0 flex items-center justify-center h-8 w-8 cursor-pointer"
    @click="scrollLeft"
  >
    <LeftOutlined />
  </span>

    <a-tabs 
    ref="tabsRef"
    v-model:activeKey="activeTab" type="editable-card"
    hide-add
    :more-icon="false"
      class="custom-tabs flex-1 overflow-hidden" @change="tabChange" @edit="onEdit">

      <a-tab-pane v-for="item in tabList" :key="item.path" :tab="item.title" :closable="item.path !== '/admin/index'" />
    </a-tabs>

    <!-- 右按钮 -->
  <span
    class="flex-shrink-0 flex items-center justify-center h-8 w-8 cursor-pointer"
    @click="scrollRight"
  >
    <RightOutlined />
  </span>

    <!-- 右侧下拉菜单 -->
    <!-- <span class="ml-auto flex items-center justify-center h-[32px] w-[32px]"> -->
    <span class="flex-shrink-0 flex items-center justify-center h-8 w-8">
      <a-dropdown>
        <template #overlay>
          <a-menu @click="handleMenuClick">
            <a-menu-item key="closeOthers">关闭其他</a-menu-item>
            <a-menu-item key="closeAll">关闭全部</a-menu-item>
          </a-menu>
        </template>

        <DownOutlined style="font-size:16px; cursor:pointer" />
      </a-dropdown>
    </span>
  </div>

  <!-- <div class="h-[44px]"></div> -->
</template>

<script setup>
import { ref } from 'vue'
import { DownOutlined, LeftOutlined, RightOutlined } from '@ant-design/icons-vue'
import { useTabList } from '@/composables/useTagList.js'
import { setTabList, getTabList } from '@/composables/cookie'

const { menuStore, activeTab, tabList, tabChange, removeTab, handleCloseTab } =
  useTabList()

const tabsRef = ref(null)

// 获取 tabs 内部的滚动容器
const getNavWrap = () => {
  if (!tabsRef.value) return null
  // 通过组件实例的 $el 获取真实 DOM，再查找滚动容器
  return tabsRef.value.$el.querySelector('.ant-tabs-nav-wrap')
}

// 向左滚动
const scrollLeft = () => {
  const wrap = getNavWrap()
  if (!wrap) return
  // 只有当前滚动位置大于 0 才向左滚动
  if (wrap.scrollLeft > 0) {
    wrap.scrollTo({
      left: Math.max(0, wrap.scrollLeft - 300),
      behavior: 'smooth'
    })
  }
}

// 向右滚动
const scrollRight = () => {
  const wrap = getNavWrap()
  if (!wrap) return
  const maxScrollLeft = wrap.scrollWidth - wrap.clientWidth
  // 只有当未达到最右边时才向右滚动
  if (wrap.scrollLeft < maxScrollLeft) {
    wrap.scrollTo({
      left: Math.min(maxScrollLeft, wrap.scrollLeft + 300),
      behavior: 'smooth'
    })
  }
}
// 关闭tab
const onEdit = (targetKey, action) => {
  if (action === 'remove') {
    removeTab(targetKey)
  }
}

// 下拉菜单
const handleMenuClick = ({ key }) => {
  handleCloseTab(key)
}
</script>

<style scoped>
/* 外层容器样式（可删掉 .ml-auto 相关代码） */
.fixed {
  background-color: #fff;
  /* 确保 padding 不会影响宽度计算（默认盒模型下，left/right 已定宽，padding 不影响总宽度） */
  box-sizing: border-box;
}

/* 强制 tabs 内部的导航容器宽度自适应 */
:deep(.custom-tabs) {
  display: flex;
  flex-direction: column;
  width: 100%;
}

:deep(.custom-tabs .ant-tabs-nav) {
  margin: 0 !important;
}

/* 去掉 tabs 底部那条线 */
:deep(.custom-tabs .ant-tabs-nav::before) {
  border-bottom: none !important;
}

/* 隐藏多余的更多按钮 */
:deep(.custom-tabs .ant-tabs-nav-more) {
  display: none !important;
}

/* 标签样式（你的原有样式保持不变） */
:deep(.custom-tabs .ant-tabs-tab) {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  height: 32px !important;
  padding: 0 18px !important;
  margin-right: 8px !important;
  border-radius: 9999px !important;
  background: #fff !important;
  border: 1px solid #d9d9d9 !important;
  font-size: 13px !important;
  color: #666 !important;
  transition: all 0.3s;
}

:deep(.custom-tabs .ant-tabs-tab .ant-tabs-tab-remove) {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  margin-left: 6px;
  font-size: 14px;
  line-height: 1;
  color: inherit;
}

:deep(.custom-tabs .ant-tabs-tab-active) {
  background: #1890ff !important;
  border-color: #1890ff !important;
}

:deep(.custom-tabs .ant-tabs-tab-active .ant-tabs-tab-btn) {
  color: #ffffff !important;
}

:deep(.custom-tabs .ant-tabs-tab-active .ant-tabs-tab-remove) {
  color: #ffffff !important;
  opacity: 0.8;
}

:deep(.custom-tabs .ant-tabs-tab-active .ant-tabs-tab-remove:hover) {
  opacity: 1;
}

:deep(.custom-tabs .ant-tabs-tab:hover) {
  border-color: #1890ff !important;
  color: #1890ff;
}


:deep(.custom-tabs .ant-tabs-nav-wrap) {
  /* 允许水平滚动 */
  overflow-x: auto !important;
  /* 隐藏滚动条（针对不同浏览器） */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
}

:deep(.custom-tabs .ant-tabs-nav-wrap::-webkit-scrollbar) {
  display: none; /* Chrome, Safari, Opera */
}

/* 确保 nav 容器不会被压缩 */
:deep(.custom-tabs .ant-tabs-nav-list) {
  transition: transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}
</style>