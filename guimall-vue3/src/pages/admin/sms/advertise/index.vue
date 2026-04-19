<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">首页轮播图</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        <!-- 与前端首页轮播组件对应，仅配置轮播图、排序与是否展示；轮播为纯展示，无需、也不配置跳转链接。 -->
      </p>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="轮播标题">
          <a-input v-model:value="searchName" placeholder="请输入关键词" class="w-56" allow-clear />
        </a-form-item>

        <a-form-item label="状态">
          <a-select v-model:value="searchStatus" placeholder="请选择" class="!w-36" allow-clear>
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">停用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button class="ml-2" @click="handleReset">重置</a-button>
        </a-form-item>

      </a-form>
    </a-card>

    <a-card :bordered="false" title="轮播图列表">

      <!-- 新增 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="router.push('/admin/sms/advertise/add')">
          <PlusOutlined />
          新增轮播图
        </a-button>
      </div>

      <a-table :dataSource="pagedData" :columns="columns" :pagination="false" rowKey="id" bordered class="w-full" />

      <div class="mt-6 flex justify-center">
        <a-pagination
          v-model:current="current"
          v-model:pageSize="size"
          :total="total"
          show-size-changer
          :pageSizeOptions="[10, 20, 50]"
          :show-total="(t) => `共 ${t} 条`"
        />
      </div>

    </a-card>

  </div>
</template>

<script setup>
// 轮播图管理列表页
// 职责：查询/分页/删除/状态切换/跳转新增编辑
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Image, Popconfirm, Switch, message } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { deleteHomeAdvertise, fetchHomeAdvertiseList, updateHomeAdvertise } from '@/api/admin/homeAdvertise'

const router = useRouter()

// 查询条件
const searchName = ref('')
const searchStatus = ref()

// 列表数据与分页状态
const allRows = ref([])
const total = ref(0)

const current = ref(1)
const size = ref(10)

const pagedData = computed(() => allRows.value)

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  {
    title: '轮播图',
    dataIndex: 'pic',
    align: 'center',
    width: 140,
    customRender: ({ text }) =>
      h('div', { class: 'flex justify-center' }, [
        h(Image, { src: text, height: 48, class: 'rounded object-cover max-w-[120px]' })
      ])
  },
  { title: '轮播标题', dataIndex: 'name', align: 'center', ellipsis: true },
  { title: '排序', dataIndex: 'sort', align: 'center', width: 80 },
  {
    title: '状态',
    align: 'center',
    width: 160,
    customRender: ({ record }) =>
      h('div', { class: 'flex justify-center py-1' }, [
        h(Switch, {
          checked: Number(record.status) === 1,
          checkedChildren: '启用',
          unCheckedChildren: '停用',
          class: 'advertise-status-switch',
          onChange: (checked) => {
            handleToggleStatus(record, checked)
          }
        })
      ])
  },
  {
    title: '操作',
    align: 'center',
    width: 200,
    customRender: ({ record }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            type: 'primary',
            class: 'inline-flex shrink-0 items-center gap-1',
            onClick: () =>
              router.push({ path: '/admin/sms/advertise/update', query: { id: record.id } })
          },
          {
            icon: () => h(EditOutlined),
            default: () => '编辑'
          }
        ),
        h(
          Popconfirm,
          {
            title: '确定删除该轮播图吗？',
            onConfirm: () => handleDelete(record.id)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  class: 'inline-flex shrink-0 items-center gap-1'
                },
                {
                  icon: () => h(DeleteOutlined),
                  default: () => '删除'
                }
              )
          }
        )
      ])
  }
]

const handleSearch = () => {
  // 查询时回到第一页
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleReset = () => {
  // 重置条件并刷新列表
  searchName.value = ''
  searchStatus.value = undefined
  const prev = current.value
  current.value = 1
  if (prev === 1) fetchList()
}

const handleDelete = async (id) => {
  // 删除按钮入参校验：id 必须存在
  if (!id) {
    message.warning('广告ID不能为空')
    return
  }
  const rsp = await deleteHomeAdvertise(id)
  if (!rsp?.success) {
    message.error(rsp?.message || '删除失败')
    return
  }
  message.success('删除成功')
  await fetchList()
}

const handleToggleStatus = async (record, checked) => {
  // 状态开关接口是 update，必须带齐 UpdateSmsHomeAdvertiseReqVO 必填字段
  if (!record?.id) {
    message.warning('广告ID不能为空')
    return
  }
  const payload = {
    name: record.name,
    type: Number(record.type ?? 0),
    pic: record.pic,
    startTime: record.startTime,
    endTime: record.endTime,
    status: checked ? 1 : 0,
    url: record.url || '',
    note: record.note || '',
    sort: Number(record.sort || 0)
  }
  const rsp = await updateHomeAdvertise(record.id, payload)
  if (!rsp?.success) {
    message.error(rsp?.message || '状态更新失败')
    return
  }
  record.status = payload.status
  message.success('状态已更新')
}

const fetchList = async () => {
  // 与后端 FindSmsHomeAdvertisePageListReqVO 对齐
  const reqVO = {
    current: current.value,
    size: size.value
  }
  if (searchName.value.trim()) reqVO.name = searchName.value.trim()
  if (searchStatus.value === 0 || searchStatus.value === 1) reqVO.status = Number(searchStatus.value)
  const rsp = await fetchHomeAdvertiseList(reqVO)
  if (!rsp?.success) {
    message.error(rsp?.message || '获取轮播图列表失败')
    return
  }
  allRows.value = rsp.data || []
  total.value = rsp.total || 0
}

onMounted(() => {
  // 页面首次进入加载
  fetchList()
})

watch([current, size], () => {
  fetchList()
})
</script>

<style scoped>
/* 比 small 略大，便于点击 */
:deep(.advertise-status-switch.ant-switch) {
  transform: scale(1.12);
  transform-origin: center center;
}
</style>
