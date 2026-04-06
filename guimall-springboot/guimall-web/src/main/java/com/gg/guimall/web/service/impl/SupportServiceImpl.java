package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.support.SupportFarmerRspVO;
import com.gg.guimall.web.service.SupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 助农专区 Service 实现类（前台）
 *
 * @author wly
 */
@Service
@Slf4j
public class SupportServiceImpl implements SupportService {

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Override
    public Response findSupportFarmerList() {
        List<PmsFarmerDO> list = pmsFarmerMapper.selectList(
                new LambdaQueryWrapper<PmsFarmerDO>()
                        .eq(PmsFarmerDO::getStatus, 1)
                        .eq(PmsFarmerDO::getIsDeleted, 0)
                        .orderByAsc(PmsFarmerDO::getId)
        );

        if (Objects.isNull(list) || list.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        List<SupportFarmerRspVO> voList = list.stream()
                .map(f -> SupportFarmerRspVO.builder()
                        .id(f.getId())
                        .name(f.getName())
                        .farmName(f.getFarmName())
                        .avatar(f.getAvatar())
                        .province(f.getProvince())
                        .city(f.getCity())
                        .region(f.getRegion())
                        .mainProduct(f.getMainProduct())
                        .description(f.getDescription())
                        .certType(f.getCertType())
                        .certDesc(f.getCertDesc())
                        .build())
                .collect(Collectors.toList());

        return Response.success(voList);
    }
}
