package com.hd.microauservice.entity;

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
 * @since 2021-07-30
 */
@Data
@Accessors(chain = true)
@TableName("sy_dict_item")
public class SyDictItemEntity extends Model<SyDictItemEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典id
     */
    @TableField("dict_id")
    private Long dictId;

    /**
     * 状态{1:启用,0:停用}
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 排序号
     */
    @TableField("sort_")
    private Integer sort;

    /**
     * 值
     */
    @TableField("code")
    private String code;

    /**
     * 显示名称
     */
    @TableField("name")
    private String name;

}
