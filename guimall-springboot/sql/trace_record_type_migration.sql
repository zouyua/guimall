-- ========================================
-- 溯源记录类型改造 + 类型表创建
-- ========================================

-- 1. 创建溯源记录类型表
CREATE TABLE IF NOT EXISTS `trace_record_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NULL DEFAULT NULL COMMENT '商品分类ID（关联pms_product_category）',
  `type_name` varchar(64) NOT NULL COMMENT '记录类型名称（如：播种、施肥、采摘）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id` (`category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '溯源记录类型表' ROW_FORMAT = Dynamic;

-- 2. 插入示例数据（请根据你的pms_product_category实际ID修改category_id）
-- 查看你的分类: SELECT id, name FROM pms_product_category;
INSERT INTO `trace_record_type` (`category_id`, `type_name`, `sort`) VALUES
-- 水果类
(3, '环境准备', 1),
(3, '修剪整形', 2),
(3, '开花授粉', 3),
(3, '疏花疏果', 4),
(3, '套袋保护', 5),
(3, '水肥管理', 6),
(3, '病虫防治', 7),
(3, '采摘收获', 8),
(3, '分拣包装', 9),
-- 蔬菜类
(4, '整地施肥', 1),
(4, '播种育苗', 2),
(4, '移栽定植', 3),
(4, '田间管理', 4),
(4, '病虫防治', 5),
(4, '采收', 6),
(4, '清洗包装', 7);

-- 3. 修改 trace_record 表：将 record_type(varchar) 改为 record_type_id(bigint)
ALTER TABLE `trace_record` ADD COLUMN `record_type_id` bigint NULL DEFAULT NULL COMMENT '记录类型ID（关联trace_record_type表）' AFTER `farmer_id`;

-- 4. 如果旧数据有 record_type 文本值，可以尝试匹配迁移（可选）
-- UPDATE trace_record tr
-- INNER JOIN trace_record_type trt ON tr.record_type = trt.type_name
-- SET tr.record_type_id = trt.id;

-- 5. 确认迁移完成后，删除旧的 record_type 字段（可选，先保留观察）
-- ALTER TABLE `trace_record` DROP COLUMN `record_type`;
