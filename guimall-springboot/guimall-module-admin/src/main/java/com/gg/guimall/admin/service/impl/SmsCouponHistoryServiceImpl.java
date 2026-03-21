package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListRspVO;
import com.gg.guimall.admin.service.SmsCouponHistoryService;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券领取记录管理 Service 实现类
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
@Service
@Slf4j
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Override
    public PageResponse<FindSmsCouponHistoryPageListRspVO> findCouponHistoryPageList(
            FindSmsCouponHistoryPageListReqVO reqVO) {

        Page<SmsCouponHistoryDO> page = couponHistoryMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getCouponId(),
                reqVO.getMemberId(),
                reqVO.getCouponCode(),
                reqVO.getMemberNickname(),
                reqVO.getUseStatus(),
                reqVO.getBeginUseTime(),
                reqVO.getEndUseTime()
        );

        List<FindSmsCouponHistoryPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsCouponHistoryPageListRspVO vo = new FindSmsCouponHistoryPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }
}

