package com.ygyin.apiplatform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygyin.apiplatform.annotation.AuthCheck;
import com.ygyin.apiplatform.common.BaseResponse;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.common.ResultUtils;
import com.ygyin.apiplatform.constant.UserConstant;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.mapper.UserApiInfoMapper;
import com.ygyin.apiplatform.model.vo.InterfaceInfoVO;
import com.ygyin.apiplatform.service.InterfaceInfoService;
import com.ygyin.apiplatformcommon.model.entity.InterfaceInfo;
import com.ygyin.apiplatformcommon.model.entity.UserApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理信息接口
 */

@RestController
@RequestMapping("/manage")
@Slf4j
public class ManageController {

    @Resource
    private UserApiInfoMapper userApiInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/data/api/call")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listApiDescByCallNum() {
        // 查询得到 top n 调用的接口，字段有 apiId 和 totalNum
        List<UserApiInfo> userApiInfos = userApiInfoMapper.listApiDescByCallNum(3);
        // 转化为 map 以方便使用接口 id 做关联查询
        Map<Long, List<UserApiInfo>> apiIdObjMap = userApiInfos.stream()
                .collect(Collectors.groupingBy(UserApiInfo::getApiId));

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        // 使用接口 id 在 interface_info 表做关联查询，得到排名前三的接口
        queryWrapper.in("id", apiIdObjMap.keySet());
        List<InterfaceInfo> interfaceList = interfaceInfoService.list(queryWrapper);
        // 判空
        ThrowUtils.throwIf(CollectionUtils.isEmpty(interfaceList), ErrorCode.SYSTEM_ERROR,
                "The list of method listApiDescByCallNum is empty");

        // 转化为 VO
        List<InterfaceInfoVO> voList = interfaceList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceVO = new InterfaceInfoVO();
            // 将原来 对象信息 copy 到 VO 中
            BeanUtils.copyProperties(interfaceInfo, interfaceVO);
            int totalCallNum = apiIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceVO.setTotalNum(totalCallNum);
            return interfaceVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(voList);
    }
}
