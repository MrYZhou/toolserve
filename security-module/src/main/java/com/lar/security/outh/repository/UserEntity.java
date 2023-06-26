package com.lar.security.outh.repository;

import lombok.Data;

import java.util.Date;

/**
 * 用户表(User)实体类
 */

@Data
public class UserEntity {

    /** 主键 */
    private String id;
    /** 用户名 */
    private String userName;
    /** 昵称 */
    private String nickName;
    /** 密码 */
    private String password;
    /** 账号状态（0正常 1停用） */
    private String status;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户类型（0管理员，1普通用户）
     */
    private String userType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 盐值
     */
    private String salt;
}
