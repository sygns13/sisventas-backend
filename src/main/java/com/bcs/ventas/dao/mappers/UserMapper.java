package com.bcs.ventas.dao.mappers;

import com.bcs.ventas.model.entities.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface UserMapper extends GeneralMapper<User> {

    Long getTotalElements(Map<String, Object> var1);

}
