<template>
  <div class="p-2 box">

    <!-- 参数列表 -->
    <a-card :bordered="false">
      <a-alert
        class="mb-4"
        type="info"
        show-icon
        banner
        message="参数定义是全局参数字典，定义商品可以使用的参数（如：保质期=12个月、产地=山东）。"
      />

      <!-- 新增参数表单 -->
      <div class="flex items-center gap-2 mb-4">
        <span class="text-gray-600 whitespace-nowrap">参数名：</span>
        <a-input
          v-model:value="newParamName"
          placeholder="如：保质期、产地"
          style="width: 200px"
          @pressEnter="handleCreate"
        />
        <span class="text-gray-600 whitespace-nowrap">参数值：</span>
        <a-input
          v-model:value="newParamValue"
          placeholder="如：12个月、山东"
          class="flex-1"
          @pressEnter="handleCreate"
        />
        <span class="text-gray-600 whitespace-nowrap">排序：</span>
        <a-input-number
          v-model:value="newParamSort"
          placeholder="排序"
          :min="0"
          style="width: 100px"
        />
        <a-button type="primary" @click="handleCreate" :disabled="!newParamName.trim() || !newParamValue.trim()">
          添加参数
        </a-button>
      </div>

      <a-table
        :dataSource="dataList"
        :columns="columns"
        :loading="loading"
        rowKey="id"
        bordered
        :pagination="false"
      />
    </a-card>

  </div>
</template>

<script setup>
import { ref, h, onMounted } from 'vue'
import { message, Button, Popconfirm, Input, InputNumber } from 'ant-design-vue'
import {
  fetchParamDefinitions,
  createParamDefinition,
  updateParamDefinition,
  deleteParamDefinition
} from '@/api/admin/paramDefinition'

const dataList = ref([])
const loading = ref(false)

// 新增状态
const newParamName = ref('')
const newParamValue = ref('')
const newParamSort = ref(0)

// 编辑状态
const editingId = ref(null)
const editingName = ref('')
const editingValue = ref('')
const editingSort = ref(0)

const columns = [
  {
    title: '参数名',
    dataIndex: 'paramName',
    width: '25%',
    customRender: ({ record }) => {
      if (editingId.value === record.id) {
        return h(Input, {
          value: editingName.value,
          onChange: e => (editingName.value = e.target.value),
          onPressEnter: () => handleSaveEdit(record)
        })
      }
      return record.paramName
    }
  },
  {
    title: '参数值',
    dataIndex: 'paramValue',
    width: '30%',
    customRender: ({ record }) => {
      if (editingId.value === record.id) {
        return h(Input, {
          value: editingValue.value,
          onChange: e => (editingValue.value = e.target.value),
          onPressEnter: () => handleSaveEdit(record)
        })
      }
      return record.paramValue
    }
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: '10%',
    align: 'center',
    customRender: ({ record }) => {
      if (editingId.value === record.id) {
        return h(InputNumber, {
          value: editingSort.value,
          min: 0,
          onChange: v => (editingSort.value = v)
        })
      }
      return record.sort
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: '20%'
  },
  {
    title: '操作',
    width: '15%',
    align: 'center',
    customRender: ({ record }) => {
      if (editingId.value === record.id) {
        return h('div', { class: 'flex justify-center gap-2' }, [
          h(Button, { size: 'small', type: 'primary', onClick: () => handleSaveEdit(record) }, () => '保存'),
          h(Button, { size: 'small', onClick: handleCancelEdit }, () => '取消')
        ])
      }
      return h('div', { class: 'flex justify-center gap-2' }, [
        h(Button, {
          size: 'small',
          onClick: () => {
            editingId.value = record.id
            editingName.value = record.paramName
            editingValue.value = record.paramValue
            editingSort.value = record.sort
          }
        }, () => '编辑'),
        h(
          Popconfirm,
          { title: '确定删除该参数？', onConfirm: () => handleDelete(record.id) },
          { default: () => h(Button, { size: 'small', danger: true }, () => '删除') }
        )
      ])
    }
  }
]

const fetchList = async () => {
  loading.value = true
  try {
    const rsp = await fetchParamDefinitions()
    dataList.value = rsp?.data || []
  } finally {
    loading.value = false
  }
}

const handleCreate = async () => {
  const name = newParamName.value.trim()
  const value = newParamValue.value.trim()
  if (!name || !value) return
  await createParamDefinition({
    paramName: name,
    paramValue: value,
    sort: newParamSort.value || 0
  })
  message.success('添加成功')
  newParamName.value = ''
  newParamValue.value = ''
  newParamSort.value = 0
  await fetchList()
}

const handleSaveEdit = async (record) => {
  const name = editingName.value.trim()
  const value = editingValue.value.trim()
  if (!name) {
    message.warn('参数名不能为空')
    return
  }
  if (!value) {
    message.warn('参数值不能为空')
    return
  }
  await updateParamDefinition(record.id, {
    paramName: name,
    paramValue: value,
    sort: editingSort.value || 0
  })
  message.success('修改成功')
  editingId.value = null
  await fetchList()
}

const handleCancelEdit = () => {
  editingId.value = null
}

const handleDelete = async (id) => {
  await deleteParamDefinition(id)
  message.success('删除成功')
  await fetchList()
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.box {
  min-height: calc(100vh - 64px);
}
</style>
