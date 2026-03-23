<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">人气推荐</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        维护首页「人气」区块展示的商品及排序（静态演示数据）。
      </p>
    </a-card>

    <a-card :bordered="false">

      <!-- 新增 -->
      <div class="mb-4">
        <a-button type="primary" class="flex w-fit items-center gap-1" @click="openModal">
          <PlusOutlined />
          添加推荐商品
        </a-button>
      </div>

      <a-table :dataSource="list" :columns="columns" :pagination="false" rowKey="rowKey" bordered class="w-full" />

    </a-card>

    <a-modal
      v-model:open="modalVisible"
      title="选择商品加入人气推荐"
      ok-text="确定"
      cancel-text="取消"
      destroy-on-close
      @ok="handleAddOk"
    >
      <a-form layout="vertical" class="mt-2">
        <a-form-item label="商品" required>
          <a-select
            v-model:value="pickProductId"
            placeholder="请选择尚未推荐的商品"
            class="w-full"
            allow-clear
            show-search
            option-filter-prop="label"
          >
            <a-select-option
              v-for="p in availablePool"
              :key="p.id"
              :value="p.id"
              :label="p.name"
            >
              {{ p.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { Button, Popconfirm, Image, message } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined, ArrowUpOutlined, ArrowDownOutlined } from '@ant-design/icons-vue'

const productPool = [
  { id: 301, name: '脐橙', pic: 'https://picsum.photos/seed/h1/80' },
  { id: 302, name: '砂糖橘', pic: 'https://picsum.photos/seed/h2/80' },
  { id: 303, name: '苹果', pic: 'https://picsum.photos/seed/h3/80' },
  { id: 304, name: '猕猴桃', pic: 'https://picsum.photos/seed/h4/80' }
]

const list = ref([
  { rowKey: 'h1', productId: 301, name: '脐橙', pic: 'https://picsum.photos/seed/h1/80', sort: 1 },
  { rowKey: 'h2', productId: 302, name: '砂糖橘', pic: 'https://picsum.photos/seed/h2/80', sort: 2 },
  { rowKey: 'h3', productId: 303, name: '苹果', pic: 'https://picsum.photos/seed/h3/80', sort: 3 }
])

const modalVisible = ref(false)
const pickProductId = ref(undefined)

const availablePool = computed(() => {
  const picked = new Set(list.value.map((r) => r.productId))
  return productPool.filter((p) => !picked.has(p.id))
})

const openModal = () => {
  pickProductId.value = undefined
  modalVisible.value = true
}

const handleAddOk = () => {
  if (!pickProductId.value) {
    message.warning('请选择商品')
    return
  }
  const p = productPool.find((x) => x.id === pickProductId.value)
  if (!p) return
  list.value.push({
    rowKey: `h${Date.now()}`,
    productId: p.id,
    name: p.name,
    pic: p.pic,
    sort: list.value.length + 1
  })
  modalVisible.value = false
  message.success('已加入推荐（演示）')
}

const move = (index, dir) => {
  const j = index + dir
  if (j < 0 || j >= list.value.length) return
  const arr = list.value
  ;[arr[index], arr[j]] = [arr[j], arr[index]]
  arr.forEach((r, i) => {
    r.sort = i + 1
  })
}

const remove = (index) => {
  list.value.splice(index, 1)
  list.value.forEach((r, i) => {
    r.sort = i + 1
  })
  message.success('已移除')
}

const columns = [
  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center',
    width: 80
  },
  {
    title: '商品图',
    dataIndex: 'pic',
    align: 'center',
    width: 100,
    customRender: ({ text }) =>
      h('div', { class: 'flex justify-center' }, [
        h(Image, { src: text, width: 56, height: 56, class: 'rounded object-cover' })
      ])
  },
  { title: '商品名称', dataIndex: 'name', align: 'center' },
  {
    title: '操作',
    align: 'center',
    width: 220,
    customRender: ({ index }) =>
      h('div', { class: 'flex flex-nowrap items-center justify-center gap-2' }, [
        h(
          Button,
          {
            size: 'small',
            disabled: index === 0,
            class: 'inline-flex items-center gap-1',
            onClick: () => move(index, -1)
          },
          {
            icon: () => h(ArrowUpOutlined),
            default: () => '上移'
          }
        ),
        h(
          Button,
          {
            size: 'small',
            disabled: index === list.value.length - 1,
            class: 'inline-flex items-center gap-1',
            onClick: () => move(index, 1)
          },
          {
            icon: () => h(ArrowDownOutlined),
            default: () => '下移'
          }
        ),
        h(
          Popconfirm,
          {
            title: '从人气推荐中移除？',
            onConfirm: () => remove(index)
          },
          {
            default: () =>
              h(
                Button,
                {
                  size: 'small',
                  danger: true,
                  class: 'inline-flex items-center gap-1'
                },
                {
                  icon: () => h(DeleteOutlined),
                  default: () => '移除'
                }
              )
          }
        )
      ])
  }
]
</script>
