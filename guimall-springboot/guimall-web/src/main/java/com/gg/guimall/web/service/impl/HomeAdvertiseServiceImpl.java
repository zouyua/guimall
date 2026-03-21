package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.SmsHomeAdvertiseDO;
import com.gg.guimall.common.domain.mapper.SmsHomeAdvertiseMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.home.HomeAdvertiseItemVO;
import com.gg.guimall.web.service.HomeAdvertiseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 首页轮播广告 Service 实现类
 */
@Service
@Slf4j
public class HomeAdvertiseServiceImpl implements HomeAdvertiseService {

    @Autowired
    private SmsHomeAdvertiseMapper smsHomeAdvertiseMapper;

    @Override
    public Response listAdvertises(Integer type) {
        if (Objects.isNull(type) || type < 0 || type > 1) {
            // 前台参数不合法直接返回空列表，避免影响首页渲染
            return Response.success(Collections.emptyList());
        }

        List<SmsHomeAdvertiseDO> list = smsHomeAdvertiseMapper.selectActiveList(type, LocalDateTime.now());
        if (Objects.isNull(list) || list.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        List<HomeAdvertiseItemVO> voList = list.stream()
                .map(item -> {
                    if (Objects.isNull(item)) {
                        return null;
                    }
                    HomeAdvertiseItemVO vo = new HomeAdvertiseItemVO();
                    vo.setId(item.getId());
                    vo.setName(item.getName());
                    vo.setPic(item.getPic());
                    vo.setUrl(item.getUrl());
                    vo.setSort(item.getSort());
                    return vo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Response.success(voList);
    }
}

