package com.ygyin.apiplatform.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户调用接口信息
 * @TableName user_api_info
 */
@TableName(value ="user_api_info")
@Data
public class UserApiInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}