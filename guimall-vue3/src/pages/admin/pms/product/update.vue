<template>
  <div class="p-2 box">
    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">修改商品信息</span>
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
        <a-form-item name="name" label="商品名称">
          <a-input v-model:value="form.name" class="gm-field" />
        </a-form-item>

        <a-form-item name="productCategoryId" label="商品分类">
          <a-select v-model:value="form.productCategoryId" allow-clear class="gm-field">
            <a-select-option v-for="item in categoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="属性分类">
          <a-select v-model:value="form.productAttributeCategoryId" allow-clear class="gm-field">
            <a-select-option v-for="item in attrCategoryOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item name="farmerId" label="关联农户">
          <a-select v-model:value="form.farmerId" allow-clear class="gm-field">
            <a-select-option v-for="item in farmerOptions" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item name="productSn" label="商品货号">
          <a-input v-model:value="form.productSn" class="gm-field" />
        </a-form-item>

        <a-form-item label="商品主图">
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

        <a-form-item name="price" label="销售价格">
          <a-input-number v-model:value="form.price" :min="0" :precision="2" class="gm-field" />
        </a-form-item>

        <a-form-item label="市场价">
          <a-input-number v-model:value="form.originalPrice" :min="0" class="gm-field" />
        </a-form-item>

        <a-form-item name="stock" label="库存">
          <a-input-number v-model:value="form.stock" :min="0" class="gm-field" />
        </a-form-item>

        <a-form-item label="销量">
          <a-input-number v-model:value="form.sale" disabled class="gm-field" />
        </a-form-item>

        <a-form-item label="单位">
          <a-input v-model:value="form.unit" class="gm-field" />
        </a-form-item>

        <a-form-item label="上架">
          <a-switch v-model:checked="publishChecked" />
        </a-form-item>

        <a-form-item label="商品描述">
          <a-textarea v-model:value="form.description" :rows="4" class="gm-field" />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

    <a-card title="SKU库存管理" class="mt-5">
      <a-alert
        class="mb-4"
        type="warning"
        show-icon
        banner
        message="SKU库存需要单独保存，不会随着上面的【保存基本信息】按钮一起保存"
      />

      <div class="mb-4">
        <a-button @click="handleAddSku">新增SKU</a-button>
        <a-button type="primary" :loading="skuSaveLoading" @click="handleSaveSku" style="margin-left:10px">
          保存SKU库存
        </a-button>
      </div>

      <a-table
        :dataSource="skuRows"
        :columns="skuColumns"
        rowKey="tempKey"
        :pagination="false"
        bordered
      />
    </a-card>

    <div class="fixed-bottom-bar">
      <a-button type="primary" @click="handleSubmit">保存基本信息</a-button>
      <a-button @click="goBack">取消</a-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch, h } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message, Input, InputNumber, Button, Popconfirm } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'

import {
  getProductDetail,
  updateProduct,
  publishProduct,
  unpublishProduct
} from '@/api/admin/product'

import { fetchProductCategoryOptions } from '@/api/admin/productCategory'
import { fetchFarmerOptions } from '@/api/admin/farmer'
import { fetchProductAttrCategoryOptions } from '@/api/admin/productAttrCategory'
import { uploadFile } from '@/api/admin/upload'

import {
  fetchSkuListByProductId,
  saveSkuList,
  deleteSku
} from '@/api/admin/productSku'

const router = useRouter()
const route = useRoute()

const formRef = ref()

const categoryOptions = ref([])
const farmerOptions = ref([])
const attrCategoryOptions = ref([])

const publishChecked = ref(false)
const picFileList = ref([])

const skuRows = ref([])
const skuSaveLoading = ref(false)

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

const handlePicUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      form.pic = res.data
      picFileList.value = [{ uid: '-1', name: file.name, status: 'done', url: res.data }]
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

const handlePicRemove = () => {
  form.pic = ''
  picFileList.value = []
}

const normalizeUnit = (v) => {
  const unit = String(v ?? '').trim()
  if (!unit || unit === 'null' || unit === 'undefined') return '斤'
  if (/[?\uFF1F\uFFFD]/.test(unit)) return '斤'
  return unit
}

const rules = {
  name: [{ required: true, message: '请输入商品名称' }],
  productCategoryId: [{ required: true, message: '请选择分类' }],
  farmerId: [{ required: true, message: '请选择农户' }],
  productSn: [{ required: true, message: '请输入商品货号' }],
  price: [{ required: true, message: '请输入价格' }],
  stock: [{ required: true, message: '请输入库存' }]
}

