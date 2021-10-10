package com.hd.microsysservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wli
 * @since 2021-09-22
 */
@Data
@TableName("sys_upgrade")
public class SysUpgradeEntity extends Model<SysUpgradeEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 附件id
     */
    @TableField("attach_id")
    private Long attachId;

    /**
     * 应用类型 
     */
    @TableField("type")
    private Integer type;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 版本名
     */
    @TableField("version_name")
    private String versionName;

    /**
     * 升级说明
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;
}
