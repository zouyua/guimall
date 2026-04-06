import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCartList } from '@/api/frontend/cart'
import { getMemberId } from '@/composables/member'

export const useCartStore = defineStore('cart', () => {
  const cartCount = ref(0)

  async function loadCartCount() {
    const memberId = getMemberId()
    if (!memberId) {
      cartCount.value = 0
      return
    }
    try {
      const res = await getCartList(memberId)
      if (res.success && res.data) {
        cartCount.value = res.data.length
      }
    } catch (e) {
      // ignore
    }
  }

  function reset() {
    cartCount.value = 0
  }

  return { cartCount, loadCartCount, reset }
})
