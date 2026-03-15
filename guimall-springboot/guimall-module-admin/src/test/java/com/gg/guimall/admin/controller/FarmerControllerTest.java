package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.farmer.*;
import com.gg.guimall.admin.service.FarmerService;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FarmerControllerTest {

    @Mock
    private FarmerService farmerService;

    @InjectMocks
    private FarmerController farmerController;

    private CreateFarmerReqVO createReqVO;
    private FindFarmerPageListReqVO pageListReqVO;
    private UpdateFarmerReqVO updateReqVO;

    @BeforeEach
    void setUp() {
        createReqVO = new CreateFarmerReqVO();
        createReqVO.setName("Test Farmer");
        createReqVO.setPhone("13800138000");
        createReqVO.setAddress("Test Address");

        pageListReqVO = new FindFarmerPageListReqVO();
        pageListReqVO.setCurrent(1);
        pageListReqVO.setSize(10);

        updateReqVO = new UpdateFarmerReqVO();
        updateReqVO.setId(1L);
        updateReqVO.setName("Updated Farmer");
        updateReqVO.setPhone("13900139000");
    }

    @Test
    void testCreateFarmer_Success() {
        Response expectedResponse = Response.success();
        when(farmerService.createFarmer(createReqVO)).thenReturn(expectedResponse);

        Response response = farmerController.createFarmer(createReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(farmerService, times(1)).createFarmer(createReqVO);
    }

    @Test
    void testCreateFarmer_ServiceReturnsFailure() {
        Response expectedResponse = Response.fail("400", "农户名称已存在");
        when(farmerService.createFarmer(createReqVO)).thenReturn(expectedResponse);

        Response response = farmerController.createFarmer(createReqVO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("400", response.getErrorCode());
        verify(farmerService, times(1)).createFarmer(createReqVO);
    }

    @Test
    void testFindFarmerPageList_Success() {
        PageResponse expectedResponse = PageResponse.success();
        when(farmerService.findFarmerPageList(pageListReqVO)).thenReturn(expectedResponse);

        PageResponse response = farmerController.findFarmerPageList(pageListReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(farmerService, times(1)).findFarmerPageList(pageListReqVO);
    }

    @Test
    void testFindFarmerPageList_WithData() {
        List<FindFarmerPageListRspVO> records = Arrays.asList(new FindFarmerPageListRspVO(), new FindFarmerPageListRspVO());
        PageResponse expectedResponse = PageResponse.success();
        expectedResponse.setRecords(records);
        when(farmerService.findFarmerPageList(pageListReqVO)).thenReturn(expectedResponse);

        PageResponse response = farmerController.findFarmerPageList(pageListReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getRecords());
        assertEquals(2, response.getRecords().size());
        verify(farmerService, times(1)).findFarmerPageList(pageListReqVO);
    }

    @Test
    void testFindFarmerDetail_Success() {
        Long id = 1L;
        FindFarmerDetailRspVO detailVO = new FindFarmerDetailRspVO();
        detailVO.setId(id);
        detailVO.setName("Test Farmer");
        Response expectedResponse = Response.success(detailVO);
        when(farmerService.findFarmerDetail(id)).thenReturn(expectedResponse);

        Response response = farmerController.findFarmerDetail(id);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        verify(farmerService, times(1)).findFarmerDetail(id);
    }

    @Test
    void testFindFarmerDetail_NotFound() {
        Long id = 999L;
        Response expectedResponse = Response.fail("404", "农户不存在");
        when(farmerService.findFarmerDetail(id)).thenReturn(expectedResponse);

        Response response = farmerController.findFarmerDetail(id);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("404", response.getErrorCode());
        verify(farmerService, times(1)).findFarmerDetail(id);
    }

    @Test
    void testUpdateFarmer_Success() {
        Response expectedResponse = Response.success();
        when(farmerService.updateFarmer(updateReqVO)).thenReturn(expectedResponse);

        Response response = farmerController.updateFarmer(updateReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(farmerService, times(1)).updateFarmer(updateReqVO);
    }

    @Test
    void testUpdateFarmer_NotFound() {
        updateReqVO.setId(999L);
        Response expectedResponse = Response.fail("404", "农户不存在");
        when(farmerService.updateFarmer(updateReqVO)).thenReturn(expectedResponse);

        Response response = farmerController.updateFarmer(updateReqVO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("404", response.getErrorCode());
        verify(farmerService, times(1)).updateFarmer(updateReqVO);
    }

    @Test
    void testDeleteFarmer_Success() {
        Long id = 1L;
        Response expectedResponse = Response.success();
        when(farmerService.deleteFarmer(id)).thenReturn(expectedResponse);

        Response response = farmerController.deleteFarmer(id);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(farmerService, times(1)).deleteFarmer(id);
    }

    @Test
    void testDeleteFarmer_NotFound() {
        Long id = 999L;
        Response expectedResponse = Response.fail("404", "农户不存在");
        when(farmerService.deleteFarmer(id)).thenReturn(expectedResponse);

        Response response = farmerController.deleteFarmer(id);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("404", response.getErrorCode());
        verify(farmerService, times(1)).deleteFarmer(id);
    }

    @Test
    void testFindFarmerOptions_Success() {
        List<FarmerOptionVO> options = Arrays.asList(
                FarmerOptionVO.builder().id(1L).name("Farmer 1").build(),
                FarmerOptionVO.builder().id(2L).name("Farmer 2").build()
        );
        Response expectedResponse = Response.success(options);
        when(farmerService.findFarmerOptions()).thenReturn(expectedResponse);

        Response response = farmerController.findFarmerOptions();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(2, ((List<?>) response.getData()).size());
        verify(farmerService, times(1)).findFarmerOptions();
    }

    @Test
    void testFindFarmerOptions_EmptyList() {
        Response expectedResponse = Response.success(Arrays.asList());
        when(farmerService.findFarmerOptions()).thenReturn(expectedResponse);

        Response response = farmerController.findFarmerOptions();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(0, ((List<?>) response.getData()).size());
        verify(farmerService, times(1)).findFarmerOptions();
    }
}