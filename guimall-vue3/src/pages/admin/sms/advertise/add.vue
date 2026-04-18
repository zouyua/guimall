<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增轮播图</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="轮播信息">
      <p class="mb-4 text-sm text-gray-500">图片将展示在前端首页轮播区域，接口字段与后端 VO 严格对齐。</p>
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="轮播标题" name="name" required>
          <a-input v-model:value="form.name" placeholder="后台识别用，如：春季活动" allow-clear />
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
          <a-input v-model:value="form.url" placeholder="可选" allow-clear />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model:value="form.note" :rows="3" placeholder="选填" allow-clear />
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
// 新增轮播图页
// 职责：表单校验 + 调用创建接口
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { createHomeAdvertise } from '@/api/admin/homeAdvertise'
import { uploadFile } from '@/api/admin/upload'

const router = useRouter()

// 表单引用（用于 validate）
const formRef = ref()
const picFileList = ref([])
// 表单数据（字段与 CreateSmsHomeAdvertiseReqVO 对齐）
const form = reactive({
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
  pic: [{ required: true, message: '请上传轮播图片', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  sort: [{ required: true, message: '请输入排序值', trigger: 'change' }]
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

const goBack = () => {
  // 返回轮播图列表页
  router.push('/admin/sms/advertise')
}

const handleSubmit = async () => {
  // 先做前端校验，校验不通过直接返回
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
    startTime: form.startTime?.trim(),
    endTime: form.endTime?.trim(),
    status: Number(form.status),
    url: form.url?.trim() || '',
    note: form.note?.trim() || '',
    sort: Number(form.sort || 0)
  }
  const rsp = await createHomeAdvertise(payload)
  if (!rsp?.success) {
    message.error(rsp?.message || '新增失败')
    return
  }
  message.success('新增成功')
  goBack()
}
</script>
