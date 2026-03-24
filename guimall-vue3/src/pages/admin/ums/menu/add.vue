<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增菜单</span>
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
        <!-- 表单：上级菜单（顶级菜单 parentId=0） -->
        <a-form-item label="上级菜单">
          <a-select v-model:value="form.parentId" placeholder="不选则为顶级菜单" class="w-full" allow-clear>
            <a-select-option :value="0">顶级菜单</a-select-option>
            <a-select-option v-for="p in parentOptions" :key="p.id" :value="p.id">{{ p.name }}</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 表单：菜单名称（必填） -->
        <a-form-item name="name" label="菜单名称" :required="true">
          <a-input v-model:value="form.name" placeholder="侧栏显示名称" allow-clear />
        </a-form-item>

        <!-- 表单：路由路径（可选，前端路由用于跳转） -->
        <a-form-item label="路由路径">
          <a-input v-model:value="form.path" placeholder="如 /admin/xxx，目录可留空" allow-clear />
        </a-form-item>

        <!-- 表单：图标组件名（可选，需与左侧菜单 icon 使用一致） -->
        <a-form-item label="图标组件名">
          <a-input v-model:value="form.icon" placeholder="如 UserOutlined，与 AdminMenu 中一致" allow-clear />
        </a-form-item>

        <!-- 表单：排序（必填，越小越靠前） -->
        <a-form-item name="sort" label="排序" :required="true">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <!-- 表单：显示/隐藏（必填；checked=true 表示 hidden=0） -->
        <a-form-item name="hidden" label="显示" :required="true">
          <a-switch :checked="form.hidden === 0" @update:checked="(v) => (form.hidden = v ? 0 : 1)" />
          <span class="ml-2 text-sm text-gray-500">关闭则侧栏隐藏该项</span>
        </a-form-item>
      </a-form>

      <!-- 提交区：提交表单（会先 validate），保存成功后返回菜单列表 -->
      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">提交</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { fetchUmsMenuOptions, createUmsMenu } from '@/api/admin/umsMenu'

const router = useRouter()

// 上级菜单下拉选项（来自后端 /admin/ums/menu/options）
const parentOptions = ref([])

const formRef = ref(null)

// 菜单新增表单校验规则
const rules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  hidden: [{ required: true, message: '请选择显示状态', trigger: 'change' }]
}

const form = reactive({
  parentId: undefined,
  name: '',
  path: '',
  icon: '',
  sort: 0,
  hidden: 0
})

const goBack = () => {
  router.push('/admin/ums/menu')
}

onMounted(async () => {
  // 初始化：加载“上级菜单”下拉选项（用于选择 parentId）
  const rsp = await fetchUmsMenuOptions()
  parentOptions.value = Array.isArray(rsp) ? rsp : rsp?.data || []
  if (!Array.isArray(parentOptions.value)) {
    console.log('fetchUmsMenuOptions failed:', rsp)
    message.error(rsp?.message || '获取菜单下拉选项失败')
  }
})

// 提交新增菜单：校验通过后调用 createUmsMenu
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  await createUmsMenu({
    parentId: form.parentId ?? 0,
    name: form.name.trim(),
    path: form.path,
    icon: form.icon,
    sort: form.sort,
    hidden: form.hidden
  })

  message.success('提交成功')
  goBack()
}
</script>
