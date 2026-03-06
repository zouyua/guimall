<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 左侧图片 -->
      <img class="left-img animate__animated animate__bounceInLeft animate__fast" :src="leftImg" />

      <!-- 右侧表单 -->
      <div class="form-wrapper animate__animated animate__bounceInRight animate__fast">
        <h1 class="title">hello !</h1>
        <p class="tips">欢迎来到 {{ title }}</p>

        <a-form
          ref="formRef"
          :model="form"
          :rules="rules"
          layout="vertical"
        >
          <!-- 用户名 -->
          <a-form-item name="username">
            <a-input v-model:value="form.username" placeholder="请输入用户名" >
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
          </a-form-item>

          <!-- 密码 -->
          <a-form-item name="password">
            <a-input-password
              v-model:value="form.password"
              placeholder="请输入密码"
            >
              <template #prefix>
                <LockOutlined />
              </template>
            </a-input-password>
          </a-form-item>

          <!-- 验证码 -->
          <a-form-item name="verificationCode">
            <div class="code-row">
              <a-input
                v-model:value="form.verificationCode"
                placeholder="验证码"
              >
                <template #prefix>
                  <SafetyOutlined />
                </template>
              </a-input>
              <img class="code-img" :src="codeUrl" @click="changeCode" />
            </div>
          </a-form-item>

          <!-- 登录按钮 -->
          <a-button
            type="primary"
            block
            size="large"
            :loading="loading"
            @click="onSubmit"
          >
            登录
          </a-button>

          <!-- 注册/忘记密码 -->
          <!-- <div class="extra">
            <router-link to="/register">
              <a-button type="primary">注册</a-button>
            </router-link>

            <router-link to="/password">
              <a-button type="link">忘记密码</a-button>
            </router-link>
          </div> -->
        </a-form>
      </div>
    </div>

    <footer>Mall-Shop © 2026 GuiMall</footer>
  </div>
</template>

<script setup >
import {
  UserOutlined,
  LockOutlined,
  SafetyOutlined,
} from '@ant-design/icons-vue'
import { login } from '@/api/admin/user'
import { ref, reactive,onMounted,onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import leftImg from '@/assets/left_img_1.png'
import { showMessage } from '@/composables/util';
import { setToken } from '@/composables/cookie';

const title = 'GuiMall'
const router = useRouter()
const route = useRoute()
//登录按钮加载
const loading = ref(false)

//定义响应式的表单对象
const form = reactive({
  username: '',
  password: '',
  verificationCode: '',
})
//表单引用
const formRef = ref(null)
//表单验证规则
const rules = {
  username: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur',
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
    { min: 6, message: '密码不能少于6位', trigger: 'blur' },
  ],
  verificationCode: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur',
    }
  ],
}

// 登录
const onSubmit = async () => {
  try {
    //先做表单校验
    await formRef.value.validate()

    //校验通过才会执行到这里
    //开始加载
    loading.value = true

    //调用登录接口
    const res = await login(form.username, form.password)

    //判断是否成功
    if (res.success === true) {
      //提示登录成功
      showMessage('登录成功', 'success')
      //跳转到后台首页
      router.push('/admin/index')
      //存储Token到Cookie中
      let token = res.data.token
      setToken(token)
    } else {
      //获取服务端返回的错误消息
      let message = res.message
      //提示消息
      showMessage(res.data.message, 'error')
    }

  } catch (err) {
    //校验失败会直接进 catch
    console.log('表单校验未通过', err)
    //message.error('服务器异常，请稍后重试')
  } finally {
    //结束加载
    loading.value = false
  }
}
// const onSubmit = () => {
//     console.log('登录')
//     login(form.username, form.password).then((res) => {
//         console.log(res)
//         // 判断是否成功
//         if (res.data.success == true) {
//             // 跳转到后台首页
//             router.push('/admin/index')
//         }
//     })
// }

const codeUrl = ref('https://www.oschina.net/action/user/captcha')

const changeCode = () => {
  codeUrl.value = `https://www.oschina.net/action/user/captcha?${Date.now()}`
}

// const handleLogin = async () => {
//   try {
//     await formRef.value.validate()
//     loading.value = true


//     //模拟登录
//     setTimeout(() => {
//       loading.value = false
//       message.success('登录成功')
//       router.push('/')
//     }, 1000)
//   } catch (err) {
//     console.log(err)
//   }
// }
//按回车键后，执行登录事件
function onKeyUp(e) {
  console.log(e)
  if (e.key === 'Enter') {
    onSubmit()
  }
}

//添加键盘监听
onMounted(() => {
  console.log('添加键盘监听')
  document.addEventListener('keyup', onKeyUp)
})

//移除键盘监听
onBeforeUnmount(() => {
  document.removeEventListener('keyup', onKeyUp)
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #79a8ff, #5b8dff);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 1000px;
  background: #eaf1ff;
  border-radius: 20px;
  display: flex;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0,0,0,.1);
}

.left-img {
  width: 420px;
}

.form-wrapper {
  flex: 1;
  padding-left: 60px;
}

.title {
  font-size: 48px;
  margin-bottom: 10px;
}

.tips {
  font-size: 24px;
  color: #666;
  margin-bottom: 30px;
}

.code-row {
  display: flex;
  gap: 10px;
}

.code-img {
  height: 40px;
  cursor: pointer;
}

/* .extra {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
} */

footer {
  margin-top: 20px;
  color: #333;
}
</style>
