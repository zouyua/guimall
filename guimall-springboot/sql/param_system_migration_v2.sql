-- 参数系统重构迁移脚本 v2
-- 执行前请备份数据库！

-- 1. 修改 pms_product_param 表结构
-- 删除旧字段
ALTER TABLE `pms_product_param` DROP COLUMN `param_name`;
ALTER TABLE `pms_product_param` DROP COLUMN `param_value`;

-- 2. 删除可能存在的旧索引
ALTER TABLE `pms_product_param` DROP INDEX `uk_product_param`;

-- 3. 添加新的唯一索引
ALTER TABLE `pms_product_param` ADD UNIQUE INDEX `uk_product_param` (`product_id`, `param_id`);
