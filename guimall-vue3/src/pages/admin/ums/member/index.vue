<template>
  <div class="p-2 box">
    <!-- 搜索区域 -->
    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-input v-model:value="searchForm.username" placeholder="用户名" allow-clear
          style="width: 150px" @pressEnter="handleSearch" />
        <a-input v-model:value="searchForm.nickname" placeholder="昵称" allow-clear
          style="width: 150px" @pressEnter="handleSearch" />
        <a-input v-model:value="searchForm.phone" placeholder="手机号" allow-clear
          style="width: 150px" @pressEnter="handleSearch" />
        <a-select v-model:value="searchForm.status" placeholder="状态" allow-clear
          style="width: 110px">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="0">禁用</a-select-option>
        </a-select>
        <a-select v-model:value="searchForm.memberLevelId" placeholder="会员等级" allow-clear
          style="width: 150px">
          <a-select-option :value="null">全部</a-select-option>
          <a-select-option v-for="lv in enabledLevels" :key="lv.id" :value="lv.id">
            {{ lv.name }}
          </a-select-option>
        </a-select>
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
        :scroll="{ x: 1200 }"
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

    <!-- 编辑会员弹窗 -->
    <a-modal
      v-model:open="editVisible"
      title="编辑会员信息"
      @ok="handleEditSubmit"
      :confirmLoading="editLoading"
      :destroyOnClose="true"
      width="560px"
    >
      <a-form layout="vertical" class="py-2">
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="用户名">
            <a-input :value="editForm.username" disabled />
          </a-form-item>
          <a-form-item label="昵称">
            <a-input v-model:value="editForm.nickname" placeholder="请输入昵称" />
          </a-form-item>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="手机号">
            <a-input v-model:value="editForm.phone" placeholder="请输入手机号" />
          </a-form-item>
          <a-form-item label="邮箱">
            <a-input v-model:value="editForm.email" placeholder="请输入邮箱" />
          </a-form-item>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="性别">
            <a-select v-model:value="editForm.gender" placeholder="请选择">
              <a-select-option :value="0">未知</a-select-option>
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="城市">
            <a-input v-model:value="editForm.city" placeholder="请输入城市" />
          </a-form-item>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="职业">
            <a-input v-model:value="editForm.job" placeholder="请输入职业" />
          </a-form-item>
          <a-form-item label="会员等级">
            <a-select v-model:value="editForm.memberLevelId" placeholder="请选择等级" allow-clear>
              <a-select-option :value="null">无</a-select-option>
              <a-select-option v-for="lv in enabledLevels" :key="lv.id" :value="lv.id">
                {{ lv.name }}（等级{{ lv.level }}，{{ lv.discount }}%折扣）
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>
        <a-form-item label="状态">
          <a-radio-group v-model:value="editForm.status">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, h, watch, onMounted } from 'vue'
import { Button, Tag, message, Modal } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  StopOutlined,
  CheckCircleOutlined,
  ManOutlined,
  WomanOutlined
} from '@ant-design/icons-vue'
import { fetchMemberList, updateMember, toggleMemberStatus } from '@/api/admin/member'
import { fetchAllEnabledLevels } from '@/api/admin/memberLevel'

// ========== 列表 ==========
const loading = ref(false)
const memberList = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const enabledLevels = ref([])

const searchForm = ref({
  username: '',
  nickname: '',
  phone: '',
  status: undefined,
  memberLevelId: undefined
})

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    align: 'center',
    width: 70
  },
  {
    title: '用户名',
    dataIndex: 'username',
    align: 'center',
    width: 120
  },
  {
    title: '昵称',
    dataIndex: 'nickname',
    align: 'center',
    width: 120,
    customRender: ({ record }) => record.nickname || '-'
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    align: 'center',
    width: 130,
    customRender: ({ record }) => record.phone || '-'
  },
  {
    title: '性别',
    align: 'center',
    width: 70,
    customRender: ({ record }) => {
      if (record.gender === 1) return h('span', { style: 'color:#3b82f6' }, '男')
      if (record.gender === 2) return h('span', { style: 'color:#ec4899' }, '女')
      return h('span', { style: 'color:#9ca3af' }, '未知')
    }
  },
  {
    title: '会员等级',
    align: 'center',
    width: 120,
    customRender: ({ record }) => {
      if (!record.memberLevelId) {
        return h(Tag, { color: 'default' }, () => '普通用户')
      }
      return h(Tag, { color: 'gold' }, () => record.memberLevelName || '未知')
    }
  },
  {
    title: '积分',
    dataIndex: 'integration',
    align: 'center',
    width: 80,
    customRender: ({ record }) =>
      h('span', { style: 'font-weight:bold;color:#f59e0b' }, record.integration || 0)
  },
  {
    title: '状态',
    align: 'center',
    width: 80,
    customRender: ({ record }) =>
      h(Tag, { color: record.status === 1 ? 'success' : 'error' },
        () => record.status === 1 ? '启用' : '禁用')
  },
  {
    title: '注册时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 170,
    customRender: ({ record }) => {
      if (!record.createTime) return '-'
      const d = record.createTime
      if (typeof d === 'string') return d.replace('T', ' ').substring(0, 19)
      return d
    }
  },
  {
    title: '操作',
    align: 'center',
    width: 180,
    fixed: 'right',
    customRender: ({ record }) => {
      return h('div', { style: 'display:flex;gap:8px;justify-content:center' }, [
        h(Button, {
          type: 'primary',
          size: 'small',
          onClick: () => openEditModal(record)
        }, {
          default: () => [h(EditOutlined), ' 编辑']
        }),
        h(Button, {
          size: 'small',
          danger: record.status === 1,
          onClick: () => handleToggleStatus(record)
        }, {
          default: () => record.status === 1
            ? [h(StopOutlined), ' 禁用']
            : [h(CheckCircleOutlined), ' 启用']
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
    Object.keys(params).forEach(k => {
      if (params[k] === '' || params[k] === undefined || params[k] === null) delete params[k]
    })
    const res = await fetchMemberList(params)
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
  searchForm.value = { username: '', nickname: '', phone: '', status: undefined, memberLevelId: undefined }
  current.value = 1
  fetchList()
}

watch([current, size], () => fetchList())

onMounted(() => {
  fetchList()
  loadEnabledLevels()
})

// ========== 等级下拉 ==========
const loadEnabledLevels = async () => {
  try {
    const res = await fetchAllEnabledLevels()
    if (res?.success) {
      enabledLevels.value = res.data || []
    }
  } catch (e) {}
}

// ========== 编辑 ==========
const editVisible = ref(false)
const editLoading = ref(false)
const editForm = ref({})

const openEditModal = (record) => {
  editForm.value = { ...record }
  editVisible.value = true
}

const handleEditSubmit = async () => {
  editLoading.value = true
  try {
    const res = await updateMember(editForm.value)
    if (res?.success) {
      message.success('更新成功')
      editVisible.value = false
      fetchList()
    } else {
      message.error(res?.message || '更新失败')
    }
  } catch (e) {
    message.error('更新失败')
  } finally {
    editLoading.value = false
  }
}

// ========== 启用/禁用 ==========
const handleToggleStatus = (record) => {
  const action = record.status === 1 ? '禁用' : '启用'
  Modal.confirm({
    title: `确认${action}`,
    content: `确定${action}会员「${record.username}」吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const res = await toggleMemberStatus(record.id)
        if (res?.success) {
          message.success(`${action}成功`)
          fetchList()
        } else {
          message.error(res?.message || `${action}失败`)
        }
      } catch (e) {
        message.error(`${action}失败`)
      }
    }
  })
}
</script>
