//消息提示
import { message, Modal } from 'ant-design-vue'
import nProgress from 'nprogress'
import { ExclamationCircleFilled } from '@ant-design/icons-vue';
import { h } from 'vue'

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

const { confirm } = Modal;
//消息弹窗框showConfirm
//确认弹窗
export function showConfirm(content = '确定操作吗？', onOk) {

  confirm({
    title: '提示',
    icon: h(ExclamationCircleFilled),
    content: content,

    okText: '确定',
    cancelText: '取消',

    onOk() {
      if (onOk) {
        onOk()
      }
    },

    onCancel() {
      console.log('取消')
    }
  })
};