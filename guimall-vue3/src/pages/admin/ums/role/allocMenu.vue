<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">分配菜单权限</span>
        <span v-if="roleName" class="text-sm text-gray-500">（当前角色：{{ roleName }}）</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="菜单权限">
      <p class="mb-4 text-sm text-gray-500">
        勾选该<strong>角色</strong>登录后台时可访问的菜单；与「角色管理」中绑定本角色的账号一致。保存为演示数据。
      </p>

      <a-tree
        checkable
        :tree-data="treeData"
        :checked-keys="checkedKeys"
        :field-names="{ children: 'children', title: 'title', key: 'key' }"
        default-expand-all
        class="max-w-2xl"
        @check="onCheck"
      />

      <div class="mt-8 flex justify-center gap-3">
        <a-button type="primary" @click="handleSave">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const roleName = ref('')

const treeData = [
  {
    title: '仪表盘',
    key: 'dashboard',
    children: [{ title: '工作台', key: 'dashboard-index' }]
  },
  {
    title: '商品模块',
    key: 'pms',
    children: [
      { title: '商品管理', key: 'pms-product' },
      { title: '商品分类', key: 'pms-cate' },
      { title: '商品类型', key: 'pms-attr' },
      { title: '产地信息', key: 'trace-origin' }
    ]
  },
  {
    title: '农户管理',
    key: 'farmer',
    children: [
      { title: '农户列表', key: 'farmer-list' },
      { title: '农户关联', key: 'farmer-link' }
    ]
  },
  {
    title: '订单模块',
    key: 'oms',
    children: [
      { title: '订单列表', key: 'oms-order' },
      { title: '发货管理', key: 'oms-deliver' },
      { title: '退货处理', key: 'oms-apply' },
      { title: '退货原因', key: 'oms-reason' }
    ]
  },
  {
    title: '营销模块',
    key: 'sms',
    children: [
      { title: '优惠券', key: 'sms-coupon' },
      { title: '新品推荐', key: 'sms-new' },
      { title: '人气推荐', key: 'sms-hot' },
      { title: '轮播图', key: 'sms-ad' }
    ]
  },
  {
    title: '权限模块',
    key: 'ums',
    children: [
      { title: '角色管理', key: 'ums-admin' },
      { title: '管理员管理', key: 'ums-role' },
      { title: '菜单管理', key: 'ums-menu' }
    ]
  }
]

const mockCheckedByRole = {
  1: [
    'dashboard',
    'dashboard-index',
    'pms',
    'pms-product',
    'pms-cate',
    'pms-attr',
    'trace-origin',
    'farmer',
    'farmer-list',
    'farmer-link',
    'oms',
    'oms-order',
    'oms-deliver',
    'oms-apply',
    'oms-reason',
    'sms',
    'sms-coupon',
    'sms-new',
    'sms-hot',
    'sms-ad',
    'ums',
    'ums-admin',
    'ums-role',
    'ums-menu'
  ],
  2: [
    'dashboard',
    'dashboard-index',
    'farmer',
    'farmer-list',
    'farmer-link',
    'oms',
    'oms-order',
    'oms-deliver',
    'oms-apply',
    'oms-reason'
  ],
  3: [
    'dashboard',
    'dashboard-index',
    'pms',
    'pms-product',
    'pms-cate',
    'pms-attr',
    'trace-origin',
    'farmer',
    'farmer-list',
    'farmer-link'
  ],
  4: ['dashboard', 'dashboard-index', 'sms', 'sms-coupon', 'sms-new', 'sms-hot', 'sms-ad'],
  5: [
    'dashboard',
    'dashboard-index',
    'pms',
    'pms-product',
    'pms-cate',
    'oms',
    'oms-order',
    'ums',
    'ums-admin',
    'ums-role'
  ]
}

const checkedKeys = ref([])

const onCheck = (keys) => {
  checkedKeys.value = keys
}

onMounted(() => {
  const rid = Number(route.query.roleId) || 1
  roleName.value = route.query.roleName || ''
  checkedKeys.value = [...(mockCheckedByRole[rid] || mockCheckedByRole[2])]
})

const goBack = () => {
  router.push('/admin/ums/role')
}

const handleSave = () => {
  console.log('保存菜单权限', { roleId: route.query.roleId, checkedKeys: checkedKeys.value })
  message.success('保存成功（演示数据）')
  goBack()
}
</script>
