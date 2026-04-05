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
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="农户姓名" name="name" :required="true">
          <a-input v-model:value="form.name" placeholder="请输入姓名" allow-clear />
        </a-form-item>

        <a-form-item label="手机号" name="phone" :required="true">
          <a-input v-model:value="form.phone" placeholder="请输入手机号" maxlength="11" allow-clear />
        </a-form-item>

        <a-form-item label="身份证号" name="idCard">
          <a-input v-model:value="form.idCard" placeholder="选填" allow-clear />
        </a-form-item>

        <a-form-item label="农场名称" name="farmName">
          <a-input v-model:value="form.farmName" placeholder="如：临桂金桔合作社" allow-clear />
        </a-form-item>

        <a-form-item label="所在地区" name="area">
          <a-cascader
            v-model:value="areaValue"
            :options="areaOptions"
            placeholder="请选择省/市/区"
            :show-search="{ filter }"
            @change="handleAreaChange"
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item label="详细地址" name="detailAddress">
          <a-input v-model:value="form.detailAddress" placeholder="街道/乡镇/村信息" allow-clear />
        </a-form-item>

        <a-form-item label="关联产地" name="originIds">
          <a-select
            v-model:value="form.originIds"
            mode="multiple"
            placeholder="请选择该农户所在的产地（可多选）"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option v-for="o in originOptions" :key="o.id" :value="o.id" :label="o.originName">
              {{ o.originName }} ({{ o.province }}{{ o.city }})
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="头像">
          <a-upload
            :max-count="1"
            list-type="picture-card"
            :file-list="avatarFileList"
            :custom-request="handleAvatarUpload"
            @remove="handleAvatarRemove"
            accept="image/*"
          >
            <div v-if="avatarFileList.length === 0">
              <PlusOutlined />
              <div class="mt-2">上传头像</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="主要农产品" name="mainProduct">
          <a-input v-model:value="form.mainProduct" placeholder="如：金桔、罗汉果" allow-clear />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="form.status" placeholder="请选择" class="w-full">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="创建时间">
          <a-input v-model:value="form.createTime" disabled />
        </a-form-item>

        <a-form-item label="简介" name="description">
          <a-textarea
            v-model:value="form.description"
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { getFarmerDetail, updateFarmer } from '@/api/admin/farmer'
import { fetchTraceOriginOptions } from '@/api/admin/traceOrigin'
import { uploadFile } from '@/api/admin/upload'
import { chinaAreaData } from '@/utils/chinaArea'

const router = useRouter()
const route = useRoute()
const formRef = ref()

const areaOptions = chinaAreaData
const areaValue = ref([])
const originOptions = ref([])
const avatarFileList = ref([])

const handleAvatarUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      form.avatar = res.data
      avatarFileList.value = [{ uid: '-1', name: file.name, status: 'done', url: res.data }]
      onSuccess(res)
    } else {
      message.error(res.message || '上传失败')
      onError(new Error(res.message))
    }
  } catch (e) {
    message.error('上传失败')
    onError(e)
  }
}
const handleAvatarRemove = () => {
  form.avatar = ''
  avatarFileList.value = []
}

const form = reactive({
  id: null,
  name: '',
  phone: '',
  idCard: '',
  farmName: '',
  province: '',
  city: '',
  region: '',
  detailAddress: '',
  avatar: '',
  mainProduct: '',
  status: 1,
  createTime: '',
  description: '',
  originIds: []
})

const rules = {
  name: [{ required: true, message: '请输入农户姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ],
  area: [{ required: true, message: '请选择所在地区', trigger: 'change' }]
}

// 级联选择器搜索过滤
const filter = (inputValue, path) => {
  return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1)
}

// 处理地区选择变化
const handleAreaChange = (value, selectedOptions) => {
  if (selectedOptions && selectedOptions.length === 3) {
    form.province = selectedOptions[0].label
    form.city = selectedOptions[1].label
    form.region = selectedOptions[2].label
  } else {
    form.province = ''
    form.city = ''
    form.region = ''
  }
}

// 根据省市区回填级联选择器的值
const setAreaValueFromForm = () => {
  if (!form.province || !form.city || !form.region) {
    areaValue.value = []
    return
  }

  for (const province of areaOptions) {
    if (province.label === form.province) {
      for (const city of province.children || []) {
        if (city.label === form.city) {
          for (const region of city.children || []) {
            if (region.label === form.region) {
              areaValue.value = [province.value, city.value, region.value]
              return
            }
          }
        }
      }
    }
  }
  areaValue.value = []
}

const loadDetail = async () => {
  const id = Number(route.query.id)
  if (!id) {
    message.warning('缺少农户ID')
    goBack()
    return
  }
  const rsp = await getFarmerDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取农户详情失败')
    goBack()
    return
  }
  Object.assign(form, {
    id: rsp.data.id,
    name: rsp.data.name || '',
    phone: rsp.data.phone || '',
    idCard: rsp.data.idCard || '',
    farmName: rsp.data.farmName || '',
    province: rsp.data.province || '',
    city: rsp.data.city || '',
    region: rsp.data.region || '',
    detailAddress: rsp.data.detailAddress || '',
    avatar: rsp.data.avatar || '',
    mainProduct: rsp.data.mainProduct || '',
    status: rsp.data.status ?? 1,
    createTime: rsp.data.createTime || '',
    description: rsp.data.description || '',
    originIds: rsp.data.originIds || []
  })
  // 回填级联选择器
  setAreaValueFromForm()
  // 回填头像
  if (form.avatar) {
    avatarFileList.value = [{ uid: '-1', name: '头像', status: 'done', url: form.avatar }]
  }
}

onMounted(async () => {
  const originRsp = await fetchTraceOriginOptions()
  if (originRsp?.success) {
    originOptions.value = originRsp.data || []
  }
  await loadDetail()
})

const goBack = () => {
  router.push('/admin/farmer')
}

// 保存按钮入参与后端 UpdateFarmerReqVO 字段严格对齐
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  const reqVO = {
    id: form.id,
    name: form.name.trim(),
    phone: form.phone.trim(),
    idCard: form.idCard?.trim() || '',
    avatar: form.avatar?.trim() || '',
    farmName: form.farmName?.trim() || '',
    province: form.province?.trim() || '',
    city: form.city?.trim() || '',
    region: form.region?.trim() || '',
    detailAddress: form.detailAddress?.trim() || '',
    mainProduct: form.mainProduct?.trim() || '',
    description: form.description?.trim() || '',
    status: form.status,
    originIds: form.originIds || []
  }
  const rsp = await updateFarmer(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '保存失败')
    return
  }
  message.success('保存成功')
  goBack()
}
</script>
