<template>
  <div class="p-2 box">
    <!-- 搜索区域 -->
    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-input v-model:value="searchForm.username" placeholder="用户名" allow-clear
          style="width: 180px" @pressEnter="handleSearch" />
        <a-input v-model:value="searchForm.nickname" placeholder="昵称" allow-clear
          style="width: 180px" @pressEnter="handleSearch" />
        <a-button type="primary" @click="handleSearch">
          <SearchOutlined /> 搜索
        </a-button>
        <a-button @click="handleReset">
          <ReloadOutlined /> 重置
        </a-button>
      </div>
    </a-card>

    <!-- 列表区域 -->
    <a-card :bordered="false">
      <a-table
        :dataSource="memberList"
        :columns="columns"
        :pagination="false"
        :loading="loading"
        rowKey="id"
        bordered
        class="w-full"
      />

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

    <!-- 调整积分弹窗 -->
    <a-modal
      v-model:open="adjustVisible"
      title="调整积分"
      @ok="handleAdjustSubmit"
      :confirmLoading="adjustLoading"
      :destroyOnClose="true"
    >
      <div class="space-y-4 py-2">
        <div>
          <span class="text-gray-500">会员：</span>
          <span class="font-bold">{{ adjustTarget.nickname || adjustTarget.username }}</span>
          <span class="text-gray-400 ml-2">（当前积分：{{ adjustTarget.integration || 0 }}）</span>
        </div>
        <a-form layout="vertical">
          <a-form-item label="调整类型">
            <a-radio-group v-model:value="adjustForm.type">
              <a-radio value="add">增加积分</a-radio>
              <a-radio value="deduct">扣减积分</a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="数量">
            <a-input-number v-model:value="adjustForm.amount" :min="1" :max="999999"
              style="width: 200px" placeholder="请输入积分数量" />
          </a-form-item>
          <a-form-item label="备注">
            <a-textarea v-model:value="adjustForm.note" :rows="2"
              placeholder="请输入调整原因（可选）" />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>

    <!-- 积分历史弹窗 -->
    <a-modal
      v-model:open="historyVisible"
      :title="'积分变动记录 — ' + (historyTarget.nickname || historyTarget.username || '')"
      width="750px"
      :footer="null"
      :destroyOnClose="true"
    >
      <div class="mb-4">
        <a-radio-group v-model:value="historyChangeType" @change="loadHistory" button-style="solid" size="small">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="0">获取</a-radio-button>
          <a-radio-button :value="1">消费</a-radio-button>
        </a-radio-group>
      </div>

      <a-table
        :dataSource="historyList"
        :columns="historyColumns"
        :pagination="false"
        :loading="historyLoading"
        rowKey="id"
        bordered
        size="small"
      />

      <div class="mt-4 flex justify-center">
        <a-pagination
          v-model:current="historyCurrent"
          v-model:pageSize="historySize"
          :total="historyTotal"
          size="small"
          :show-total="(t) => `共 ${t} 条`"
        />
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, h, watch, onMounted } from 'vue'
import { Button, Tag, message } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  HistoryOutlined
} from '@ant-design/icons-vue'
import {
  fetchMemberIntegrationList,
  fetchIntegrationHistory,
  adjustIntegration
} from '@/api/admin/integration'

// ========== 会员列表 ==========
const loading = ref(false)
const memberList = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)

const searchForm = ref({
  username: '',
  nickname: ''
})

