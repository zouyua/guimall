/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3306
 Source Schema         : guimall

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 23/03/2026 15:14:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oms_cart_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_item`;
CREATE TABLE `oms_cart_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NULL DEFAULT NULL,
  `product_id` bigint NULL DEFAULT NULL,
  `product_sku_id` bigint NULL DEFAULT NULL,
  `quantity` int NULL DEFAULT NULL COMMENT '数量',
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_sub_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_sku_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_status` int NULL DEFAULT 0,
  `product_category_id` bigint NULL DEFAULT NULL,
  `product_sn` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_attr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '销售属性',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_cart_item
-- ----------------------------

-- ----------------------------
-- Table structure for oms_company_address
-- ----------------------------
DROP TABLE IF EXISTS `oms_company_address`;
CREATE TABLE `oms_company_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `send_status` int NULL DEFAULT 0,
  `receive_status` int NULL DEFAULT 0,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '发货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_company_address
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `member_id` bigint NOT NULL COMMENT '会员ID',
  `farmer_id` bigint NULL DEFAULT NULL COMMENT '农户ID',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `member_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际支付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费',
  `promotion_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销金额',
  `integration_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '积分抵扣金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '后台调整金额',
  `pay_type` int NULL DEFAULT NULL COMMENT '支付方式:0未支付1支付宝2微信',
  `source_type` int NULL DEFAULT 0 COMMENT '订单来源:0WEB1APP',
  `status` int NULL DEFAULT 0 COMMENT '订单状态:0待付款1待发货2已发货3已完成4已关闭',
  `order_type` int NULL DEFAULT 0 COMMENT '订单类型',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `auto_confirm_day` int NULL DEFAULT 7 COMMENT '自动确认天数',
  `integration` int NULL DEFAULT NULL COMMENT '赠送积分',
  `growth` int NULL DEFAULT NULL COMMENT '成长值',
  `promotion_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '促销信息',
  `bill_type` int NULL DEFAULT 0 COMMENT '发票类型',
  `bill_header` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bill_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `receiver_province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `receiver_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `receiver_region` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `receiver_detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` int NULL DEFAULT 0 COMMENT '确认收货',
  `delete_status` int NULL DEFAULT 0 COMMENT '删除状态',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `comment_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint NULL DEFAULT NULL,
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `product_quantity` int NULL DEFAULT NULL COMMENT '购买数量',
  `product_sku_id` bigint NULL DEFAULT NULL,
  `product_sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_category_id` bigint NULL DEFAULT NULL,
  `promotion_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `promotion_amount` decimal(10, 2) NULL DEFAULT NULL,
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL,
  `integration_amount` decimal(10, 2) NULL DEFAULT NULL,
  `real_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最终金额',
  `gift_integration` int NULL DEFAULT 0,
  `gift_growth` int NULL DEFAULT 0,
  `product_attr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品销售属性(JSON)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_operate_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_operate_history`;
CREATE TABLE `oms_order_operate_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL,
  `operate_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `order_status` int NULL DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作历史' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_operate_history
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_return_apply
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_apply`;
CREATE TABLE `oms_order_return_apply`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NULL DEFAULT NULL,
  `company_address_id` bigint NULL DEFAULT NULL,
  `product_id` bigint NULL DEFAULT NULL,
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `member_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_amount` decimal(10, 2) NULL DEFAULT NULL,
  `return_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `return_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_attr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_count` int NULL DEFAULT NULL,
  `product_price` decimal(10, 2) NULL DEFAULT NULL,
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `proof_pics` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退货申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_return_apply
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_return_reason
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_reason`;
CREATE TABLE `oms_order_return_reason`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货原因',
  `sort` int NULL DEFAULT 0,
  `status` int NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退货原因' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_return_reason
-- ----------------------------

-- ----------------------------
-- Table structure for pms_farmer
-- ----------------------------
DROP TABLE IF EXISTS `pms_farmer`;
CREATE TABLE `pms_farmer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '农户ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '农户姓名',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `id_card` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `farm_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '农场名称',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `main_product` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主要农产品',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '农户介绍',
  `status` int NULL DEFAULT 1 COMMENT '状态：0禁用1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '农户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_farmer
