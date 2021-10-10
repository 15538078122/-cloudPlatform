package com.hd.micromonitorservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wli
 * @since 2021-09-01
 */
@Setter
@Getter
@TableName("sy_monitor")
public class SyMonitorEntity extends Model<SyMonitorEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("service_name")
    private String serviceName;

    @TableField("show_name")
    private String showName;

    @TableField("heartbeat_tm")
    private Date heartbeatTm;

    @TableField("client_id")
    private String clientId;
}
