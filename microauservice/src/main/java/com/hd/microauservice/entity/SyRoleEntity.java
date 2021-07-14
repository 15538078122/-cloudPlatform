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
@TableName("sy_role")
public class SyRoleEntity extends Model<SyRoleEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租户id
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
    @TableField("sort_")
    private Integer sort;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

}
