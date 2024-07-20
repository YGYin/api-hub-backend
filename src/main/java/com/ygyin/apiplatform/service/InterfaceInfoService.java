package com.ygyin.apiplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygyin.apiplatformcommon.model.entity.InterfaceInfo;

/**
* @author yg
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-06-19 12:13:24
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
