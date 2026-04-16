package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListRspVO;
import com.gg.guimall.admin.service.SmsCouponHistoryService;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private UmsMemberMapper umsMemberMapper;

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

        Map<Long, String> nicknameMap = buildNicknameMap(page.getRecords());

        List<FindSmsCouponHistoryPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsCouponHistoryPageListRspVO vo = new FindSmsCouponHistoryPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    if (!StringUtils.hasText(vo.getMemberNickname())) {
                        String nickname = nicknameMap.get(vo.getMemberId());
                        vo.setMemberNickname(nickname);
                        // 读取时顺带修复历史脏数据，避免后续查询反复回填
                        if (StringUtils.hasText(nickname) && item.getId() != null) {
                            SmsCouponHistoryDO update = new SmsCouponHistoryDO();
                            update.setId(item.getId());
                            update.setMemberNickname(nickname);
                            couponHistoryMapper.updateById(update);
                        }
                    }
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    private Map<Long, String> buildNicknameMap(List<SmsCouponHistoryDO> records) {
        if (records == null || records.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<Long> memberIds = records.stream()
                .filter(item -> item.getMemberId() != null && !StringUtils.hasText(item.getMemberNickname()))
                .map(SmsCouponHistoryDO::getMemberId)
                .collect(Collectors.toSet());
        if (memberIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, String> nicknameMap = new HashMap<>();
        List<UmsMemberDO> members = umsMemberMapper.selectBatchIds(memberIds);
        for (UmsMemberDO member : members) {
            if (member == null || member.getId() == null) {
                continue;
            }
            String nickname = StringUtils.hasText(member.getNickname()) ? member.getNickname().trim() : null;
            if (!StringUtils.hasText(nickname) && StringUtils.hasText(member.getUsername())) {
                nickname = member.getUsername().trim();
            }
            if (StringUtils.hasText(nickname)) {
                nicknameMap.put(member.getId(), nickname);
            }
        }
        return nicknameMap;
    }
}

