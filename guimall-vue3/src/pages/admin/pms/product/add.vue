<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">添加商品</span>
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

        <a-form-item label="商品副标题">
          <a-input v-model:value="form.subTitle" placeholder="请输入商品副标题" />
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
              <plus-outlined />
              <div class="mt-2">上传图片</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="商品相册">
          <a-upload
            list-type="picture-card"
            :file-list="albumFileList"
            :custom-request="handleAlbumUpload"
            @remove="handleAlbumRemove"
            accept="image/*"
            multiple
          >
            <div>
              <plus-outlined />
              <div class="mt-2">上传图片</div>
            </div>
          </a-upload>
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

        <a-form-item label="单位">
          <a-input v-model:value="form.unit" placeholder="如：斤、箱" />
        </a-form-item>

        <a-form-item label="上架状态">
          <a-switch v-model:checked="publishChecked" />
        </a-form-item>

        <a-form-item label="商品描述">
          <RichEditor v-model="form.detailHtml" />
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 商品参数 -->
    <a-card :bordered="false" title="商品参数" class="mt-5">
      <a-alert
        class="mb-4"
        type="info"
        show-icon
        banner
        message="从参数字典中选择参数。如需添加新参数，请前往【参数管理】页面。"
      />

      <a-spin :spinning="paramLoading">
        <a-table
          :dataSource="paramRows"
          :columns="paramColumns"
          rowKey="paramId"
          :pagination="paramPagination"
          @change="handleParamTableChange"
          bordered
          size="small"
        />
      </a-spin>
    </a-card>

    <!-- SKU库存 -->
    <a-card title="SKU库存管理（必填）" class="mt-5">
      <a-alert
        class="mb-4"
        type="warning"
        show-icon
        banner
        message="至少添加一个SKU规格，否则用户无法下单购买"
      />

      <div class="mb-4">
        <a-button type="primary" @click="handleAddSku">新增SKU</a-button>
      </div>

      <a-table
        :dataSource="skuRows"
        :columns="skuColumns"
        rowKey="tempKey"
        :pagination="false"
        bordered
      />

    </a-card>

    <!-- 底部固定操作栏 -->
    <div class="fixed-bottom-bar">
      <a-button type="primary" @click="handleSubmit">提交</a-button>
      <a-button @click="goBack">取消</a-button>
    </div>

  </div>
</template>

<script setup>
import { reactive, ref, h, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, Input, InputNumber, Button, Popconfirm, Spin, Checkbox } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { createProduct } from '@/api/admin/product'
import { fetchProductCategoryOptions } from '@/api/admin/productCategory'
import { fetchFarmerOptions } from '@/api/admin/farmer'
import { uploadFile } from '@/api/admin/upload'
import { fetchParamDefinitions, createParamDefinition } from '@/api/admin/paramDefinition'
import RichEditor from '@/components/RichEditor.vue'

const router = useRouter()
const formRef = ref(null)
const categoryOptions = ref([])
const farmerOptions = ref([])
const publishChecked = ref(false)
const picFileList = ref([])
const albumFileList = ref([])

// SKU 管理
const skuRows = ref([])

// 动态参数行（模板驱动）
const paramRows = ref([])
const paramLoading = ref(false)
const paramPagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: total => `共 ${total} 条`
})
// 全局维护已选中的参数ID集合
const selectedParamIds = ref(new Set())

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

const syncAlbumFields = () => {
  const list = albumFileList.value
    .map(item => item.url || item.response?.data || '')
    .map(item => String(item).trim())
    .filter(Boolean)
  form.albumPicList = list
  form.albumPics = list.join(',')
}

const handleAlbumUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      albumFileList.value = [
        ...albumFileList.value,
        {
          uid: `${Date.now()}-${Math.random()}`,
          name: file.name,
          status: 'done',
          url: res.data
        }
      ]
      syncAlbumFields()
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

const handleAlbumRemove = (file) => {
  albumFileList.value = albumFileList.value.filter(item => item.uid !== file.uid)
  syncAlbumFields()
}

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  productCategoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  farmerId: [{ required: true, message: '请选择关联农户', trigger: 'change' }],
  productSn: [{ required: true, message: '请输入商品货号', trigger: 'blur' }],
  price: [{ required: true, message: '请输入销售价格', trigger: 'change' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'change' }]
}

const form = reactive({
  name: '',
  productCategoryId: undefined,
  farmerId: undefined,
  productSn: '',
  subTitle: '',
  pic: '',
  albumPics: '',
  albumPicList: [],
  price: undefined,
  originalPrice: undefined,
  stock: 0,
  unit: '斤',
  publishStatus: 0,
  detailHtml: ''
})

const goBack = () => {
  router.push('/admin/pms/product')
}

// 商品参数表格列（从字典选择）
const paramColumns = [
  {
    title: '选择',
    width: '10%',
    align: 'center',
    customRender: ({ record }) =>
      h(Checkbox, {
        checked: record.selected,
        onChange: e => {
          record.selected = e.target.checked
          // 同步更新全局选中集合
          if (e.target.checked) {
            selectedParamIds.value.add(record.paramId)
          } else {
            selectedParamIds.value.delete(record.paramId)
          }
          console.log('Checkbox changed:', record.key, 'selected:', record.selected)
        }
      })
  },
  {
    title: '参数名',
    dataIndex: 'key',
    width: '30%'
  },
  {
    title: '参数值',
    dataIndex: 'value',
    width: '60%'
  }
]

