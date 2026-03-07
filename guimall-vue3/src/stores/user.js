import { defineStore } from "pinia"
import { ref } from "vue"
import { removeToken } from "@/composables/cookie"
import { getUserInfo } from "@/api/admin/user"

export const useUserStore = defineStore(
  "user",
  () => {
    const userInfo = ref({})
    function setUserInfo() {
      getUserInfo().then(res => {
        if (res.success == true) {
          userInfo.value = res.data
        }
      })
    }

    //退出登录
    function logout() {
      //删除cookie中的token令牌
      removeToken()
      //删除登录用户信息
      userInfo.value = {}
    }
    return { userInfo, setUserInfo,logout }
  },
  {
    persist: true,
  }
)