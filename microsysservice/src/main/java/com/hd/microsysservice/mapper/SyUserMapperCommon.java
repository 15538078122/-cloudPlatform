package com.hd.microsysservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.microsysservice.entity.SyUserEntity;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyUserMapperCommon extends BaseMapper<SyUserEntity> {
    //    @Select("select sleep(5)")
    //@Select("SELECT id FROM sy_user WHERE delete_flag=0 AND account = #{account} AND enterprise_id = #{enterId}")
    //@Select("SELECT id FROM platsys.sy_user WHERE delete_flag=0 AND account = #{account} AND enterprise_id = #{enterId}")
    //Long getUserId(String account,String enterId);

    @Select("SELECT id FROM sy_user WHERE delete_flag=0 AND id_center = #{centerUserId}")
    //@Select("SELECT id FROM platsys.sy_user WHERE delete_flag=0 AND id_center = #{centerUserId}")
    Long getUserIdByCenterUserId(Long centerUserId);
}
