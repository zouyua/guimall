<template>
    <a-menu v-model:selectedKeys="selectedKeys" v-model:openKeys="openKeys" mode="inline" theme="dark"
        :style="{ borderRight: 0 }">
        <template v-for="menu in menus">
            <!-- 如果没有子菜单，则渲染为 a-menu-item -->
            <!-- <a-menu-item v-if="!menu.children || menu.children.length === 0" :key="menu.path" @click="goRoute(menu.path)">
                <component :is="menu.icon" />
                <span>{{ menu.title }}</span>
            </a-menu-item> -->
            <!-- 一级菜单 -->
            <a-menu-item v-if="!menu.children" :key="menu.path" @click="goRoute(menu.path)">
                <component :is="menu.icon" />
                <span>{{ menu.title }}</span>
            </a-menu-item>

            <!-- 二级菜单 -->
            <!-- 如果有子菜单，则渲染为 a-sub-menu -->
            <a-sub-menu v-else :key="menu.key">
                <template #title>
                    <component :is="menu.icon" />
                    <span>{{ menu.title }}</span>
                </template>
                <a-menu-item v-for="child in menu.children" :key="child.path" @click="goRoute(child.path)">
                    <component v-if="child.icon" :is="child.icon" class="mr-2" />
                    <span>{{ child.title }}</span>
                </a-menu-item>
            </a-sub-menu>
        </template>
    </a-menu>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMenuStore } from '@/stores/menu'

import {
    DashboardOutlined,
    TeamOutlined,
    AppstoreOutlined,
    ShoppingCartOutlined,
    UnorderedListOutlined,
    RollbackOutlined,
    FormOutlined,
    CarOutlined,
    GiftOutlined,
    TagsOutlined,
    RocketOutlined,
    HeartOutlined,
    PlayCircleOutlined,
    EnvironmentOutlined,
    SafetyCertificateOutlined,
    UserOutlined,
    PlusOutlined,
    ShoppingOutlined,
    ApartmentOutlined,
    SettingOutlined,
    LinkOutlined
} from '@ant-design/icons-vue'

//引入useMenuStore
const menuStore = useMenuStore()

const router = useRouter()
const route = useRoute()

//是否折叠
const isCollapse = computed(() => menuStore.menuWidth !== '200px')

// 当前选中菜单
const selectedKeys = ref([route.path])
const openKeys = ref([])

// 路由变化自动高亮
watch(
    () => route.path,
    (newPath) => {
        selectedKeys.value = [newPath]
    }
)

// 跳转
const goRoute = (path) => {
    router.push(path)
}

// 菜单结构
const menus = [
    {
        key: 'AdminIndex',
        title: '仪表盘',
        icon: DashboardOutlined,
        path: '/admin/index',//一级菜单路径
        // children: [
        //     {path: 'dashboard' }
        // ]
        // children: [
        //     { title: '仪表盘', icon: DashboardOutlined, path: '/admin/dashboard' }
        // ]
    },
    {
        key: 'product',
        title: '商品模块',
        icon: AppstoreOutlined,
        children: [
            { title: '商品管理', icon: ShoppingOutlined, path: '/admin/pms/product' },
            { title: '添加商品', icon: PlusOutlined, path: '/admin/pms/product/add' },
            { title: '商品分类', icon: ApartmentOutlined, path: '/admin/pms/productCate' },
            { title: '商品类型', icon: SettingOutlined, path: '/admin/pms/productAttr' },
            { title: '产地信息', icon: EnvironmentOutlined, path: '/admin/trace/origin' }
        ]
    },
    {
        key: 'farmer',
        title: '农户管理',
        icon: TeamOutlined,
        children: [
            { title: '农户列表', icon: UserOutlined, path: '/admin/farmer' },
            { title: '农户关联', icon: LinkOutlined, path: '/admin/farmer/farmer-link' }
        ]
    },
    {
        key: 'order',
        title: '订单模块',
        icon: ShoppingCartOutlined,
        children: [
            { title: '订单列表', icon: UnorderedListOutlined, path: '/admin/oms/order' },
            { title: '发货管理', icon: CarOutlined, path: '/admin/oms/order/deliverOrderList' },
            { title: '退货处理', icon: RollbackOutlined, path: '/admin/oms/apply' },
            { title: '退货原因处理', icon: FormOutlined, path: '/admin/oms/apply/reason' }
        ]
    },
    {
        key: 'marketing',
        title: '营销模块',
        icon: GiftOutlined,
        children: [
            { title: '优惠券管理', icon: TagsOutlined, path: '/admin/sms/coupon' },
            { title: '新品推荐', icon: RocketOutlined, path: '/admin/sms/new' },
            { title: '人气推荐', icon: HeartOutlined, path: '/admin/sms/hot' },
            { title: '轮播图管理', icon: PlayCircleOutlined, path: '/admin/sms/advertise' }
        ]
    },
    {
        key: 'system',
        title: '权限模块',
        icon: SettingOutlined,
        children: [
            { title: '角色管理', icon: UserOutlined, path: '/admin/ums/admin' },
            { title: '管理员管理', icon: SafetyCertificateOutlined, path: '/admin/ums/role' },
            { title: '菜单管理', icon: AppstoreOutlined, path: '/admin/ums/menu' }
        ]
    }
]
</script>

<style scoped>
/* 强制菜单标题使用 flex 对齐 */
.ant-menu-submenu-title,
.ant-menu-item {
    display: flex !important;
    align-items: center !important;
}

/* 图标固定大小 */
.ant-menu .anticon {
    font-size: 16px;
    display: inline-flex !important;
    align-items: center;
}

/* 修复：解决暗色主题下选中项或悬停项可能出现的背景变白问题 */
/* 选中项的背景颜色：使用 Ant Design 默认蓝色 */
:deep(.ant-menu-dark.ant-menu-inline .ant-menu-item-selected) {
    background-color: #1677ff !important;
}

/* 悬停时的背景颜色：使用透明白色，使其在暗色背景下稍微亮一点 */
:deep(.ant-menu-dark .ant-menu-item:hover),
:deep(.ant-menu-dark .ant-menu-submenu-title:hover) {
    background-color: rgba(255, 255, 255, 0.08) !important;
}

/* 解决子菜单展开后的背景色一致性 */
:deep(.ant-menu-dark .ant-menu-sub) {
    background-color: #000c17 !important;
}
</style>