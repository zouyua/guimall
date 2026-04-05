-- 参数系统重构迁移脚本
-- 执行前请备份数据库！

-- 1. 给 pms_param_definition 表添加 param_value 字段（如果不存在）
ALTER TABLE `pms_param_definition`
ADD COLUMN IF NOT EXISTS `param_value` varchar(255) NOT NULL DEFAULT '' COMMENT '参数值（如：1个月、山东）' AFTER `param_name`;

-- 2. 修改 pms_product_param 表结构
-- 先检查字段是否存在，如果存在则删除
ALTER TABLE `pms_product_param`
DROP COLUMN IF EXISTS `param_name`,
DROP COLUMN IF EXISTS `param_value`;

-- 3. 添加唯一索引（如果不存在）
-- 先删除可能存在的旧索引
ALTER TABLE `pms_product_param`
DROP INDEX IF EXISTS `uk_product_param`;

-- 添加新的唯一索引
ALTER TABLE `pms_product_param`
ADD UNIQUE INDEX `uk_product_param` (`product_id`, `param_id`);

-- 注意：
-- 1. 执行此脚本后，pms_param_definition 表中已有的参数需要手动填写 param_value
-- 2. pms_product_param 表中的旧数据将丢失 param_value，需要重新在商品编辑页面选择参数