-- ----------------------------
INSERT INTO `pms_farmer` VALUES (2, '周大妈', '13000001111', '', 'httpS://example.com/avatar2.png', '周大妈生态果园', '广西壮族自治区', '桂林市', '平乐县', '', '有机水果', '专注有机水果种植10年', 1, '2026-03-15 22:53:17', '2026-03-15 22:53:17');
INSERT INTO `pms_farmer` VALUES (4, '张大爷', '13812345678', '430101199001011234', 'https://img.example.com/avatar/z.png', '张大爷生态农庄', '广西壮族自治区', '桂林市', '荔浦市', '', '有机蔬菜', '专注有机蔬菜种植 10 年。', 1, '2026-03-15 23:33:48', '2026-03-15 23:33:48');
INSERT INTO `pms_farmer` VALUES (5, '桂林临桂区-周大姐', '13800138001', '450300199001011234', 'https://static.example.com/avatar/farmer-zhou.png', '临桂助农合作社', '广西壮族自治区', '桂林市', '临桂区', '两江镇XX村 18号', '砂糖橘,罗汉果,桂林米粉原料', '自产自销，支持溯源。', 1, '2026-03-18 22:50:41', '2026-03-18 22:50:41');
INSERT INTO `pms_farmer` VALUES (6, '阳朔兴坪-蒋大哥', '13800138006', '450321198812123456', 'https://static.example.com/avatar/farmer-jiang.png', '兴坪生态果蔬基地', '广西壮族自治区', '桂林市', '阳朔县', '兴坪镇朝板山村 9 号', '金桔,马蹄,时令蔬菜', '主打阳朔金桔和冬季马蹄，支持订单溯源。', 1, '2026-03-20 10:01:00', '2026-03-20 10:01:00');
INSERT INTO `pms_farmer` VALUES (7, '永福罗汉果-唐大姐', '13800138007', '450326199203155678', 'https://static.example.com/avatar/farmer-tang.png', '永福罗汉果合作社', '广西壮族自治区', '桂林市', '永福县', '苏桥镇田间路 28 号', '罗汉果,果干,养生原料', '专注永福罗汉果标准化种植与加工。', 1, '2026-03-20 10:05:00', '2026-03-20 10:05:00');