const goBack = () => {
  router.push('/admin/pms/product')
}

onMounted(() => {
  init()
})

watch(() => route.query.id, (newId, oldId) => {
  if (newId && newId !== oldId) {
    Object.assign(form, {
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
    picFileList.value = []
    skuRows.value = []
    init()
  }
})

const init = async () => {
  const id = Number(route.query.id)
  if (!id) return

  const [categoryRsp, farmerRsp, attrRsp, detailRsp] = await Promise.all([
    fetchProductCategoryOptions(),
    fetchFarmerOptions(),
    fetchProductAttrCategoryOptions(),
    getProductDetail(id)
  ])

  categoryOptions.value = categoryRsp?.data || []
  farmerOptions.value = farmerRsp?.data || []
  attrCategoryOptions.value = attrRsp?.data || []

  Object.assign(form, detailRsp.data)
  form.unit = normalizeUnit(form.unit)
  publishChecked.value = form.publishStatus === 1
  picFileList.value = form.pic
    ? [{ uid: '-1', name: '商品主图', status: 'done', url: form.pic }]
    : []

  await fetchSkuList()
}

const fetchSkuList = async () => {
  const rsp = await fetchSkuListByProductId(form.id)
  if (!rsp?.success) {
    message.error('获取SKU失败')
    return
  }

  skuRows.value = (rsp.data || []).map((item, i) => {
    let specKey = ''
    let specValue = ''
    if (Array.isArray(item.specs) && item.specs.length > 0) {
      specKey = item.specs[0].specKey || ''
      specValue = item.specs[0].specValue || ''
    }

    return {
      ...item,
      specKey,
      specValue,
      tempKey: item.id || `${Date.now()}-${i}`,
      price: item.price ? Number(item.price) : undefined,
      stock: item.stock ? Number(item.stock) : 0,
      promotionPrice: item.promotionPrice ? Number(item.promotionPrice) : undefined,
      lowStock: item.lowStock || 0
    }
  })
}

const skuColumns = [
  {
    title: '规格名称',
    width: '15%',
    customRender: ({ record }) =>
      h(Input, {
        class: 'gm-field',
        value: record.specKey,
        onChange: e => (record.specKey = e.target.value),
        placeholder: '如：重量'
      })
  },
  {
    title: '规格值',
    width: '15%',
    customRender: ({ record }) =>
      h(Input, {
        class: 'gm-field',
        value: record.specValue,
        onChange: e => (record.specValue = e.target.value),
        placeholder: '如：3斤'
      })
  },
  {
    title: 'SKU编码',
    width: '15%',
    customRender: ({ record }) =>
      h(Input, {
        class: 'gm-field',
        value: record.skuCode,
        onChange: e => (record.skuCode = e.target.value)
      })
  },
  {
    title: '价格',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
        class: 'gm-field',
        value: record.price,
        onChange: v => (record.price = v),
        min: 0,
        precision: 2
      })
  },
  {
    title: '库存',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
        class: 'gm-field',
        value: record.stock,
        onChange: v => (record.stock = v),
        min: 0
      })
  },
  {
    title: '促销价',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
        class: 'gm-field',
        value: record.promotionPrice,
        onChange: v => (record.promotionPrice = v),
        min: 0,
        precision: 2,
        placeholder: '选填'
      })
  },
  {
    title: '预警库存',
    width: '10%',
    customRender: ({ record }) =>
      h(InputNumber, {
        class: 'gm-field',
        value: record.lowStock,
        onChange: v => (record.lowStock = v),
        min: 0,
        placeholder: '选填'
      })
  },
  {
    title: '操作',
    width: '9%',
    align: 'center',
    customRender: ({ record }) =>
      h(
        Popconfirm,
        {
          title: '确认删除？',
          onConfirm: () => handleDeleteSku(record)
        },
        {
          default: () => h(Button, { danger: true, size: 'small' }, () => '删除')
        }
      )
  }
]

const handleAddSku = () => {
  skuRows.value.push({
    tempKey: Date.now(),
    productId: form.id,
    skuCode: '',
    specKey: '',
    specValue: '',
    price: undefined,
    stock: 0,
    promotionPrice: undefined,
    lowStock: 0,
    pic: ''
  })
}

