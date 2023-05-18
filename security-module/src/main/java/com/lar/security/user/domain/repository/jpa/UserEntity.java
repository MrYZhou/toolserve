package com.lar.security.user.domain.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * 用户表(User)实体类
 */
@Table(name = "sys_user" )
@Entity
@Data
public class UserEntity {

    /** 主键 */
    @Id
    @Column(name = "id" )
    private String id;
    /** 用户名 */
    @Column(name = "userName" )
    private String userName;
    /** 昵称 */
    @Column(name = "nickName")
    private String nickName;
    /** 密码 */
    @Column(name = "password")
    private String password;
    /** 账号状态（0正常 1停用） */
    private String status;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /** 用户性别（0男，1女，2未知） */
    private String sex;
    /** 头像 */
    private String avatar;
    /** 用户类型（0管理员，1普通用户） */
    private String userType;
    /** 创建时间 */
    @CreatedDate
    private Date createTime;
    /** 更新时间 */
    @LastModifiedDate
    private Date updateTime;
}
