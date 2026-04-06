import axios from "@/axios";

// 添加商品到购物车
export function addCartItem(data) {
  return axios.post("/cart/add", data);
}

// 获取购物车列表
export function getCartList(memberId) {
  return axios.get("/cart/list", { params: { memberId } });
}

// 修改购物车数量
export function updateCartQuantity(memberId, id, quantity) {
  return axios.get("/cart/update/quantity", { params: { memberId, id, quantity } });
}

// 删除购物车项
export function deleteCartItems(data) {
  return axios.post("/cart/delete", data);
}

// 清空购物车
export function clearCart(data) {
  return axios.post("/cart/clear", data);
}
