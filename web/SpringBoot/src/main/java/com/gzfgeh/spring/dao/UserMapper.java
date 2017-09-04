package com.gzfgeh.spring.dao;

import com.gzfgeh.spring.dao.model.User;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Created by guzhenfu on 2017/9/1.
 */
public interface UserMapper {
    @Select("select * from student where id = #{id}")
    User selectById(String id);
}
