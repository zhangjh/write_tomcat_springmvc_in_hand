package com.test.springmvc.controller;

import com.test.springmvc.annotation.Autowired;
import com.test.springmvc.annotation.Controller;
import com.test.springmvc.annotation.RequestMapping;
import com.test.springmvc.service.UserServiceImpl;
import com.test.springmvc.servlet.domain.Request;
import com.test.springmvc.servlet.domain.Response;

/**
 * @author : jihong.zjh@alibaba-inc.com
 * @date : 2019-03-21
 * Description:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/add")
    public void add(Request request, Response response) {
        System.out.println("request /add");
        userService.add();
        response.outPrint("request /add");
    }

    @RequestMapping("/update")
    public void update(Request request, Response response) {
        System.out.println("request /update");
        userService.update();
        response.outPrint("request /update");
    }
}
