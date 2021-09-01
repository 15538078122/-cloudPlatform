package com.hd.micromonitorservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
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
@Accessors(chain = true)
@TableName("sy_func_operator")
public class SyFuncOperatorEntity extends Model<SyFuncOperatorEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("func_id")
    private Long funcId;

    @TableField("name")
    private String name;

    @TableField("note")
    private String note;

}
