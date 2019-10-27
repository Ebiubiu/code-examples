package me.ele.demo.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ele.demo.aop.aspject.RedisCacheLockAspect;
import me.ele.demo.business.dto.UserDto;
import me.ele.demo.business.service.IUserCacheService;
import me.ele.demo.ut.base.BaseJunit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class IUserCacheServiceImplTest extends BaseJunit {

    @Autowired
    private IUserCacheService userCacheService;

    @Autowired
    private RedisCacheLockAspect redisCacheAspect;

    @Test
    public void addUser() {
        Long id1 = userCacheService.addUser(UserDto.builder().id(10L).age(10).name("张三").build());
        Long id2 = userCacheService.addUser(UserDto.builder().id(10L).age(10).name("张三").build());
        Assert.assertEquals("addUser验证失败", id1, id2);
    }

    @Test
    public void getList() {
        List<UserDto> list1 = userCacheService.getList();
        List<UserDto> list2 = userCacheService.getList();
        Assert.assertEquals("getList验证失败", list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            Assert.assertEquals("getList验证失败", list1.get(i), list2.get(i));
        }
    }

    @Test
    public void getById() {
        UserDto userDto = userCacheService.getById(20L);
        UserDto userDto2 = userCacheService.getById(20L);
        Assert.assertEquals("getById验证失败", userDto, userDto2);
    }

    @Test
    public void update() {
        String key = "getById_" + 30L;
        userCacheService.getById(30L);
        Assert.assertNotNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
        userCacheService.delete(30L);
        Assert.assertNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
        userCacheService.getById(30L);
        Assert.assertNotNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
        userCacheService.update(UserDto.builder().id(30L).name("张三").age(20).build());
        Assert.assertNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));

    }

    @Test
    public void updateReturn() {
        String key = "getById_" + 40;
        userCacheService.getById(40L);
        Assert.assertNotNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
        userCacheService.delete(40L);
        Assert.assertNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
        userCacheService.updateReturn(40L);
        Assert.assertNotNull(redisCacheAspect.getRedisTemplateCache().opsForValue().get(key));
    }


}