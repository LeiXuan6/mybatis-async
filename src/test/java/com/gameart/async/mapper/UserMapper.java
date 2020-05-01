package com.gameart.async.mapper;


import com.gameart.async.annotations.AsyncMethod;
import com.gameart.async.annotations.AsyncType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * A org.mybatis.spring sample mapper. This interface will be used by MapperFactoryBean to create a proxy implementation
 * at Spring application startup.
 */
@Mapper
public interface UserMapper {

  com.gameart.async.domain.User getUser(String userId);

//  @AsyncMethod(type = AsyncType.INSERT)
  int insert(@Param("userId") String userId, @Param("name") String name);

}
