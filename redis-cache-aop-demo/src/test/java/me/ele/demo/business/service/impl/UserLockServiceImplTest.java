package me.ele.demo.business.service.impl;

import me.ele.demo.business.dto.UserDto;
import me.ele.demo.business.service.IUserLockService;
import me.ele.demo.ut.base.BaseJunit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UserLockServiceImplTest extends BaseJunit {

    @Autowired
    private IUserLockService userLockService;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    UserDto userDto = UserDto.builder().id(100L).name("test1").age(10).build();

    private static final AtomicInteger atomicInteger = new AtomicInteger();

    @Test
    public void addUser() throws Exception {
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                userLockService.updateUser(userDto);
                atomicInteger.incrementAndGet();
            });
        }
        Thread.sleep(11000);
        Assert.assertEquals("验证失败", 1, atomicInteger.intValue());

    }


}