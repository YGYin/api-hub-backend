package com.ygyin.apiplatform.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@SpringBootTest
public class UserApiInfoServiceTest {

    @Resource
    private UserApiInfoService userApiInfoService;

    @Test
    void callNumCount() {
        boolean isCount = userApiInfoService.callNumCount(22L, 1808767204406345730L);
        Assertions.assertTrue(isCount);
    }
}