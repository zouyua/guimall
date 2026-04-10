-- ============================================================
-- 积分系统数据库变更脚本
-- 日期: 2026-04-11
-- ============================================================

-- 1. 创建积分变动历史表
CREATE TABLE IF NOT EXISTS `ums_integration_history` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `member_id`   BIGINT       NOT NULL                COMMENT '会员ID',
    `change_count` INT         NOT NULL                COMMENT '变动积分数量（正数）',
    `change_type` INT          NOT NULL DEFAULT 0      COMMENT '变动类型：0获取 1消费',
    `source_type` INT          NOT NULL DEFAULT 0      COMMENT '积分来源：0订单完成奖励 1下单积分抵扣 2管理员手动调整 3注册赠送 4订单取消退还',
    `source_id`   BIGINT       DEFAULT NULL            COMMENT '来源业务ID（如订单ID）',
    `note`        VARCHAR(500) DEFAULT NULL            COMMENT '备注说明',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_source` (`source_type`, `source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分变动历史表';

-- 2. 为订单表添加积分相关字段
ALTER TABLE `oms_order`
    ADD COLUMN `use_integration`    INT            DEFAULT NULL COMMENT '下单时使用的积分数量' AFTER `coupon_amount`,
    ADD COLUMN `integration_amount` DECIMAL(10, 2) DEFAULT NULL COMMENT '积分抵扣金额'       AFTER `use_integration`;
