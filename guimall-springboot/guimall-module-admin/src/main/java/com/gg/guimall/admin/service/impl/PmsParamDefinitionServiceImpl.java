package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.ParamDefinitionReqVO;
import com.gg.guimall.admin.model.vo.pms.ParamDefinitionRspVO;
import com.gg.guimall.admin.service.PmsParamDefinitionService;
import com.gg.guimall.common.domain.dos.PmsParamDefinitionDO;
import com.gg.guimall.common.domain.mapper.PmsParamDefinitionMapper;
import com.gg.guimall.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数定义 Service 实现
 */
@Service
public class PmsParamDefinitionServiceImpl implements PmsParamDefinitionService {

    @Autowired
    private PmsParamDefinitionMapper pmsParamDefinitionMapper;

    @Override
    public Response listByCategoryId(Long categoryId) {
        List<PmsParamDefinitionDO> list = pmsParamDefinitionMapper.selectByCategoryId(categoryId);
        List<ParamDefinitionRspVO> voList = list.stream()
                .map(d -> ParamDefinitionRspVO.builder()
                        .id(d.getId())
                        .categoryId(d.getCategoryId())
                        .paramName(d.getParamName())
                        .sort(d.getSort())
                        .createTime(d.getCreateTime())
                        .build())
                .collect(Collectors.toList());
        return Response.success(voList);
    }

    @Override
    public Response create(ParamDefinitionReqVO reqVO) {
        PmsParamDefinitionDO paramDO = PmsParamDefinitionDO.builder()
                .categoryId(reqVO.getCategoryId())
                .paramName(reqVO.getParamName().trim())
                .sort(reqVO.getSort() != null ? reqVO.getSort() : 0)
                .build();
        pmsParamDefinitionMapper.insert(paramDO);
        return Response.success();
    }

    @Override
    public Response update(ParamDefinitionReqVO reqVO) {
        PmsParamDefinitionDO paramDO = PmsParamDefinitionDO.builder()
                .id(reqVO.getId())
                .paramName(reqVO.getParamName().trim())
                .sort(reqVO.getSort() != null ? reqVO.getSort() : 0)
                .build();
        pmsParamDefinitionMapper.updateById(paramDO);
        return Response.success();
    }

    @Override
    public Response delete(Long id) {
        pmsParamDefinitionMapper.deleteById(id);
        return Response.success();
    }
}
