package com.ygyin.apiplatform.model.dto.userapiinfo;

import com.ygyin.apiplatform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * 用户使用对应字段进行查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserApiInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 调用接口的用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long apiId;

    /**
     * 接口剩余可调用次数
     */
    private Integer remainNum;

    /**
     * 当前调用总次数
     */
    private Integer totalNum;

    /**
     * 接口调用状态 (0-正常调用, 1-禁止调用)
     */
    private Integer status;
}