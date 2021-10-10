package com.hd.microsysservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.common.vo.SyFuncOpUrlVo;
import com.hd.microsysservice.entity.SyFuncOpUrlEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyFuncOpUrlMapper extends BaseMapper<SyFuncOpUrlEntity> {
    List<SyFuncOpUrlEntity> selectUserPerm(@Param("userId") Long userId);

    @Select("SELECT url.url,url.perm_code,urlmap.notes " +
            "from sy_func_op_url url JOIN sy_url_mapping urlmap on url.perm_code=urlmap.perm_code WHERE url.func_op_id=#{funcOprId}")
    List<SyFuncOpUrlVo> getfunOpUrl(@Param("funcOprId") Long funcOprId);
}