-- ----------------------------
-- Table structure for pms_farmer_product_relation
-- ----------------------------
DROP TABLE IF EXISTS `pms_farmer_product_relation`;
CREATE TABLE `pms_farmer_product_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `farmer_id` bigint NOT NULL COMMENT '农户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `origin_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产地',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_farmer_product_product`(`product_id` ASC) USING BTREE,
  INDEX `fk_farmer_product_farmer`(`farmer_id` ASC) USING BTREE,
  CONSTRAINT `fk_farmer_product_farmer` FOREIGN KEY (`farmer_id`) REFERENCES `pms_farmer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_farmer_product_product` FOREIGN KEY (`product_id`) REFERENCES `pms_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '农户商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_farmer_product_relation
-- ----------------------------

-- ----------------------------
-- Table structure for pms_product
-- ----------------------------
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '商品分类ID',
  `product_attribute_category_id` bigint NULL DEFAULT NULL COMMENT '商品属性分类ID',
  `farmer_id` bigint NULL DEFAULT NULL COMMENT '农户ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '副标题',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品货号',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图',
  `album_pics` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品相册图片(逗号分割)',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销价格',
  `stock` int NULL DEFAULT 0 COMMENT '库存',
  `low_stock` int NULL DEFAULT NULL COMMENT '库存预警值',
  `unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位(斤/箱/袋)',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量(克)',
  `publish_status` int NULL DEFAULT 0 COMMENT '上架状态:0下架1上架',
  `verify_status` int NULL DEFAULT 0 COMMENT '审核状态:0未审核1通过',
  `delete_status` int NULL DEFAULT 0 COMMENT '删除状态：0未删除；1已删除',
  `sale` int NULL DEFAULT 0 COMMENT '销量',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键词',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `detail_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情标题',
  `detail_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详情描述',
  `detail_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '产品详情网页内容',
  `promotion_type` int NULL DEFAULT 0 COMMENT '促销类型：0没有促销使用原价;1使用满减价格；2使用会员价；',
  `promotion_start_time` datetime NULL DEFAULT NULL,
  `promotion_end_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product
-- ----------------------------
INSERT INTO `pms_product` VALUES (9, 5, 1, 4, '精品荔浦芋头', '产自张大爷生态农庄', 'SP202603150001', 'https://img.example.com/product/lipy.png', 'https://img.example.com/product/lipy.png,https://img.example.com/product/lipy2.png', '香甜软糯的荔浦芋头。', 19.90, 29.90, NULL, 100, NULL, '斤', 500.00, 0, 0, 0, 0, 1, '芋头,蔬菜', '首单限购 5 斤', '荔浦芋头详情', '菜园直供，现摘现发。', '<p>菜园直供，现摘现发。</p>', 0, NULL, NULL, '2026-03-16 00:41:08', '2026-03-18 23:13:28');
INSERT INTO `pms_product` VALUES (10, 8, 3, 5, '桂林砂糖橘（现摘现发）', '甜度高，果皮薄，助农直发', 'GLSTJ-20260318-001', 'https://static.example.com/pic/stj-main.jpg', 'https://static.example.com/pic/stj-1.jpg,https://static.example.com/pic/stj-2.jpg', '来自桂林临桂区果园，新鲜采摘。', 39.90, 49.90, NULL, 500, NULL, '斤', 2500.00, 1, 0, 0, 0, 0, '桂林,砂糖橘,助农', '默认顺丰/中通，偏远地区补差价', '桂林砂糖橘详情', '香甜多汁，支持溯源', '<p>桂林临桂区果园直发，坏果包赔。</p>', 0, NULL, NULL, '2026-03-18 23:09:55', '2026-03-18 23:13:39');
INSERT INTO `pms_product` VALUES (11, 10, 4, 6, '阳朔金桔礼盒 5 斤装', '桂林阳朔核心产区，酸甜平衡', 'GLYSJJ-20260320-001', 'https://static.example.com/pic/ys-jinju-main.jpg', 'https://static.example.com/pic/ys-jinju-1.jpg,https://static.example.com/pic/ys-jinju-2.jpg', '果肉饱满，皮薄清香，适合鲜食和泡茶。', 56.80, 69.90, NULL, 360, NULL, '盒', 2500.00, 1, 0, 0, 0, 1, '桂林,阳朔,金桔,助农', '礼盒包装，支持企业团购', '阳朔金桔详情', '阳朔直采，新鲜发货', '<p>阳朔兴坪镇合作果园直供，支持扫码查看产地信息。</p>', 0, NULL, NULL, '2026-03-20 10:10:00', '2026-03-20 10:10:00');
INSERT INTO `pms_product` VALUES (12, 11, 5, 7, '永福罗汉果 12 枚装', '广西桂林永福县道地罗汉果', 'GLYFLHG-20260320-001', 'https://static.example.com/pic/yf-lhg-main.jpg', 'https://static.example.com/pic/yf-lhg-1.jpg,https://static.example.com/pic/yf-lhg-2.jpg', '清甜回甘，适合泡水饮用，家庭常备。', 68.00, 88.00, NULL, 280, NULL, '盒', 1500.00, 1, 0, 0, 0, 2, '桂林,永福,罗汉果,养生', '精选大果，坏果包赔', '永福罗汉果详情', '永福原产地，助农直供', '<p>来自桂林永福罗汉果标准化种植基地。</p>', 0, NULL, NULL, '2026-03-20 10:12:00', '2026-03-20 10:12:00');
INSERT INTO `pms_product` VALUES (13, 12, 6, 4, '荔浦马蹄（净重 3 斤）', '脆甜爽口，桂林荔浦时令蔬果', 'GLLPMT-20260320-001', 'https://static.example.com/pic/lipu-mati-main.jpg', 'https://static.example.com/pic/lipu-mati-1.jpg,https://static.example.com/pic/lipu-mati-2.jpg', '当季采收，肉质洁白，适合煲汤与鲜食。', 29.90, 39.90, NULL, 420, NULL, '袋', 1500.00, 1, 0, 0, 0, 3, '桂林,荔浦,马蹄,农家', '顺丰冷链可选', '荔浦马蹄详情', '脆甜多汁，现采现发', '<p>桂林荔浦产区当季新鲜马蹄，支持溯源。</p>', 0, NULL, NULL, '2026-03-20 10:15:00', '2026-03-20 10:15:00');

-- ----------------------------
-- Table structure for pms_product_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute`;
CREATE TABLE `pms_product_attribute`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_attribute_category_id` bigint NULL DEFAULT NULL COMMENT '属性分类ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名称',
  `select_type` int NULL DEFAULT 0 COMMENT '选择类型:0唯一1单选2多选',
  `input_type` int NULL DEFAULT 0 COMMENT '输入类型:0手动1列表',
  `input_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可选值列表',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `filter_type` int NULL DEFAULT 0 COMMENT '筛选类型',
  `search_type` int NULL DEFAULT 0 COMMENT '搜索类型',
  `related_status` int NULL DEFAULT 0 COMMENT '是否关联；0->不关联；1->关联',
  `hand_add_status` int NULL DEFAULT 0 COMMENT '是否支持手动新增',
  `type` int NULL DEFAULT 0 COMMENT '属性的类型；0规格1参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_attribute
