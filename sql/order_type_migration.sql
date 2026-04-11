-- 为订单表添加 order_type 字段，区分普通商品订单和会员等级订单
ALTER TABLE oms_order ADD COLUMN order_type INT DEFAULT 0 COMMENT '订单类型:0普通商品订单 1会员等级订单';
