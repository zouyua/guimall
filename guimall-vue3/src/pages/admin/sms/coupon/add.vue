<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">新增优惠券</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form ref="formRef" :model="form" :rules="rules" layout="horizontal" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
        <a-form-item label="优惠券名称" name="name" required>
          <a-input v-model:value="form.name" placeholder="请输入优惠券名称" allow-clear />
        </a-form-item>
        <a-form-item label="优惠类型" name="type" required>
          <a-select v-model:value="form.type" class="w-full max-w-xs">
            <a-select-option :value="0">全场赠券</a-select-option>
            <a-select-option :value="1">会员赠券</a-select-option>
            <a-select-option :value="2">购物赠券</a-select-option>
            <a-select-option :value="3">注册赠券</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="使用平台" name="platform" required>
          <a-select v-model:value="form.platform" class="w-full max-w-xs">
            <a-select-option :value="0">全部</a-select-option>
            <a-select-option :value="1">移动端</a-select-option>
            <a-select-option :value="2">WEB</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="发行总量" name="count" required>
          <a-input-number v-model:value="form.count" :min="1" :precision="0" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="优惠金额" name="amount" required>
          <a-input-number v-model:value="form.amount" :min="0" :precision="2" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="最低消费金额" name="minAmount" required>
          <a-input-number v-model:value="form.minAmount" :min="0" :precision="2" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="每人限领数量" name="perLimit" required>
          <a-input-number v-model:value="form.perLimit" :min="1" :precision="0" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="使用范围" name="useType" required>
          <a-select v-model:value="form.useType" class="w-full max-w-xs" @change="handleUseTypeChange">
            <a-select-option :value="0">全场</a-select-option>
            <a-select-option :value="1">指定分类</a-select-option>
            <a-select-option :value="2">指定商品</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 指定分类 -->
        <a-form-item v-if="form.useType === 1" label="选择分类">
          <a-select
            v-model:value="selectedCategoryIds"
            mode="multiple"
            placeholder="请选择分类"
            class="w-full"
            :options="categoryOptions"
            :field-names="{ label: 'name', value: 'id' }"
          />
        </a-form-item>

        <!-- 指定商品 -->
        <a-form-item v-if="form.useType === 2" label="选择商品">
          <a-button @click="showProductModal = true">选择商品</a-button>
          <div v-if="selectedProducts.length > 0" class="mt-2">
            <a-tag
              v-for="product in selectedProducts"
              :key="product.id"
              closable
              @close="removeProduct(product.id)"
              class="mb-2"
            >
              {{ product.name }}
            </a-tag>
          </div>
        </a-form-item>
        <a-form-item label="有效期" name="startTime" required>
          <a-range-picker
            :value="[form.startTime, form.endTime]"
            @change="handleTimeRangeChange"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            class="w-full"
            :placeholder="['开始时间', '结束时间']"
          />
          <div class="text-xs text-gray-400 mt-1">即优惠券的使用有效时间范围</div>
        </a-form-item>
        <a-form-item label="领取开始时间">
          <a-date-picker
            v-model:value="form.enableTime"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            class="w-full max-w-xs"
            placeholder="不设置则与使用开始时间相同"
          />
          <div class="text-xs text-gray-400 mt-1">可提前于使用开始时间领取，不设置则默认与使用开始时间相同</div>
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

    <!-- 商品选择弹窗 -->
    <a-modal
      v-model:open="showProductModal"
      title="选择商品"
      width="800px"
      @ok="handleProductModalOk"
      @cancel="showProductModal = false"
    >
      <a-table
        :dataSource="productList"
        :columns="productColumns"
        :loading="productLoading"
        :pagination="productPagination"
        :row-selection="{ selectedRowKeys: selectedProductIds, onChange: onProductSelectChange }"
        @change="handleProductTableChange"
        rowKey="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'pic'">
            <img :src="record.pic" class="w-12 h-12 object-cover rounded" />
          </template>
          <template v-if="column.key === 'price'">
            ¥{{ record.price }}
          </template>
        </template>
      </a-table>
    </a-modal>

  </div>
</template>

<script setup>
// 新增优惠券页
// 职责：表单校验 + 调用创建接口
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { createCoupon } from '@/api/admin/coupon'
import { fetchProductCategoryOptions } from '@/api/admin/productCategory'
import { fetchProductList } from '@/api/admin/product'

const router = useRouter()

// 表单引用（用于 validate）
const formRef = ref()
// 表单数据（字段与 CreateSmsCouponReqVO 对齐）
const form = reactive({
  name: '',
  type: 0,
  platform: 0,
  count: 1,
  amount: undefined,
  perLimit: 1,
  minAmount: undefined,
  useType: 0,
  startTime: undefined,
  endTime: undefined,
  enableTime: undefined,
  note: ''
})

