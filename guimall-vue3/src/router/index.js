import { createRouter, createWebHashHistory } from 'vue-router'
import Admin from '@/layouts/admin/admin.vue'

// 统一在这里声明所有路由
const routes = [
    {
        path: '/', // 路由地址
        component: () => import('@/pages/frontend/home/index.vue'), // 对应组件
        meta: { // meta 信息
            title: 'guimall 首页' // 页面标题
        }
    },
    {
        path: '/trace/:productId', // 溯源详情页
        component: () => import('@/pages/frontend/trace/index.vue'),
        meta: {
            title: '商品溯源'
        }
    },
    {
        path: '/category', // 商品分类
        component: () => import('@/pages/frontend/product/category.vue'),
        meta: { title: '商品分类' }
    },
    {
        path: '/support', // 助农专区
        component: () => import('@/pages/frontend/support/index.vue'),
        meta: { title: '助农专区' }
    },
    {
        path: '/about', // 关于我们
        component: () => import('@/pages/frontend/common/placeholder.vue'),
        props: { title: '关于我们', description: 'GUIMALL 致力于通过数字化手段，让桂林深山里的好物走向全国，助力农户增收。' },
        meta: { title: '关于我们' }
    },
    {
        path: '/cart',
        component: () => import('@/pages/frontend/cart/index.vue'),
        meta: { title: '购物车' }
    },
    {
        path: '/checkout',
        component: () => import('@/pages/frontend/checkout/index.vue'),
        meta: { title: '确认订单' }
    },
    {
        path: '/coupon-center',
        component: () => import('@/pages/frontend/coupon/index.vue'),
        meta: { title: '领券中心' }
    },
    {
        path: '/user/coupons',
        component: () => import('@/pages/frontend/user/coupons.vue'),
        meta: { title: '我的优惠券' }
    },
    {
        path: '/pay',
        component: () => import('@/pages/frontend/pay/index.vue'),
        meta: { title: '订单支付' }
    },
    {
        path: '/pay/result',
        component: () => import('@/pages/frontend/pay/result.vue'),
        meta: { title: '支付结果' }
    },
    {
        path: '/my-orders',
        component: () => import('@/pages/frontend/order/index.vue'),
        meta: { title: '我的订单' }
    },
    {
        path: '/order/detail',
        component: () => import('@/pages/frontend/order/detail.vue'),
        meta: { title: '订单详情' }
    },
    {
        path: '/order/return',
        component: () => import('@/pages/frontend/order/return.vue'),
        meta: { title: '申请退货' }
    },
    {
        path: '/member/center',
        component: () => import('@/pages/frontend/member/center.vue'),
        meta: { title: '个人中心' }
    },
    {
        path: '/member/login',
        component: () => import('@/pages/frontend/member/login.vue'),
        meta: { title: '会员登录' }
    },
    {
        path: '/login',//登录页
        component: () => import('@/pages/admin/login.vue'),
        meta: {
            title: 'guimall 登录页'
        }
    },
    {
        path: '/product/detail', // 前台商品详情页
        component: () => import('@/pages/frontend/product/detail.vue'),
        meta: {
            title: '商品详情'
        }
    },
    {
        path: '/admin',//后台首页
        component: Admin,
        redirect: '/admin/index', // 默认子路由
        children: [
            {
                path: '/admin/index',
                component: () => import('@/pages/admin/index.vue'),
                meta: { title: '仪表盘' }
            },
            {
                path: 'farmer',
                component: () => import('@/pages/admin/farmer/index.vue'),
                meta: { title: '农户列表' }
            },
            {
                path: 'farmer/add',
                component: () => import('@/pages/admin/farmer/add.vue'),
                meta: { title: '添加农户' }
            },
            {
                path: 'farmer/update',
                component: () => import('@/pages/admin/farmer/update.vue'),
                meta: { title: '更改农户信息' }
            },
            {
                path: 'farmer/farmer-link',
                component: () => import('@/pages/admin/farmer/components/farmerLink.vue'),
                meta: { title: '农户关联' }
            },
            {
                path: 'pms/product',
                component: () => import('@/pages/admin/pms/product/index.vue'),
                meta: { title: '商品管理' }
            },
            {
                path: 'pms/product/add',
                component: () => import('@/pages/admin/pms/product/add.vue'),
                meta: { title: '添加商品' }
            },
            {
                path: 'pms/product/update',
                component: () => import('@/pages/admin/pms/product/update.vue'),
                meta: { title: '更改商品信息' }
            },
            {
                path: 'pms/product/detail',
                component: () => import('@/pages/admin/pms/product/components/ProductDetail.vue'),
                meta: { title: '商品详情' }
            },
            {
                path: 'pms/productCate',
                component: () => import('@/pages/admin/pms/productCate/index.vue'),
                meta: { title: '商品分类' }
            },
            {
                path: 'pms/productCate/add',
                component: () => import('@/pages/admin/pms/productCate/add.vue'),
                meta: { title: '添加商品分类' }
            },
            {
                path: 'pms/productCate/update',
                component: () => import('@/pages/admin/pms/productCate/update.vue'),
                meta: { title: '更改商品分类' }
            },
            {
                path: 'pms/paramDefinition',
                component: () => import('@/pages/admin/pms/paramDefinition/index.vue'),
                meta: { title: '参数管理' }
            },
            {
                path: 'oms/order',
                component: () => import('@/pages/admin/oms/order/index.vue'),
                meta: { title: '订单列表' }
            },
            {
                path: 'oms/order/orderDetail',
                component: () => import('@/pages/admin/oms/order/components/orderDetail.vue'),
                meta: { title: '订单详情' }
            },
            {
                path: 'oms/order/deliverOrderList',
                component: () => import('@/pages/admin/oms/order/components/deliverOrderList.vue'),
                meta: { title: '发货管理' }
            },
            {
                path: 'oms/apply',
                component: () => import('@/pages/admin/oms/apply/index.vue'),
                meta: { title: '退货处理' }
            },
            {
                path: 'oms/apply/reason',
                component: () => import('@/pages/admin/oms/apply/components/reason.vue'),
                meta: { title: '退货原因处理' }
            },
            {
                path: 'oms/apply/applyDetail',
                component: () => import('@/pages/admin/oms/apply/components/applyDetail.vue'),
                meta: { title: '退货详情' }
            },
            {
                path: 'sms/coupon',
                component: () => import('@/pages/admin/sms/coupon/index.vue'),
                meta: { title: '优惠券管理' }
            },
            {
                path: 'sms/coupon/add',
                component: () => import('@/pages/admin/sms/coupon/add.vue'),
                meta: { title: '添加优惠券' }
            },
            {
                path: 'sms/coupon/update',
                component: () => import('@/pages/admin/sms/coupon/update.vue'),
                meta: { title: '更改优惠券' }
            },
            {
                path: 'sms/coupon/history',
                component: () => import('@/pages/admin/sms/coupon/components/history.vue'),
                meta: { title: '优惠券领取详情' }
            },
            {
                path: 'sms/new',
                component: () => import('@/pages/admin/sms/new/index.vue'),
                meta: { title: '新品推荐' }
            },
            {
                path: 'sms/hot',
                component: () => import('@/pages/admin/sms/hot/index.vue'),
                meta: { title: '人气推荐' }
            },
            {
                path: 'sms/advertise',
                component: () => import('@/pages/admin/sms/advertise/index.vue'),
                meta: { title: '轮播图管理' }
            },
            {
                path: 'sms/advertise/add',
                component: () => import('@/pages/admin/sms/advertise/add.vue'),
                meta: { title: '新增轮播图' }
            },
            {
                path: 'sms/advertise/update',
                component: () => import('@/pages/admin/sms/advertise/update.vue'),
                meta: { title: '编辑轮播图' }
            },
            {
                path: 'trace/origin',
                component: () => import('@/pages/admin/trace/origin.vue'),
                meta: { title: '产地信息' }
            },
            {
                path: 'trace/origin/add',
                component: () => import('@/pages/admin/trace/add.vue'),
                meta: { title: '新增产地' }
            },
            {
                path: 'trace/origin/update',
                component: () => import('@/pages/admin/trace/update.vue'),
                meta: { title: '编辑产地' }
            },
            {
                path: 'trace/record',
                component: () => import('@/pages/admin/trace/record.vue'),
                meta: { title: '溯源记录管理' }
            },
            {
                path: 'ums/admin',
                component: () => import('@/pages/admin/ums/admin/index.vue'),
                meta: { title: '角色管理' }
            },
            {
                path: 'ums/role',
                component: () => import('@/pages/admin/ums/role/index.vue'),
                meta: { title: '管理员管理' }
            },
            {
                path: 'ums/role/allocMenu',
                component: () => import('@/pages/admin/ums/role/components/allocMenu.vue'),
                meta: { title: '分配菜单权限' }
            },
            {
                path: 'ums/menu',
                component: () => import('@/pages/admin/ums/menu/index.vue'),
                meta: { title: '菜单管理' }
            },
            {
                path: 'ums/menu/add',
                component: () => import('@/pages/admin/ums/menu/add.vue'),
                meta: { title: '新增菜单' }
            },
            {
                path: 'ums/menu/update',
                component: () => import('@/pages/admin/ums/menu/update.vue'),
                meta: { title: '编辑菜单' }
            }
        ]
    }
]

// 创建路由
const router = createRouter({
    // 指定路由的历史管理方式，hash 模式指的是 URL 的路径是通过 hash 符号（#）进行标识
    history: createWebHashHistory(),
    // routes: routes 的缩写
    routes, 
})

// ES6 模块导出语句，它用于将 router 对象导出，以便其他文件可以导入和使用这个对象
export default router
