package com.hd.microsysservice.utils;

import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.entity.SyMenuEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * @Author: liwei
 * @Description:
 */
public class VoConvertUtils {
    /**
     * 拷贝对象属性
     *
     * @param from 源对象
     * @param to   目标对象
     */
    public static void convertObject(Object from, Object to) {
        Assert.notNull(from, "源对象不能为空！");
        Assert.notNull(to, "目标对象不能为空！");
        BeanUtils.copyProperties(from, to);
    }
    public static SyMenuVo syMenuToVo(SyMenuEntity syMenuEntity){
        SyMenuVo syMenuVo=new SyMenuVo();
        convertObject(syMenuEntity,syMenuVo);
        return  syMenuVo;
    }
    public static SyMenuEntity syMenuToEntity(SyMenuVo syMenuVo){
        SyMenuEntity syMenuEntity=new SyMenuEntity();
        convertObject(syMenuVo,syMenuEntity);
        return  syMenuEntity;
    }
    public static SyMenuBtnVo syMenuBtnToVo(SyMenuBtnEntity syMenuBtnEntity){
        SyMenuBtnVo syMenuBtnVo=new SyMenuBtnVo();
        convertObject(syMenuBtnEntity,syMenuBtnVo);
        return  syMenuBtnVo;
    }
    public static SyMenuBtnEntity syMenuBtnToEntity(SyMenuBtnVo syMenuBtnVo){
        SyMenuBtnEntity syMenuBtnEntity=new SyMenuBtnEntity();
        convertObject(syMenuBtnVo,syMenuBtnEntity);
        return  syMenuBtnEntity;
    }




}
