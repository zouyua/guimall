import '@/assets/main.css'//引入main.css样式文件
import 'animate.css';//引入animate.css动画库
import 'nprogress/nprogress.css'//引入nprogress页面加载进度条样式

//引入全局状态管理 Pinia
import pinia from '@/stores'
import { createApp } from 'vue'//引入createApp方法

import App from '@/App.vue'//引入根组件App.vue
//导入路由
import router from '@/router'
//导入全局路由守卫
import '@/permission'
//引入样式
import 'ant-design-vue/dist/reset.css'


const app = createApp(App)



//应用路由
app.use(router)
// app.use(pinia) 将 Pinia 实例添加到 Vue 应用中，使其在整个应用中可用。
app.use(pinia)


app.mount('#app')
// 创建应用，并将App根组件挂载到<div id="app"></div>中
// createApp(App).use(router).mount('#app')