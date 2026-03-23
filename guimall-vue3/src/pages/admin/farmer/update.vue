<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑农户</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form
        :model="form"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="农户姓名" required>
          <a-input v-model:value="form.name" placeholder="请输入姓名" allow-clear />
        </a-form-item>

        <a-form-item label="手机号" required>
          <a-input v-model:value="form.phone" placeholder="请输入手机号" maxlength="11" allow-clear />
        </a-form-item>

        <a-form-item label="所在地区">
          <a-input v-model:value="form.region" placeholder="如：广西桂林市临桂区" allow-clear />
        </a-form-item>

        <a-form-item label="头像地址">
          <a-input v-model:value="form.avatar" placeholder="图片 URL" allow-clear />
        </a-form-item>

        <a-form-item label="认证状态">
          <a-select v-model:value="form.certStatus" placeholder="请选择" class="w-full" allow-clear>
            <a-select-option :value="0">待审核</a-select-option>
            <a-select-option :value="1">已认证</a-select-option>
            <a-select-option :value="2">未通过</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="关联商品数">
          <a-input-number v-model:value="form.productCount" :min="0" class="w-full" disabled />
        </a-form-item>

        <a-form-item label="注册时间">
          <a-input v-model:value="form.createTime" disabled />
        </a-form-item>

        <a-form-item label="简介">
          <a-textarea
            v-model:value="form.intro"
            :rows="4"
            placeholder="选填：种植品类、基地介绍等"
            allow-clear
          />
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
  name: '',
  phone: '',
  region: '',
  avatar: '',
  certStatus: 0,
  productCount: 0,
  createTime: '',
  intro: ''
})

const mockById = {
  1: {
    id: 1,
    name: '张三',
    phone: '13800138001',
    region: '广西桂林',
    avatar: 'https://picsum.photos/seed/f1/80',
    certStatus: 1,
    productCount: 5,
    createTime: '2026-03-01',
    intro: '桂林砂糖橘种植基地。'
  },
  2: {
    id: 2,
    name: '李四',
    phone: '13900139002',
    region: '江西赣州',
    avatar: 'https://picsum.photos/seed/f2/80',
    certStatus: 1,
    productCount: 3,
    createTime: '2026-03-05',
    intro: ''
  },
  3: {
    id: 3,
    name: '王五',
    phone: '13700137003',
    region: '云南昆明',
    avatar: 'https://picsum.photos/seed/f3/80',
    certStatus: 0,
    productCount: 0,
    createTime: '2026-03-12',
    intro: '新入驻，资料审核中。'
  },
  4: {
    id: 4,
    name: '赵六',
    phone: '13600136004',
    region: '四川成都',
    avatar: 'https://picsum.photos/seed/f4/80',
    certStatus: 2,
    productCount: 1,
    createTime: '2026-03-14',
    intro: ''
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
  router.push('/admin/farmer')
}

const handleSubmit = () => {
  if (!form.name?.trim()) {
    message.warning('请输入农户姓名')
    return
  }
  if (!form.phone?.trim()) {
    message.warning('请输入手机号')
    return
  }
  if (!/^1\d{10}$/.test(form.phone.trim())) {
    message.warning('请输入正确的 11 位手机号')
    return
  }
  console.log('保存农户', { ...form })
  message.success('保存成功（演示数据，未请求后端）')
  goBack()
}
</script>
