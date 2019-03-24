package com.test.springmvc.service;

import com.test.springmvc.annotation.Autowired;
import com.test.springmvc.annotation.Service;
import com.test.springmvc.dao.UserDaoImpl;

/**
 * @author : jihong.zjh@alibaba-inc.com
 * @date : 2019-03-21
 * Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDaoImpl userDao;

    @Override
    public void add() {
        System.out.println("start to insert data");
        userDao.add();
    }

    @Override
    public void update() {
        System.out.println("start to update data");
        userDao.update();
    }
}
