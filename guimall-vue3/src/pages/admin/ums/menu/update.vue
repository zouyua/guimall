<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑菜单</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="菜单信息">
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <!-- 表单：上级菜单（可调整层级） -->
        <a-form-item name="parentId" label="上级菜单" :required="true">
          <a-select v-model:value="form.parentId" class="w-full">
            <a-select-option :value="0">顶级菜单</a-select-option>
            <a-select-option v-for="p in parentOptions" :key="p.id" :value="p.id">{{ p.name }}</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 表单：菜单名称（必填） -->
        <a-form-item name="name" label="菜单名称" :required="true">
          <a-input v-model:value="form.name" allow-clear />
        </a-form-item>

        <!-- 表单：路由路径（可选） -->
        <a-form-item label="路由路径">
          <a-input v-model:value="form.path" allow-clear />
        </a-form-item>

        <!-- 表单：图标组件名（可选） -->
        <a-form-item label="图标组件名">
          <a-input v-model:value="form.icon" allow-clear />
        </a-form-item>

        <!-- 表单：排序（必填） -->
        <a-form-item name="sort" label="排序" :required="true">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <!-- 表单：显示/隐藏（必填） -->
        <a-form-item name="hidden" label="显示" :required="true">
          <a-switch :checked="form.hidden === 0" @update:checked="(v) => (form.hidden = v ? 0 : 1)" />
        </a-form-item>
      </a-form>

      <!-- 提交区：保存修改后返回菜单列表 -->
      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { fetchUmsMenuList, fetchUmsMenuOptions, updateUmsMenu } from '@/api/admin/umsMenu'

const router = useRouter()
const route = useRoute()

const parentOptions = ref([])

// 编辑菜单表单模型（对应 UpdateUmsMenuReqVO）
const form = reactive({
  id: null,
  parentId: 0,
  name: '',
  path: '',
  icon: '',
  sort: 0,
  hidden: 0
})

const formRef = ref(null)

// 编辑菜单表单校验规则
const rules = {
  parentId: [{ required: true, message: '请选择上级菜单', trigger: 'change' }],
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  hidden: [{ required: true, message: '请选择显示状态', trigger: 'change' }]
}

onMounted(async () => {
  // 1) 获取上级菜单下拉选项
  const options = await fetchUmsMenuOptions()
  parentOptions.value = Array.isArray(options) ? options : options?.data || []
  if (!Array.isArray(parentOptions.value)) {
    console.log('fetchUmsMenuOptions failed:', options)
    message.error(options?.message || '获取菜单下拉选项失败')
    return
  }

  // 2) 根据路由参数加载要编辑的菜单
  const id = Number(route.query.id)
  if (!id || Number.isNaN(id)) return

  const rsp = await fetchUmsMenuList({ current: 1, size: 1000 })
  const row = rsp?.success ? (rsp.data || []).find((r) => Number(r.id) === id) : null

  if (row) {
    Object.assign(form, {
      id: row.id,
      parentId: row.parentId ?? 0,
      name: row.name ?? '',
      path: row.path ?? '',
      icon: row.icon ?? '',
      sort: row.sort ?? 0,
      hidden: row.hidden ?? 0
    })
  }
})

const goBack = () => {
  router.push('/admin/ums/menu')
}

// 提交编辑：表单校验通过后调用 updateUmsMenu
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  await updateUmsMenu({
    id: form.id,
    parentId: form.parentId ?? 0,
    name: form.name.trim(),
    path: form.path,
    icon: form.icon,
    sort: form.sort,
    hidden: form.hidden
  })

  message.success('保存成功')
  goBack()
}
</script>
