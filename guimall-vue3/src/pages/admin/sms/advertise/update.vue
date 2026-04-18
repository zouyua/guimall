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
          <a-upload
            :max-count="1"
            list-type="picture-card"
            :file-list="picFileList"
            :custom-request="handlePicUpload"
            @remove="handlePicRemove"
            accept="image/*"
          >
            <div v-if="picFileList.length === 0">
              <PlusOutlined />
              <div class="mt-2">上传图片</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="广告位置" name="type" required>
          <a-select v-model:value="form.type" class="w-full max-w-xs">
            <a-select-option :value="0">WEB首页轮播</a-select-option>
            <a-select-option :value="1">APP首页轮播</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="开始时间" name="startTime" required>
          <a-date-picker
            v-model:value="form.startTime"
            show-time
            value-format="YYYY-MM-DD HH:mm:ss"
            format="YYYY-MM-DD HH:mm:ss"
            class="w-full"
            placeholder="请选择开始时间"
          />
        </a-form-item>

        <a-form-item label="结束时间" name="endTime" required>
          <a-date-picker
            v-model:value="form.endTime"
            show-time
            value-format="YYYY-MM-DD HH:mm:ss"
            format="YYYY-MM-DD HH:mm:ss"
            class="w-full"
            placeholder="请选择结束时间"
          />
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
        <a-button type="primary" :loading="detailLoading" :disabled="detailLoading || !form.id" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { getHomeAdvertiseDetail, updateHomeAdvertise } from '@/api/admin/homeAdvertise'
import { uploadFile } from '@/api/admin/upload'

const router = useRouter()
const route = useRoute()

const formRef = ref()
const picFileList = ref([])
const detailLoading = ref(false)
const detailReqToken = ref(0)

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

const resetForm = () => {
  Object.assign(form, {
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
  picFileList.value = []
}

const rules = {
  name: [{ required: true, message: '请输入轮播标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择广告位置', trigger: 'change' }],
  pic: [{ required: true, message: '请上传轮播图片', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  sort: [{ required: true, message: '请输入排序值', trigger: 'change' }]
}

const normalizeDateTime = (value) => {
  if (!value) return ''
  const raw = String(value).trim()
  const normalized = raw.replace('T', ' ')
  return normalized.length > 19 ? normalized.slice(0, 19) : normalized
}

const getRouteAdvertiseId = () => {
  const rawId = Array.isArray(route.query.id) ? route.query.id[0] : route.query.id
  const id = Number(rawId)
  if (Number.isNaN(id) || id <= 0) return null
  return id
}

const handlePicUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      form.pic = res.data
      picFileList.value = [{ uid: '-1', name: file.name, status: 'done', url: res.data }]
      onSuccess(res)
      return
    }
    message.error(res.message || '上传失败')
    onError(new Error(res.message || '上传失败'))
  } catch (e) {
    message.error('上传失败')
    onError(e)
  }
}

const handlePicRemove = () => {
  form.pic = ''
  picFileList.value = []
}

const loadDetail = async () => {
  const id = getRouteAdvertiseId()
  if (!id) {
    resetForm()
    return
  }

  const token = ++detailReqToken.value
  detailLoading.value = true
  resetForm()

  try {
    const rsp = await getHomeAdvertiseDetail(id)
    if (token !== detailReqToken.value) return
    if (!rsp?.success || !rsp?.data) {
      message.error(rsp?.message || '获取轮播图详情失败')
      return
    }

    Object.assign(form, rsp.data)
    form.startTime = normalizeDateTime(form.startTime)
    form.endTime = normalizeDateTime(form.endTime)

    if (form.pic) {
      picFileList.value = [{ uid: '-1', name: '轮播图', status: 'done', url: form.pic }]
    }
  } finally {
    if (token === detailReqToken.value) {
      detailLoading.value = false
    }
  }
}

watch(
  () => route.query.id,
  () => {
    loadDetail()
  },
  { immediate: true }
)

const goBack = () => {
  router.push('/admin/sms/advertise')
}

const handleSubmit = async () => {
  if (detailLoading.value) {
    message.warning('详情加载中，请稍后')
    return
  }
  if (!form.id) {
    message.warning('广告ID不能为空')
    return
  }

  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  const payload = {
    name: form.name.trim(),
    type: Number(form.type),
    pic: form.pic.trim(),
    startTime: form.startTime?.trim(),
    endTime: form.endTime?.trim(),
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
