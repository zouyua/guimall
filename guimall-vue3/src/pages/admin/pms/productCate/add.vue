<template>
  <a-modal
    v-model:open="visible"
    title="新增分类"
    width="600px"
    @ok="handleSubmit"
    @cancel="visible=false"
  >

    <a-form :model="form">

      <a-form-item
        label="分类名称"
        :rules="[{ required:true,message:'请输入分类名称'}]"
      >
        <a-input v-model:value="form.name"/>
      </a-form-item>

      <a-form-item label="上级分类">
        <a-select v-model:value="form.parentId">

          <a-select-option :value="0">
            一级分类
          </a-select-option>

          <a-select-option
            v-for="item in categoryList"
            :key="item.id"
            :value="item.id"
          >
            {{ item.name }}
          </a-select-option>

        </a-select>
      </a-form-item>

      <a-form-item label="商品单位">
        <a-input v-model:value="form.productUnit"/>
      </a-form-item>

      <a-form-item label="排序">
        <a-input-number v-model:value="form.sort" :min="0"/>
      </a-form-item>

      <a-form-item label="导航栏显示">
        <a-switch
          v-model:checked="form.navStatus"
          :checked-value="1"
          :un-checked-value="0"
        />
      </a-form-item>

      <a-form-item label="是否显示">
        <a-switch
          v-model:checked="form.showStatus"
          :checked-value="1"
          :un-checked-value="0"
        />
      </a-form-item>

      <a-form-item label="关键词">
        <a-input v-model:value="form.keywords"/>
      </a-form-item>

      <a-form-item label="分类图标">
        <a-input v-model:value="form.icon"/>
      </a-form-item>

      <a-form-item label="分类描述">
        <a-textarea v-model:value="form.description"/>
      </a-form-item>

    </a-form>

  </a-modal>
</template>

<script setup>

import {ref,reactive} from 'vue'

const visible = ref(false)

const categoryList = ref([])

const form = reactive({

parentId:0,
name:'',
productUnit:'',
navStatus:1,
showStatus:1,
sort:0,
icon:'',
keywords:'',
description:''

})


const open = ()=>{
visible.value = true
}


const handleSubmit = ()=>{

console.log('新增分类',form)

visible.value=false

}

defineExpose({
open
})

</script>