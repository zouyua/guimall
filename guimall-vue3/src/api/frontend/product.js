import axios from "@/axios";

/**
 * 前台首页模块接口
 */

// 获取首页轮播图 (type: 0-WEB, 1-APP)
export function getHomeAdvertises(type = 0) {
  return axios.get("/home/advertises", { params: { type } });
}

/**
 * 前台商品模块接口
 */

// 获取商品分类树
export function getCategoryTree() {
  return axios.get("/pms/product/category/tree");
}

// 商品列表分页查询
export function getProductList(data) {
  return axios.post("/pms/product/list", data);
}

// 获取新品推荐列表
export function getNewProducts() {
  return axios.get("/home/new-products");
}

// 获取人气推荐列表
export function getRecommendProducts() {
  return axios.get("/home/recommend-products");
}

// 获取商品详情
export function getProductDetail(id) {
  return axios.get(`/pms/product/${id}`);
}

/**
 * 前台溯源模块接口
 */

// 获取溯源详情
export function getTraceDetail(productId) {
  return axios.get(`/trace/${productId}`);
}

// 获取助农商品列表
export function getAidAgricultureProducts(data) {
  return axios.post('/pms/product/list', { ...data, isAidAgriculture: 1 })
}

// 获取签约帮扶农户列表
export function getSupportFarmers() {
  return axios.get('/support/farmers')
}

/**
 * 购物车相关接口
 */

// 添加商品到购物车
export function addCartItem(data) {
  return axios.post("/cart/add", data);
}

// 获取购物车列表
export function getCartList() {
  return axios.get("/cart/list");
}

// 修改购物车数量
export function updateCartQuantity(data) {
  return axios.post("/cart/update/quantity", data);
}

// 删除购物车项
export function deleteCartItem(id) {
  return axios.post("/cart/delete", { id });
}
