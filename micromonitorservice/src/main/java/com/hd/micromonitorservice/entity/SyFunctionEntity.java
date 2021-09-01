package com.hd.micromonitorservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wli
 * @since 2021-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sy_function")
public class SyFunctionEntity extends Model<SyFunctionEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

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
     * 0:目录；1:功能
     */
    @TableField("type")
    private Short type;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

}
