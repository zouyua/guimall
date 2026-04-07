package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.FindPmsSkuStockRspVO;
import com.gg.guimall.admin.model.vo.pms.PmsSkuSpecItemVO;
import com.gg.guimall.admin.model.vo.pms.PmsSkuStockVO;
import com.gg.guimall.admin.service.PmsSkuStockService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsSkuSpecDO;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuSpecMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 Service 实现类
 **/
@Service
@Slf4j
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private PmsSkuSpecMapper skuSpecMapper;

    @Autowired
    private PmsProductMapper productMapper;

    /**
     * 根据商品ID查询SKU列表（含规格明细）
     */
    @Override
    public Response findSkuListByProductId(Long productId) {
        List<PmsSkuStockDO> doList = skuStockMapper.selectByProductId(productId);
        if (doList.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        // 批量查询规格，按 skuId 分组
        List<PmsSkuSpecDO> allSpecs = skuSpecMapper.selectByProductId(productId);
        Map<Long, List<PmsSkuSpecItemVO>> specMap = allSpecs.stream()
                .collect(Collectors.groupingBy(PmsSkuSpecDO::getSkuId,
                        Collectors.mapping(s -> PmsSkuSpecItemVO.builder()
                                .specKey(s.getSpecKey())
                                .specValue(s.getSpecValue())
                                .sort(s.getSort())
                                .build(), Collectors.toList())));

        List<FindPmsSkuStockRspVO> voList = doList.stream().map(item -> {
            FindPmsSkuStockRspVO vo = new FindPmsSkuStockRspVO();
            BeanUtils.copyProperties(item, vo);
            vo.setSpecs(specMap.getOrDefault(item.getId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());

        return Response.success(voList);
    }

    /**
     * 批量保存SKU（先删后插，覆盖更新）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response saveSkuList(Long productId, List<PmsSkuStockVO> skuList) {
        // 校验商品是否存在
        PmsProductDO productDO = productMapper.selectById(productId);
        if (productDO == null || !Objects.equals(productDO.getIsDeleted(), 0)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 先删除该商品下所有SKU及规格
        skuSpecMapper.deleteByProductId(productId);
        skuStockMapper.deleteByProductId(productId);

        // 再批量插入
        if (Objects.nonNull(skuList) && !skuList.isEmpty()) {
            skuList.forEach(vo -> {
                PmsSkuStockDO skuDO = PmsSkuStockDO.builder()
                        .productId(productId)
                        .skuCode(vo.getSkuCode())
                        .price(vo.getPrice())
                        .promotionPrice(vo.getPromotionPrice())
                        .stock(Objects.isNull(vo.getStock()) ? 0 : vo.getStock())
                        .lowStock(Objects.isNull(vo.getLowStock()) ? 0 : vo.getLowStock())
                        .lockStock(0)
                        .sale(0)
                        .pic(vo.getPic())
                        .build();
                skuStockMapper.insert(skuDO);
                Long skuId = skuDO.getId();

                // 写入规格明细（过滤空值）
                if (Objects.nonNull(vo.getSpecs()) && !vo.getSpecs().isEmpty()) {
                    int sort = 0;
                    for (PmsSkuSpecItemVO spec : vo.getSpecs()) {
                        // 跳过空的规格值
                        if (spec.getSpecValue() == null || spec.getSpecValue().trim().isEmpty()) {
                            continue;
                        }
                        skuSpecMapper.insert(PmsSkuSpecDO.builder()
                                .skuId(skuId)
                                .productId(productId)
                                .specKey(spec.getSpecKey())
                                .specValue(spec.getSpecValue())
                                .sort(sort++)
                                .build());
                    }
                }
            });
        }

        return Response.success();
    }

    /**
     * 删除单个SKU
     */
    @Override
    public Response deleteSku(Long id) {
        PmsSkuStockDO skuDO = skuStockMapper.selectById(id);
        if (Objects.isNull(skuDO)) {
            throw new BizException(ResponseCodeEnum.SKU_NOT_FOUND);
        }
        skuStockMapper.deleteById(id);
        return Response.success();
    }
}
