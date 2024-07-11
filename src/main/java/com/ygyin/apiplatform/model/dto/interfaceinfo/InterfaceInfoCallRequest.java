package com.ygyin.apiplatform.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求(管理员)
 *
 */
@Data
public class InterfaceInfoCallRequest implements Serializable {

    /**
     * 接口 id
     */
    private Long id;

    /**
     * 用户传递的请求参数
     */
    private String userReqParams;


    private static final long serialVersionUID = 1L;
}