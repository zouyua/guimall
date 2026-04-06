package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.ParamDefinitionReqVO;
import com.gg.guimall.common.utils.Response;

/**
 * 参数定义 Service
 */
public interface PmsParamDefinitionService {

    Response listAll();

    Response page(Integer current, Integer size, String paramName);

    Response create(ParamDefinitionReqVO reqVO);

    Response update(ParamDefinitionReqVO reqVO);

    Response delete(Long id);
}
