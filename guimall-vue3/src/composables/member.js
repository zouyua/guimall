const MEMBER_INFO_KEY = 'memberInfo'
const PROFILE_CACHE_TTL = 5 * 60 * 1000
let refreshPromise = null

function normalizeMemberInfo(info) {
  if (!info || typeof info !== 'object') return null
  const memberId = info.memberId ?? info.id ?? null
  return {
    ...info,
    memberId,
    id: info.id ?? memberId,
    nickname: info.nickname || info.username || '',
    icon: info.icon || info.avatar || ''
  }
}

function readMemberInfoRaw() {
  const raw = localStorage.getItem(MEMBER_INFO_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch (e) {
    localStorage.removeItem(MEMBER_INFO_KEY)
    return null
  }
}

// 存储会员信息到 localStorage（与现有信息做合并，避免字段丢失）
export function setMemberInfo(info) {
  const current = readMemberInfoRaw() || {}
  const normalized = normalizeMemberInfo({ ...current, ...(info || {}) })
  if (!normalized) {
    return null
  }
  localStorage.setItem(MEMBER_INFO_KEY, JSON.stringify(normalized))
  return normalized
}

export function getMemberInfo() {
  const normalized = normalizeMemberInfo(readMemberInfoRaw())
  if (!normalized) return null
  return normalized
}

export function removeMemberInfo() {
  localStorage.removeItem(MEMBER_INFO_KEY)
}

// 获取当前登录会员ID（未登录返回null）
export function getMemberId() {
  const info = getMemberInfo()
  return info?.memberId ?? info?.id ?? null
}

// 判断是否已登录
export function isMemberLoggedIn() {
  return !!getMemberId()
}

// 拉取并补全会员资料（头像、手机号、积分等）
export async function refreshMemberInfo(options = {}) {
  const { force = false } = options
  const localInfo = getMemberInfo()
  const memberId = localInfo?.memberId ?? localInfo?.id
  if (!memberId) return null

  const fetchedAt = Number(localInfo?._profileFetchedAt || 0)
  if (!force && fetchedAt && Date.now() - fetchedAt < PROFILE_CACHE_TTL) {
    return localInfo
  }

  if (refreshPromise) {
    return refreshPromise
  }

  refreshPromise = (async () => {
    try {
      const { getMemberInfoApi } = await import('@/api/frontend/member')
      const res = await getMemberInfoApi(memberId)
      if (res?.success && res.data) {
        return setMemberInfo({
          ...localInfo,
          ...res.data,
          memberId,
          id: res.data.id ?? memberId,
          _profileFetchedAt: Date.now()
        })
      }
    } catch (e) {
      // 忽略补拉失败，保留本地最小登录态
    }
    return getMemberInfo()
  })().finally(() => {
    refreshPromise = null
  })

  return refreshPromise
}
