package com.ygyin.apiplatform.service.impl.gateway;

import com.ygyin.apiplatform.service.UserApiInfoService;
import com.ygyin.apiplatformcommon.service.UserApiInfoGatewayService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class UserApiInfoGatewayServiceImpl implements UserApiInfoGatewayService {

    @Resource
    private UserApiInfoService userApiInfoService;

    /**
     * 中转调用本地 UserApiInfoService 中的 callNumCount
     * @param apiId
     * @param userId
     * @return
     */
    @Override
    public boolean callNumCount(long apiId, long userId) {
        return userApiInfoService.callNumCount(apiId, userId);
    }
}