-- ----------------------------
INSERT INTO `pms_product_attribute` VALUES (1, 3, '净含量', 1, 1, '5斤,10斤,20斤', 0, 1, 0, 0, 1, 0);
INSERT INTO `pms_product_attribute` VALUES (2, 3, '产地', 0, 0, '', 1, 0, 1, 0, 1, 1);
INSERT INTO `pms_product_attribute` VALUES (3, 4, '果径', 1, 1, '小果,中果,大果', 0, 1, 0, 0, 1, 0);
INSERT INTO `pms_product_attribute` VALUES (4, 4, '甜度等级', 1, 1, 'A 级,B 级', 1, 1, 0, 0, 1, 0);
INSERT INTO `pms_product_attribute` VALUES (5, 5, '果型', 1, 1, '中果,大果', 0, 1, 0, 0, 1, 0);
INSERT INTO `pms_product_attribute` VALUES (6, 5, '建议冲泡次数', 1, 1, '3 次,5 次,8 次', 1, 0, 1, 0, 1, 1);
INSERT INTO `pms_product_attribute` VALUES (7, 6, '口感', 1, 1, '脆甜,清甜', 0, 1, 0, 0, 1, 0);
INSERT INTO `pms_product_attribute` VALUES (8, 6, '推荐吃法', 1, 1, '鲜食,煲汤,炖煮', 1, 0, 1, 0, 1, 1);

-- ----------------------------
-- Table structure for pms_product_attribute_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_category`;
CREATE TABLE `pms_product_attribute_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性分类名称',
  `attribute_count` int NULL DEFAULT 0 COMMENT '属性数量',
  `param_count` int NULL DEFAULT 0 COMMENT '参数数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_attribute_category
-- ----------------------------
INSERT INTO `pms_product_attribute_category` VALUES (1, '蔬菜属性', 0, 0);
INSERT INTO `pms_product_attribute_category` VALUES (2, '水果属性', 0, 0);
INSERT INTO `pms_product_attribute_category` VALUES (3, '水果通用属性', 1, 1);
INSERT INTO `pms_product_attribute_category` VALUES (4, '桂林柑橘类属性', 2, 1);
INSERT INTO `pms_product_attribute_category` VALUES (5, '桂林罗汉果属性', 2, 1);
INSERT INTO `pms_product_attribute_category` VALUES (6, '桂林根茎类属性', 2, 1);

