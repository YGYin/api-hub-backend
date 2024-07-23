package com.ygyin.apiplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygyin.apiplatformcommon.model.entity.UserApiInfo;

import java.util.List;

/**
* @author yg
* @description 针对表【user_api_info(用户调用接口信息)】的数据库操作Mapper
* @createDate 2024-07-08 23:32:59
* @Entity com.ygyin.apiplatform.model.entity.UserApiInfo
*/
public interface UserApiInfoMapper extends BaseMapper<UserApiInfo> {

    // select apiId, sum(totalNum) as totalCallNum
    // from user_api_info group by apiId order by totalCallNum desc limit 3
    List<UserApiInfo> listApiDescByCallNum(int topLimit);
}