// 加载全局参数字典
const loadAllParams = async () => {
  paramLoading.value = true
  try {
    const rsp = await fetchParamDefinitions({
      current: paramPagination.value.current,
      size: paramPagination.value.pageSize
    })

    if (!rsp?.success) return

    // PageResponse 的数据直接在 data 字段，不是 data.records
    const defs = rsp?.data || []
    paramPagination.value.total = rsp?.total || 0

    paramRows.value = defs.map(d => ({
      paramId: d.id,
      key: d.paramName,
      value: d.paramValue,
      selected: selectedParamIds.value.has(d.id) // 从全局集合判断是否选中
    }))
  } finally {
    paramLoading.value = false
  }
}

// 分页切换
const handleParamTableChange = (pagination) => {
  paramPagination.value.current = pagination.current
  paramPagination.value.pageSize = pagination.pageSize
  loadAllParams()
}

// 构建 productParams 数组（只提交已勾选的参数ID）
const buildProductParams = () => {
  const result = Array.from(selectedParamIds.value).map(paramId => ({ paramId }))
  console.log('buildProductParams - selectedParamIds:', selectedParamIds.value)
  console.log('buildProductParams - result:', result)
  return result
}

// SKU 表格列
const skuColumns = [
  {
    title: '规格名称',
    width: '15%',
    customRender: ({ record }) =>
      h(Input, {
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
        value: record.skuCode,
        onChange: e => (record.skuCode = e.target.value),
        placeholder: '必填'
      })
  },
  {
    title: '价格',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
        value: record.price,
        onChange: v => (record.price = v),
        min: 0,
        precision: 2,
        placeholder: '必填'
      })
  },
  {
    title: '库存',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
        value: record.stock,
        onChange: v => (record.stock = v),
        min: 0,
        placeholder: '必填'
      })
  },
  {
    title: '促销价',
    width: '12%',
    customRender: ({ record }) =>
      h(InputNumber, {
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
          title: '确认删除?',
          onConfirm: () => handleDeleteSku(record)
        },
        {
          default: () =>
            h(Button, { danger: true, size: 'small' }, () => '删除')
        }
      )
  }
]

const handleAddSku = () => {
  skuRows.value.push({
    tempKey: Date.now(),
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

const handleDeleteSku = (record) => {
  skuRows.value = skuRows.value.filter(r => r.tempKey !== record.tempKey)
}

onMounted(async () => {
  const [categoryRsp, farmerRsp] = await Promise.all([
    fetchProductCategoryOptions(),
    fetchFarmerOptions()
  ])
  categoryOptions.value = categoryRsp?.data || []
  farmerOptions.value = farmerRsp?.data || []

  // 加载全局参数字典
  await loadAllParams()
})

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  // 验证 SKU
  if (skuRows.value.length === 0) {
    message.error('请至少添加一个SKU规格')
    return
  }

  // 验证每个 SKU 的必填字段
  for (let i = 0; i < skuRows.value.length; i++) {
    const sku = skuRows.value[i]
    if (!sku.skuCode || !sku.skuCode.trim()) {
      message.error(`第 ${i + 1} 个SKU的编码不能为空`)
      return
    }
    if (!sku.price || sku.price <= 0) {
      message.error(`第 ${i + 1} 个SKU的价格必须大于0`)
      return
    }
    if (sku.stock === undefined || sku.stock === null || sku.stock < 0) {
      message.error(`第 ${i + 1} 个SKU的库存不能为空`)
      return
    }
  }

  const productData = {
    productCategoryId: form.productCategoryId,
    farmerId: form.farmerId,
    name: form.name.trim(),
    subTitle: form.subTitle?.trim() || null,
    productSn: form.productSn.trim(),
    pic: form.pic?.trim() || null,
    albumPicList: form.albumPicList || [],
    albumPics: form.albumPics || null,
    detailHtml: form.detailHtml || null,
    price: form.price,
    marketPrice: form.originalPrice ?? null,
    stock: form.stock,
    unit: form.unit?.trim() || null,
    productParams: buildProductParams(),
    skuStockList: skuRows.value.map(sku => ({
      skuCode: sku.skuCode.trim(),
      price: sku.price,
      stock: sku.stock,
      promotionPrice: sku.promotionPrice || null,
      lowStock: sku.lowStock || 0,
      pic: sku.pic || '',
      specs: [
        { specKey: sku.specKey || '', specValue: sku.specValue || '' }
      ]
    }))
  }

  await createProduct(productData)

  if (publishChecked.value) {
    message.info('商品已创建。请在商品列表点击"上架"开关完成上架。')
  } else {
    message.success('创建成功')
  }

  // 清空表单
  Object.assign(form, {
    name: '',
    productCategoryId: undefined,
    farmerId: undefined,
    productSn: '',
    subTitle: '',
    pic: '',
    albumPics: '',
    albumPicList: [],
    price: undefined,
    originalPrice: undefined,
    stock: 0,
    unit: '斤',
    publishStatus: 0,
    detailHtml: ''
  })

  // 清空其他状态
  publishChecked.value = false
  picFileList.value = []
  albumFileList.value = []
  skuRows.value = []
  selectedParamIds.value.clear()

  // 重新加载参数列表以重置选中状态
  await loadAllParams()

  goBack()
}
</script>

<style scoped>
.fixed-bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
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

.box {
  padding-bottom: 72px;
}

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
