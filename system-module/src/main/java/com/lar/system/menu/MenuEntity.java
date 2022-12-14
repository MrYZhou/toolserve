package com.lar.system.menu;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "sys_menu")
@Entity
@Data
public class MenuEntity {
  @Id
  @Column(name = "id")
  private String id;
  /** 菜单名 */
  private String menuName;
  /** 路由地址 */
  private String path;
  /** 组件路径 */
  private String component;
  /** 菜单状态（0显示 1隐藏） */
  private String visible;
  /** 菜单状态（0正常 1停用） */
  private String status;
  /** 权限标识 */
  private String perms;
  /** 菜单图标 */
  private String icon;

  private Long createBy;

  private Date createTime;

  private Long updateBy;

  private Date updateTime;
  /** 是否删除（0未删除 1已删除） */
  private Integer delFlag;
  /** 备注 */
  private String remark;
}
