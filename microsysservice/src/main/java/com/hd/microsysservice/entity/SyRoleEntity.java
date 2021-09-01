package com.hd.microsysservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
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
@Accessors(chain = true)
@TableName("sy_role")
public class SyRoleEntity extends Model<SyRoleEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private String enterpriseId;

    /**
     * 状态{1：启用，0：停用}
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 排序号
     */
    @TableField("sort_num ")
    private Integer sortNum;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 数据权限：0：所有；1：本级及以下；2：仅本级
     */
    @TableField("data_privilege")
    private short dataPrivilege;

    /**     * 备注
     */
    @TableField("note")
    private String note;

}
