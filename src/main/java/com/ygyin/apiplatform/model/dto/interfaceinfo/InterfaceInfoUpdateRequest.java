package com.ygyin.apiplatform.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求(管理员)
 *
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * 接口 id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口启动状态 (0-关闭, 1-开启)
     */
    private Integer status;

    /**
     * 请求参数
     */
    private String reqParams;

    /**
     * 请求头
     */
    private String reqHeader;

    /**
     * 响应头
     */
    private String respHeader;

    /**
     * 请求类型
     */
    private String method;

    private static final long serialVersionUID = 1L;
}