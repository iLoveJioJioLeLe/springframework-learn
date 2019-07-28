package com.yy.springframework.mvc.controller;

import com.yy.springframework.mvc.model.ResponseEntity;
import com.yy.springframework.mvc.model.User;
import com.yy.springframework.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 2019/7/14.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserById")
    @ResponseBody
    public ResponseEntity getUserById(@RequestBody User user,
                                      HttpServletRequest request,
                                      @CookieValue(value = "ut", required = false) String userToken,
                                      @RequestHeader("content-type") String contentType) throws Exception {
        // 获取WebApplication
        WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
        // 获取ServletContext
        ServletContext servletContext = webApplicationContext.getServletContext();
        System.out.println(userToken);
        System.out.println(contentType);
        User user1 =  userService.getUserById(user);
        if (user1 == null) {
            throw new Exception("用户不存在");
        }
        return ResponseEntity.success(user1);
    }
}
