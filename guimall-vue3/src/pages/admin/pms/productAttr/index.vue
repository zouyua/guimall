<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="text-base font-semibold text-gray-900 mb-1">商品类型</div>
      <p class="text-sm text-gray-500 mb-0 leading-relaxed">
        管理商品规格、参数等属性类型，与分类关联后可在发布商品时使用。
      </p>
    </a-card>

    <a-row :gutter="[20, 20]">
      <a-col v-for="item in entries" :key="item.key" :xs="24" :sm="12" :lg="8">
        <a-card
          :bordered="false"
          class="product-attr-entry-card h-full cursor-pointer rounded-lg border border-gray-100 bg-white shadow-sm transition-all duration-200 hover:border-blue-200 hover:shadow-md"
          @click="item.onCardClick"
        >
          <!-- 主操作：与商品管理列表「新增」按钮同款 -->
          <div class="mb-5">
            <a-button
              type="primary"
              class="flex w-fit items-center gap-1"
              @click.stop="item.onCardClick"
            >
              <component :is="item.actionIcon" />
              {{ item.actionLabel }}
            </a-button>
          </div>

          <!-- 说明区：两卡结构一致 -->
          <div class="flex gap-4">
            <div
              class="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl text-xl"
              :class="item.iconBgClass"
            >
              <component :is="item.decorIcon" />
            </div>
            <div class="min-w-0 flex-1 pt-0.5">
              <div class="mb-1.5 text-base font-semibold text-gray-900">
                {{ item.blockTitle }}
              </div>
              <p class="m-0 text-sm leading-relaxed text-gray-500">
                {{ item.blockDesc }}
              </p>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import {
  PlusOutlined,
  UnorderedListOutlined,
  FileAddOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

const goList = () => {
  router.push('/admin/pms/productAttr/productAttrList')
}

const goAdd = () => {
  router.push('/admin/pms/productAttr/addProductAttr')
}

const entries = [
  {
    key: 'list',
    actionLabel: '进入列表',
    actionIcon: UnorderedListOutlined,
    decorIcon: UnorderedListOutlined,
    iconBgClass: 'bg-blue-50 text-blue-600',
    blockTitle: '商品类型列表',
    blockDesc: '查看、筛选与维护全部属性类型，支持按关联分类检索。',
    onCardClick: goList
  },
  {
    key: 'add',
    actionLabel: '新增商品类型',
    actionIcon: PlusOutlined,
    decorIcon: FileAddOutlined,
    iconBgClass: 'bg-emerald-50 text-emerald-600',
    blockTitle: '创建属性类型',
    blockDesc: '新建属性类型并配置录入方式，与分类绑定后在发布商品时选用。',
    onCardClick: goAdd
  }
]
</script>
