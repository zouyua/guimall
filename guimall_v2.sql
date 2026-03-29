/*
 Guimall 数据库重新设计版 v2

 优化说明：
   1. 统一软删除字段为 is_deleted tinyint(1) NOT NULL DEFAULT 0
   2. 统一布尔/状态字段为 tinyint(1) NOT NULL DEFAULT，语义更清晰
   3. 删除冗余表 pms_farmer_product_relation（pms_product.farmer_id 已满足一对多）
   4. 删除 pms_product_category.product_count（易产生脏数据，改为实时 COUNT 查询）
   5. 合并 pms_product_category 的 nav_status/show_status 为单一 status
   6. pms_product 精简字段：移除无用的 detail_title，original_price 改名为 market_price 语义更清晰
   7. sms_coupon.min_point 改名为 min_amount，消除歧义（point 通常指积分）
   8. t_user 表统一重命名为 ums_admin，与模块命名规范保持一致
   9. 全表补齐 create_time / update_time（ON UPDATE CURRENT_TIMESTAMP）
  10. 补充关键业务字段索引（分类/商品/农户查询高频字段）
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ================================================================
-- PMS 商品管理模块
-- ================================================================

-- ----------------------------
-- pms_product_category 商品分类
-- 改动：移除 product_count（冗余统计字段），合并 nav_status+show_status 为 status
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_category`;
CREATE TABLE `pms_product_category` (
  `id`          bigint       NOT NULL AUTO_INCREMENT                     COMMENT '分类ID',
  `parent_id`   bigint       NOT NULL DEFAULT 0                          COMMENT '父分类ID，0表示一级分类',
  `name`        varchar(64)  NOT NULL                                    COMMENT '分类名称',
  `level`       tinyint(1)   NOT NULL DEFAULT 0                          COMMENT '分类层级：0一级 1二级',
  `product_unit` varchar(32) NULL     DEFAULT NULL                       COMMENT '商品单位',
  `status`      tinyint(1)   NOT NULL DEFAULT 1                          COMMENT '启用状态：0禁用 1启用',
  `sort`        int          NOT NULL DEFAULT 0                          COMMENT '排序（升序）',
  `icon`        varchar(500) NULL     DEFAULT NULL                       COMMENT '分类图标URL',
  `keywords`    varchar(255) NULL     DEFAULT NULL                       COMMENT 'SEO关键词',
  `description` varchar(500) NULL     DEFAULT NULL                       COMMENT '分类描述',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP          COMMENT '创建时间',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_status_sort` (`status`, `sort`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品分类';

-- ----------------------------
-- pms_farmer 农户信息
-- ----------------------------
DROP TABLE IF EXISTS `pms_farmer`;
CREATE TABLE `pms_farmer` (
  `id`             bigint       NOT NULL AUTO_INCREMENT              COMMENT '农户ID',
  `name`           varchar(64)  NOT NULL                            COMMENT '农户姓名',
  `phone`          varchar(20)  NOT NULL                            COMMENT '联系电话',
  `id_card`        varchar(32)  NULL     DEFAULT NULL               COMMENT '身份证号（脱敏存储）',
  `avatar`         varchar(500) NULL     DEFAULT NULL               COMMENT '头像URL',
  `farm_name`      varchar(128) NULL     DEFAULT NULL               COMMENT '农场/基地名称',
  `province`       varchar(64)  NULL     DEFAULT NULL               COMMENT '省',
  `city`           varchar(64)  NULL     DEFAULT NULL               COMMENT '市',
  `region`         varchar(64)  NULL     DEFAULT NULL               COMMENT '区/县',
  `detail_address` varchar(255) NULL     DEFAULT NULL               COMMENT '详细地址',
  `main_product`   varchar(255) NULL     DEFAULT NULL               COMMENT '主营产品描述',
  `description`    varchar(1000) NULL    DEFAULT NULL               COMMENT '农户/基地简介',
  `status`         tinyint(1)   NOT NULL DEFAULT 1                  COMMENT '状态：0禁用 1正常',
  `is_deleted`     tinyint(1)   NOT NULL DEFAULT 0                  COMMENT '是否删除：0否 1是',
  `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  `update_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_status` (`status`, `is_deleted`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '农户信息';

-- ----------------------------
-- pms_product_attribute_category 商品属性分组
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_category`;
CREATE TABLE `pms_product_attribute_category` (
  `id`              bigint       NOT NULL AUTO_INCREMENT            COMMENT '属性分组ID',
  `name`            varchar(64)  NOT NULL                          COMMENT '属性分组名称',
  `attribute_count` int          NOT NULL DEFAULT 0                COMMENT '属性数量（规格）',
  `param_count`     int          NOT NULL DEFAULT 0                COMMENT '参数数量（参数）',
  `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品属性分组';

-- ----------------------------
-- pms_product_attribute 商品属性/规格定义
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute`;
CREATE TABLE `pms_product_attribute` (
  `id`                           bigint       NOT NULL AUTO_INCREMENT  COMMENT '属性ID',
  `product_attribute_category_id` bigint      NOT NULL                 COMMENT '属性分组ID',
  `name`                         varchar(64)  NOT NULL                 COMMENT '属性名称',
  `select_type`                  tinyint(1)   NOT NULL DEFAULT 0       COMMENT '属性选择类型：0单选 1多选 2唯一',
  `input_type`                   tinyint(1)   NOT NULL DEFAULT 0       COMMENT '录入方式：0手动 1从列表选',
  `input_list`                   varchar(255) NULL     DEFAULT NULL    COMMENT '可选值列表（逗号分隔）',
  `sort`                         int          NOT NULL DEFAULT 0       COMMENT '排序',
  `filter_type`                  tinyint(1)   NOT NULL DEFAULT 0       COMMENT '是否作为筛选项：0否 1是',
  `search_type`                  tinyint(1)   NOT NULL DEFAULT 0       COMMENT '检索类型：0不检索 1模糊 2精确',
  `related_status`               tinyint(1)   NOT NULL DEFAULT 0       COMMENT '是否关联SKU：0否 1是',
  `hand_add_status`              tinyint(1)   NOT NULL DEFAULT 0       COMMENT '是否支持手动新增：0否 1是',
  `type`                         tinyint(1)   NOT NULL DEFAULT 0       COMMENT '属性类型：0规格 1参数',
  `create_time`                  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_category` (`product_attribute_category_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品属性/规格定义';

-- ----------------------------
-- pms_product 商品主表
-- 改动：price 代表日常售价，market_price 为市场价（划线价），promotion_price 为活动价
--       移除冗余的 detail_title、original_price 改名为 market_price
--       is_new/is_recommend 替代原来含义模糊的 new_status/recommend_status
-- ----------------------------
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product` (
  `id`                           bigint         NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_category_id`          bigint         NOT NULL                COMMENT '商品分类ID',
  `farmer_id`                    bigint         NULL     DEFAULT NULL   COMMENT '农户ID',
  `product_attribute_category_id` bigint        NULL     DEFAULT NULL   COMMENT '属性分组ID',
  `product_sn`                   varchar(64)    NOT NULL                COMMENT '货号/商品编码',
  `name`                         varchar(128)   NOT NULL                COMMENT '商品名称',
  `sub_title`                    varchar(255)   NULL     DEFAULT NULL   COMMENT '副标题/卖点',
  `pic`                          varchar(500)   NULL     DEFAULT NULL   COMMENT '主图URL',
  `album_pics`                   varchar(2000)  NULL     DEFAULT NULL   COMMENT '相册图（逗号分隔）',
  `description`                  text           NULL                    COMMENT '商品描述（富文本）',
  `detail_html`                  text           NULL                    COMMENT '详情内容（PC端HTML）',
  `price`                        decimal(10,2)  NOT NULL DEFAULT 0.00  COMMENT '日常售价',
  `market_price`                 decimal(10,2)  NULL     DEFAULT NULL   COMMENT '市场价（用于显示划线价）',
  `promotion_price`              decimal(10,2)  NULL     DEFAULT NULL   COMMENT '活动促销价',
  `stock`                        int            NOT NULL DEFAULT 0      COMMENT '总库存',
  `low_stock`                    int            NULL     DEFAULT NULL   COMMENT '库存预警值',
  `unit`                         varchar(16)    NULL     DEFAULT NULL   COMMENT '计量单位',
  `weight`                       decimal(10,2)  NULL     DEFAULT NULL   COMMENT '商品重量（kg）',
  `publish_status`               tinyint(1)     NOT NULL DEFAULT 0      COMMENT '上架状态：0下架 1上架',
  `is_new`                       tinyint(1)     NOT NULL DEFAULT 0      COMMENT '新品推荐：0否 1是',
  `is_recommend`                 tinyint(1)     NOT NULL DEFAULT 0      COMMENT '人气推荐：0否 1是',
  `sort`                         int            NOT NULL DEFAULT 0      COMMENT '排序权重（降序）',
  `sale`                         int            NOT NULL DEFAULT 0      COMMENT '已售数量',
  `keywords`                     varchar(255)   NULL     DEFAULT NULL   COMMENT '关键词',
  `note`                         varchar(500)   NULL     DEFAULT NULL   COMMENT '备注（内部使用）',
  `promotion_type`               tinyint        NULL     DEFAULT NULL   COMMENT '促销类型：0无 1满减 2会员价',
  `promotion_start_time`         datetime       NULL     DEFAULT NULL   COMMENT '促销开始时间',
  `promotion_end_time`           datetime       NULL     DEFAULT NULL   COMMENT '促销结束时间',
  `is_deleted`                   tinyint(1)     NOT NULL DEFAULT 0      COMMENT '软删除：0否 1是',
  `create_time`                  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`                  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_category` (`product_category_id`),
  INDEX `idx_farmer` (`farmer_id`),
  INDEX `idx_publish` (`publish_status`, `is_deleted`),
  INDEX `idx_new_recommend` (`is_new`, `is_recommend`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品主表';

-- ----------------------------
-- pms_sku_stock SKU库存
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_stock`;
CREATE TABLE `pms_sku_stock` (
  `id`               bigint        NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id`       bigint        NOT NULL               COMMENT '商品ID',
  `sku_code`         varchar(64)   NOT NULL               COMMENT 'SKU编码',
  `price`            decimal(10,2) NOT NULL DEFAULT 0.00  COMMENT 'SKU价格',
  `promotion_price`  decimal(10,2) NULL     DEFAULT NULL  COMMENT 'SKU促销价',
  `stock`            int           NOT NULL DEFAULT 0     COMMENT '库存',
  `low_stock`        int           NULL     DEFAULT NULL  COMMENT '预警库存',
  `lock_stock`       int           NOT NULL DEFAULT 0     COMMENT '锁定库存（已下单未支付）',
  `pic`              varchar(500)  NULL     DEFAULT NULL  COMMENT 'SKU图片',
  `sale`             int           NOT NULL DEFAULT 0     COMMENT 'SKU销量',
  `sp_data`          varchar(1000) NULL     DEFAULT NULL  COMMENT '销售属性JSON（[{key,value}]）',
  `create_time`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_product_id` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = 'SKU库存';

-- ----------------------------
-- pms_product_attribute_value 商品属性值
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_value`;
CREATE TABLE `pms_product_attribute_value` (
  `id`                   bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id`           bigint       NOT NULL               COMMENT '商品ID',
  `product_attribute_id` bigint       NOT NULL               COMMENT '属性ID',
  `value`                varchar(500) NOT NULL               COMMENT '属性值',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品属性值';

-- ----------------------------
-- pms_product_full_reduction 满减规则
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_full_reduction`;
CREATE TABLE `pms_product_full_reduction` (
  `id`           bigint        NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id`   bigint        NOT NULL               COMMENT '商品ID',
  `full_price`   decimal(10,2) NOT NULL               COMMENT '满减门槛（元）',
  `reduce_price` decimal(10,2) NOT NULL               COMMENT '减免金额（元）',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品满减规则';

-- ================================================================
-- OMS 订单管理模块
-- ================================================================

-- ----------------------------
-- oms_cart_item 购物车
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_item`;
CREATE TABLE `oms_cart_item` (
  `id`                bigint        NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id`         bigint        NOT NULL               COMMENT '会员ID',
  `product_id`        bigint        NOT NULL               COMMENT '商品ID',
  `product_sku_id`    bigint        NULL     DEFAULT NULL  COMMENT 'SKU ID',
  `product_name`      varchar(128)  NOT NULL               COMMENT '商品名称快照',
  `product_pic`       varchar(500)  NULL     DEFAULT NULL  COMMENT '商品图片快照',
  `product_sub_title` varchar(255)  NULL     DEFAULT NULL  COMMENT '副标题快照',
  `product_sku_code`  varchar(64)   NULL     DEFAULT NULL  COMMENT 'SKU编码快照',
  `product_attr`      varchar(500)  NULL     DEFAULT NULL  COMMENT '销售属性快照（JSON）',
  `price`             decimal(10,2) NOT NULL               COMMENT '加购时单价',
  `quantity`          int           NOT NULL DEFAULT 1     COMMENT '数量',
  `is_deleted`        tinyint(1)    NOT NULL DEFAULT 0     COMMENT '是否删除',
  `create_time`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_member` (`member_id`, `is_deleted`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '购物车';

-- ----------------------------
-- oms_company_address 公司发货地址
-- ----------------------------
DROP TABLE IF EXISTS `oms_company_address`;
CREATE TABLE `oms_company_address` (
  `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `address_name`   varchar(128) NOT NULL               COMMENT '地址别名',
  `is_default_send`    tinyint(1) NOT NULL DEFAULT 0   COMMENT '是否默认发货地',
  `is_default_receive` tinyint(1) NOT NULL DEFAULT 0   COMMENT '是否默认退货地',
  `contact_name`   varchar(64)  NOT NULL               COMMENT '联系人',
  `phone`          varchar(20)  NOT NULL               COMMENT '联系电话',
  `province`       varchar(64)  NOT NULL               COMMENT '省',
  `city`           varchar(64)  NOT NULL               COMMENT '市',
  `region`         varchar(64)  NOT NULL               COMMENT '区',
  `detail_address` varchar(255) NOT NULL               COMMENT '详细地址',
  `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '公司发货/退货地址';

-- ----------------------------
-- oms_order 订单主表
-- 改动：将收货地址字段独立清晰命名，移除冗余的 member_username（可通过 member_id JOIN 查询）
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order` (
  `id`                    bigint        NOT NULL AUTO_INCREMENT  COMMENT '订单ID',
  `order_sn`              varchar(64)   NOT NULL                 COMMENT '订单编号（唯一）',
  `member_id`             bigint        NOT NULL                 COMMENT '会员ID',
  `member_username`       varchar(64)   NULL     DEFAULT NULL    COMMENT '用户账号快照',
  `farmer_id`             bigint        NULL     DEFAULT NULL    COMMENT '农户ID',
  `coupon_id`             bigint        NULL     DEFAULT NULL    COMMENT '使用的优惠券ID',
  -- 金额信息
  `total_amount`          decimal(10,2) NOT NULL DEFAULT 0.00   COMMENT '商品总金额',
  `freight_amount`        decimal(10,2) NOT NULL DEFAULT 0.00   COMMENT '运费',
  `promotion_amount`      decimal(10,2) NOT NULL DEFAULT 0.00   COMMENT '活动优惠金额',
  `coupon_amount`         decimal(10,2) NOT NULL DEFAULT 0.00   COMMENT '优惠券抵扣金额',
  `pay_amount`            decimal(10,2) NOT NULL DEFAULT 0.00   COMMENT '实付金额',
  -- 收货地址快照
  `receiver_name`         varchar(64)   NOT NULL                 COMMENT '收货人姓名',
  `receiver_phone`        varchar(20)   NOT NULL                 COMMENT '收货人电话',
  `receiver_province`     varchar(64)   NOT NULL                 COMMENT '省',
  `receiver_city`         varchar(64)   NOT NULL                 COMMENT '市',
  `receiver_region`       varchar(64)   NOT NULL                 COMMENT '区',
  `receiver_detail_address` varchar(255) NOT NULL               COMMENT '详细地址',
  `receiver_post_code`    varchar(20)   NULL     DEFAULT NULL    COMMENT '邮编',
  -- 状态
  `status`                tinyint       NOT NULL DEFAULT 0       COMMENT '订单状态：0待付款 1待发货 2已发货 3已完成 4已关闭 5无效',
  `pay_type`              tinyint       NULL     DEFAULT NULL    COMMENT '支付方式：1支付宝 2微信 3货到付款',
  `source_type`           tinyint       NOT NULL DEFAULT 0       COMMENT '订单来源：0PC 1APP',
  -- 物流
  `delivery_company`      varchar(64)   NULL     DEFAULT NULL    COMMENT '物流公司',
  `delivery_sn`           varchar(64)   NULL     DEFAULT NULL    COMMENT '物流单号',
  `delivery_time`         datetime      NULL     DEFAULT NULL    COMMENT '发货时间',
  `receive_time`          datetime      NULL     DEFAULT NULL    COMMENT '确认收货时间',
  -- 支付
  `pay_time`              datetime      NULL     DEFAULT NULL    COMMENT '支付时间',
  `payment_sn`            varchar(128)  NULL     DEFAULT NULL    COMMENT '第三方支付流水号',
  -- 备注
  `note`                  varchar(500)  NULL     DEFAULT NULL    COMMENT '用户备注',
  `admin_note`            varchar(500)  NULL     DEFAULT NULL    COMMENT '运营备注',
  `confirm_status`        tinyint       NOT NULL DEFAULT 0       COMMENT '确认收货状态：0未确认 1已确认',
  `is_deleted`            tinyint(1)    NOT NULL DEFAULT 0       COMMENT '软删除',
  `create_time`           datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time`           datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_order_sn` (`order_sn`),
  INDEX `idx_member` (`member_id`),
  INDEX `idx_status` (`status`, `is_deleted`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '订单主表';

-- ----------------------------
-- oms_order_item 订单商品行
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item` (
  `id`               bigint        NOT NULL AUTO_INCREMENT  COMMENT 'ID',
  `order_id`         bigint        NOT NULL                 COMMENT '订单ID',
  `order_sn`         varchar(64)   NOT NULL                 COMMENT '订单编号',
  `product_id`       bigint        NOT NULL                 COMMENT '商品ID',
  `product_sku_id`   bigint        NULL     DEFAULT NULL    COMMENT 'SKU ID',
  `product_name`     varchar(128)  NOT NULL                 COMMENT '商品名称快照',
  `product_pic`      varchar(500)  NULL     DEFAULT NULL    COMMENT '商品图片快照',
  `product_sku_code` varchar(64)   NULL     DEFAULT NULL    COMMENT 'SKU编码快照',
  `product_attr`     varchar(500)  NULL     DEFAULT NULL    COMMENT '销售属性快照',
  `product_price`    decimal(10,2) NOT NULL                 COMMENT '下单单价',
  `product_quantity` int           NOT NULL DEFAULT 1       COMMENT '购买数量',
  `real_amount`      decimal(10,2) NOT NULL DEFAULT 0.00    COMMENT '实付小计',
  `create_time`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_product_id` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '订单商品行';

-- ----------------------------
-- oms_order_return_apply 退货申请
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_apply`;
CREATE TABLE `oms_order_return_apply` (
  `id`              bigint        NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id`           bigint        NOT NULL               COMMENT '订单ID',
  `order_sn`           varchar(64)   NULL     DEFAULT NULL  COMMENT '订单编号',
  `company_address_id` bigint        NULL     DEFAULT NULL  COMMENT '收货公司地址ID',
  `product_id`         bigint        NULL     DEFAULT NULL  COMMENT '退货商品ID',
  `member_username`    varchar(64)   NULL     DEFAULT NULL  COMMENT '申请会员账号',
  `return_amount`      decimal(10,2) NOT NULL               COMMENT '退款金额',
  `return_name`        varchar(64)   NULL     DEFAULT NULL  COMMENT '退货人姓名',
  `return_phone`       varchar(20)   NULL     DEFAULT NULL  COMMENT '退货人电话',
  `reason`             varchar(200)  NULL     DEFAULT NULL  COMMENT '退货原因',
  `description`        varchar(500)  NULL     DEFAULT NULL  COMMENT '用户补充说明',
  `proof_pics`         varchar(2000) NULL     DEFAULT NULL  COMMENT '凭证图片（逗号分隔）',
  `product_pic`        varchar(500)  NULL     DEFAULT NULL  COMMENT '商品图片快照',
  `product_name`       varchar(128)  NULL     DEFAULT NULL  COMMENT '商品名称快照',
  `product_attr`       varchar(500)  NULL     DEFAULT NULL  COMMENT '商品属性快照',
  `product_count`      int           NULL     DEFAULT NULL  COMMENT '退货数量',
  `product_price`      decimal(10,2) NULL     DEFAULT NULL  COMMENT '商品单价快照',
  `status`             tinyint       NOT NULL DEFAULT 0     COMMENT '状态：0待处理 1退货中 2已完成 3已拒绝',
  `handle_note`        varchar(500)  NULL     DEFAULT NULL  COMMENT '处理备注',
  `handle_time`        datetime      NULL     DEFAULT NULL  COMMENT '处理时间',
  `create_time`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_order` (`order_id`),
  INDEX `idx_member_username` (`member_username`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '退货申请';

-- ================================================================
-- SMS 营销模块
-- ================================================================

-- ----------------------------
-- sms_coupon 优惠券
-- 改动：min_point 改名为 min_amount（避免与积分 point 混淆）
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon` (
  `id`            bigint        NOT NULL AUTO_INCREMENT  COMMENT '优惠券ID',
  `name`          varchar(100)  NOT NULL                 COMMENT '优惠券名称',
  `type`          tinyint       NOT NULL DEFAULT 0       COMMENT '类型：0全场赠券 1会员赠券 2购物赠券 3注册赠券',
  `platform`      tinyint       NOT NULL DEFAULT 0       COMMENT '使用平台：0全部 1移动端 2WEB',
  `amount`        decimal(10,2) NOT NULL DEFAULT 0.00    COMMENT '面额（元）',
  `min_amount`    decimal(10,2) NOT NULL DEFAULT 0.00    COMMENT '使用门槛（满X元可用，0表示无门槛）',
  `per_limit`     int           NOT NULL DEFAULT 1       COMMENT '每人限领数量',
  `publish_count` int           NOT NULL DEFAULT 0       COMMENT '发行总数量',
  `total_count`   int           NOT NULL DEFAULT 0       COMMENT '发放总数量（0不限）',
  `receive_count` int           NOT NULL DEFAULT 0       COMMENT '已领取数量',
  `use_count`     int           NOT NULL DEFAULT 0       COMMENT '已使用数量',
  `use_type`      tinyint       NOT NULL DEFAULT 0       COMMENT '使用范围：0全场 1指定分类 2指定商品',
  `enable_time`   datetime      NULL     DEFAULT NULL    COMMENT '领取开始时间',
  `start_time`    datetime      NOT NULL                 COMMENT '有效期开始时间',
  `end_time`      datetime      NOT NULL                 COMMENT '有效期结束时间',
  `code`          varchar(64)   NULL     DEFAULT NULL    COMMENT '优惠码',
  `member_level`  int           NULL     DEFAULT NULL    COMMENT '会员等级限制',
  `status`        tinyint(1)    NOT NULL DEFAULT 1       COMMENT '状态：0禁用 1启用',
  `note`          varchar(255)  NULL     DEFAULT NULL    COMMENT '使用说明',
  `create_time`   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_status` (`status`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券';

-- ----------------------------
-- sms_coupon_member 会员领取记录
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_member`;
CREATE TABLE `sms_coupon_member` (
  `id`           bigint    NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id`    bigint    NOT NULL               COMMENT '优惠券ID',
  `member_id`    bigint    NOT NULL               COMMENT '会员ID',
  `used_order_id` bigint   NULL     DEFAULT NULL  COMMENT '使用订单ID（NULL表示未使用）',
  `status`       tinyint   NOT NULL DEFAULT 0     COMMENT '状态：0未使用 1已使用 2已过期',
  `receive_time` datetime  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_time`     datetime  NULL     DEFAULT NULL  COMMENT '使用时间',
  PRIMARY KEY (`id`),
  INDEX `idx_member` (`member_id`, `status`),
  INDEX `idx_coupon` (`coupon_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '会员优惠券领取记录';

-- ----------------------------
-- sms_home_advertise 首页轮播广告
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_advertise`;
CREATE TABLE `sms_home_advertise` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '广告ID',
  `name`        varchar(100) NOT NULL               COMMENT '广告名称',
  `type`        tinyint      NOT NULL DEFAULT 0     COMMENT '投放位置：0PC Banner 1APP Banner',
  `pic`         varchar(500) NOT NULL               COMMENT '广告图片URL',
  `url`         varchar(500) NULL     DEFAULT NULL  COMMENT '点击跳转链接',
  `sort`        int          NOT NULL DEFAULT 0     COMMENT '排序（升序）',
  `status`      tinyint(1)   NOT NULL DEFAULT 1     COMMENT '状态：0禁用 1启用',
  `click_count` int          NOT NULL DEFAULT 0     COMMENT '点击次数',
  `order_count` int          NOT NULL DEFAULT 0     COMMENT '下单量',
  `start_time`  datetime     NULL     DEFAULT NULL  COMMENT '展示开始时间',
  `end_time`    datetime     NULL     DEFAULT NULL  COMMENT '展示结束时间',
  `note`        varchar(255) NULL     DEFAULT NULL  COMMENT '备注',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_status_sort` (`status`, `sort`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '首页广告/轮播';

-- ----------------------------
-- sms_home_new_product 新品推荐
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_new_product`;
CREATE TABLE `sms_home_new_product` (
  `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id`       bigint       NOT NULL               COMMENT '商品ID',
  `product_name`     varchar(128) NULL     DEFAULT NULL  COMMENT '商品名称',
  `recommend_status` tinyint(1)   NOT NULL DEFAULT 1     COMMENT '推荐状态：0不推荐 1推荐',
  `sort`             int          NOT NULL DEFAULT 0     COMMENT '排序（升序）',
  `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '首页新品推荐';

-- ----------------------------
-- sms_home_recommend_product 人气推荐
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_recommend_product`;
CREATE TABLE `sms_home_recommend_product` (
  `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id`       bigint       NOT NULL               COMMENT '商品ID',
  `product_name`     varchar(128) NULL     DEFAULT NULL  COMMENT '商品名称',
  `recommend_status` tinyint(1)   NOT NULL DEFAULT 1     COMMENT '推荐状态：0不推荐 1推荐',
  `sort`             int          NOT NULL DEFAULT 0     COMMENT '排序（升序）',
  `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '首页人气推荐';

-- ================================================================
-- UMS 用户/权限模块
-- ================================================================

-- ----------------------------
-- ums_member 前台会员
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member` (
  `id`                     bigint       NOT NULL AUTO_INCREMENT  COMMENT '会员ID',
  `member_level_id`        bigint       NULL     DEFAULT NULL    COMMENT '会员等级ID',
  `username`               varchar(64)  NOT NULL                 COMMENT '用户名',
  `password`               varchar(128) NOT NULL                 COMMENT '密码（Bcrypt）',
  `nickname`               varchar(64)  NULL     DEFAULT NULL    COMMENT '昵称',
  `phone`                  varchar(20)  NULL     DEFAULT NULL    COMMENT '手机号',
  `email`                  varchar(100) NULL     DEFAULT NULL    COMMENT '邮箱',
  `icon`                   varchar(500) NULL     DEFAULT NULL    COMMENT '头像URL',
  `gender`                 tinyint      NULL     DEFAULT NULL    COMMENT '性别：0未知 1男 2女',
  `birthday`               date         NULL     DEFAULT NULL    COMMENT '生日',
  `city`                   varchar(64)  NULL     DEFAULT NULL    COMMENT '城市',
  `job`                    varchar(100) NULL     DEFAULT NULL    COMMENT '职业',
  `personalized_signature` varchar(200) NULL     DEFAULT NULL    COMMENT '个性签名',
  `source_type`            tinyint      NULL     DEFAULT NULL    COMMENT '来源：0WEB 1APP',
  `integration`            int          NULL     DEFAULT 0       COMMENT '积分',
  `growth`                 int          NULL     DEFAULT 0       COMMENT '成长值',
  `luckey_count`           int          NULL     DEFAULT 0       COMMENT '剩余抽奖次数',
  `history_integration`    int          NULL     DEFAULT 0       COMMENT '历史积分总数',
  `status`                 tinyint(1)   NOT NULL DEFAULT 1       COMMENT '状态：0禁用 1正常',
  `create_time`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_username` (`username`),
  INDEX `idx_phone` (`phone`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '前台会员';

-- ----------------------------
-- ums_member_receive_address 会员收货地址
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_receive_address`;
CREATE TABLE `ums_member_receive_address` (
  `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `member_id`      bigint       NOT NULL               COMMENT '会员ID',
  `name`           varchar(64)  NOT NULL               COMMENT '收货人姓名',
  `phone`          varchar(20)  NOT NULL               COMMENT '联系电话',
  `is_default`     tinyint(1)   NOT NULL DEFAULT 0     COMMENT '是否默认：0否 1是',
  `post_code`      varchar(20)  NULL     DEFAULT NULL  COMMENT '邮编',
  `province`       varchar(64)  NOT NULL               COMMENT '省',
  `city`           varchar(64)  NOT NULL               COMMENT '市',
  `region`         varchar(64)  NOT NULL               COMMENT '区',
  `detail_address` varchar(255) NOT NULL               COMMENT '详细地址',
  `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_member` (`member_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '会员收货地址';

-- ----------------------------
-- ums_admin 后台管理员（原 t_user）
-- 改动：统一命名规范，密码字段明确为 Bcrypt，补充 update_time
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username`    varchar(64)  NOT NULL               COMMENT '登录用户名',
  `password`    varchar(128) NOT NULL               COMMENT '密码（Bcrypt）',
  `nickname`    varchar(64)  NULL     DEFAULT NULL  COMMENT '昵称',
  `avatar`      varchar(500) NULL     DEFAULT NULL  COMMENT '头像URL',
  `email`       varchar(100) NULL     DEFAULT NULL  COMMENT '邮箱',
  `note`        varchar(255) NULL     DEFAULT NULL  COMMENT '备注',
  `status`      tinyint(1)   NOT NULL DEFAULT 1     COMMENT '状态：0禁用 1启用',
  `is_deleted`  tinyint(1)   NOT NULL DEFAULT 0     COMMENT '软删除',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '后台管理员';

-- ----------------------------
-- ums_role 角色
-- ----------------------------
DROP TABLE IF EXISTS `ums_role`;
CREATE TABLE `ums_role` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name`        varchar(64)  NOT NULL               COMMENT '角色名称',
  `description` varchar(255) NULL     DEFAULT NULL  COMMENT '角色描述',
  `status`      tinyint(1)   NOT NULL DEFAULT 1     COMMENT '状态：0禁用 1启用',
  `sort`        int          NOT NULL DEFAULT 0     COMMENT '排序',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '后台角色';

-- ----------------------------
-- ums_admin_role_relation 管理员与角色关联
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_role_relation`;
CREATE TABLE `ums_admin_role_relation` (
  `id`       bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `admin_id` bigint NOT NULL               COMMENT '管理员ID',
  `role_id`  bigint NOT NULL               COMMENT '角色ID',
  PRIMARY KEY (`id`),
  INDEX `idx_admin` (`admin_id`),
  INDEX `idx_role` (`role_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '管理员-角色关联';

-- ----------------------------
-- ums_menu 后台菜单/权限
-- ----------------------------
DROP TABLE IF EXISTS `ums_menu`;
CREATE TABLE `ums_menu` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id`   bigint       NOT NULL DEFAULT 0     COMMENT '父菜单ID，0为顶层',
  `title`       varchar(64)  NOT NULL               COMMENT '菜单名称',
  `name`        varchar(64)  NULL     DEFAULT NULL  COMMENT '前端路由name',
  `path`        varchar(255) NULL     DEFAULT NULL  COMMENT '前端路由path',
  `component`   varchar(255) NULL     DEFAULT NULL  COMMENT '前端组件路径',
  `icon`        varchar(128) NULL     DEFAULT NULL  COMMENT '图标',
  `type`        tinyint      NOT NULL DEFAULT 0     COMMENT '类型：0目录 1菜单 2按钮',
  `sort`        int          NOT NULL DEFAULT 0     COMMENT '排序',
  `status`      tinyint(1)   NOT NULL DEFAULT 1     COMMENT '状态：0隐藏 1显示',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_parent` (`parent_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '后台菜单';

-- ----------------------------
-- ums_role_menu_relation 角色与菜单关联
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_menu_relation`;
CREATE TABLE `ums_role_menu_relation` (
  `id`      bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL               COMMENT '角色ID',
  `menu_id` bigint NOT NULL               COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  INDEX `idx_role` (`role_id`),
  INDEX `idx_menu` (`menu_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '角色-菜单关联';

-- ================================================================
-- TRACE 溯源模块
-- ================================================================

-- ----------------------------
-- trace_origin 产地信息
-- ----------------------------
DROP TABLE IF EXISTS `trace_origin`;
CREATE TABLE `trace_origin` (
  `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '产地ID',
  `origin_name` varchar(128)  NOT NULL               COMMENT '产地名称',
  `province`    varchar(64)   NOT NULL               COMMENT '省',
  `city`        varchar(64)   NOT NULL               COMMENT '市',
  `region`      varchar(64)   NULL     DEFAULT NULL  COMMENT '区/县',
  `longitude`   decimal(10,6) NULL     DEFAULT NULL  COMMENT '经度（GIS定位）',
  `latitude`    decimal(10,6) NULL     DEFAULT NULL  COMMENT '纬度（GIS定位）',
  `description` varchar(1000) NULL     DEFAULT NULL  COMMENT '产地描述',
  `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '农产品产地';

-- ----------------------------
-- trace_product_origin 商品-产地关联
-- 说明：一个商品可能来自多个产地批次，因此使用关联表而非直接在 pms_product 加 origin_id
-- ----------------------------
DROP TABLE IF EXISTS `trace_product_origin`;
CREATE TABLE `trace_product_origin` (
  `id`         bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id` bigint NOT NULL               COMMENT '商品ID',
  `origin_id`  bigint NOT NULL               COMMENT '产地ID',
  `is_primary` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否主产地：0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`),
  INDEX `idx_origin` (`origin_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品-产地关联';

-- ----------------------------
-- trace_record 种植/生产记录（成长轨迹时间线）
-- ----------------------------
DROP TABLE IF EXISTS `trace_record`;
CREATE TABLE `trace_record` (
  `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id`  bigint        NOT NULL               COMMENT '商品ID',
  `farmer_id`   bigint        NULL     DEFAULT NULL  COMMENT '农户ID',
  `record_type` varchar(32)   NOT NULL               COMMENT '记录类型：播种/施肥/灌溉/采摘/加工/检测/入库等',
  `content`     varchar(1000) NOT NULL               COMMENT '操作内容描述',
  `pic`         varchar(500)  NULL     DEFAULT NULL  COMMENT '现场照片URL',
  `record_time` datetime      NOT NULL               COMMENT '操作时间',
  `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  PRIMARY KEY (`id`),
  INDEX `idx_product` (`product_id`),
  INDEX `idx_record_time` (`product_id`, `record_time`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '农产品成长记录/溯源时间线';

-- ----------------------------
-- trace_qrcode 溯源二维码
-- ----------------------------
DROP TABLE IF EXISTS `trace_qrcode`;
CREATE TABLE `trace_qrcode` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id`  bigint       NOT NULL               COMMENT '商品ID（唯一）',
  `trace_url`   varchar(500) NOT NULL               COMMENT '溯源页面URL',
  `qrcode_url`  varchar(500) NULL     DEFAULT NULL  COMMENT '二维码图片URL',
  `scan_count`  int          NOT NULL DEFAULT 0     COMMENT '累计扫码次数',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品溯源二维码';

-- ================================================================
-- 初始化数据
-- ================================================================

-- 商品分类（一级分类）
INSERT INTO `pms_product_category` (`id`,`parent_id`,`name`,`level`,`product_unit`,`status`,`sort`,`icon`,`keywords`,`description`) VALUES
(1, 0, '荔浦农产', 0, '件', 1, 1, '', '荔浦,芋头,马蹄', '荔浦县特色农产品'),
(2, 0, '阳朔特产', 0, '件', 1, 2, '', '阳朔,金桔,沙田柚', '阳朔县地理标志产品'),
(3, 0, '永福道地', 0, '件', 1, 3, '', '永福,罗汉果,寿乡', '永福县道地药材与农产'),
(4, 0, '恭城月柿', 0, '件', 1, 4, '', '恭城,月柿,柿子', '恭城县月柿系列'),
(5, 0, '桂林米粉', 0, '件', 1, 5, '', '桂林米粉,卤水', '桂林地道米粉原料与制品'),
(6, 0, '山野珍品', 0, '件', 1, 6, '', '山珍,野菌,竹笋', '桂林山区野生与半野生特产'),
(7, 0, '绿色蔬菜', 0, '件', 1, 7, '', '有机蔬菜,绿色食品', '桂林地区绿色认证蔬菜'),
(8, 0, '广西特色水果', 0, '斤', 1, 8, '', '广西,桂林,水果', '广西桂林助农电商平台-特色水果分类');

-- 商品分类（二级分类）
INSERT INTO `pms_product_category` (`id`,`parent_id`,`name`,`level`,`product_unit`,`status`,`sort`,`icon`,`keywords`,`description`) VALUES
(9,  8, '桂林砂糖橘', 1, '斤', 1, 1, '', '砂糖橘,沃柑', '桂林本地水果'),
(10, 8, '阳朔金桔',   1, '盒', 1, 2, '', '阳朔,金桔,礼盒', '桂林阳朔代表性金桔品类'),
(11, 8, '永福罗汉果', 1, '盒', 1, 3, '', '永福,罗汉果,养生', '桂林永福道地罗汉果品类'),
(12, 1, '荔浦马蹄',   1, '袋', 1, 1, '', '荔浦,马蹄', '桂林荔浦特色根茎类农产品'),
(13, 1, '荔浦芋头',   1, '斤', 1, 2, '', '荔浦,芋头', '桂林荔浦核心地标食材'),
(14, 3, '永福罗汉果原果', 1, '盒', 1, 1, '', '永福,罗汉果', '永福道地罗汉果原果'),
(15, 5, '桂林米粉礼盒', 1, '盒', 1, 1, '', '桂林米粉,礼盒', '正宗桂林米粉礼盒装');

-- 管理员初始数据（密码: admin123，Bcrypt加密）
INSERT INTO `ums_admin` (`id`,`username`,`password`,`nickname`,`status`) VALUES
(1, 'admin', '$2a$10$NZ5o7r2E2tM0E/CEeRGMkO/4/j2hLYLa1V1GVQb2C5mJfVFzQfxqO', '超级管理员', 1);

-- 产地数据（桂林各县区）
INSERT INTO `trace_origin` (`id`,`origin_name`,`province`,`city`,`region`,`longitude`,`latitude`,`description`) VALUES
(1, '荔浦芋头核心产区', '广西壮族自治区', '桂林市', '荔浦市', 110.3958, 24.4883, '荔浦芋头产地，漓江流域肥沃红壤，年均气温19°C，常年云雾滋润，孕育出肉质细腻、香气浓郁的荔浦芋头，是国家地理标志保护产品。'),
(2, '阳朔金桔产区',     '广西壮族自治区', '桂林市', '阳朔县', 110.4969, 24.7756, '阳朔县滨江沿岸小气候区，光热充足、昼夜温差大，所产金桔色泽金黄、汁多皮薄，是阳朔最具代表性的农业地标产品。'),
(3, '永福罗汉果道地产区', '广西壮族自治区', '桂林市', '永福县', 109.9831, 24.9797, '永福县被誉为中国长寿之乡，是罗汉果的原产地和主产区，独特的山地气候和富硒土壤造就了道地品质。'),
(4, '恭城月柿产区', '广西壮族自治区', '桂林市', '恭城瑶族自治县', 110.8282, 24.8320, '恭城瑶族自治县月柿种植历史逾千年，秋季漫山柿红，所产月柿晶莹透亮，甜度高、无涩感，是广西著名出口农产品。'),
(5, '荔浦马蹄产区', '广西壮族自治区', '桂林市', '荔浦市', 110.3958, 24.4883, '荔浦市沿河低洼水田区，土质疏松富含有机质，是马蹄的优质产区，所产马蹄个大皮薄、清甜爽脆。');

-- 溯源记录示例（荔浦芋头，product_id 待实际商品录入后调整）
INSERT INTO `trace_record` (`product_id`,`farmer_id`,`record_type`,`content`,`record_time`) VALUES
(1, 1, '播种', '选用优质荔浦芋头种苗，株行距50cm×60cm，施足基肥（农家肥+复合肥），完成整地起垄播种。', '2025-03-15 08:00:00'),
(1, 1, '施肥', '追施有机肥（腐熟猪粪）一次，配合叶面喷施微量元素肥，促进块茎膨大，全程不施化学农药。', '2025-05-20 09:00:00'),
(1, 1, '灌溉', '采用沟灌方式补水，保持田间湿润，并对病虫害进行人工物理防治，记录田间生长状况良好。', '2025-07-10 07:30:00'),
(1, 1, '采摘', '人工逐株采收，鲜芋头经初步清洗分级，平均单个重量1.2-2.5kg，外观圆润、切面紫色纹路清晰。', '2025-10-08 06:00:00'),
(1, 1, '检测', '送样至广西农科院检测中心，检测报告显示：农药残留未检出，重金属含量符合GB2762标准，品质优良。', '2025-10-12 14:00:00'),
(1, 1, '入库', '完成预冷处理后入恒温仓库（温度8-12摄氏度，湿度85%），出库前完成二次抽检，附溯源二维码包装出仓。', '2025-10-15 10:00:00');

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- 权限模块遗留表（兼容现有代码，未做重构）
-- ================================================================

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id`          bigint       NOT NULL AUTO_INCREMENT,
  `username`    varchar(64)  NOT NULL,
  `password`    varchar(128) NOT NULL,
  `avatar`      varchar(500) NULL     DEFAULT NULL,
  `is_deleted`  tinyint(1)   NOT NULL DEFAULT 0,
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '后台管理员（权限模块）';

DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id`          bigint      NOT NULL AUTO_INCREMENT,
  `username`    varchar(64) NOT NULL,
  `role`        varchar(64) NOT NULL,
  `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_username` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '管理员-角色关联（权限模块）';

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id`          bigint       NOT NULL AUTO_INCREMENT,
  `parent_id`   bigint       NOT NULL DEFAULT 0,
  `title`       varchar(64)  NOT NULL,
  `name`        varchar(64)  NULL     DEFAULT NULL,
  `path`        varchar(255) NULL     DEFAULT NULL,
  `icon`        varchar(128) NULL     DEFAULT NULL,
  `sort`        int          NOT NULL DEFAULT 0,
  `hidden`      tinyint(1)   NOT NULL DEFAULT 0,
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_parent` (`parent_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '后台菜单（权限模块）';

DROP TABLE IF EXISTS `t_role_menu_relation`;
CREATE TABLE `t_role_menu_relation` (
  `id`      bigint      NOT NULL AUTO_INCREMENT,
  `role`    varchar(64) NOT NULL,
  `menu_id` bigint      NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_role` (`role`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '角色-菜单关联（权限模块）';

-- ================================================================
-- SMS 优惠券领取记录
-- ================================================================

DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history` (
  `id`              bigint       NOT NULL AUTO_INCREMENT,
  `coupon_id`       bigint       NOT NULL,
  `member_id`       bigint       NOT NULL,
  `coupon_code`     varchar(64)  NULL     DEFAULT NULL,
  `member_nickname` varchar(64)  NULL     DEFAULT NULL,
  `get_type`        tinyint      NOT NULL DEFAULT 1 COMMENT '获取方式：0后台赠送 1用户领取',
  `use_status`      tinyint      NOT NULL DEFAULT 0 COMMENT '0未使用 1已使用 2已过期',
  `use_time`        datetime     NULL     DEFAULT NULL,
  `order_id`        bigint       NULL     DEFAULT NULL,
  `order_sn`        varchar(64)  NULL     DEFAULT NULL,
  `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_member` (`member_id`),
  INDEX `idx_coupon` (`coupon_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券领取记录';

-- ================================================================
-- OMS 退货原因
-- ================================================================

DROP TABLE IF EXISTS `oms_order_return_reason`;
CREATE TABLE `oms_order_return_reason` (
  `id`          bigint       NOT NULL AUTO_INCREMENT,
  `name`        varchar(128) NOT NULL COMMENT '退货原因',
  `sort`        int          NOT NULL DEFAULT 0,
  `status`      tinyint(1)   NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '退货原因';

-- ================================================================
-- PMS 农户-商品关联（保留，部分查询可能依赖）
-- ================================================================

DROP TABLE IF EXISTS `pms_farmer_product_relation`;
CREATE TABLE `pms_farmer_product_relation` (
  `id`         bigint NOT NULL AUTO_INCREMENT,
  `farmer_id`  bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_farmer` (`farmer_id`),
  INDEX `idx_product` (`product_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '农户-商品关联';

-- ================================================================
-- SMS 优惠券适用范围关联（商品/分类）
-- ================================================================

DROP TABLE IF EXISTS `sms_coupon_product_relation`;
CREATE TABLE `sms_coupon_product_relation` (
  `id`           bigint NOT NULL AUTO_INCREMENT,
  `coupon_id`    bigint NOT NULL COMMENT '优惠券ID',
  `product_id`   bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) NULL DEFAULT NULL COMMENT '商品名称快照',
  `product_sn`   varchar(64)  NULL DEFAULT NULL COMMENT '商品货号快照',
  PRIMARY KEY (`id`),
  INDEX `idx_coupon` (`coupon_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券-商品关联';

DROP TABLE IF EXISTS `sms_coupon_product_category_relation`;
CREATE TABLE `sms_coupon_product_category_relation` (
  `id`                    bigint       NOT NULL AUTO_INCREMENT,
  `coupon_id`             bigint       NOT NULL COMMENT '优惠券ID',
  `product_category_id`   bigint       NOT NULL COMMENT '商品分类ID',
  `product_category_name` varchar(64)  NULL DEFAULT NULL COMMENT '分类名称快照',
  `parent_category_name`  varchar(64)  NULL DEFAULT NULL COMMENT '父分类名称快照',
  PRIMARY KEY (`id`),
  INDEX `idx_coupon` (`coupon_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '优惠券-商品分类关联';

-- ================================================================
-- 测试数据
-- ================================================================

-- 农户数据
INSERT INTO `pms_farmer` (`id`,`name`,`phone`,`id_card`,`farm_name`,`province`,`city`,`region`,`detail_address`,`main_product`,`description`,`status`) VALUES
(1, '张建国', '13978001001', '450303198501011234', '荔浦芋头示范基地', '广西壮族自治区', '桂林市', '荔浦市', '荔浦市双江镇大塘村', '荔浦芋头、荔浦马蹄', '从事荔浦芋头种植20余年，基地面积200亩，获得国家绿色食品认证，年产量超150吨。', 1),
(2, '李秀英', '13978001002', '450321199002022345', '阳朔金桔生态园', '广西壮族自治区', '桂林市', '阳朔县', '阳朔县白沙镇古板村', '阳朔金桔、沙田柚', '阳朔本地金桔种植户，采用生态种植方式，无农药残留，金桔色泽金黄、皮薄汁多。', 1),
(3, '周文华', '13978001003', '450323197803033456', '永福罗汉果专业合作社', '广西壮族自治区', '桂林市', '永福县', '永福县堡里乡福寿村', '永福罗汉果', '永福道地罗汉果种植传承人，合作社成员120户，年产罗汉果800万个，远销东南亚。', 1),
(4, '蒋桂花', '13978001004', '450328198606044567', '恭城月柿农庄', '广西壮族自治区', '桂林市', '恭城瑶族自治县', '恭城县莲花镇栗木村', '恭城月柿、脆柿', '恭城瑶族月柿种植第三代传人，柿园面积80亩，所产月柿晶莹剔透、口感极佳。', 1),
(5, '唐志强', '13978001005', '450305199105055678', '桂林有机蔬菜基地', '广西壮族自治区', '桂林市', '临桂区', '临桂区四塘镇官庄村', '有机蔬菜、番茄、辣椒', '专注有机蔬菜种植，获有机农产品认证，供应桂林市区各大商超。', 1);

-- 商品数据（使用公网可访问的图片）
INSERT INTO `pms_product` (`id`,`product_category_id`,`farmer_id`,`product_sn`,`name`,`sub_title`,`pic`,`album_pics`,`description`,`price`,`market_price`,`promotion_price`,`stock`,`low_stock`,`unit`,`weight`,`publish_status`,`is_new`,`is_recommend`,`sort`,`sale`) VALUES
(1, 13, 1, 'LP-YT-001', '荔浦芋头 新鲜现挖', '国家地理标志产品 · 漓江流域富硒红壤种植', 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=600&q=80', 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=600&q=80', '<p>荔浦芋头，国家地理标志保护产品，产自广西桂林荔浦市，漓江流域肥沃红壤种植，肉质细腻、香气浓郁。</p>', 38.00, 55.00, 32.00, 500, 50, '斤', 2.50, 1, 1, 1, 10, 328),
(2, 13, 1, 'LP-YT-002', '荔浦芋头礼盒装 5斤', '送礼优选 · 精品挑选 · 真空锁鲜', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=600&q=80', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=600&q=80', '<p>精选荔浦芋头礼盒，5斤装，真空包装，适合送礼或自用。</p>', 88.00, 120.00, NULL, 200, 20, '盒', 5.00, 1, 0, 1, 8, 156),
(3, 10, 2, 'YS-JJ-001', '阳朔滑皮金桔 新鲜现摘', '皮薄汁多 · 甜而不酸 · 阳朔特产', 'https://images.unsplash.com/photo-1548164723-63d3c0bee5f6?w=600&q=80', 'https://images.unsplash.com/photo-1548164723-63d3c0bee5f6?w=600&q=80', '<p>阳朔金桔，皮薄汁多，甜而不酸，现摘现发，产地直供。</p>', 28.00, 40.00, 24.00, 800, 100, '斤', 1.00, 1, 1, 0, 9, 512),
(4, 10, 2, 'YS-JJ-002', '阳朔金桔礼盒 10斤装', '优质金桔礼盒 · 产地直发 · 顺丰包邮', 'https://images.unsplash.com/photo-1611080626919-7cf5a9dbab12?w=600&q=80', 'https://images.unsplash.com/photo-1611080626919-7cf5a9dbab12?w=600&q=80', '<p>阳朔金桔礼盒10斤，精品挑选，适合商务送礼。</p>', 128.00, 168.00, NULL, 300, 30, '盒', 10.00, 1, 0, 0, 7, 89),
(5, 14, 3, 'YF-LHG-001', '永福罗汉果 大果型', '道地药材 · 养生佳品 · 长寿之乡出品', 'https://images.unsplash.com/photo-1519996529931-28324d5a630e?w=600&q=80', 'https://images.unsplash.com/photo-1519996529931-28324d5a630e?w=600&q=80', '<p>永福道地罗汉果，长寿之乡出品，清热润肺，适合泡茶饮用。</p>', 45.00, 68.00, 39.00, 600, 60, '盒', 0.50, 1, 1, 1, 6, 276),
(6, 12, 1, 'LP-MT-001', '荔浦马蹄 新鲜脆甜', '清甜爽脆 · 沿河低洼水田种植', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80', '<p>荔浦马蹄，沿河低洼水田种植，个大皮薄，清甜爽脆。</p>', 12.00, 18.00, NULL, 1000, 100, '袋', 2.00, 1, 0, 1, 5, 198),
(7, 15, 5, 'GL-MF-001', '桂林米粉礼盒 正宗干米粉', '百年老字号工艺 · 桂林人的乡愁', 'https://images.unsplash.com/photo-1569050467447-ce54b3bbc37d?w=600&q=80', 'https://images.unsplash.com/photo-1569050467447-ce54b3bbc37d?w=600&q=80', '<p>正宗桂林米粉，采用传统工艺制作，圆润细腻，煮后不糊汤。</p>', 36.00, 48.00, 29.00, 400, 40, '盒', 1.50, 1, 0, 0, 4, 145),
(8, 4, 4, 'GC-YS-001', '恭城月柿 自然晾晒', '千年瑶乡秘法 · 晶莹剔透 · 入口即化', 'https://images.unsplash.com/photo-1579613832125-5d34a13ffe2a?w=600&q=80', 'https://images.unsplash.com/photo-1579613832125-5d34a13ffe2a?w=600&q=80', '<p>恭城月柿，传统晾晒工艺，柿霜厚实，甜糯软滑，营养丰富。</p>', 52.00, 72.00, 45.00, 350, 35, '盒', 2.00, 1, 1, 1, 3, 203),
(9, 7, 5, 'GL-SC-001', '桂林有机圣女果 樱桃番茄', '有机认证 · 产地直供 · 酸甜可口', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&q=80', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&q=80', '<p>桂林有机圣女果，有机认证，酸甜可口，富含维生素C。</p>', 18.00, 25.00, NULL, 700, 70, '斤', 1.00, 1, 0, 0, 2, 87),
(10, 9, 2, 'GL-STJ-001', '桂林砂糖橘 蜜甜无核', '化渣极好 · 入口即甜 · 桂林特产', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=600&q=80', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=600&q=80', '<p>桂林砂糖橘，化渣极好，甜度高无核，老少皆宜。</p>', 22.00, 32.00, 18.00, 900, 90, '斤', 1.00, 1, 1, 1, 1, 445);

-- SKU数据
INSERT INTO `pms_sku_stock` (`product_id`,`sku_code`,`price`,`promotion_price`,`stock`,`low_stock`,`lock_stock`,`sale`,`sp_data`) VALUES
(1, 'LP-YT-001-3J', 38.00, 32.00, 200, 20, 0, 180, '[{"key":"重量","value":"3斤"}]'),
(1, 'LP-YT-001-5J', 58.00, 49.00, 200, 20, 0, 148, '[{"key":"重量","value":"5斤"}]'),
(1, 'LP-YT-001-10J', 108.00, 92.00, 100, 10, 0, 0, '[{"key":"重量","value":"10斤"}]'),
(3, 'YS-JJ-001-3J', 28.00, 24.00, 400, 40, 0, 256, '[{"key":"重量","value":"3斤"}]'),
(3, 'YS-JJ-001-5J', 42.00, 36.00, 400, 40, 0, 256, '[{"key":"重量","value":"5斤"}]'),
(5, 'YF-LHG-001-10GE', 45.00, 39.00, 300, 30, 0, 150, '[{"key":"规格","value":"10个装"}]'),
(5, 'YF-LHG-001-20GE', 82.00, 72.00, 300, 30, 0, 126, '[{"key":"规格","value":"20个装"}]'),
(10, 'GL-STJ-001-5J', 22.00, 18.00, 500, 50, 0, 280, '[{"key":"重量","value":"5斤"}]'),
(10, 'GL-STJ-001-10J', 40.00, 34.00, 400, 40, 0, 165, '[{"key":"重量","value":"10斤"}]');

-- 首页广告轮播
INSERT INTO `sms_home_advertise` (`name`,`type`,`pic`,`url`,`sort`,`status`) VALUES
('荔浦芋头地理标志产品', 0, 'https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1200&q=80', '/category?categoryId=13', 1, 1),
('阳朔金桔产地直供', 0,