const handleDeleteSku = async (record) => {
  if (!record.id) {
    skuRows.value = skuRows.value.filter(r => r.tempKey !== record.tempKey)
    return
  }

  await deleteSku(record.id)
  message.success('删除成功')
  fetchSkuList()
}

const handleSaveSku = async () => {
  skuSaveLoading.value = true
  try {
    const payload = skuRows.value.map(row => ({
      id: row.id,
      productId: form.id,
      skuCode: row.skuCode,
      price: row.price,
      stock: row.stock,
      promotionPrice: row.promotionPrice || null,
      lowStock: row.lowStock || 0,
      pic: row.pic || '',
      specs: [{ specKey: row.specKey || '', specValue: row.specValue || '' }]
    }))

    const rsp = await saveSkuList(form.id, payload)
    if (!rsp?.success) {
      message.error('保存失败')
      return
    }

    message.success('SKU保存成功')
    fetchSkuList()
  } finally {
    skuSaveLoading.value = false
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()

  await updateProduct({
    ...form,
    unit: normalizeUnit(form.unit),
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
.fixed-bottom-bar {
  position: fixed;
  bottom: 0;
  left: 200px;
  right: 0;
  z-index: 99;
  display: flex;
  justify-content: center;
  gap: 12px;
  padding: 12px 24px;
  background: #fff;
  border-top: 1px solid #f0f0f0;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.06);
}

@media (max-width: 768px) {
  .fixed-bottom-bar {
    left: 0;
  }
}

.box {
  padding-bottom: 72px;
}

:deep(.gm-field .ant-input),
:deep(.gm-field.ant-input),
:deep(.gm-field .ant-input-number),
:deep(.gm-field.ant-input-number),
:deep(.gm-field .ant-input-number-input),
:deep(.gm-field .ant-select-selector),
:deep(.gm-field .ant-select-selection-search-input),
:deep(.gm-field.ant-select .ant-select-selector),
:deep(.gm-field .ant-input-affix-wrapper),
:deep(.gm-field.ant-input-affix-wrapper),
:deep(.gm-field textarea.ant-input) {
  background: #f5f6f8;
  border-radius: 10px;
  color: rgba(0, 0, 0, 0.88);
}

:deep(.gm-field .ant-input),
:deep(.gm-field.ant-input),
:deep(.gm-field .ant-input-number),
:deep(.gm-field.ant-input-number),
:deep(.gm-field .ant-input-affix-wrapper),
:deep(.gm-field.ant-input-affix-wrapper),
:deep(.gm-field .ant-select-selector),
:deep(.gm-field.ant-select .ant-select-selector),
:deep(.gm-field textarea.ant-input) {
  border-color: #cfd3dc;
}

:deep(.gm-field .ant-input:hover),
:deep(.gm-field.ant-input:hover),
:deep(.gm-field .ant-input-number:hover),
:deep(.gm-field.ant-input-number:hover),
:deep(.gm-field .ant-input-affix-wrapper:hover),
:deep(.gm-field.ant-input-affix-wrapper:hover),
:deep(.gm-field.ant-select:hover .ant-select-selector),
:deep(.gm-field textarea.ant-input:hover) {
  border-color: #b8bcc7;
}

:deep(.gm-field .ant-input:focus),
:deep(.gm-field .ant-input-focused),
:deep(.gm-field .ant-input-number-focused),
:deep(.gm-field.ant-select-focused .ant-select-selector),
:deep(.gm-field textarea.ant-input:focus) {
  border-color: #aeb4c2;
  box-shadow: 0 0 0 2px rgba(207, 211, 220, 0.35);
}

:deep(.gm-field .ant-select-selection-item),
:deep(.gm-field .ant-select-selection-placeholder),
:deep(.gm-field .ant-select-selection-search-input),
:deep(.gm-field .ant-input-number-input),
:deep(.gm-field .ant-input::placeholder),
:deep(.gm-field textarea.ant-input::placeholder) {
  color: rgba(0, 0, 0, 0.45);
}

:deep(.gm-field .ant-select-selection-item) {
  color: rgba(0, 0, 0, 0.88);
}

:deep(.gm-field .ant-input[disabled]),
:deep(.gm-field .ant-input-number-disabled),
:deep(.gm-field textarea.ant-input[disabled]) {
  background: #f1f2f5;
  color: rgba(0, 0, 0, 0.45);
}
</style>
