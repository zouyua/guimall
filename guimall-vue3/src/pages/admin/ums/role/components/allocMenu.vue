<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">分配菜单权限</span>
        <span v-if="roleName" class="text-sm text-gray-500">（当前角色：{{ roleDisplay(roleName) }}）</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="菜单权限">
      <!-- 说明：
           1) Tree 用于展示菜单层级结构
           2) checkedKeys 用于回显/提交当前角色拥有的菜单权限（这里 key=menuId，字符串形式）
           3) 保存会调用后端 /admin/ums/role/allocMenu/save 将权限写入 t_role_menu_relation -->
      <p class="mb-4 text-sm text-gray-500">
        勾选该<strong>角色</strong>登录后台时可访问的菜单；与「角色管理」中绑定本角色的账号一致。
        保存后权限会生效。
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

      <!-- 提交区：保存权限并返回角色列表 -->
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
import { initUmsRoleAllocMenu, saveUmsRoleAllocMenu } from '@/api/admin/umsRole'

const router = useRouter()
const route = useRoute()

const roleName = ref('')

// =========================
// 角色名称展示映射（仅用于前端“识别”，不影响 roleId->roleCode 的提交逻辑）
// =========================
const ROLE_LABEL_MAP = {
  ROLE_ADMIN: '超级管理员',
  ROLE_ORDER: '订单管理员',
  ROLE_PRODUCT: '商品管理员',
  ROLE_MKT: '营销管理员',
  ROLE_VISITOR: '联调/UAT账号'
}

const roleDisplay = (roleCode) => ROLE_LABEL_MAP[roleCode] || roleCode || ''

// Ant Design Vue Tree 需要的树结构：由后端 init 接口返回（key=menuId 字符串）
const treeData = ref([])

// Tree 已选中的 key 列表（由后端 init 接口返回，用于回显）

// 当前树选中菜单 key（最终提交到后端）
const checkedKeys = ref([])

const onCheck = (keys) => {
  checkedKeys.value = keys
}

// 初始化：加载角色信息 + 菜单树 + checkedKeys
onMounted(async () => {
  const rid = Number(route.query.roleId)
  if (!rid || Number.isNaN(rid)) return

  const rsp = await initUmsRoleAllocMenu(rid)
  if (rsp?.success) {
    roleName.value = rsp.data?.roleName || route.query.roleName || ''
    treeData.value = rsp.data?.treeData || []
    checkedKeys.value = rsp.data?.checkedKeys || []
  }
})

const goBack = () => {
  router.push('/admin/ums/role')
}

// 保存：把 checkedKeys 作为 roleId -> menuId 的映射保存
const handleSave = async () => {
  const rid = Number(route.query.roleId)
  if (!rid || Number.isNaN(rid)) {
    message.warning('缺少角色信息')
    return
  }

  await saveUmsRoleAllocMenu({
    roleId: rid,
    checkedKeys: checkedKeys.value
  })

  message.success('保存成功')
  goBack()
}
</script>
