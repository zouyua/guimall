package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;

/**
 * 助农专区 Service（前台）
 *
 * @author wly
 */
public interface SupportService {

    /**
     * 查询签约帮扶农户列表
     */
    Response findSupportFarmerList();
}
