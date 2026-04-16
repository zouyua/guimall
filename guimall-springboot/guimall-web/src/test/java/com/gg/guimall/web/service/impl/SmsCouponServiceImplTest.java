package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.SmsCouponDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.coupon.ReceiveCouponReqVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsCouponServiceImplTest {

    @Mock
    private SmsCouponMapper couponMapper;

    @Mock
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Mock
    private UmsMemberMapper umsMemberMapper;

    @InjectMocks
    private SmsCouponServiceImpl smsCouponService;

    @Test
    void receiveCoupon_shouldFillNicknameFromMemberWhenRequestNicknameMissing() {
        Long couponId = 5L;
        Long memberId = 1001L;

        ReceiveCouponReqVO reqVO = ReceiveCouponReqVO.builder()
                .couponId(couponId)
                .memberId(memberId)
                .memberNickname(null)
                .build();

        SmsCouponDO couponDO = SmsCouponDO.builder()
                .id(couponId)
                .publishCount(10)
                .receiveCount(0)
                .perLimit(1)
                .enableTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        UmsMemberDO memberDO = UmsMemberDO.builder()
                .id(memberId)
                .nickname("测试会员A")
                .username("test_user_a")
                .build();

        when(couponMapper.selectById(couponId)).thenReturn(couponDO);
        when(couponHistoryMapper.selectCount(any())).thenReturn(0L);
        when(umsMemberMapper.selectById(memberId)).thenReturn(memberDO);

        Response response = smsCouponService.receiveCoupon(reqVO);

        assertTrue(response.isSuccess());

        ArgumentCaptor<SmsCouponHistoryDO> historyCaptor = ArgumentCaptor.forClass(SmsCouponHistoryDO.class);
        verify(couponHistoryMapper).insert(historyCaptor.capture());

        SmsCouponHistoryDO inserted = historyCaptor.getValue();
        assertNotNull(inserted);
        assertEquals("测试会员A", inserted.getMemberNickname());
    }
}

