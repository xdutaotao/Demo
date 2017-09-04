package com.gzfgeh.spring.service;

import com.gzfgeh.spring.dao.UserMapper;
import com.gzfgeh.spring.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by guzhenfu on 2017/9/1.
 */
@Service
public class UserService implements Serializable{

    @Autowired
    private UserMapper userMapper;

    public User selectById(String id){
        return userMapper.selectById(id);
    }
}
