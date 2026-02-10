//消息提示
import { message } from 'ant-design-vue'
import nProgress from 'nprogress'

export function showMessage(
  content = '提示内容',
  type = 'success'
) {
  switch (type) {
    case 'success':
      message.success(content)
      break
    case 'error':
      message.error(content)
      break
    case 'warning':
      message.warning(content)
      break
    case 'info':
      message.info(content)
      break
    default:
      message.info(content)
  }
}

//显示页面加载 Loading
export function showPageLoading() {
  nProgress.start()
}

//隐藏页面加载 Loading
export function hidePageLoading() {
  nProgress.done()
}
