package com.gzfgeh.spring.dao;

import com.gzfgeh.spring.dao.model.User;

/**
 * Created by guzhenfu on 2017/9/1.
 */

public interface UserMapper {
    User selectById(String id);
}