const columns = [
  {
    title: '序号',
    align: 'center',
    width: 70,
    customRender: ({ index }) => (current.value - 1) * size.value + index + 1
  },
  { title: 'ID', dataIndex: 'id', align: 'center', width: 80 },
  { title: '用户名', dataIndex: 'username', align: 'center' },
  { title: '昵称', dataIndex: 'nickname', align: 'center' },
  { title: '手机号', dataIndex: 'phone', align: 'center' },
  {
    title: '当前积分',
    dataIndex: 'integration',
    align: 'center',
    width: 120,
    customRender: ({ record }) =>
      h('span', { style: { fontWeight: 'bold', color: '#059669' } },
        record.integration || 0)
  },
  {
    title: '历史总积分',
    dataIndex: 'historyIntegration',
    align: 'center',
    width: 120,
    customRender: ({ record }) => record.historyIntegration || 0
  },
  {
    title: '状态',
    align: 'center',
    width: 80,
    customRender: ({ record }) =>
      h(Tag, { color: record.status === 1 ? 'success' : 'error' },
        () => record.status === 1 ? '正常' : '禁用')
  },
  {
    title: '操作',
    align: 'center',
    width: 200,
    customRender: ({ record }) => {
      return h('div', { style: 'display:flex;gap:8px;justify-content:center' }, [
        h(Button, {
          type: 'primary',
          size: 'small',
          onClick: () => openAdjust(record)
        }, {
          default: () => [h(EditOutlined), ' 调整积分']
        }),
        h(Button, {
          size: 'small',
          onClick: () => openHistory(record)
        }, {
          default: () => [h(HistoryOutlined), ' 记录']
        })
      ])
    }
  }
]

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      current: current.value,
      size: size.value,
      ...searchForm.value
    }
    // 清除空值
    Object.keys(params).forEach(k => { if (!params[k] && params[k] !== 0) delete params[k] })
    const res = await fetchMemberIntegrationList(params)
    if (res?.success) {
      memberList.value = res.data || []
      total.value = res.total || 0
    } else {
      message.error(res?.message || '查询失败')
    }
  } catch (e) {
    message.error('查询失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  current.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.value = { username: '', nickname: '' }
  current.value = 1
  fetchList()
}

watch([current, size], () => fetchList())

onMounted(() => fetchList())

// ========== 调整积分 ==========
const adjustVisible = ref(false)
const adjustLoading = ref(false)
const adjustTarget = ref({})
const adjustForm = ref({
  type: 'add',
  amount: null,
  note: ''
})

const openAdjust = (record) => {
  adjustTarget.value = record
  adjustForm.value = { type: 'add', amount: null, note: '' }
  adjustVisible.value = true
}

const handleAdjustSubmit = async () => {
  if (!adjustForm.value.amount || adjustForm.value.amount <= 0) {
    message.warning('请输入有效的积分数量')
    return
  }

  adjustLoading.value = true
  try {
    const delta = adjustForm.value.type === 'add'
      ? adjustForm.value.amount
      : -adjustForm.value.amount

    const res = await adjustIntegration({
      memberId: adjustTarget.value.id,
      delta,
      note: adjustForm.value.note || undefined
    })

    if (res?.success) {
      message.success('积分调整成功')
      adjustVisible.value = false
      fetchList()
    } else {
      message.error(res?.message || '调整失败')
    }
  } catch (e) {
    message.error('调整失败')
  } finally {
    adjustLoading.value = false
  }
}

// ========== 积分历史 ==========
const historyVisible = ref(false)
const historyLoading = ref(false)
const historyTarget = ref({})
const historyList = ref([])
const historyTotal = ref(0)
const historyCurrent = ref(1)
const historySize = ref(10)
const historyChangeType = ref(null)

const sourceTypeMap = {
  0: '订单完成',
  1: '下单抵扣',
  2: '管理员调整',
  3: '注册赠送',
  4: '取消退还'
}

const historyColumns = [
  {
    title: '序号',
    align: 'center',
    width: 60,
    customRender: ({ index }) => (historyCurrent.value - 1) * historySize.value + index + 1
  },
  {
    title: '变动',
    align: 'center',
    width: 100,
    customRender: ({ record }) => {
      const isAdd = record.changeType === 0
      return h('span', {
        style: { fontWeight: 'bold', color: isAdd ? '#059669' : '#dc2626' }
      }, (isAdd ? '+' : '-') + record.changeCount)
    }
  },
  {
    title: '类型',
    align: 'center',
    width: 100,
    customRender: ({ record }) =>
      h(Tag, { color: record.changeType === 0 ? 'green' : 'red' },
        () => record.changeType === 0 ? '获取' : '消费')
  },
  {
    title: '来源',
    align: 'center',
    width: 110,
    customRender: ({ record }) => sourceTypeMap[record.sourceType] || '未知'
  },
  { title: '备注', dataIndex: 'note', align: 'center', ellipsis: true },
  { title: '时间', dataIndex: 'createTime', align: 'center', width: 170 }
]

const openHistory = (record) => {
  historyTarget.value = record
  historyCurrent.value = 1
  historyChangeType.value = null
  historyVisible.value = true
  loadHistory()
}

const loadHistory = async () => {
  historyLoading.value = true
  try {
    const params = {
      memberId: historyTarget.value.id,
      current: historyCurrent.value,
      size: historySize.value
    }
    if (historyChangeType.value !== null) {
      params.changeType = historyChangeType.value
    }
    const res = await fetchIntegrationHistory(params)
    if (res?.success) {
      historyList.value = res.data || []
      historyTotal.value = res.total || 0
    }
  } catch (e) {
    message.error('加载历史记录失败')
  } finally {
    historyLoading.value = false
  }
}

watch([historyCurrent, historySize], () => {
  if (historyVisible.value) loadHistory()
})
</script>
