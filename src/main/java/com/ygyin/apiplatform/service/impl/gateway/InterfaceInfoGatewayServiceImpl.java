package com.ygyin.apiplatform.service.impl.gateway;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.mapper.InterfaceInfoMapper;
import com.ygyin.apiplatformcommon.model.entity.InterfaceInfo;
import com.ygyin.apiplatformcommon.service.InterfaceInfoGatewayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InterfaceInfoGatewayServiceImpl implements InterfaceInfoGatewayService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 通过 url 还有请求方法，从检查数据库中检查被调用接口是否存在
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getCallInterfaceInfo(String url, String method) {
        // 判空
        ThrowUtils.throwIf(StringUtils.isAnyBlank(url,method),
                ErrorCode.PARAMS_ERROR, "Called API's url or method is null");

        // wrapper 过滤条件
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);
        InterfaceInfo callInterface = (InterfaceInfo) interfaceInfoMapper.selectOne(queryWrapper);
        return callInterface;
    }
}
