package com.ygyin.apiplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.exception.BusinessException;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.model.entity.InterfaceInfo;
import com.ygyin.apiplatform.service.InterfaceInfoService;
import com.ygyin.apiplatform.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author yg
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-06-19 12:13:24
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建时，检验参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口地址过长");
        }
        if (StringUtils.isNotBlank(method) && method.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口请求类型过长");
        }
    }

}




