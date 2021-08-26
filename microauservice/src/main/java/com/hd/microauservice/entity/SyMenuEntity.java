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
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
@Data
@Accessors(chain = true)
@TableName("sy_menu")
public class SyMenuEntity extends Model<SyMenuEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父节点id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private String enterpriseId;


    /**
     * 0:目录；1:菜单；
     */
    @TableField("type")
    private Short type;

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
     * 状态,0:停用，1：启用
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 相关url
     */
    @TableField("url")
    private String url;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 图标样式
     */
    @TableField("icon_class")
    private String iconClass;

    @TableField("is_visible")
    private Boolean isVisible;
    public  SyMenuEntity(){
        isVisible=true;
    }
}
