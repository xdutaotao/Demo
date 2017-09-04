package com.gzfgeh.spring.dao;

import com.gzfgeh.spring.dao.model.User;

/**
 * mybatis 注解形式 加上 //@Select("select * from student where id = #{id}")
 *          xml形式 去掉 注解
 * Created by guzhenfu on 2017/9/1.
 */
public interface UserMapper {

    //@Select("select * from student where id = #{id}")
    User selectById(String id);

    /**
     * 修改
     */
    int updateById(User order);

    /**
     * 修改
     */
    int insert(User order);
}
