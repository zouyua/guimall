-- ========================================
-- ER 重构：产地-农户 M:N 关联
-- 新建 pms_farmer_origin 表
-- 废弃 trace_product_origin 和 pms_farmer_product_relation
-- ========================================

-- 1. 创建 农户-产地 M:N 关联表
CREATE TABLE IF NOT EXISTS `pms_farmer_origin` (
  `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `farmer_id`   bigint   NOT NULL               COMMENT '农户ID',
  `origin_id`   bigint   NOT NULL               COMMENT '产地ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_farmer_origin` (`farmer_id`, `origin_id`),
  INDEX `idx_farmer` (`farmer_id`),
  INDEX `idx_origin` (`origin_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '农户-产地关联表（M:N）';

-- 2. 从旧的 trace_product_origin 迁移数据到 pms_farmer_origin
-- 通过 商品→农户 + 旧绑定关系中的 origin_id 来推导
INSERT IGNORE INTO `pms_farmer_origin` (`farmer_id`, `origin_id`)
SELECT DISTINCT tpo.farmer_id, tpo.origin_id
FROM `trace_product_origin` tpo
WHERE tpo.farmer_id IS NOT NULL AND tpo.origin_id IS NOT NULL;

-- 3. 如果旧表中 farmer_id 为空但 product_id 关联了农户，也迁移
INSERT IGNORE INTO `pms_farmer_origin` (`farmer_id`, `origin_id`)
SELECT DISTINCT p.farmer_id, tpo.origin_id
FROM `trace_product_origin` tpo
INNER JOIN `pms_product` p ON tpo.product_id = p.id
WHERE tpo.farmer_id IS NULL AND p.farmer_id IS NOT NULL AND tpo.origin_id IS NOT NULL;

-- 4. 废弃旧表（先保留，确认无误后可删除）
-- DROP TABLE IF EXISTS `trace_product_origin`;
-- DROP TABLE IF EXISTS `pms_farmer_product_relation`;

-- 5. 插入示例关联数据（根据实际 farmer_id 和 origin_id 修改）
-- INSERT INTO `pms_farmer_origin` (`farmer_id`, `origin_id`) VALUES
-- (1, 1),  -- 农户1 关联 产地1
-- (2, 1),  -- 农户2 关联 产地1
-- (1, 2);  -- 农户1 也在 产地2 种植
