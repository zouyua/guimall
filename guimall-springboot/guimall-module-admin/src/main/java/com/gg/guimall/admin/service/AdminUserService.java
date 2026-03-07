package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.gg.guimall.common.utils.Response;

public interface AdminUserService {
    /*
    * 修改密码
    * @param updateAdminUserPasswordReqVO
    * @return
    * */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

    /*
    * 获取当前登录用户信息
    * @return
    * */
    Response findUserInfo();
}
