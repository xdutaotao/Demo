package com.gzfgeh.spring.service;

import com.gzfgeh.spring.dao.UserMapper;
import com.gzfgeh.spring.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by guzhenfu on 2017/9/1.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public User selectById(String id){
        return mapper.selectById(id);
    }
}
