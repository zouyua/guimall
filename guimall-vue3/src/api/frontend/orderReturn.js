import axios from '@/axios'

/**
 * 查询退货原因列表
 */
export function getReturnReasons() {
  return axios.get('/order/return/reasons')
}

/**
 * 创建退货申请
 */
export function createReturnApply(data) {
  return axios.post('/order/return/apply', data)
}

/**
 * 取消订单
 */
export function cancelOrder(data) {
  return axios.post('/order/return/cancel', data)
}
