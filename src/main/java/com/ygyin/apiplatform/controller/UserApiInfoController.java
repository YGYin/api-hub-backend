package com.ygyin.apiplatform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ygyin.apiplatform.annotation.AuthCheck;
import com.ygyin.apiplatform.common.BaseResponse;
import com.ygyin.apiplatform.common.DeleteRequest;
import com.ygyin.apiplatform.common.ErrorCode;
import com.ygyin.apiplatform.common.ResultUtils;
import com.ygyin.apiplatform.constant.CommonConstant;
import com.ygyin.apiplatform.constant.UserConstant;
import com.ygyin.apiplatform.exception.BusinessException;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.model.dto.userapiinfo.UserApiInfoAddRequest;
import com.ygyin.apiplatform.model.dto.userapiinfo.UserApiInfoQueryRequest;
import com.ygyin.apiplatform.model.dto.userapiinfo.UserApiInfoUpdateRequest;
import com.ygyin.apiplatform.service.UserApiInfoService;
import com.ygyin.apiplatform.service.UserService;
import com.ygyin.apiplatformcommon.model.entity.User;
import com.ygyin.apiplatformcommon.model.entity.UserApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * API 接口管理
 */
@RestController
@RequestMapping("/userApiInfo")
@Slf4j
public class UserApiInfoController {

    @Resource
    private UserApiInfoService userApiInfoService;

    @Resource
    private UserService userService;


    // region 增删改查

    /**
     * 创建
     *
     * @param userApiInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserApiInfo(@RequestBody UserApiInfoAddRequest userApiInfoAddRequest, HttpServletRequest request) {
        if (userApiInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserApiInfo userApiInfo = new UserApiInfo();
        BeanUtils.copyProperties(userApiInfoAddRequest, userApiInfo);

        // 校验
        userApiInfoService.validUserApiInfo(userApiInfo, true);
        User loginUser = userService.getLoginUser(request);
        userApiInfo.setUserId(loginUser.getId());

        boolean result = userApiInfoService.save(userApiInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newUserApiInfoId = userApiInfo.getId();
        return ResultUtils.success(newUserApiInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserApiInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserApiInfo oldUserApiInfo = userApiInfoService.getById(id);
        ThrowUtils.throwIf(oldUserApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserApiInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userApiInfoService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 更新
     *
     * @param userApiInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserApiInfo(@RequestBody UserApiInfoUpdateRequest userApiInfoUpdateRequest, HttpServletRequest request) {
        if (userApiInfoUpdateRequest == null || userApiInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserApiInfo userApiInfo = new UserApiInfo();
        BeanUtils.copyProperties(userApiInfoUpdateRequest, userApiInfo);

        // 参数校验
        userApiInfoService.validUserApiInfo(userApiInfo, false);
        User loginUser = userService.getLoginUser(request);
        long id = userApiInfoUpdateRequest.getId();
        // 判断接口 id 是否存在
        UserApiInfo oldUserApiInfo = userApiInfoService.getById(id);
        ThrowUtils.throwIf(oldUserApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserApiInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean updateRes = userApiInfoService.updateById(userApiInfo);
        return ResultUtils.success(updateRes);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserApiInfo> getUserApiInfoById(long id) {
        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        UserApiInfo userApiInfo = userApiInfoService.getById(id);
        if (userApiInfo == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        return ResultUtils.success(userApiInfo);
    }

    /**
     * 获取列表（仅管理员）
     *
     * @param userApiInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<UserApiInfo>> listUserApiInfo(UserApiInfoQueryRequest userApiInfoQueryRequest) {
        UserApiInfo interfaceQuery = new UserApiInfo();
        if (userApiInfoQueryRequest != null)
            BeanUtils.copyProperties(userApiInfoQueryRequest, interfaceQuery);

        QueryWrapper<UserApiInfo> wrapper = new QueryWrapper<>(interfaceQuery);
        List<UserApiInfo> userApiInfoList = userApiInfoService.list(wrapper);


        return ResultUtils.success(userApiInfoList);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param userApiInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserApiInfo>> listUserApiInfoByPage(UserApiInfoQueryRequest userApiInfoQueryRequest,
                                                                     HttpServletRequest request) {
        if (userApiInfoQueryRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        UserApiInfo interfaceQuery = new UserApiInfo();
        BeanUtils.copyProperties(userApiInfoQueryRequest, interfaceQuery);
        // 获取当前页号以及页面大小
        long current = userApiInfoQueryRequest.getCurrent();
        long size = userApiInfoQueryRequest.getPageSize();

        String interfaceSortedField = userApiInfoQueryRequest.getSortField();
        String sortOrder = userApiInfoQueryRequest.getSortOrder();

        // 限制爬虫
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);
        // 对 description 支持模糊搜索
        QueryWrapper<UserApiInfo> wrapper = new QueryWrapper<>(interfaceQuery);
//        wrapper.like(StringUtils.isNotBlank(description), "description", description);
        wrapper.orderBy(StringUtils.isNotBlank(interfaceSortedField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), interfaceSortedField);

        Page<UserApiInfo> interfacePage = userApiInfoService.page(new Page<>(current, size), wrapper);
        return ResultUtils.success(interfacePage);
    }

}
