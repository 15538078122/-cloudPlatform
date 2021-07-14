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
@Accessors(chain = true)
@TableName("sy_func_op_url")
public class SyFuncOpUrlEntity extends Model<SyFuncOpUrlEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * url地址
     */
    @TableField("url")
    private String url;

    /**
     * controller的className.method
     */
    @TableField("perm_code")
    private String permCode;

    /**
     * 功能id
     */
    @TableField("func_op_id")
    private Long funcOpId;

}
