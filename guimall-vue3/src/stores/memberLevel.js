import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCurrentMemberLevel } from '@/api/frontend/member'
import { getMemberId } from '@/composables/member'

export const useMemberLevelStore = defineStore('memberLevel', () => {
  const levelInfo = ref(null)   // { id, name, level, discount, ... }
  const loaded = ref(false)

  async function loadLevel() {
    const memberId = getMemberId()
    if (!memberId) {
      levelInfo.value = null
      loaded.value = false
      return
    }
    // 避免重复请求
    if (loaded.value) return
    try {
      const res = await getCurrentMemberLevel(memberId)
      if (res.success && res.data) {
        levelInfo.value = res.data
      } else {
        levelInfo.value = null
      }
    } catch (e) {
      levelInfo.value = null
    }
    loaded.value = true
  }

  function reset() {
    levelInfo.value = null
    loaded.value = false
  }

  return { levelInfo, loaded, loadLevel, reset }
})
