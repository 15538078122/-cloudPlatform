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
 * @since 2021-07-30
 */
@Data

@Accessors(chain = true)
@TableName("sy_dict")
public class SyDictEntity extends Model<SyDictEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("enterprise_id")
    private String enterpriseId;

    /**
     * 编号
     */
    @TableField("code")
    private String code;

    /**
     * 排序号
     */
    @TableField("sort_")
    private Integer sort;

    /**
     * 状态{1:启用,0:停用}
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 类别
     */
    @TableField("cate")
    private String cate;

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
