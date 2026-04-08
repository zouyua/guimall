import axios from "axios";
import { getToken, getMemberToken, removeToken, removeMemberToken } from "./composables/cookie";
import { showMessage } from "./composables/util";
import router from "./router";

// 创建 Axios 实例
const instance = axios.create({
    baseURL: "/api", // API 基础 URL
    timeout: 7000, // 请求超时时间
})

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    const token = getToken();
    const memberToken = getMemberToken();

    // 优先使用管理员 Token (用于后台接口)
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
        console.log('添加管理员 Token');
    }
    // 如果没有管理员 Token，使用会员 Token (用于前台接口)
    else if (memberToken) {
        config.headers['MemberToken'] = 'Bearer ' + memberToken
        console.log('添加会员 Token');
    }

    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
});

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response.data
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么

    // 处理 token 失效（401 未授权）
    if (error.response && error.response.status === 401) {
        const token = getToken();
        const memberToken = getMemberToken();

        // 清除失效的 token
        if (token) {
            removeToken();
            showMessage('登录已过期，请重新登录', 'error');
            router.push('/admin/login');
        } else if (memberToken) {
            removeMemberToken();
            showMessage('登录已过期，请重新登录', 'error');
            router.push('/member/login');
        }

        return Promise.reject(error);
    }

    //若后台有错误提示就用提示文字，默认提示为’请求失败’
    let errorMsg = error.response?.data?.message || '请求失败'
    //显示错误消息
    showMessage(errorMsg, 'error')

    return Promise.reject(error)
})

// 暴露出去
export default instance;
