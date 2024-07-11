package com.ygyin.apiplatform.model.dto.userapiinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求，管理员给用户增加调用次数
 * 用于接收前端的请求参数
 */
@Data
public class UserApiInfoAddRequest implements Serializable {

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

//    /**
//     * 接口调用状态 (0-正常调用, 1-禁止调用)
//     */
//    private Integer status;

}