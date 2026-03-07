// 声明菜单相关的全局状态：动态展开、收缩
//引入Pinia的defineStore函数，声明store;Vue的ref函数
// 使用 Pinia 定义一个名为 'menu'(唯一) 的 store，包含一个响应式变量 menuWidth 和一个函数 handleMenuWidth 来切换菜单的宽度。
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMenuStore = defineStore('menu', () => {
  // 左边栏菜单默认宽度
  const menuWidth = ref("200px")
  
  // 展开或伸缩左边栏菜单
  function handleMenuWidth() {
      menuWidth.value = menuWidth.value == '200px' ? '64px' : '200px'
  }
  
  return { menuWidth, handleMenuWidth }
},
{
  //开启持久化
  persist: true,
}
)