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
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="上级菜单">
          <a-select v-model:value="form.parentId" placeholder="不选则为顶级菜单" class="w-full" allow-clear>
            <a-select-option :value="0">顶级菜单</a-select-option>
            <a-select-option v-for="p in parentOptions" :key="p.id" :value="p.id">{{ p.name }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="菜单名称" required>
          <a-input v-model:value="form.name" placeholder="侧栏显示名称" allow-clear />
        </a-form-item>

        <a-form-item label="路由路径">
          <a-input v-model:value="form.path" placeholder="如 /admin/xxx，目录可留空" allow-clear />
        </a-form-item>

        <a-form-item label="图标组件名">
          <a-input v-model:value="form.icon" placeholder="如 UserOutlined，与 AdminMenu 中一致" allow-clear />
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="显示">
          <a-switch :checked="form.hidden === 0" @update:checked="(v) => (form.hidden = v ? 0 : 1)" />
          <span class="ml-2 text-sm text-gray-500">关闭则侧栏隐藏该项</span>
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">提交</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const parentOptions = [
  { id: 2, name: '商品模块' },
  { id: 5, name: '权限模块' }
]

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

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入菜单名称')
    return
  }
  console.log('新增菜单', { ...form })
  message.success('提交成功（演示数据）')
  goBack()
}
</script>
