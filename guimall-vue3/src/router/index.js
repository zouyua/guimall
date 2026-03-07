import Index from '@/pages/frontend/index.vue'
import Login from '@/pages/admin/login.vue'
import AdminIndex from '@/pages/admin/index.vue'
// import AdminDashboard from '@/pages/admin/dashboard/index.vue'
// import AdminFarmer from '@/pages/admin/farmer/index.vue'
// import AdminOms from '@/pages/admin/oms'
// import AdminPms from '@/pages/admin/product/index.vue'
// import AdminSms from '@/pages/admin/sms'
// import AdminTrace from '@/pages/admin/trace'
// import AdminUms from '@/pages/admin/ums'
import { createRouter, createWebHashHistory } from 'vue-router'
import Admin from '@/layouts/admin/admin.vue'

// 统一在这里声明所有路由
const routes = [
    {
        path: '/', // 路由地址
        component: Index, // 对应组件
        meta: { // meta 信息
            title: 'guimall 首页' // 页面标题
        }
    },
    {
        path: '/login',//登录页
        component: Login,
        meta: {
            title: 'guimall 登录页'
        }
    },
    {
        path: '/admin',//后台首页
        component: Admin,
        redirect: '/admin/index', // 默认子路由
        children: [
            {
                path: '/admin/index',
                component: AdminIndex,
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
                path: 'pms/product/farmer-link',
                component: () => import('@/pages/admin/pms/product/index.vue'), // 暂时指向商品列表作为占位
                meta: { title: '农户关联' }
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
                path: 'pms/productAttr',
                component: () => import('@/pages/admin/pms/productAttr/index.vue'),
                meta: { title: '商品类型' }
            },
            {
                path: 'pms/productAttr/productAttrList',
                component: () => import('@/pages/admin/pms/productAttr/productAttrList.vue'),
                meta: { title: '商品类型列表' }
            },
            {
                path: 'pms/productAttr/addProductAttr',
                component: () => import('@/pages/admin/pms/productAttr/addProductAttr.vue'),
                meta: { title: '添加商品类型' }
            },
            {
                path: 'pms/productAttr/updateProductAttr',
                component: () => import('@/pages/admin/pms/productAttr/updateProductAttr.vue'),
                meta: { title: '更改商品类型' }
            },
            {
                path: 'oms/order',
                component: () => import('@/pages/admin/oms/order/index.vue'),
                meta: { title: '订单列表' }
            },
            {
                path: 'oms/order/orderDetail',
                component: () => import('@/pages/admin/oms/order/orderDetail.vue'),
                meta: { title: '订单详情' }
            },
            {
                path: 'oms/order/deliverOrderList',
                component: () => import('@/pages/admin/oms/order/deliverOrderList.vue'),
                meta: { title: '发货管理' }
            },
            {
                path: 'oms/apply',
                component: () => import('@/pages/admin/oms/apply/index.vue'),
                meta: { title: '退货处理' }
            },
            {
                path: 'oms/apply/reason',
                component: () => import('@/pages/admin/oms/apply/reason.vue'),
                meta: { title: '退货原因处理' }
            },
            {
                path: 'oms/apply/applyDetail',
                component: () => import('@/pages/admin/oms/apply/applyDetail.vue'),
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
                component: () => import('@/pages/admin/sms/coupon/history.vue'),
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
                meta: { title: '广告管理' }
            },
            {
                path: 'sms/advertise/add',
                component: () => import('@/pages/admin/sms/advertise/add.vue'),
                meta: { title: '添加广告' }
            },
            {
                path: 'sms/advertise/update',
                component: () => import('@/pages/admin/sms/advertise/update.vue'),
                meta: { title: '更改广告' }
            },
            {
                path: 'trace/origin',
                component: () => import('@/pages/admin/trace/origin.vue'),
                meta: { title: '产地信息' }
            },
            {
                path: 'ums/admin',
                component: () => import('@/pages/admin/ums/admin/index.vue'),
                meta: { title: '管理员管理' }
            },
            {
                path: 'ums/role',
                component: () => import('@/pages/admin/ums/role/index.vue'),
                meta: { title: '角色管理' }
            },
            {
                path: 'ums/role/allocMenu',
                component: () => import('@/pages/admin/ums/role/allocMenu.vue'),
                meta: { title: '分配菜单' }
            },
            {
                path: 'ums/menu',
                component: () => import('@/pages/admin/ums/menu/index.vue'),
                meta: { title: '菜单管理' }
            },
            {
                path: 'ums/menu/add',
                component: () => import('@/pages/admin/ums/menu/add.vue'),
                meta: { title: '添加菜单' }
            },
            {
                path: 'ums/menu/update',
                component: () => import('@/pages/admin/ums/menu/update.vue'),
                meta: { title: '更改菜单' }
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
