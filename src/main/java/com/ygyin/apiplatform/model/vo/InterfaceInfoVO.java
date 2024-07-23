package com.ygyin.apiplatform.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息视图（脱敏）
 */
@Data
public class InterfaceInfoVO implements Serializable {

    /**
     * 接口 id
     */
    private Long id;

    /**
     * 创建接口用户
     */
    private Long userId;

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

    /**
     * 接口总调用次数
     */
    private Integer totalNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}