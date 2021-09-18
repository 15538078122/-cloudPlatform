package com.hd.auserver.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@TableName("account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity extends Model<AccountEntity> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("enterprise")
    private String enterprise;

    @TableField("account")
    private String account;

    @JSONField(serialize = false)
    @TableField("password")
    private String password;


    @TableField("create_time")
    private Date createTime;

    @TableLogic("delete_flag")
    boolean deleteFlag;
}
