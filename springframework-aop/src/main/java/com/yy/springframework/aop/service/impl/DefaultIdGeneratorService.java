package com.yy.springframework.aop.service.impl;

import com.yy.springframework.aop.service.IdGeneratorService;

import java.util.Random;

/**
 * Created by 2019/6/8.
 */
public class DefaultIdGeneratorService implements IdGeneratorService {
    public Long getId() {
        Random random = new Random();
        return random.nextLong();
    }
}
