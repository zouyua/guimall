<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑轮播图</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="轮播信息">
      <p class="mb-4 text-sm text-gray-500">
        与前端首页轮播一致，仅维护图片与展示顺序，不包含跳转链接。
      </p>
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="轮播标题" required>
          <a-input v-model:value="form.title" allow-clear />
        </a-form-item>

        <a-form-item label="轮播图片" required>
          <a-input v-model:value="form.pic" allow-clear />
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="启用">
          <a-switch :checked="form.enabled === 1" @update:checked="(v) => (form.enabled = v ? 1 : 0)" />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.remark" :rows="3" allow-clear />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const form = reactive({
  id: null,
  title: '',
  pic: '',
  sort: 1,
  enabled: 1,
  remark: ''
})

const mockById = {
  1: {
    id: 1,
    title: '春季鲜果节',
    pic: 'https://picsum.photos/seed/ad1/120/60',
    sort: 1,
    enabled: 1,
    remark: ''
  },
  2: {
    id: 2,
    title: '会员福利日',
    pic: 'https://picsum.photos/seed/ad2/120/60',
    sort: 2,
    enabled: 1,
    remark: '排序第 2 张'
  },
  3: {
    id: 3,
    title: '下线占位',
    pic: 'https://picsum.photos/seed/ad3/120/60',
    sort: 3,
    enabled: 0,
    remark: ''
  }
}

const loadMock = () => {
  const id = Number(route.query.id)
  const row = mockById[id] || mockById[1]
  Object.assign(form, row)
}

onMounted(() => {
  loadMock()
})

const goBack = () => {
  router.push('/admin/sms/advertise')
}

const handleSubmit = () => {
  if (!form.title?.trim() || !form.pic?.trim()) {
    message.warning('请填写轮播标题与轮播图片')
    return
  }
  console.log('保存轮播图', { ...form })
  message.success('保存成功（演示数据）')
  goBack()
}
</script>
