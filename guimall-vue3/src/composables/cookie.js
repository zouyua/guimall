import { useCookies } from '@vueuse/integrations/useCookies'

const cookie = useCookies()

// ============================== Token 令牌 ==============================

// 管理端 Token 的 key
const TOKEN_KEY = 'Authorization'

// 前台会员 Token 的 key
const MEMBER_TOKEN_KEY = 'MemberToken'

// 获取管理端 Token 值
export function getToken() {
    return cookie.get(TOKEN_KEY)
}

// 设置管理端 Token 到 Cookie 中
export function setToken(token) {
    return cookie.set(TOKEN_KEY, token, { path: '/' })
}

// 删除管理端 Token
export function removeToken() {
    return cookie.remove(TOKEN_KEY, { path: '/' })
}

// 获取前台会员 Token 值
export function getMemberToken() {
    return cookie.get(MEMBER_TOKEN_KEY)
}

// 设置前台会员 Token 到 Cookie 中
export function setMemberToken(token) {
    return cookie.set(MEMBER_TOKEN_KEY, token, { path: '/' })
}

// 删除前台会员 Token
export function removeMemberToken() {
    return cookie.remove(MEMBER_TOKEN_KEY, { path: '/' })
}

// ============================== 标签页 ==============================

// 存储在 Cookie 中的标签页数据的 key
const TAB_LIST_KEY = 'tabList'

// 获取 TabList
export function getTabList() {
    return cookie.get(TAB_LIST_KEY)
}

// 存储 TabList 到 Cookie 中
export function setTabList(tabList) {
    return cookie.set(TAB_LIST_KEY, tabList, { path: '/' })
}
