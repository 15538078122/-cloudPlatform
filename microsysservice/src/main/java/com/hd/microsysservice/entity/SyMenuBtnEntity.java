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
 * @since 2021-07-12
 */
@Data
@Accessors(chain = true)
@TableName("sy_menu_btn")
public class SyMenuBtnEntity extends Model<SyMenuBtnEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("enterprise_id")
    private String enterpriseId;

    /**
     * 关联菜单id
     */
    @TableField("menu_id")
    private Long menuId;

    /**
     * 状态{1:启用,0:停用}
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 关联操作id
     */
    @TableField("func_op_id")
    private Long funcOpId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    @TableField("html_id")
    private String htmlId;

    /**
     * click handler
     */
    @TableField("jshandler")
    private String jshandler;

    /**
     * 图标样式
     */
    @TableField("icon_class")
    private String iconClass;

    @TableField("is_visible")
    private Boolean isVisible;
    public  SyMenuBtnEntity(){
        isVisible=true;
        enabled=1;
    }
}
