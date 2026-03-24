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
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="轮播标题" name="name" required>
          <a-input v-model:value="form.name" allow-clear />
        </a-form-item>

        <a-form-item label="轮播图片" name="pic" required>
          <a-input v-model:value="form.pic" allow-clear />
        </a-form-item>

        <a-form-item label="广告位置" name="type" required>
          <a-select v-model:value="form.type" class="w-full max-w-xs">
            <a-select-option :value="0">WEB首页轮播</a-select-option>
            <a-select-option :value="1">APP首页轮播</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="开始时间" name="startTime" required>
          <a-input v-model:value="form.startTime" allow-clear />
        </a-form-item>

        <a-form-item label="结束时间" name="endTime" required>
          <a-input v-model:value="form.endTime" allow-clear />
        </a-form-item>

        <a-form-item label="排序" name="sort" required>
          <a-input-number v-model:value="form.sort" :min="0" class="w-full max-w-xs" />
        </a-form-item>

        <a-form-item label="状态" name="status" required>
          <a-switch :checked="form.status === 1" @update:checked="(v) => (form.status = v ? 1 : 0)" />
        </a-form-item>

        <a-form-item label="跳转地址">
          <a-input v-model:value="form.url" allow-clear />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.note" :rows="3" allow-clear />
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
// 编辑轮播图页
// 职责：加载详情 + 表单校验 + 保存更新
import { reactive, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getHomeAdvertiseDetail, updateHomeAdvertise } from '@/api/admin/homeAdvertise'

const router = useRouter()
const route = useRoute()

// 表单引用（用于 validate）
const formRef = ref()
// 表单数据（字段与 UpdateSmsHomeAdvertiseReqVO 对齐）
const form = reactive({
  id: undefined,
  name: '',
  type: 0,
  pic: '',
  startTime: '',
  endTime: '',
  sort: 1,
  status: 1,
  url: '',
  note: ''
})

// 表单规则（必填项提示）
const rules = {
  name: [{ required: true, message: '请输入轮播标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择广告位置', trigger: 'change' }],
  pic: [{ required: true, message: '请输入轮播图片', trigger: 'blur' }],
  startTime: [{ required: true, message: '请输入开始时间', trigger: 'blur' }],
  endTime: [{ required: true, message: '请输入结束时间', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序值', trigger: 'change' }]
}

const loadDetail = async () => {
  // 从路由 query 读取 id，并加载详情
  const id = Number(route.query.id)
  if (!id) {
    message.warning('广告ID不能为空')
    goBack()
    return
  }
  const rsp = await getHomeAdvertiseDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取轮播图详情失败')
    return
  }
  Object.assign(form, rsp.data)
}

onMounted(() => {
  // 首次进入页面自动加载
  loadDetail()
})

const goBack = () => {
  router.push('/admin/sms/advertise')
}

const handleSubmit = async () => {
  // 保存按钮入参校验：id 必须存在
  if (!form.id) {
    message.warning('广告ID不能为空')
    return
  }
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }
  // 提交入参与后端 VO 严格对齐
  const payload = {
    name: form.name.trim(),
    type: Number(form.type),
    pic: form.pic.trim(),
    startTime: form.startTime.trim(),
    endTime: form.endTime.trim(),
    status: Number(form.status),
    url: form.url?.trim() || '',
    note: form.note?.trim() || '',
    sort: Number(form.sort || 0)
  }
  const rsp = await updateHomeAdvertise(form.id, payload)
  if (!rsp?.success) {
    message.error(rsp?.message || '保存失败')
    return
  }
  message.success('保存成功')
  goBack()
}
</script>
