// 存储会员信息到 localStorage
export function setMemberInfo(info) {
  localStorage.setItem('memberInfo', JSON.stringify(info))
}

export function getMemberInfo() {
  const info = localStorage.getItem('memberInfo')
  return info ? JSON.parse(info) : null
}

export function removeMemberInfo() {
  localStorage.removeItem('memberInfo')
}

// 获取当前登录会员ID（未登录返回null）
export function getMemberId() {
  const info = getMemberInfo()
  return info ? info.memberId : null
}

// 判断是否已登录
export function isMemberLoggedIn() {
  return !!getMemberInfo()
}
