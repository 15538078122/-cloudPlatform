package com.hd.microauservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sy_org")
public class SyOrgEntity extends Model<SyOrgEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 上级id
     */
    @TableField("parent_id")
    private Long parentId;

    @TableField("enterprise_id")
    private String enterpriseId;

    /**
     * 本级编号
     */
    @TableField("level_code")
    private String levelCode;

    /**
     * 树形编号
     */
    @TableField("path_code")
    private String pathCode;

    /**
     * 类别{0：组织；1:部门}
     */
    @TableField("type")
    private short type;

    /**
     * 状态{0：停用，1：启用}
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 图标样式
     */
    @TableField("icon_class")
    private String iconClass;


}
