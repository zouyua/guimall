//引入全局状态管理 Pinia
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'//引入 Pinia 持久化插件

const pinia = createPinia()
//持久化插件
pinia.use(piniaPluginPersistedstate)

//暴露出去
export default pinia