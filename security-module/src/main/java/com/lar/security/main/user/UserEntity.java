package com.lar.security.main.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/** 用户表(User)实体类 */
@Table(name = "sys_user")
@Entity
@Data
public class UserEntity {

  /** 主键 */
  @Id
  @Column(name = "id")
  private String id;
  /** 用户名 */
  @Column(name = "userName")
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
  private String phoneNumber;
  /** 用户性别（0男，1女，2未知） */
  private String sex;
  /** 头像 */
  private String avatar;
  /** 用户类型（0管理员，1普通用户） */
  private String userType;
  /** 创建人的用户id */
  private Long createBy;
  /** 创建时间 */
  private Date createTime;
  /** 更新人 */
  private Long updateBy;
  /** 更新时间 */
  private Date updateTime;
  /** 删除标志（0代表未删除，1代表已删除） */
  private Integer delFlag;
}
