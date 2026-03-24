<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">更改商品信息</span>
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
        <a-form-item name="name" label="商品名称" :required="true">
          <a-input v-model:value="form.name" placeholder="请输入商品名称" />
        </a-form-item>

        <a-form-item name="productCategoryId" label="商品分类" :required="true">
          <a-select v-model:value="form.productCategoryId" placeholder="请选择分类" class="w-full" allow-clear>
            <a-select-option v-for="item in categoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="属性分类">
          <a-select v-model:value="form.productAttributeCategoryId" placeholder="可选" class="w-full" allow-clear>
            <a-select-option v-for="item in attrCategoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item name="farmerId" label="关联农户" :required="true">
          <a-select v-model:value="form.farmerId" placeholder="请选择农户" class="w-full" allow-clear>
            <a-select-option v-for="item in farmerOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item name="productSn" label="商品货号" :required="true">
          <a-input v-model:value="form.productSn" placeholder="请输入商品货号" />
        </a-form-item>

        <a-form-item label="主图地址">
          <a-input v-model:value="form.pic" placeholder="图片 URL" />
        </a-form-item>

        <a-form-item name="price" label="销售价格" :required="true">
          <a-input-number v-model:value="form.price" :min="0" :precision="2" class="w-full" />
        </a-form-item>

        <a-form-item label="市场价">
          <a-input-number v-model:value="form.originalPrice" :min="0" :precision="2" class="w-full" />
        </a-form-item>

        <a-form-item name="stock" label="库存" :required="true">
          <a-input-number v-model:value="form.stock" :min="0" class="w-full" />
        </a-form-item>

        <a-form-item label="销量">
          <a-input-number v-model:value="form.sale" :min="0" class="w-full" disabled />
        </a-form-item>

        <a-form-item label="单位">
          <a-input v-model:value="form.unit" placeholder="如：斤、箱" />
        </a-form-item>

        <a-form-item label="上架">
          <a-switch v-model:checked="publishChecked" />
        </a-form-item>

        <a-form-item label="商品描述">
          <a-textarea v-model:value="form.description" :rows="4" placeholder="请输入商品描述" />
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
import { reactive, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getProductDetail, updateProduct, publishProduct, unpublishProduct } from '@/api/admin/product'
import { fetchProductCategoryOptions } from '@/api/admin/productCategory'
import { fetchFarmerOptions } from '@/api/admin/farmer'
import { fetchProductAttrCategoryOptions } from '@/api/admin/productAttrCategory'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const publishChecked = ref(false)
const categoryOptions = ref([])
const farmerOptions = ref([])
const attrCategoryOptions = ref([])

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  productCategoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  farmerId: [{ required: true, message: '请选择关联农户', trigger: 'change' }],
  productSn: [{ required: true, message: '请输入商品货号', trigger: 'blur' }],
  price: [{ required: true, message: '请输入销售价格', trigger: 'change' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'change' }]
}

const form = reactive({
  id: null,
  name: '',
  productCategoryId: undefined,
  productAttributeCategoryId: undefined,
  farmerId: undefined,
  productSn: '',
  pic: '',
  price: undefined,
  originalPrice: undefined,
  stock: 0,
  sale: 0,
  unit: '斤',
  publishStatus: 0,
  description: ''
})

onMounted(() => {
  fetchInit()
})

const goBack = () => {
  router.push('/admin/pms/product')
}

const fetchInit = async () => {
  const id = Number(route.query.id)
  if (!id || Number.isNaN(id)) return

  const [categoryRsp, farmerRsp, attrRsp, detailRsp] = await Promise.all([
    fetchProductCategoryOptions(),
    fetchFarmerOptions(),
    fetchProductAttrCategoryOptions(),
    getProductDetail(id)
  ])

  categoryOptions.value = categoryRsp?.data || []
  farmerOptions.value = farmerRsp?.data || []
  attrCategoryOptions.value = attrRsp?.data || []

  if (!detailRsp?.success || !detailRsp?.data) {
    message.error(detailRsp?.message || '获取商品详情失败')
    return
  }

  Object.assign(form, {
    id: detailRsp.data.id,
    name: detailRsp.data.name ?? '',
    productCategoryId: detailRsp.data.productCategoryId,
    productAttributeCategoryId: detailRsp.data.productAttributeCategoryId,
    farmerId: detailRsp.data.farmerId,
    productSn: detailRsp.data.productSn ?? '',
    pic: detailRsp.data.pic ?? '',
    price: detailRsp.data.price,
    originalPrice: detailRsp.data.originalPrice,
    stock: detailRsp.data.stock ?? 0,
    sale: detailRsp.data.sale ?? 0,
    unit: detailRsp.data.unit ?? '斤',
    publishStatus: detailRsp.data.publishStatus ?? 0,
    description: detailRsp.data.description ?? ''
  })
  publishChecked.value = Number(form.publishStatus) === 1
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  await updateProduct({
    id: form.id,
    productCategoryId: form.productCategoryId,
    productAttributeCategoryId: form.productAttributeCategoryId,
    farmerId: form.farmerId,
    name: form.name.trim(),
    productSn: form.productSn.trim(),
    pic: form.pic?.trim() || null,
    description: form.description?.trim() || null,
    price: form.price,
    originalPrice: form.originalPrice,
    stock: form.stock,
    unit: form.unit?.trim() || null,
    publishStatus: publishChecked.value ? 1 : 0
  })

  if (publishChecked.value) {
    await publishProduct(form.id)
  } else {
    await unpublishProduct(form.id)
  }
  message.success('保存成功')
  goBack()
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
