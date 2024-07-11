package com.ygyin.apiplatform.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ygyin.apiplatform.annotation.AuthCheck;
import com.ygyin.apiplatform.common.*;
import com.ygyin.apiplatform.constant.CommonConstant;
import com.ygyin.apiplatform.constant.UserConstant;
import com.ygyin.apiplatform.exception.BusinessException;
import com.ygyin.apiplatform.exception.ThrowUtils;
import com.ygyin.apiplatform.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.ygyin.apiplatform.model.dto.interfaceinfo.InterfaceInfoCallRequest;
import com.ygyin.apiplatform.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ygyin.apiplatform.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.ygyin.apiplatform.model.entity.InterfaceInfo;
import com.ygyin.apiplatform.model.entity.User;
import com.ygyin.apiplatform.model.enums.InterfaceStatusEnum;
import com.ygyin.apiplatform.service.InterfaceInfoService;
import com.ygyin.apiplatform.service.UserService;
import com.ygyin.apiplatformsdk.client.ApiClient;
import com.ygyin.apiplatformsdk.model.SampleModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * API 接口管理
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * API 客户端实例
     */
    @Resource
    private ApiClient apiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);

        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());

        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 更新（用户）
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);

        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User loginUser = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断接口 id 是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean updateRes = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateRes);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null)
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceQuery);

        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>(interfaceQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(wrapper);


        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                     HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        InterfaceInfo interfaceQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceQuery);
        // 获取当前页号以及页面大小
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();

        String interfaceSortedField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        // 从当前 query 中取其 description 用于实现对其模糊搜索
        String description = interfaceQuery.getDescription();
        interfaceQuery.setDescription(null);
        // 限制爬虫
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);
        // 对 description 支持模糊搜索
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>(interfaceQuery);
        wrapper.like(StringUtils.isNotBlank(description), "description", description);
        wrapper.orderBy(StringUtils.isNotBlank(interfaceSortedField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), interfaceSortedField);

        Page<InterfaceInfo> interfacePage = interfaceInfoService.page(new Page<>(current, size), wrapper);
        return ResultUtils.success(interfacePage);
    }

    /**
     * 发布上线接口
     *
     * @param apiIdRequest
     * @param request
     * @return
     */
    @PostMapping("/publish")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> publishInterfaceInfo(@RequestBody ApiIdRequest apiIdRequest, HttpServletRequest request) {
        // 判断参数是否存在合法
        if (apiIdRequest == null || apiIdRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        // 1. 通过接口 id 判断接口是否存在
        long id = apiIdRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 2. 判断接口是否能够被调用
        SampleModel model = new SampleModel();
        model.setModelInfo("testingModel");
        String sampleInfo = apiClient.getSampleInfoByPost(model);
        if (StringUtils.isBlank(sampleInfo))
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用失败");

        // waiting for modifying
        // 仅本人或管理员可编辑
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceStatusEnum.INTERFACE_ONLINE.getValue());
        boolean updateRes = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateRes);
    }

    /**
     * 下线接口
     *
     * @param apiIdRequest
     * @param request
     * @return
     */
    @PostMapping("/demount")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> demountInterfaceInfo(@RequestBody ApiIdRequest apiIdRequest, HttpServletRequest request) {
        // 判断参数是否存在合法
        if (apiIdRequest == null || apiIdRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        // 1. 通过接口 id 判断接口是否存在
        long id = apiIdRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 下线接口不需要判断能否被调用
        // 仅本人或管理员可编辑
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceStatusEnum.INTERFACE_OFFLINE.getValue());
        boolean updateRes = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateRes);
    }

    /**
     * 测试接口，将前端的请求通过该接口平台后端进行转发，对模拟接口进行调用
     *
     * @param interfaceInfoCallRequest
     * @param request
     * @return
     */
    @PostMapping("/call")
    public BaseResponse<Object> callInterfaceInfo(@RequestBody InterfaceInfoCallRequest interfaceInfoCallRequest, HttpServletRequest request) {
        // 判断参数是否存在合法
        if (interfaceInfoCallRequest == null || interfaceInfoCallRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        // 通过接口 id 判断接口是否存在
        long id = interfaceInfoCallRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(oldInterfaceInfo.getStatus() == InterfaceStatusEnum.INTERFACE_OFFLINE.getValue(), ErrorCode.SYSTEM_ERROR,"该接口未开启(未上线)");

        // 获取当前登录用户，以获得其 ak 和 sk
        User curUser = userService.getLoginUser(request);
        String ak = curUser.getAccessKey();
        String sk = curUser.getSecretKey();
        // 新建一个 ApiClient 来封装 ak sk
        ApiClient apiClient = new ApiClient(ak, sk);
        // 从接口调用请求中的用户请求参数，并将用户请求的参数转换为 model 对象
        String userReqParams = interfaceInfoCallRequest.getUserReqParams();
        Gson gson = new Gson();
        SampleModel model = gson.fromJson(userReqParams, SampleModel.class);
        // 通过携带了当前用户 ak sk 的 ApiClient 调用对应接口，拿到返回信息
        String sampleInfoByPost = apiClient.getSampleInfoByPost(model);

        return ResultUtils.success(sampleInfoByPost);
    }
}
