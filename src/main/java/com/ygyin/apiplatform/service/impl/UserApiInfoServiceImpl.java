package com.ygyin.apiplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.exception.BusinessException;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.mapper.UserApiInfoMapper;
import com.ygyin.apiplatform.service.UserApiInfoService;
import com.ygyin.apiplatformcommon.model.entity.UserApiInfo;
import org.springframework.stereotype.Service;

/**
 * @author yg
 * @description 针对表【user_api_info(用户调用接口信息)】的数据库操作Service实现
 * @createDate 2024-07-08 23:32:59
 */
@Service
public class UserApiInfoServiceImpl extends ServiceImpl<UserApiInfoMapper, UserApiInfo>
        implements UserApiInfoService {

    @Override
    public void validUserApiInfo(UserApiInfo userApiInfo, boolean add) {
        if (userApiInfo == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        Long apiId = userApiInfo.getApiId();
        Long userId = userApiInfo.getUserId();
        Integer remainNum = userApiInfo.getRemainNum();
        // 创建时，检验参数不能为空
        if (add)
            ThrowUtils.throwIf(apiId <= 0 || userId <= 0, ErrorCode.PARAMS_ERROR, "当前接口或用户不存在");

        // 有参数则校验
        ThrowUtils.throwIf(remainNum < 0, ErrorCode.PARAMS_ERROR, "用户调用当前接口剩余次数小于 0");
    }

    @Override
    public boolean callNumCount(long apiId, long userId) {
        // 判断参数是否存在
        ThrowUtils.throwIf(apiId <= 0 || userId <= 0, ErrorCode.PARAMS_ERROR, "当前接口或用户不存在");
        // 先根据 apiId 和 userId 查到对应记录
        UpdateWrapper<UserApiInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("apiId", apiId);
        updateWrapper.eq("userId", userId);
        // 需要验证用户剩余接口调用次数大于 0
//        updateWrapper.gt("remainNum",0);
        // 筛选出对应记录后更新 remainNum 和 totalNum
        updateWrapper.setSql("remainNum = remainNum - 1, totalNum = totalNum + 1");
        boolean isUpdate = this.update(updateWrapper);
        return isUpdate;
    }

}