-- ----------------------------
-- Table structure for pms_product_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_value`;
CREATE TABLE `pms_product_attribute_value`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL,
  `product_attribute_id` bigint NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性值；手动添加规格或参数的值，参数单值，规格有多个时以逗号隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_attribute_value
-- ----------------------------

-- ----------------------------
-- Table structure for pms_product_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_category`;
CREATE TABLE `pms_product_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID：0表示一级分类',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称',
  `level` int NULL DEFAULT 0 COMMENT '分类级别：0->1级；1->2级',
  `product_count` int NULL DEFAULT 0 COMMENT '商品数量',
  `product_unit` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `nav_status` int NULL DEFAULT 1 COMMENT '是否显示在导航栏：0->不显示；1->显示',
  `show_status` int NULL DEFAULT 1 COMMENT '是否显示状态：0->不显示；1->显示',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_category
-- ----------------------------
INSERT INTO `pms_product_category` VALUES (1, 0, '水果', 0, 0, '斤', 1, 1, 1, 'https://img.example.com/icon/fruit.png', '水果,新鲜水果', '各类新鲜水果', '2026-03-15 22:58:20');
INSERT INTO `pms_product_category` VALUES (2, 1, '恭城柿子', 1, 0, '斤', 1, 1, 2, 'https://img.example.com/icon/shizi.png', '柿子,恭城柿子', '精品柿子', '2026-03-15 22:59:58');
INSERT INTO `pms_product_category` VALUES (5, 0, '蔬菜', 0, 0, '斤', 1, 1, 1, 'https://img.example.com/icon/fruit.png', '蔬菜,新鲜蔬菜', '各类新鲜蔬菜', '2026-03-15 23:14:19');
INSERT INTO `pms_product_category` VALUES (6, 5, '芋头', 1, 0, '斤', 1, 1, 1, 'https://img.example.com/icon/lipy.png', '芋头, 芋头类', '商品描述芋头', '2026-03-15 23:41:16');
INSERT INTO `pms_product_category` VALUES (7, 5, '芋头', 1, 0, '斤', 1, 1, 1, 'https://img.example.com/icon/lipy.png', '芋头, 芋头类', '商品描述芋头', '2026-03-15 23:53:22');
INSERT INTO `pms_product_category` VALUES (8, 0, '广西特色水果', 0, 0, '件', 1, 1, 0, 'https://static.example.com/icon/gx.png', '广西,桂林,水果', '广西桂林助农电商平台-特色水果分类', '2026-03-18 22:55:03');
INSERT INTO `pms_product_category` VALUES (9, 8, '桂林砂糖橘', 1, 0, '斤', 1, 1, 1, 'https://static.example.com/icon/fruit.png', '砂糖橘,沃柑', '桂林本地水果', '2026-03-18 22:58:19');
INSERT INTO `pms_product_category` VALUES (10, 8, '阳朔金桔', 1, 0, '盒', 1, 1, 2, 'https://static.example.com/icon/jinju.png', '阳朔,金桔,礼盒', '桂林阳朔代表性金桔品类', '2026-03-20 09:58:00');
INSERT INTO `pms_product_category` VALUES (11, 8, '永福罗汉果', 1, 0, '盒', 1, 1, 3, 'https://static.example.com/icon/luohanguo.png', '永福,罗汉果,养生', '桂林永福道地罗汉果品类', '2026-03-20 09:59:00');
INSERT INTO `pms_product_category` VALUES (12, 5, '荔浦马蹄', 1, 0, '袋', 1, 1, 4, 'https://static.example.com/icon/mati.png', '荔浦,马蹄', '桂林荔浦特色根茎类农产品', '2026-03-20 10:00:00');
INSERT INTO `pms_product_category` VALUES (13, 5, '荔浦芋头精选', 1, 0, '斤', 1, 1, 5, 'https://static.example.com/icon/lipu-yutou.png', '荔浦,芋头', '桂林荔浦核心地标食材', '2026-03-20 10:00:30');

-- ----------------------------
-- Table structure for pms_product_full_reduction
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_full_reduction`;
CREATE TABLE `pms_product_full_reduction`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `full_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '满多少',
  `reduce_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '减多少',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_full_reduction_product`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_full_reduction_product` FOREIGN KEY (`product_id`) REFERENCES `pms_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品满减（只针对商品）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_full_reduction
-- ----------------------------
INSERT INTO `pms_product_full_reduction` VALUES (3, 10, 99.00, 10.00);
INSERT INTO `pms_product_full_reduction` VALUES (4, 10, 199.00, 25.00);

-- ----------------------------
-- Table structure for pms_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_stock`;
CREATE TABLE `pms_sku_stock`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL,
  `sku_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单品促销价格',
  `stock` int NULL DEFAULT 0 COMMENT '库存',
  `low_stock` int NULL DEFAULT 0 COMMENT '预警库存',
  `lock_stock` int NULL DEFAULT 0 COMMENT '锁定库存',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '展示图片',
  `sale` int NULL DEFAULT 0,
  `sp_data` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '销售属性(JSON)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sku_stock_product`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_sku_stock_product` FOREIGN KEY (`product_id`) REFERENCES `pms_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SKU库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_sku_stock
