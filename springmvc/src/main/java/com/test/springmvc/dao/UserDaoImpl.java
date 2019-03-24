package com.test.springmvc.dao;

import com.test.springmvc.annotation.Repository;

/**
 * @author : jihong.zjh@alibaba-inc.com
 * @date : 2019-03-21
 * Description:
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public void add() {
        // mock
        System.out.println("insert data to db success.");
    }

    @Override
    public void update() {
        // mock
        System.out.println("update data success");
    }
}