// 分类和商品选择
const categoryOptions = ref([])
const selectedCategoryIds = ref([])
const selectedProducts = ref([])
const selectedProductIds = ref([])
const showProductModal = ref(false)
const productList = ref([])
const productLoading = ref(false)
const productPagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const productColumns = [
  { title: '商品图片', key: 'pic', width: 100 },
  { title: '商品名称', dataIndex: 'name' },
  { title: '价格', key: 'price', width: 120 },
  { title: '库存', dataIndex: 'stock', width: 100 }
]

// 表单规则（必填项提示）
const rules = {
  name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择优惠类型', trigger: 'change' }],
  platform: [{ required: true, message: '请选择使用平台', trigger: 'change' }],
  count: [{ required: true, message: '请输入发行总量', trigger: 'change' }],
  amount: [{ required: true, message: '请输入优惠金额', trigger: 'change' }],
  perLimit: [{ required: true, message: '请输入每人限领数量', trigger: 'change' }],
  minAmount: [{ required: true, message: '请输入最低消费金额', trigger: 'change' }],
  useType: [{ required: true, message: '请选择使用范围', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择有效期', trigger: 'change' }],
}

const goBack = () => {
  // 返回优惠券列表页
  router.push('/admin/sms/coupon')
}

// 有效期范围选择
const handleTimeRangeChange = (dates) => {
  if (dates && dates.length === 2) {
    form.startTime = dates[0]
    form.endTime = dates[1]
  } else {
    form.startTime = undefined
    form.endTime = undefined
  }
}

// 加载分类选项
const loadCategoryOptions = async () => {
  const res = await fetchProductCategoryOptions()
  if (res.success) {
    categoryOptions.value = res.data || []
  }
}

// 加载商品列表
const loadProductList = async () => {
  productLoading.value = true
  try {
    const res = await fetchProductList({
      current: productPagination.current,
      size: productPagination.pageSize
    })
    if (res.success) {
      productList.value = res.data || []
      productPagination.total = res.total || 0
    }
  } finally {
    productLoading.value = false
  }
}

// 使用范围改变
const handleUseTypeChange = (value) => {
  if (value === 0) {
    selectedCategoryIds.value = []
    selectedProducts.value = []
    selectedProductIds.value = []
  }
}

// 商品表格分页
const handleProductTableChange = (pagination) => {
  productPagination.current = pagination.current
  productPagination.pageSize = pagination.pageSize
  loadProductList()
}

// 商品选择改变
const onProductSelectChange = (selectedKeys, selectedRows) => {
  selectedProductIds.value = selectedKeys
}

// 确认选择商品
const handleProductModalOk = () => {
  selectedProducts.value = productList.value.filter(p => selectedProductIds.value.includes(p.id))
  showProductModal.value = false
}

// 移除商品
const removeProduct = (productId) => {
  selectedProducts.value = selectedProducts.value.filter(p => p.id !== productId)
  selectedProductIds.value = selectedProductIds.value.filter(id => id !== productId)
}

const handleSubmit = async () => {
  // 先做前端校验，校验不通过直接返回
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  // 校验使用范围
  if (form.useType === 1 && selectedCategoryIds.value.length === 0) {
    message.warning('请选择分类')
    return
  }
  if (form.useType === 2 && selectedProducts.value.length === 0) {
    message.warning('请选择商品')
    return
  }

  // 校验有效期
  if (!form.startTime || !form.endTime) {
    message.warning('请选择有效期')
    return
  }

  // 提交入参与后端 VO 严格对齐
  const payload = {
    type: Number(form.type),
    name: form.name.trim(),
    platform: Number(form.platform),
    count: Number(form.count),
    amount: Number(form.amount),
    perLimit: Number(form.perLimit),
    minAmount: Number(form.minAmount),
    startTime: form.startTime,
    endTime: form.endTime,
    enableTime: form.enableTime || form.startTime, // 未设置则默认与使用开始时间相同
    useType: Number(form.useType),
    note: form.note?.trim() || ''
  }

  // 根据使用范围添加对应的ID列表
  if (form.useType === 1) {
    payload.productCategoryIds = selectedCategoryIds.value
  } else if (form.useType === 2) {
    payload.productIds = selectedProductIds.value
  }

  const rsp = await createCoupon(payload)
  if (!rsp?.success) {
    message.error(rsp?.message || '新增失败')
    return
  }
  message.success('新增成功')
  // 延迟跳转，确保后端数据已保存
  setTimeout(() => {
    goBack()
  }, 300)
}

onMounted(() => {
  loadCategoryOptions()
  loadProductList()
})
</script>
