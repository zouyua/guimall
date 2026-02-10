<script setup>
import router from './router';

  // js代码，setup标识通常和组合式API搭配使用，用于告诉Vue需要再编译时进行一些处理，让我们可以更简洁地使用组合式 API
  //引入自定义组件
// import HelloWorld from './components/HelloWorld.vue'
// import TheWelcome from './components/TheWelcome.vue'
</script>

<template>
  <!-- html代码 -->
   <router-view></router-view>
  <!-- <header>
    <img alt="Vue logo" class="logo" src="./assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />
    </div>
  </header>

  <main>
    <TheWelcome />
  </main> -->
</template>

<style>
/* 自定义顶部加载 Loading颜色 */
#nprogress .bar {
  background: #e2ffe4 !important;
}
  /* css代码，scoped表示节点内css样式只针对此组件有效，不影响其他组件 */
/* header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
} */
</style>
