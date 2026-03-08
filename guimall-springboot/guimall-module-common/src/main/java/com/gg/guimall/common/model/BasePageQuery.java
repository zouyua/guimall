package com.gg.guimall.common.model;

import lombok.Data;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/7
 * @description:基础分页请求类
 * */
@Data
public class BasePageQuery {
    /*
    * 当前页码，默认第一页*/
    private Long current = 1L;
    /*
    * 每页展示的数据数量，默认每页展示10条数据*/
    private  Long size = 10L;
}
