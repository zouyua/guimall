package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.FindPmsProductFullReductionRspVO;
import com.gg.guimall.admin.model.vo.pms.PmsProductFullReductionVO;
import com.gg.guimall.admin.service.PmsProductFullReductionService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsProductFullReductionDO;
import com.gg.guimall.common.domain.mapper.PmsProductFullReductionMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: 商品满减 Service 实现类
 **/
@Service
@Slf4j
public class PmsProductFullReductionServiceImpl implements PmsProductFullReductionService {

    @Autowired
    private PmsProductFullReductionMapper fullReductionMapper;

    @Autowired
    private PmsProductMapper productMapper;

    /**
     * 根据商品ID查询满减列表
     */
    @Override
    public Response findFullReductionListByProductId(Long productId) {
        List<PmsProductFullReductionDO> doList = fullReductionMapper.selectByProductId(productId);

        List<FindPmsProductFullReductionRspVO> voList = doList.stream().map(item -> {
            FindPmsProductFullReductionRspVO vo = new FindPmsProductFullReductionRspVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        return Response.success(voList);
    }

    /**
     * 批量保存满减规则（先删后插，覆盖更新）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response saveFullReductionList(Long productId, List<PmsProductFullReductionVO> fullReductionList) {
        // 校验商品是否存在
        PmsProductDO productDO = productMapper.selectById(productId);
        if (productDO == null || !Objects.equals(productDO.getIsDeleted(), 0)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 先删除该商品下所有满减规则
        fullReductionMapper.deleteByProductId(productId);

        // 再批量插入
        if (Objects.nonNull(fullReductionList) && !fullReductionList.isEmpty()) {
            fullReductionList.forEach(vo -> {
                PmsProductFullReductionDO reductionDO = PmsProductFullReductionDO.builder()
                        .productId(productId)
                        .fullPrice(vo.getFullPrice())
                        .reducePrice(vo.getReducePrice())
                        .build();
                fullReductionMapper.insert(reductionDO);
            });
        }

        return Response.success();
    }

    /**
     * 删除单条满减规则
     */
    @Override
    public Response deleteFullReduction(Long id) {
        PmsProductFullReductionDO reductionDO = fullReductionMapper.selectById(id);
        if (Objects.isNull(reductionDO)) {
            throw new BizException(ResponseCodeEnum.FULL_REDUCTION_NOT_FOUND);
        }
        fullReductionMapper.deleteById(id);
        return Response.success();
    }
}
