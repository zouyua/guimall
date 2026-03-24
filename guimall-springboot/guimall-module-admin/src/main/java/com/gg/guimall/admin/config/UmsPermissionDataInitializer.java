package com.gg.guimall.admin.config;

import com.gg.guimall.common.domain.dos.UmsMenuDO;
import com.gg.guimall.common.domain.dos.UmsRoleMenuRelationDO;
import com.gg.guimall.common.domain.dos.UserDO;
import com.gg.guimall.common.domain.dos.UserRoleDO;
import com.gg.guimall.common.domain.mapper.UmsMenuMapper;
import com.gg.guimall.common.domain.mapper.UmsRoleMenuRelationMapper;
import com.gg.guimall.common.domain.mapper.UserMapper;
import com.gg.guimall.common.domain.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 权限模块初始化数据（用于本地演示/联调）
 *
 * 说明：
 * - 若库里已经有菜单数据，则不覆盖用户自定义内容。
 * - 仅在 `t_menu` 为空时插入默认菜单与角色-菜单关系，并补全一些测试账号。
 */
@Component
public class UmsPermissionDataInitializer implements CommandLineRunner {

    @Autowired
    private UmsMenuMapper menuMapper;

    @Autowired
    private UmsRoleMenuRelationMapper roleMenuRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void run(String... args) {
        // 如果已有菜单数据，避免覆盖
        long menuCount = menuMapper.selectCount(null);
        if (menuCount > 0) {
            return;
        }

        Date now = new Date();

        // 1) 菜单树（id 在前端里有“删除按钮禁用”演示逻辑，所以保持稳定）
        List<UmsMenuDO> menus = Arrays.asList(
                UmsMenuDO.builder().id(1L).parentId(0L).title("仪表盘").name("dashboard").path("/admin/index").icon("DashboardOutlined").sort(0).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(2L).parentId(0L).title("商品模块").name("pms").path("").icon("AppstoreOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(3L).parentId(0L).title("农户管理").name("farmer").path("").icon("TeamOutlined").sort(2).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(4L).parentId(0L).title("订单模块").name("oms").path("").icon("ShoppingCartOutlined").sort(3).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(5L).parentId(0L).title("权限模块").name("ums").path("").icon("SettingOutlined").sort(9).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(6L).parentId(0L).title("营销模块").name("sms").path("").icon("GiftOutlined").sort(4).hidden(0).createTime(now).build(),

                // dashboard children
                UmsMenuDO.builder().id(7L).parentId(1L).title("工作台").name("dashboard-index").path("/admin/index").icon("").sort(0).hidden(0).createTime(now).build(),

                // pms children
                UmsMenuDO.builder().id(8L).parentId(2L).title("商品管理").name("pms-product").path("/admin/pms/product").icon("ShoppingOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(9L).parentId(2L).title("商品分类").name("pms-cate").path("/admin/pms/productCate").icon("ApartmentOutlined").sort(2).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(10L).parentId(2L).title("商品类型").name("pms-attr").path("/admin/pms/productAttr").icon("SettingOutlined").sort(3).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(11L).parentId(2L).title("产地信息").name("trace-origin").path("/admin/trace/origin").icon("EnvironmentOutlined").sort(4).hidden(0).createTime(now).build(),

                // farmer children
                UmsMenuDO.builder().id(12L).parentId(3L).title("农户列表").name("farmer-list").path("/admin/farmer").icon("UserOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(13L).parentId(3L).title("农户关联").name("farmer-link").path("/admin/farmer/farmer-link").icon("LinkOutlined").sort(2).hidden(0).createTime(now).build(),

                // oms children
                UmsMenuDO.builder().id(14L).parentId(4L).title("订单列表").name("oms-order").path("/admin/oms/order").icon("UnorderedListOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(15L).parentId(4L).title("发货管理").name("oms-deliver").path("/admin/oms/order/deliverOrderList").icon("CarOutlined").sort(2).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(16L).parentId(4L).title("退货处理").name("oms-apply").path("/admin/oms/apply").icon("RollbackOutlined").sort(3).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(17L).parentId(4L).title("退货原因处理").name("oms-reason").path("/admin/oms/apply/reason").icon("FormOutlined").sort(4).hidden(0).createTime(now).build(),

                // sms children
                UmsMenuDO.builder().id(18L).parentId(6L).title("优惠券管理").name("sms-coupon").path("/admin/sms/coupon").icon("TagsOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(19L).parentId(6L).title("新品推荐").name("sms-new").path("/admin/sms/new").icon("RocketOutlined").sort(2).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(20L).parentId(6L).title("人气推荐").name("sms-hot").path("/admin/sms/hot").icon("HeartOutlined").sort(3).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(21L).parentId(6L).title("轮播图管理").name("sms-ad").path("/admin/sms/advertise").icon("PlayCircleOutlined").sort(4).hidden(0).createTime(now).build(),

                // ums children
                UmsMenuDO.builder().id(22L).parentId(5L).title("角色管理").name("ums-admin").path("/admin/ums/admin").icon("UserOutlined").sort(1).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(23L).parentId(5L).title("管理员管理").name("ums-role").path("/admin/ums/role").icon("SafetyCertificateOutlined").sort(2).hidden(0).createTime(now).build(),
                UmsMenuDO.builder().id(24L).parentId(5L).title("菜单管理").name("ums-menu").path("/admin/ums/menu").icon("AppstoreOutlined").sort(3).hidden(0).createTime(now).build()
        );

        for (UmsMenuDO m : menus) {
            menuMapper.insert(m);
        }

        // 2) 角色-菜单关系（与 allocMenu.vue 的演示勾选一致：用菜单 id 作为 checkedKeys key）
        // ROLE_ADMIN
        bindRoleMenu("ROLE_ADMIN", Arrays.asList(1L, 7L, 2L, 8L, 9L, 10L, 11L, 3L, 12L, 13L, 4L, 14L, 15L, 16L, 17L, 6L, 18L, 19L, 20L, 21L, 5L, 22L, 23L, 24L));
        // ROLE_ORDER
        bindRoleMenu("ROLE_ORDER", Arrays.asList(1L, 7L, 3L, 12L, 13L, 4L, 14L, 15L, 16L, 17L));
        // ROLE_PRODUCT
        bindRoleMenu("ROLE_PRODUCT", Arrays.asList(1L, 7L, 2L, 8L, 9L, 10L, 11L, 3L, 12L, 13L));
        // ROLE_MKT
        bindRoleMenu("ROLE_MKT", Arrays.asList(1L, 7L, 6L, 18L, 19L, 20L, 21L));
        // ROLE_VISITOR
        bindRoleMenu("ROLE_VISITOR", Arrays.asList(1L, 7L, 2L, 8L, 9L, 4L, 14L, 5L, 22L, 23L));

        // 3) 补全后台账号
        UserDO admin = userMapper.findByUsername("admin");
        UserDO test = userMapper.findByUsername("test");
        if (admin == null || test == null) {
            return;
        }

        // 新增/补全用户
        upsertUser("order_mgr", admin.getPassword(), "张订单", false, now);
        upsertUser("product_mgr", admin.getPassword(), "李商品", false, now);
        upsertUser("mkt_mgr", admin.getPassword(), "王营销", false, now);
        upsertUser("test_uat", test.getPassword(), "联调测试", false, now);

        // 绑定用户-角色（单角色：先清再写）
        bindUserRole("admin", "ROLE_ADMIN", now);
        bindUserRole("test", "ROLE_VISITOR", now);
        bindUserRole("order_mgr", "ROLE_ORDER", now);
        bindUserRole("product_mgr", "ROLE_PRODUCT", now);
        bindUserRole("mkt_mgr", "ROLE_MKT", now);
        bindUserRole("test_uat", "ROLE_VISITOR", now);
    }

    private void bindRoleMenu(String roleCode, List<Long> menuIds) {
        // 这里用“先删再插”简化：初始化阶段只在 t_menu 为空时触发
        roleMenuRelationMapper.deleteByRole(roleCode);
        for (Long mid : menuIds) {
            roleMenuRelationMapper.insert(UmsRoleMenuRelationDO.builder()
                    .role(roleCode)
                    .menuId(mid)
                    .build());
        }
    }

    private void upsertUser(String username, String password, String nickname, boolean isDeleted, Date now) {
        UserDO existed = userMapper.findByUsername(username);
        if (existed != null) {
            existed.setAvatar(nickname);
            existed.setIsDeleted(isDeleted);
            existed.setUpdateTime(now);
            userMapper.updateById(existed);
            return;
        }
        userMapper.insert(UserDO.builder()
                .username(username)
                .password(password)
                .avatar(nickname)
                .createTime(now)
                .updateTime(now)
                .isDeleted(isDeleted)
                .build());
    }

    private void bindUserRole(String username, String roleCode, Date now) {
        // 单角色：先清再写
        userRoleMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUsername, username));

        userRoleMapper.insert(UserRoleDO.builder()
                .username(username)
                .role(roleCode)
                .createTime(now)
                .build());
    }
}

