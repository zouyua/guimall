-- ============================================
-- 会员等级系统 SQL 迁移脚本
-- 执行时间: 2026-04-11
-- ============================================

-- 1. 创建会员等级表
CREATE TABLE IF NOT EXISTS `ums_member_level` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '等级名称，如：普通会员、银卡会员、金卡会员、钻石会员',
    `level` int(11) NOT NULL DEFAULT 0 COMMENT '等级序号（越大等级越高）',
    `price` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '开通/升级价格（元），0表示免费（如默认等级）',
    `discount` int(11) NOT NULL DEFAULT 100 COMMENT '折扣率（百分比），如 95 表示打9.5折，100表示无折扣',
    `free_freight_point` decimal(10,2) DEFAULT 0.00 COMMENT '免邮门槛（预留字段）',
    `comment_growth_point` int(11) DEFAULT 0 COMMENT '每次评论获得的成长值（预留）',
    `priviledge_free_freight` int(1) DEFAULT 0 COMMENT '是否有免邮特权 0否1是（预留）',
    `priviledge_member_price` int(1) DEFAULT 1 COMMENT '是否有会员价特权 0否1是',
    `priviledge_birthday` int(1) DEFAULT 0 COMMENT '是否有生日特权 0否1是（预留）',
    `note` varchar(255) DEFAULT NULL COMMENT '等级说明/备注',
    `default_status` int(1) DEFAULT 0 COMMENT '是否为默认等级 0否1是，注册时自动分配',
    `status` int(1) DEFAULT 1 COMMENT '状态 0禁用 1启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';

-- 2. 初始化默认等级数据
INSERT INTO `ums_member_level` (`name`, `level`, `price`, `discount`, `note`, `default_status`, `status`) VALUES
('普通会员', 0, 0.00, 100, '注册即为普通会员，无额外折扣', 1, 1),
('银卡会员', 1, 29.90, 98, '享受全场9.8折优惠', 0, 1),
('金卡会员', 2, 99.90, 95, '享受全场9.5折优惠', 0, 1),
('钻石会员', 3, 199.90, 90, '享受全场9折优惠', 0, 1);

-- 3. 确保 ums_member 表有 member_level_id 字段且设置默认值
-- 如果已存在该字段，此语句可能报错可忽略
-- ALTER TABLE `ums_member` MODIFY COLUMN `member_level_id` bigint(20) DEFAULT 1 COMMENT '会员等级ID';

-- 4. 将所有尚未设置等级的会员关联到默认等级（普通会员 id=1）
UPDATE `ums_member` SET `member_level_id` = 1 WHERE `member_level_id` IS NULL OR `member_level_id` = 0;

-- 5. 在 oms_order 表添加会员等级折扣字段（promotion_amount 已存在，用它存会员折扣金额）
-- promotion_amount 字段已存在于 oms_order 表中，无需新增
-- 如果需要单独区分会员折扣来源，可取消注释以下语句：
-- ALTER TABLE `oms_order` ADD COLUMN `member_level_discount` decimal(10,2) DEFAULT 0.00 COMMENT '会员等级折扣金额' AFTER `promotion_amount`;
