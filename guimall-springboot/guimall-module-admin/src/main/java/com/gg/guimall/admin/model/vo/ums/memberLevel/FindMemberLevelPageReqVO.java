package com.gg.guimall.admin.model.vo.ums.memberLevel;

import com.gg.guimall.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员等级列表查询请求 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindMemberLevelPageReqVO extends BasePageQuery {
    /** 等级名称（模糊查询） */
    private String name;
    /** 状态筛选 */
    private Integer status;
}
