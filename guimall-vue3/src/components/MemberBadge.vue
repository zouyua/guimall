<template>
  <router-link to="/member/center" class="flex items-center gap-1.5 text-sm text-stone-600 hover:text-emerald-600 transition-colors"
    :title="levelStore.levelInfo && levelStore.levelInfo.level > 0 ? levelStore.levelInfo.name + ' · ' + (levelStore.levelInfo.discount / 10).toFixed(1) + '折' : ''">
    <!-- 头像区域（带等级框） -->
    <div class="relative">
      <img :src="avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + nickname"
        :class="[
          'w-7 h-7 rounded-full object-cover',
          hasLevel ? 'ring-2 ring-offset-1 ' + ringClass : 'border border-emerald-100'
        ]" />
      <!-- 等级角标（头像右下角） -->
      <span v-if="hasLevel"
        :class="cornerBadgeClass"
        class="absolute -bottom-0.5 -right-0.5 w-3.5 h-3.5 rounded-full flex items-center justify-center text-[8px] font-black leading-none border border-white shadow-sm"
      >
        {{ levelStore.levelInfo.level }}
      </span>
    </div>
    <span>{{ nickname }}</span>
  </router-link>
</template>

<script setup>
import { onMounted, computed } from 'vue'
import { useMemberLevelStore } from '@/stores/memberLevel'

const props = defineProps({
  nickname: { type: String, default: '会员' },
  avatar: { type: String, default: '' }
})

const levelStore = useMemberLevelStore()

const hasLevel = computed(() => levelStore.levelInfo && levelStore.levelInfo.level > 0)

// 头像边框颜色（ring）
const ringClass = computed(() => {
  const level = levelStore.levelInfo?.level || 0
  if (level >= 3) return 'ring-amber-400'   // 钻石 - 金色框
  if (level >= 2) return 'ring-yellow-400'  // 金卡 - 黄金框
  if (level >= 1) return 'ring-slate-400'   // 银卡 - 银色框
  return ''
})

// 右下角等级角标颜色
const cornerBadgeClass = computed(() => {
  const level = levelStore.levelInfo?.level || 0
  if (level >= 3) return 'bg-amber-500 text-white'
  if (level >= 2) return 'bg-yellow-500 text-white'
  if (level >= 1) return 'bg-slate-500 text-white'
  return ''
})

onMounted(() => {
  levelStore.loadLevel()
})
</script>
