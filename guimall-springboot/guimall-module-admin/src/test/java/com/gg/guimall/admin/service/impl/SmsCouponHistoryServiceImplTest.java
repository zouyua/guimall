package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListRspVO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsCouponHistoryServiceImplTest {

    @Mock
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Mock
    private UmsMemberMapper umsMemberMapper;

    @InjectMocks
    private SmsCouponHistoryServiceImpl smsCouponHistoryService;

    @Test
    void findCouponHistoryPageList_shouldBackfillNicknameWhenHistoryNicknameMissing() {
        SmsCouponHistoryDO historyDO = SmsCouponHistoryDO.builder()
                .id(1L)
                .couponId(5L)
                .memberId(2001L)
                .memberNickname(null)
                .couponCode("CPN_TEST_001")
                .build();

        Page<SmsCouponHistoryDO> page = new Page<>(1, 10);
        page.setTotal(1);
        page.setRecords(Collections.singletonList(historyDO));

        UmsMemberDO memberDO = UmsMemberDO.builder()
                .id(2001L)
                .nickname("领取用户A")
                .username("user_a")
                .build();

        when(couponHistoryMapper.selectPageList(
                anyLong(),
                anyLong(),
                any(),
                any(),
                anyString(),
                anyString(),
                any(),
                any(),
                any()
        )).thenReturn(page);
        when(umsMemberMapper.selectBatchIds(anyCollection())).thenReturn(Collections.singletonList(memberDO));

        FindSmsCouponHistoryPageListReqVO reqVO = new FindSmsCouponHistoryPageListReqVO();
        reqVO.setCurrent(1L);
        reqVO.setSize(10L);
        reqVO.setCouponId(5L);

        PageResponse<FindSmsCouponHistoryPageListRspVO> rsp = smsCouponHistoryService.findCouponHistoryPageList(reqVO);

        assertNotNull(rsp.getData());
        assertEquals(1, rsp.getData().size());
        assertEquals("领取用户A", rsp.getData().get(0).getMemberNickname());
    }
}

