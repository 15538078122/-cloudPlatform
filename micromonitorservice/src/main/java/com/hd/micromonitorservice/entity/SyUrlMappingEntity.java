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
 * @author liwei
 * @since 2021-07-08
 */
@Data
@Accessors(chain = true)
@TableName("sy_url_mapping")
public class SyUrlMappingEntity extends Model<SyUrlMappingEntity> {

    private static final long serialVersionUID=1L;

    /**
     * url地址
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * url地址
     */
    @TableField(value = "url")
    private String url;

    @TableField("perm_code")
    private String permCode;

    /**
     * controller的className.method
     */
    @TableField("handler")
    private String handler;

    /**
     * 注释
     */
    @TableField("notes")
    private String notes;


}
