<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">首页轮播图</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        与前端首页轮播组件对应，仅配置轮播图、排序与是否展示；轮播为纯展示，无需、也不配置跳转链接。
      </p>
    </a-card>

    <a-card :bordered="false" class="mb-5">
      <a-form layout="inline" class="flex flex-wrap items-center gap-4">

        <a-form-item label="轮播标题">
          <a-input v-model:value="searchTitle" placeholder="请输入关键词" class="w-56" allow-clear />
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

    <a-card :bordered="false">

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
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { Button, Image, Popconfirm, Switch } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const router = useRouter()

const searchTitle = ref('')
const searchStatus = ref()

const allRows = ref([
  {
    id: 1,
    title: '春季鲜果节',
    pic: 'https://picsum.photos/seed/ad1/120/60',
    sort: 1,
    enabled: 1
  },
  {
    id: 2,
    title: '会员福利日',
    pic: 'https://picsum.photos/seed/ad2/120/60',
    sort: 2,
    enabled: 1
  },
  {
    id: 3,
    title: '下线占位',
    pic: 'https://picsum.photos/seed/ad3/120/60',
    sort: 3,
    enabled: 0
  }
])

const current = ref(1)
const size = ref(10)

const filtered = computed(() => {
  let list = allRows.value
  if (searchTitle.value.trim()) {
    list = list.filter((r) => r.title.includes(searchTitle.value.trim()))
  }
  if (searchStatus.value === 0 || searchStatus.value === 1) {
    list = list.filter((r) => r.enabled === searchStatus.value)
  }
  return list
})

const total = computed(() => filtered.value.length)

const pagedData = computed(() => {
  const start = (current.value - 1) * size.value
  return filtered.value.slice(start, start + size.value)
})

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
  { title: '轮播标题', dataIndex: 'title', align: 'center', ellipsis: true },
  { title: '排序', dataIndex: 'sort', align: 'center', width: 80 },
  {
    title: '状态',
    align: 'center',
    width: 160,
    customRender: ({ record }) =>
      h('div', { class: 'flex justify-center py-1' }, [
        h(Switch, {
          checked: record.enabled === 1,
          checkedChildren: '启用',
          unCheckedChildren: '停用',
          class: 'advertise-status-switch',
          onChange: (checked) => {
            record.enabled = checked ? 1 : 0
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
  current.value = 1
}

const handleReset = () => {
  searchTitle.value = ''
  searchStatus.value = undefined
  current.value = 1
}

const handleDelete = (id) => {
  allRows.value = allRows.value.filter((r) => r.id !== id)
  if (pagedData.value.length === 0 && current.value > 1) current.value--
}
</script>

<style scoped>
/* 比 small 略大，便于点击 */
:deep(.advertise-status-switch.ant-switch) {
  transform: scale(1.12);
  transform-origin: center center;
}
</style>
