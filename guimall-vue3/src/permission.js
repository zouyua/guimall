import router from '@/router/index'
import { getToken } from '@/composables/cookie'
import { showMessage } from '@/composables/util'
import { showPageLoading, hidePageLoading } from '@/composables/util'


// 全局路由前置守卫
router.beforeEach((to, from, next) => {
    console.log('==> 全局路由前置守卫')

    //展示页面加载 Loading
    showPageLoading()

    // 若用户想访问后台（以/admin为前缀的路由）
    let token = getToken()

    if (to.path.startsWith('/admin')) {
        // 未登录
        if (!token) {
            showMessage('请先登录', 'warning')
            return next({ path: '/login' })
        }
    }

    // 若用户已经登录且访问登录页
    if (token && to.path === '/login') {
      showMessage('您已登录，无需重复登录', 'warning')
      return next({ path: '/admin/index' })
    }

    next()
})

// 全局路由后置守卫
router.afterEach((to, from) => {
    // 动态设置页面 Title
    let title = (to.meta.title ? to.meta.title : '') + ' - guimall'
    document.title = title

    //隐藏页面加载 Loading
    hidePageLoading()
})

