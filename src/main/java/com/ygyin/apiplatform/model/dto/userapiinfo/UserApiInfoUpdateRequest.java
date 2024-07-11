package com.ygyin.apiplatform.model.dto.userapiinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求(管理员)
 *
 */
@Data
public class UserApiInfoUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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

    private static final long serialVersionUID = 1L;
}