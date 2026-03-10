package com.gg.guimall.admin.model.vo.pms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.*;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:商品分页
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品分页查询")
public class FindPmsProductPageListReqVO extends BasePageQuery {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 上架状态
     */
    private Integer publishStatus;

}
