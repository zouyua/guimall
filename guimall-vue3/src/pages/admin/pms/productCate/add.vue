<template>
  <div class="p-2 box">
    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack()">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">添加商品分类</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="分类信息">
      <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item name="name" label="分类名称" :required="true">
          <a-input v-model:value="form.name" placeholder="请输入分类名称" />
        </a-form-item>

        <a-form-item label="上级分类">
          <a-select v-model:value="form.parentId" class="w-full" placeholder="请选择">
            <a-select-option :value="0">一级分类</a-select-option>
            <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="商品单位">
          <a-input v-model:value="form.productUnit" placeholder="例如：斤 / 个 / 箱" />
        </a-form-item>

        <a-form-item label="排序">
          <a-input-number v-model:value="form.sort" :min="0" class="w-full" />
        </a-form-item>

        <a-form-item label="是否启用">
          <a-switch
            v-model:checked="form.status"
            :checked-value="1"
            :un-checked-value="0"
          />
        </a-form-item>

        <a-form-item label="关键词">
          <a-input v-model:value="form.keywords" placeholder="用于搜索优化" />
        </a-form-item>

        <a-form-item label="分类图标">
          <a-upload
            :max-count="1"
            list-type="picture-card"
            :file-list="iconFileList"
            :custom-request="handleIconUpload"
            @remove="handleIconRemove"
            accept="image/*"
          >
            <div v-if="iconFileList.length === 0">
              <PlusOutlined />
              <div class="mt-2">上传图片</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="分类描述">
          <a-textarea v-model:value="form.description" :rows="3" placeholder="请输入分类描述" />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">提交</a-button>
        <a-button @click="goBack()">取消</a-button>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { createProductCategory, fetchProductCategoryOptions } from '@/api/admin/productCategory'
import { uploadFile } from '@/api/admin/upload'

const router = useRouter()
const formRef = ref(null)

const categoryList = ref([])
const iconFileList = ref([])

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const form = reactive({
  parentId: 0,
  name: '',
  productUnit: '',
  status: 1,
  sort: 0,
  icon: '',
  keywords: '',
  description: ''
})

const resetForm = () => {
  Object.assign(form, {
    parentId: 0,
    name: '',
    productUnit: '',
    status: 1,
    sort: 0,
    icon: '',
    keywords: '',
    description: ''
  })
  iconFileList.value = []
}

const handleIconUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      form.icon = res.data
      iconFileList.value = [{ uid: '-1', name: file.name, status: 'done', url: res.data }]
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

const handleIconRemove = () => {
  form.icon = ''
  iconFileList.value = []
}

const goBack = (refresh = false) => {
  if (refresh) {
    router.push({ path: '/admin/pms/productCate', query: { refresh: Date.now().toString() } })
    return
  }
  router.push('/admin/pms/productCate')
}

const fetchOptions = async () => {
  const rsp = await fetchProductCategoryOptions()
  categoryList.value = rsp?.data || []
}

onMounted(async () => {
  await fetchOptions()
})

onActivated(() => {
  resetForm()
})

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  await createProductCategory({
    parentId: form.parentId,
    name: form.name.trim(),
    level: form.parentId && form.parentId !== 0 ? 1 : 0,
    productUnit: form.productUnit?.trim() || null,
    status: form.status,
    sort: form.sort,
    icon: form.icon?.trim() || null,
    keywords: form.keywords?.trim() || null,
    description: form.description?.trim() || null
  })

  message.success('新增成功')
  goBack(true)
}
</script>

<style scoped>
:deep(.ant-input),
:deep(.ant-input-number),
:deep(.ant-select-selector),
:deep(.ant-input-affix-wrapper) {
  border: 1px solid #d9d9d9 !important;
  border-radius: 6px !important;
}

:deep(.ant-input:hover),
:deep(.ant-select-selector:hover),
:deep(.ant-input-number:hover) {
  border-color: #bfbfbf !important;
}

:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-select-focused .ant-select-selector),
:deep(.ant-input-number-focused) {
  border-color: #4096ff !important;
  box-shadow: 0 0 0 2px rgba(64, 150, 255, 0.15);
}

:deep(.ant-select-selector) {
  height: 32px !important;
  display: flex;
  align-items: center;
}

:deep(.ant-input-number) {
  width: 100%;
}

:deep(textarea.ant-input) {
  border-radius: 6px !important;
}
</style>
