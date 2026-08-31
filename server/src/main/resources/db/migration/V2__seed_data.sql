-- ============================================================
-- 占位图片记录(惰性初始化)
-- 保证 Merchant/Store 查询 LEFT JOIN stored_file 永不返回 null url
-- id=1 为默认 Logo, id=2 为默认封面图
-- ============================================================
INSERT IGNORE INTO stored_file (id, path, url, mime_type, size, provider, created_at)
VALUES
    (1, 'static/default-logo.svg', '/static/default-logo.svg', 'image/svg+xml', 300, 'local', NOW()),
    (2, 'static/default-cover.svg', '/static/default-cover.svg', 'image/svg+xml', 350, 'local', NOW());

-- ============================================================
-- Demo 样例数据(固定Demo街区:温州梧田老街,店铺均为虚构,幂等可重复执行)
-- 固定ID采用101段,避免与手工测试数据(低ID段)冲突
-- 覆盖 POI 详情聚合三种验证形态:
--   poi 101 = STORE, 关联有封面门店
--   poi 102 = STORE, 关联无封面门店(验证默认封面回退)
--   poi 103 = SCENIC, 无门店关联(验证摘要字段为 null)
-- ============================================================
INSERT IGNORE INTO street_area (id, name, introduction, cover_image_file_id, longitude, latitude, status, created_at, updated_at)
VALUES (101, '梧田老街', '塘河水乡历史街区，传统商贸与市井文化聚集地', 2, 120.6540000, 27.9810000, 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO merchant (id, name, type, contact_person, contact_phone, introduction, logo_file_id, status, created_at, updated_at)
VALUES (101, '老街风味餐饮', 'FOOD', '张三', '13800000001', '老街传统小吃集合商户', 1, 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO store (id, merchant_id, name, address, longitude, latitude, phone, business_hours, cover_image_file_id, business_status, status, created_at, updated_at)
VALUES
    (101, 101, '老街馄饨铺(老街店)', '梧田老街中段18号', 120.6541000, 27.9811000, '0577-86001001', '10:00-21:00', 2, 'OPEN', 'ACTIVE', NOW(), NOW()),
    (102, 101, '桂香糕点铺(老街店)', '梧田老街东段6号', 120.6546000, 27.9814000, '0577-86001002', '08:30-20:00', NULL, 'OPEN', 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO poi (id, street_area_id, name, poi_type, store_id, longitude, latitude, description, status, created_at, updated_at)
VALUES
    (101, 101, '老街馄饨铺', 'STORE', 101, 120.6541000, 27.9811000, '老街传统馄饨老铺，门店有关联封面图', 'ACTIVE', NOW(), NOW()),
    (102, 101, '桂香糕点铺', 'STORE', 102, 120.6546000, 27.9814000, '传统糕点铺，门店无封面图(验证默认封面回退)', 'ACTIVE', NOW(), NOW()),
    (103, 101, '塘河埠头文化广场', 'SCENIC', NULL, 120.6538000, 27.9808000, '街区核心景观节点，非门店POI(验证摘要字段为null)', 'ACTIVE', NOW(), NOW());

-- ============================================================
-- ExperienceRoute Demo 样例数据(幂等可重复执行,固定ID 101段)
--   route 101 = 周末慢逛路线, ACTIVE, 3个POI按顺序: 101 -> 103 -> 102
-- ============================================================
INSERT IGNORE INTO experience_route (id, street_area_id, name, theme, description, estimated_duration, status, created_at, updated_at)
VALUES (101, 101, '周末慢逛路线', 'SLOW_WALK', '从老街小吃出发，经塘河埠头再到糕点铺，适合半日慢节奏逛街。', 180, 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO experience_route_item (id, route_id, poi_id, sequence, recommendation_reason, created_at, updated_at)
VALUES
    (101, 101, 101, 1, '先吃碗馄饨垫底，开启慢逛行程', NOW(), NOW()),
    (102, 101, 103, 2, '饭后到塘河埠头散步消食', NOW(), NOW()),
    (103, 101, 102, 3, '行程收尾带盒传统糕点回家', NOW(), NOW());

-- ============================================================
-- Coupon/Task Demo 样例数据(幂等可重复执行,固定ID 101段)
--   coupon 101/102 = ACTIVE,在有效期内,可直接演示领取与核销闭环
--   task  101/102 = ACTIVE 到店任务,奖励积分,复用核销码完成模式
-- ============================================================
INSERT IGNORE INTO coupon (id, merchant_id, store_id, name, description, discount_text, valid_from, valid_to, status, created_at, updated_at)
VALUES
    (101, 101, 101, '馄饨满减券', '到店出示核销码，满50元立减10元。每桌限用一张。', '满50减10', '2026-08-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
    (102, 101, 102, '糕点九折券', '到店出示核销码，全场糕点享9折优惠。', '全场9折', '2026-08-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO task (id, title, description, task_type, source_type, source_id, store_id, reward_type, reward_value, start_at, end_at, status, created_at, updated_at)
VALUES
    (101, '到店打卡老街馄饨铺', '到店后向店员出示任务核销码，由店员在商户后台输入完成验证，即可获得积分奖励。', 'STORE_VISIT', 'MERCHANT', 101, 101, 'POINT', 10, '2026-08-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
    (102, '到店打卡桂香糕点铺', '到店后向店员出示任务核销码，由店员在商户后台输入完成验证，即可获得积分奖励。', 'STORE_VISIT', 'MERCHANT', 102, 102, 'POINT', 20, '2026-08-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW());

-- ============================================================
-- Activity Demo 样例数据(幂等可重复执行,固定ID 101段)
--   activity 101 = PUBLISHED, 有封面, 无POI(整个街区的活动)
--   activity 102 = PUBLISHED, 无封面(验证默认封面回退), 关联POI 103
--   activity 103 = DRAFT, 用于验证状态流转 DRAFT -> PUBLISHED -> OFFLINE
-- ============================================================
INSERT IGNORE INTO activity (id, street_area_id, title, summary, description, cover_file_id, activity_type, start_time, end_time, location, poi_id, status, created_at, updated_at)
VALUES
    (101, 101, '老街夜市美食节', '街区年度夜市美食活动', '汇集老街馄饨、桂香糕点等传统小吃与本地风味的年度夜市，全街开放。', 2, 'FESTIVAL', '2026-09-10 18:00:00', '2026-09-12 22:00:00', '梧田老街全段', NULL, 'PUBLISHED', NOW(), NOW()),
    (102, 101, '传统戏曲折子戏惠民演出', '地方传统戏曲专场', '传统折子戏连台演出，免费入场，先到先得。', NULL, 'PERFORMANCE', '2026-09-20 19:30:00', '2026-09-20 21:00:00', '塘河埠头文化广场', 103, 'PUBLISHED', NOW(), NOW()),
    (103, 101, '中秋主题灯会', '传统花灯展览与猜灯谜', NULL, NULL, 'EXHIBITION', '2026-09-25 18:00:00', '2026-09-27 21:00:00', NULL, NULL, 'DRAFT', NOW(), NOW());
