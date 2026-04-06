<template>
  <div class="p-2 box">
    <!-- 商品列表选择区域 -->
    <a-card :bordered="false" class="mb-5" v-if="!selectedProductId">
      <div class="mb-4">
        <h3 class="text-lg font-semibold mb-2">选择商品进行溯源管理</h3>
        <a-form layout="inline" class="mb-4">
          <a-form-item label="商品名称">
            <a-input v-model:value="searchProductName" placeholder="请输入商品名称" style="width: 200px" allow-clear />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="loadProductList">查询</a-button>
            <a-button class="ml-2" @click="handleResetSearch">重置</a-button>
          </a-form-item>
        </a-form>
      </div>
      <a-table
        :dataSource="productListData"
        :columns="productColumns"
        :pagination="productPagination"
        rowKey="id"
        bordered
        @change="handleProductTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'pic'">
            <a-image :src="record.pic" :width="50" />
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="selectProduct(record)">管理溯源</a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 已选择商品后的溯源记录管理区域 -->
    <a-card :bordered="false" class="mb-5" v-else>
      <div class="flex items-center gap-4">
        <a-button @click="backToProductList">
          <ArrowLeftOutlined /> 返回商品列表
        </a-button>
        <span class="text-base font-semibold">当前商品：{{ currentProductName }}</span>
        <a-tag v-if="currentFarmerName" color="blue">农户：{{ currentFarmerName }}</a-tag>
        <a-tag v-if="currentOriginName && currentOriginName !== '未绑定'" color="cyan">产地：{{ currentOriginName }}</a-tag>
      </div>
    </a-card>

    <a-card :bordered="false" title="溯源记录管理" v-if="selectedProductId">
      <template #extra>
        <a-button type="primary" @click="handleAdd">
          <PlusOutlined /> 新增轨迹节点
        </a-button>
      </template>

      <a-empty v-if="records.length === 0" description="该商品暂无溯源记录" />

      <div v-else class="record-timeline-container">
        <a-table :dataSource="records" :columns="columns" rowKey="id" bordered>
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'pic'">
              <a-image :src="record.pic" :width="60" v-if="record.pic" />
              <span v-else class="text-gray-400">无图片</span>
            </template>
            <template v-if="column.dataIndex === 'recordTypeName'">
              <a-tag color="green">{{ record.recordTypeName }}</a-tag>
            </template>
            <template v-if="column.dataIndex === 'farmerName'">
              <span>{{ record.farmerName || currentFarmerName || '-' }}</span>
            </template>
            <template v-if="column.key === 'action'">
              <div class="flex gap-2">
                <a-button size="small" type="link" @click="handleEdit(record)">编辑</a-button>
                <a-popconfirm title="确定删除此条记录吗？" @confirm="handleDelete(record.id)">
                  <a-button size="small" type="link" danger>删除</a-button>
                </a-popconfirm>
              </div>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal
      v-model:open="modalVisible"
      :title="isEdit ? '编辑记录' : '新增轨迹记录'"
      @ok="handleModalOk"
      :confirmLoading="submitting"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item label="记录类型" required>
          <a-select v-model:value="form.recordTypeId" placeholder="请选择记录类型">
            <a-select-option v-for="type in recordTypeOptions" :key="type.id" :value="type.id">
              {{ type.typeName }}
            </a-select-option>
          </a-select>
          <p class="mt-1 text-xs text-gray-400">记录类型根据商品分类自动加载</p>
        </a-form-item>
        <a-form-item label="记录时间" required>
          <a-date-picker v-model:value="form.recordTime" show-time class="w-full" value-format="YYYY-MM-DD HH:mm:ss" />
        </a-form-item>
        <a-form-item label="详细内容" required>
          <a-textarea v-model:value="form.content" placeholder="描述该阶段的具体操作..." :rows="4" />
        </a-form-item>
        <a-form-item label="现场照片">
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
          <p class="text-xs text-gray-400">支持 jpg/png 格式，非必填</p>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { PlusOutlined, ArrowLeftOutlined } from '@ant-design/icons-vue'
import { fetchTraceRecordList, createTraceRecord, updateTraceRecord, deleteTraceRecord } from '@/api/admin/traceRecord'
import { fetchProductList } from '@/api/admin/product'
import { getRecordTypesByCategoryId } from '@/api/admin/traceRecordType'
import { uploadFile } from '@/api/admin/upload'
import { getFarmerDetail } from '@/api/admin/farmer'

const route = useRoute()
const selectedProductId = ref(null)
const currentProductName = ref('')
const currentProductCategoryId = ref(null)
const currentFarmerName = ref('')
const currentFarmerId = ref(null)
const currentOriginName = ref('')
const records = ref([])
const recordTypeOptions = ref([])
const loading = ref(false)

// 照片上传相关
const picFileList = ref([])

const handlePicUpload = async ({ file, onSuccess, onError }) => {
  try {
    const res = await uploadFile(file)
    if (res.success) {
      form.value.pic = res.data
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
  form.value.pic = ''
  picFileList.value = []
}

// 商品列表相关
const searchProductName = ref('')
const productListData = ref([])
const productPagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const productColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '商品图片', dataIndex: 'pic', width: 100 },
  { title: '商品名称', dataIndex: 'name' },
  { title: '分类', dataIndex: 'categoryName', width: 120 },
  { title: '农户', dataIndex: 'farmerName', width: 120 },
  { title: '价格', dataIndex: 'price', width: 100 },
  { title: '操作', key: 'action', width: 120, align: 'center' }
]

const modalVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const form = ref({
  id: null,
  productId: null,
  farmerId: null,
  recordTypeId: null,
  recordTime: '',
  content: '',
  pic: ''
})

const columns = [
  { title: '记录时间', dataIndex: 'recordTime', width: 180 },
  { title: '类型', dataIndex: 'recordTypeName', width: 120 },
  { title: '操作农户', dataIndex: 'farmerName', width: 120 },
  { title: '内容', dataIndex: 'content' },
  { title: '现场图', dataIndex: 'pic', width: 100 },
  { title: '操作', key: 'action', width: 120, align: 'center' }
]

// 加载商品列表
const loadProductList = async () => {
  const res = await fetchProductList({
    current: productPagination.value.current,
    size: productPagination.value.pageSize,
    name: searchProductName.value || undefined
  })
  if (res.success) {
    productListData.value = res.data
    productPagination.value.total = res.total
  }
}

const handleProductTableChange = (pagination) => {
  productPagination.value.current = pagination.current
  loadProductList()
}

const handleResetSearch = () => {
  searchProductName.value = ''
  productPagination.value.current = 1
  loadProductList()
}

const selectProduct = async (product) => {
  selectedProductId.value = product.id
  currentProductName.value = product.name
  currentProductCategoryId.value = product.productCategoryId
  currentFarmerName.value = product.farmerName || ''
  currentFarmerId.value = product.farmerId || null
  fetchRecords()
  loadRecordTypes(product.productCategoryId)

  // 通过农户详情获取产地信息
  if (product.farmerId) {
    try {
      const farmerRsp = await getFarmerDetail(product.farmerId)
      if (farmerRsp?.success && farmerRsp.data?.originNames?.length > 0) {
        currentOriginName.value = farmerRsp.data.originNames.join('、')
      } else {
        currentOriginName.value = '未绑定'
      }
    } catch {
      currentOriginName.value = '未绑定'
    }
  } else {
    currentOriginName.value = '未绑定'
  }
}

const backToProductList = () => {
  selectedProductId.value = null
  currentProductName.value = ''
  currentProductCategoryId.value = null
  currentFarmerName.value = ''
  currentFarmerId.value = null
  currentOriginName.value = ''
  records.value = []
  recordTypeOptions.value = []
}

// 加载溯源记录类型
const loadRecordTypes = async (categoryId) => {
  if (!categoryId) {
    recordTypeOptions.value = []
    return
  }
  const res = await getRecordTypesByCategoryId(categoryId)
  if (res.success) {
    recordTypeOptions.value = res.data || []
  }
}

// 监听商品分类变化，自动加载对应的记录类型
watch(currentProductCategoryId, (newCategoryId) => {
  if (newCategoryId) {
    loadRecordTypes(newCategoryId)
  }
})

const fetchRecords = async () => {
  if (!selectedProductId.value) return
  loading.value = true
  try {
    const res = await fetchTraceRecordList({ productId: selectedProductId.value })
    if (res.success) {
      records.value = res.data
    }
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  if (!recordTypeOptions.value || recordTypeOptions.value.length === 0) {
    message.warning('该商品分类暂无可用的溯源记录类型，请先在数据库中配置')
    return
  }
  isEdit.value = false
  // 默认使用商品关联的农户作为操作人
  form.value = {
    id: null,
    productId: selectedProductId.value,
    farmerId: currentFarmerId.value,
    recordTypeId: recordTypeOptions.value[0]?.id || null,
    recordTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
    content: '',
    pic: ''
  }
  picFileList.value = []
  modalVisible.value = true
}

const handleEdit = (record) => {
  isEdit.value = true
  form.value = { ...record }
  // 回显已有图片
  if (record.pic) {
    picFileList.value = [{ uid: '-1', name: '现场照片', status: 'done', url: record.pic }]
  } else {
    picFileList.value = []
  }
  modalVisible.value = true
}

const handleModalOk = async () => {
  if (!form.value.content || !form.value.recordTime) {
    return message.warning('请填写必填项')
  }
  submitting.value = true
  try {
    const api = isEdit.value ? updateTraceRecord : createTraceRecord
    const res = await api(form.value)
    if (res.success) {
      message.success(isEdit.value ? '修改成功' : '新增成功')
      modalVisible.value = false
      fetchRecords()
    }
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (id) => {
  const res = await deleteTraceRecord(id)
  if (res.success) {
    message.success('删除成功')
    fetchRecords()
  }
}

onMounted(() => {
  // 如果路由传了 productId
  const pid = route.query.productId
  if (pid) {
    selectedProductId.value = Number(pid)
    fetchProductList({ current: 1, size: 1, id: pid }).then(res => {
      if (res.success && res.data.length > 0) {
        const product = res.data[0]
        currentProductName.value = product.name
        currentProductCategoryId.value = product.productCategoryId
        currentFarmerName.value = product.farmerName || ''
        currentFarmerId.value = product.farmerId || null
        fetchRecords()
        loadRecordTypes(product.productCategoryId)
      }
    })
  } else {
    loadProductList()
  }
})
</script>

<style scoped>
.record-timeline-container {
  min-height: 400px;
}
</style>
