package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 树节点响应 VO（用于分配菜单权限）
 */
@Data
@ApiModel("树节点响应 VO")
public class TreeNodeRspVO {
    private String title;
    private String key;
    private List<TreeNodeRspVO> children;
}

