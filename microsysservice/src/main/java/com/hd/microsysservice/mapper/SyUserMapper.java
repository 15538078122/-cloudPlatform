package com.hd.microsysservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.entity.SyUserEntity;
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
public interface SyUserMapper extends BaseMapper<SyUserEntity> {
    @Select("select sleep(5)")
    Long sleep();

    Integer getUserDataPrivilege(@Param("userId") Long userId);

    List<SyUserVo> userbyrole(@Param("enterId") String enterId,@Param("roles") List<String> roles,@Param("orgId") Long orgId, @Param("orderby") String orderby, IPage page);
}