-- ----------------------------
INSERT INTO `pms_sku_stock` VALUES (6, 10, 'GLSTJ-5J', 39.90, 35.90, 200, 10, 0, 'https://static.example.com/pic/stj-5j.jpg', 0, '[{\"key\":\"净含量\",\"value\":\"5斤\"}]');
INSERT INTO `pms_sku_stock` VALUES (7, 10, 'GLSTJ-10J', 69.90, 59.90, 120, 10, 0, 'https://static.example.com/pic/stj-10j.jpg', 0, '[{\"key\":\"净含量\",\"value\":\"10斤\"}]');
INSERT INTO `pms_sku_stock` (`product_id`, `sku_code`, `price`, `promotion_price`, `stock`, `low_stock`, `lock_stock`, `pic`, `sale`, `sp_data`) VALUES
(9, 'SKU-9-A', 19.90, NULL, 300, 10, 0, 'https://img.example.com/product/lipy.png', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(9, 'SKU-9-B', 21.89, NULL, 180, 10, 0, 'https://img.example.com/product/lipy.png', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(14, 'SKU-14-A', 26.80, NULL, 250, 10, 0, 'https://static.example.com/pic/mifen-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(14, 'SKU-14-B', 29.48, NULL, 160, 10, 0, 'https://static.example.com/pic/mifen-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(15, 'SKU-15-A', 79.90, NULL, 120, 10, 0, 'https://static.example.com/pic/teaoil-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(15, 'SKU-15-B', 87.89, NULL, 80, 10, 0, 'https://static.example.com/pic/teaoil-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(16, 'SKU-16-A', 18.90, NULL, 220, 10, 0, 'https://static.example.com/pic/chili-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(16, 'SKU-16-B', 20.79, NULL, 140, 10, 0, 'https://static.example.com/pic/chili-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(17, 'SKU-17-A', 42.80, NULL, 190, 10, 0, 'https://static.example.com/pic/gongcheng-shizi-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(17, 'SKU-17-B', 47.08, NULL, 110, 10, 0, 'https://static.example.com/pic/gongcheng-shizi-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(18, 'SKU-18-A', 23.90, NULL, 210, 10, 0, 'https://static.example.com/pic/tomato-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(18, 'SKU-18-B', 26.29, NULL, 130, 10, 0, 'https://static.example.com/pic/tomato-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]'),
(19, 'SKU-19-A', 16.90, NULL, 240, 10, 0, 'https://static.example.com/pic/douchi-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"A\"}]'),
(19, 'SKU-19-B', 18.59, NULL, 150, 10, 0, 'https://static.example.com/pic/douchi-main.jpg', 0, '[{\"key\":\"默认\",\"value\":\"B\"}]');

-- ----------------------------
-- Table structure for sms_coupon
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT 0 COMMENT '优惠券类型：0全场赠券1会员赠券2购物赠券3注册赠券',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `platform` int NULL DEFAULT 2 COMMENT '使用平台：0全部1移动端2WEB',
  `count` int NULL DEFAULT NULL COMMENT '总数量',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `per_limit` int NULL DEFAULT 1 COMMENT '每人限领数量',
  `min_point` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '最低消费金额',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `use_type` int NULL DEFAULT 0 COMMENT '使用范围：0全场1指定分类2指定商品',
  `note` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `publish_count` int NULL DEFAULT 0 COMMENT '发行数量',
  `use_count` int NULL DEFAULT 0 COMMENT '已使用数量',
  `receive_count` int NULL DEFAULT 0 COMMENT '领取数量',
  `enable_time` datetime NULL DEFAULT NULL COMMENT '领取开始时间',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠码',
  `member_level` int NULL DEFAULT 0 COMMENT '会员等级限制',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon
-- ----------------------------
INSERT INTO `sms_coupon` (`id`, `type`, `name`, `platform`, `count`, `amount`, `per_limit`, `min_point`, `start_time`, `end_time`, `use_type`, `note`, `publish_count`, `use_count`, `receive_count`, `enable_time`, `code`, `member_level`, `create_time`, `update_time`) VALUES
(1, 0, '桂林助农满99减15', 0, 300, 15.00, 1, 99.00, '2026-03-01 00:00:00', '2026-06-30 23:59:59', 0, '桂林特色农产品专区可用', 300, 26, 38, NULL, NULL, 0, '2026-03-20 09:10:00', '2026-03-20 09:10:00'),
(2, 0, '水果专场满59减8', 0, 500, 8.00, 1, 59.00, '2026-03-10 00:00:00', '2026-07-10 23:59:59', 0, '水果生鲜限时活动', 500, 40, 65, NULL, NULL, 0, '2026-03-20 09:20:00', '2026-03-20 09:20:00'),
(3, 0, '蔬菜专场满39减5', 0, 400, 5.00, 2, 39.00, '2026-03-15 00:00:00', '2026-08-15 23:59:59', 0, '蔬菜商品可叠加店铺活动', 400, 18, 33, NULL, NULL, 0, '2026-03-20 09:30:00', '2026-03-20 09:30:00');

-- ----------------------------
-- Table structure for sms_coupon_history
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `coupon_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券码',
  `member_nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会员昵称',
  `get_type` int NULL DEFAULT 1 COMMENT '获取方式：0后台赠送1用户领取',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `use_status` int NULL DEFAULT 0 COMMENT '使用状态：0未使用1已使用2已过期',
  `use_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券领取记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_history
-- ----------------------------
INSERT INTO `sms_coupon_history` (`id`, `coupon_id`, `member_id`, `coupon_code`, `member_nickname`, `get_type`, `create_time`, `use_status`, `use_time`, `order_id`, `order_sn`) VALUES
(1, 1, 1001, NULL, '桂林用户A', 1, '2026-03-21 10:21:00', 0, NULL, NULL, NULL),
(2, 1, 1002, NULL, '桂林用户B', 1, '2026-03-21 13:33:00', 1, '2026-03-22 11:20:00', 9001, 'GL202603220001'),
(3, 2, 1003, NULL, '阳朔用户C', 1, '2026-03-22 08:15:00', 0, NULL, NULL, NULL),
(4, 3, 1004, NULL, '临桂用户D', 1, '2026-03-22 09:10:00', 2, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sms_coupon_product_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_product_category_relation`;
CREATE TABLE `sms_coupon_product_category_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint NULL DEFAULT NULL,
  `product_category_id` bigint NULL DEFAULT NULL,
  `product_category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券分类关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_product_category_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupon_product_relation
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_product_relation`;
CREATE TABLE `sms_coupon_product_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint NULL DEFAULT NULL,
  `product_id` bigint NULL DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_sn` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券商品关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_product_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sms_home_advertise
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_advertise`;
CREATE TABLE `sms_home_advertise`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告名称',
  `type` int NULL DEFAULT 0 COMMENT '广告位置：0WEB首页轮播1APP首页轮播',
  `pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告图片',
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1 COMMENT '状态：0下线1上线',
  `click_count` int NULL DEFAULT 0 COMMENT '点击量',
  `order_count` int NULL DEFAULT 0 COMMENT '下单量',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '首页广告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_advertise
-- ----------------------------
INSERT INTO `sms_home_advertise` (`id`, `name`, `type`, `pic`, `start_time`, `end_time`, `status`, `click_count`, `order_count`, `url`, `note`, `sort`, `create_time`) VALUES
(1, '桂林砂糖橘产地直供', 0, 'https://static.example.com/pic/banner-guilin-orange.jpg', '2026-03-01 00:00:00', '2026-12-31 23:59:59', 1, 268, 31, '', '首页主推桂林砂糖橘', 1, '2026-03-20 11:00:00'),
(2, '荔浦芋头新鲜上架', 0, 'https://static.example.com/pic/banner-lipu-yam.jpg', '2026-03-01 00:00:00', '2026-12-31 23:59:59', 1, 182, 19, '', '荔浦芋头助农专场', 2, '2026-03-20 11:02:00'),
(3, '永福罗汉果健康季', 0, 'https://static.example.com/pic/banner-yongfu-lhg.jpg', '2026-03-01 00:00:00', '2026-12-31 23:59:59', 1, 143, 12, '', '罗汉果冲饮专区', 3, '2026-03-20 11:05:00');

-- ----------------------------
-- Table structure for sms_home_new_product
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_new_product`;
CREATE TABLE `sms_home_new_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recommend_status` int NULL DEFAULT 1 COMMENT '推荐状态',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新品推荐' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_new_product
-- ----------------------------
INSERT INTO `sms_home_new_product` (`id`, `product_id`, `product_name`, `recommend_status`, `sort`) VALUES
(1, 11, '阳朔金桔礼盒 5 斤装', 1, 1),
(2, 12, '永福罗汉果 12 枚装', 1, 2),
(3, 13, '荔浦马蹄（净重 3 斤）', 1, 3);

-- ----------------------------
-- Table structure for sms_home_recommend_product
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_recommend_product`;
CREATE TABLE `sms_home_recommend_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recommend_status` int NULL DEFAULT 1 COMMENT '推荐状态',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '推荐商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_recommend_product
-- ----------------------------
INSERT INTO `sms_home_recommend_product` (`id`, `product_id`, `product_name`, `recommend_status`, `sort`) VALUES
(1, 10, '桂林砂糖橘（现摘现发）', 1, 1),
(2, 9, '精品荔浦芋头', 1, 2),
(3, 11, '阳朔金桔礼盒 5 斤装', 1, 3);

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级菜单',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单名称',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由名称',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由路径',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `hidden` int NULL DEFAULT 0 COMMENT '是否隐藏',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------

-- ----------------------------
-- Table structure for t_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu_relation`;
CREATE TABLE `t_role_menu_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_menu_relation
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '$2b$10$/dyyv//oYYo0uzdFO1.kIeaCdBKJw9IrkJ1jJQI4vWVEP23R7KsF.', NULL, '2026-01-28 21:21:33', '2026-01-28 21:21:33', 0);
INSERT INTO `t_user` VALUES (2, 'test', '$2a$10$X50/Vmo/vg6CMLeLrcPJNOgGHnANGBHQ0neaRRz32wSTSwvrtlRx6', NULL, '2026-02-07 16:45:36', '2026-03-08 03:57:44', 0);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `role` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 'admin', 'ROLE_ADMIN', '2026-01-28 21:21:33');
INSERT INTO `t_user_role` VALUES (2, 'test', 'ROLE_VISITOR', '2026-02-07 16:07:55');

-- ----------------------------
-- Table structure for trace_origin
-- ----------------------------
DROP TABLE IF EXISTS `trace_origin`;
CREATE TABLE `trace_origin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产地ID',
  `origin_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产地名称',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '产地介绍',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '农产品产地表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trace_origin
-- ----------------------------
INSERT INTO `trace_origin` VALUES (1, '桂林临桂区两江镇', '广西壮族自治区', '桂林市', '临桂区', '土壤疏松，昼夜温差适中，适合柑橘类生长。', '2026-03-20 10:20:00');
INSERT INTO `trace_origin` VALUES (2, '桂林阳朔县兴坪镇', '广西壮族自治区', '桂林市', '阳朔县', '喀斯特地貌与漓江气候共同形成优质金桔产区。', '2026-03-20 10:21:00');
INSERT INTO `trace_origin` VALUES (3, '桂林永福县苏桥镇', '广西壮族自治区', '桂林市', '永福县', '罗汉果传统核心产区，种植历史悠久。', '2026-03-20 10:22:00');
INSERT INTO `trace_origin` VALUES (4, '桂林荔浦市青山镇', '广西壮族自治区', '桂林市', '荔浦市', '地理标志农产品集中产区，根茎类品质稳定。', '2026-03-20 10:23:00');

-- ----------------------------
-- Table structure for trace_product_origin
-- ----------------------------
DROP TABLE IF EXISTS `trace_product_origin`;
CREATE TABLE `trace_product_origin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `origin_id` bigint NOT NULL COMMENT '产地ID',
  `farmer_id` bigint NULL DEFAULT NULL COMMENT '农户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品产地关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trace_product_origin
-- ----------------------------
INSERT INTO `trace_product_origin` VALUES (1, 10, 1, 5, '2026-03-20 10:25:00');
INSERT INTO `trace_product_origin` VALUES (2, 11, 2, 6, '2026-03-20 10:26:00');
INSERT INTO `trace_product_origin` VALUES (3, 12, 3, 7, '2026-03-20 10:27:00');
INSERT INTO `trace_product_origin` VALUES (4, 13, 4, 4, '2026-03-20 10:28:00');

-- ----------------------------
-- Table structure for trace_qrcode
-- ----------------------------
DROP TABLE IF EXISTS `trace_qrcode`;
CREATE TABLE `trace_qrcode`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `qrcode_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二维码图片',
  `trace_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '溯源页面地址',
  `scan_count` int NULL DEFAULT 0 COMMENT '扫码次数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '溯源二维码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trace_qrcode
-- ----------------------------

-- ----------------------------
-- Table structure for trace_record
-- ----------------------------
DROP TABLE IF EXISTS `trace_record`;
CREATE TABLE `trace_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `farmer_id` bigint NULL DEFAULT NULL COMMENT '农户ID',
  `record_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录类型(播种/施肥/采摘)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '溯源内容',
  `record_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  `pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '农产品溯源记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trace_record
-- ----------------------------

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `member_level_id` bigint NULL DEFAULT NULL COMMENT '会员等级ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int NULL DEFAULT 1 COMMENT '账户状态：0禁用1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` int NULL DEFAULT 0 COMMENT '性别：0未知1男2女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `job` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职业',
  `personalized_signature` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个性签名',
  `source_type` int NULL DEFAULT 0 COMMENT '用户来源：0WEB1APP',
  `integration` int NULL DEFAULT 0 COMMENT '积分',
  `growth` int NULL DEFAULT 0 COMMENT '成长值',
  `luckey_count` int NULL DEFAULT 0 COMMENT '剩余抽奖次数',
  `history_integration` int NULL DEFAULT 0 COMMENT '历史积分',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_member
-- ----------------------------

-- ----------------------------
-- Table structure for ums_member_receive_address
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_receive_address`;
CREATE TABLE `ums_member_receive_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `default_status` int NULL DEFAULT 0 COMMENT '是否默认地址',
  `post_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮编',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员收货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_member_receive_address
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
