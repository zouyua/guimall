<!-- <script setup>
import router from './router';

  // js代码，setup标识通常和组合式API搭配使用，用于告诉Vue需要再编译时进行一些处理，让我们可以更简洁地使用组合式 API
  //引入自定义组件
// import HelloWorld from './components/HelloWorld.vue'
// import TheWelcome from './components/TheWelcome.vue'
</script> -->

<template>
  <a-config-provider :locale="zhCN">
    <router-view />
  </a-config-provider>
</template>

<script setup>

import zhCN from 'ant-design-vue/es/locale/zh_CN'

</script>

<style>
/* 自定义顶部加载 Loading颜色 */
#nprogress .bar {
  background: #10b981 !important; /* 调整为与主题一致的翡翠绿 */
}
/* 隐藏右上角的旋转光圈 */
#nprogress .spinner {
  display: none !important;
}
</style>
