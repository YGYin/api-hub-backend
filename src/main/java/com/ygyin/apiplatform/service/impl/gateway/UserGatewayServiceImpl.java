package com.ygyin.apiplatform.service.impl.gateway;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.mapper.UserMapper;
import com.ygyin.apiplatformcommon.model.entity.User;
import com.ygyin.apiplatformcommon.service.UserGatewayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 用户公共服务实现类，用于网关业务逻辑
 */
@DubboService
public class UserGatewayServiceImpl implements UserGatewayService {

    @Resource
    private UserMapper userMapper;

    /**
     * 从数据库中查看调用接口的用户是否已经分配 accessKey
     *
     * @param accessKey
     * @return
     */
    @Override
    public User getCallUser(String accessKey) {
        // 判断 ak 或 sk 为空则抛异常
        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey),
                ErrorCode.PARAMS_ERROR, "AK or SK may not be allocated");

        // 通过 query wrapper 查询数据库中符合的记录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);

        User callUser = userMapper.selectOne(queryWrapper);
        return callUser;
    }
}
