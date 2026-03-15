package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.gg.guimall.admin.service.AdminUserService;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {

    @Mock
    private AdminUserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    private UpdateAdminUserPasswordReqVO validReqVO;

    @BeforeEach
    void setUp() {
        validReqVO = new UpdateAdminUserPasswordReqVO();
        validReqVO.setOldPassword("oldPassword123");
        validReqVO.setNewPassword("newPassword456");
    }

    @Test
    void testUpdatePassword_Success() {
        Response expectedResponse = Response.success();
        when(userService.updatePassword(validReqVO)).thenReturn(expectedResponse);

        Response response = adminUserController.updatePassword(validReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(userService, times(1)).updatePassword(validReqVO);
    }

    @Test
    void testUpdatePassword_ServiceReturnsFailure() {
        Response expectedResponse = Response.fail("400", "旧密码不正确");
        when(userService.updatePassword(validReqVO)).thenReturn(expectedResponse);

        Response response = adminUserController.updatePassword(validReqVO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("400", response.getErrorCode());
        verify(userService, times(1)).updatePassword(validReqVO);
    }

    @Test
    void testFindUserInfo_Success() {
        Response expectedResponse = Response.success();
        when(userService.findUserInfo()).thenReturn(expectedResponse);

        Response response = adminUserController.findUserInfo();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(userService, times(1)).findUserInfo();
    }

    @Test
    void testFindUserInfo_ServiceReturnsData() {
        Response expectedResponse = Response.success();
        expectedResponse.setData("user data");
        when(userService.findUserInfo()).thenReturn(expectedResponse);

        Response response = adminUserController.findUserInfo();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        verify(userService, times(1)).findUserInfo();
    }

    @Test
    void testFindUserInfo_ServiceReturnsFailure() {
        Response expectedResponse = Response.fail("401", "用户未登录");
        when(userService.findUserInfo()).thenReturn(expectedResponse);

        Response response = adminUserController.findUserInfo();

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("401", response.getErrorCode());
        verify(userService, times(1)).findUserInfo();
    }
}