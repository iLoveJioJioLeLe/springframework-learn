package com.yy.springframework.mvc.aop;

import com.yy.springframework.mvc.model.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 2019/7/21.
 */
@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse resp, Exception e) {
        return ResponseEntity.fail(e.getMessage());
    }
}
