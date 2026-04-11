<template>
  <div class="p-2 box">

    <a-card :bordered="false" class="mb-5">
      <div class="flex flex-wrap items-center gap-4">
        <a-button class="flex items-center gap-1" @click="goBack">
          <ArrowLeftOutlined />
          返回列表
        </a-button>
        <span class="text-base font-semibold">编辑优惠券</span>
      </div>
    </a-card>

    <a-card :bordered="false" title="基本信息">
      <a-form ref="formRef" :model="form" :rules="rules" layout="horizontal" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
        <a-form-item label="优惠券名称" name="name" required>
          <a-input v-model:value="form.name" allow-clear />
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
          <a-input-number v-model:value="form.count" :min="1" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="已领取">
          <a-input-number v-model:value="form.receiveCount" :min="0" disabled class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="优惠金额" name="amount" required>
          <a-input-number v-model:value="form.amount" :min="0" :precision="2" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="最低消费金额" name="minAmount" required>
          <a-input-number v-model:value="form.minAmount" :min="0" :precision="2" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="每人限领数量" name="perLimit" required>
          <a-input-number v-model:value="form.perLimit" :min="1" class="w-full max-w-xs" />
        </a-form-item>
        <a-form-item label="使用范围" name="useType" required>
          <a-select v-model:value="form.useType" class="w-full max-w-xs">
            <a-select-option :value="0">全场</a-select-option>
            <a-select-option :value="1">指定分类</a-select-option>
            <a-select-option :value="2">指定商品</a-select-option>
          </a-select>
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
          <a-textarea v-model:value="form.note" :rows="3" allow-clear />
        </a-form-item>
      </a-form>

      <div class="mt-6 flex justify-center gap-3">
        <a-button type="primary" @click="handleSubmit">保存</a-button>
        <a-button @click="goBack">取消</a-button>
      </div>
    </a-card>

  </div>
</template>

<script setup>
// 编辑优惠券页
// 职责：加载详情 + 表单校验 + 保存更新
import { reactive, onMounted, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getCouponDetail, updateCoupon } from '@/api/admin/coupon'

const router = useRouter()
const route = useRoute()

// 表单引用（用于 validate）
const formRef = ref()
// 表单数据（字段与 UpdateSmsCouponReqVO 对齐）
const form = reactive({
  id: undefined,
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
  note: '',
  receiveCount: 0
})

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

const loadDetail = async () => {
  // 从路由 query 读取 id，并加载优惠券详情
  const queryId = route.query.id
  if (!queryId) {
    message.warning('优惠券ID不能为空')
    goBack()
    return
  }
  const id = Number(queryId)
  form.id = id
  const rsp = await getCouponDetail(id)
  if (!rsp?.success || !rsp?.data) {
    message.error(rsp?.message || '获取优惠券详情失败')
    return
  }
  Object.assign(form, rsp.data, {
    id, // 确保 id 不被覆盖丢失
    startTime: rsp.data.startTime || undefined,
    endTime: rsp.data.endTime || undefined,
    enableTime: rsp.data.enableTime || undefined
  })
}

onMounted(() => {
  // 首次进入页面自动加载
  loadDetail()
})

// 路由 query 变化时重新加载（组件被复用时 onMounted 不会再触发）
watch(() => route.query.id, (newId, oldId) => {
  if (newId && newId !== oldId) {
    loadDetail()
  }
})

const goBack = () => {
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

const handleSubmit = async () => {
  // 保存按钮入参校验：id 必须存在
  if (!form.id) {
    message.warning('优惠券ID不能为空')
    return
  }
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }

  // 校验有效期
  if (!form.startTime || !form.endTime) {
    message.warning('请选择有效期')
    return
  }

  // 提交入参与后端 VO 严格对齐
  const payload = {
    id: form.id,
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
  const rsp = await updateCoupon(form.id, payload)
  if (!rsp?.success) {
    message.error(rsp?.message || '保存失败')
    return
  }
  message.success('保存成功')
  goBack()
}
</script>
