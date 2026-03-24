package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 分配菜单权限初始化响应 VO
 */
@Data
@ApiModel("分配菜单权限初始化响应 VO")
public class AllocMenuInitRspVO {

    private String roleName;

    /**
     * Ant Tree 需要的树结构
     */
    private List<TreeNodeRspVO> treeData;

    /**
     * Ant Tree 的 checkedKeys（本项目里使用菜单 id 作为 key）
     */
    private List<String> checkedKeys;
}

