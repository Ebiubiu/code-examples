package me.ele.demo.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ele.demo.aop.annotition.CacheAuto;
import me.ele.demo.aop.annotition.CacheType;
import me.ele.demo.business.dto.UserDto;
import me.ele.demo.business.service.IUserCacheService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class UserCacheServiceImpl implements IUserCacheService {

    @CacheAuto(key = "'add_'+#userDto.id",timeOutSeconds = 10)
    @Override
    public Long addUser(UserDto userDto) {
        return 10L;
    }

    @CacheAuto(key = "USER_ALL")
    @Override
    public List<UserDto> getList() {
        log.info("执行代码---getList");
        int age = new Random().nextInt(1000) % 77;
        return Arrays.asList(
                UserDto.builder().age(age).name("小二").build(),
                UserDto.builder().age(age).name("张三").build(),
                UserDto.builder().age(age).name("李三").build(),
                UserDto.builder().age(age).name("王五").build()
        );
    }

    @CacheAuto(key = "#id", keyPrefix = "getById_")
    @Override
    public UserDto getById(Long id) {
        log.info("执行代码---getById:{}", id);
        return UserDto.builder().id(id).age((int) (id % 100)).name("张三").build();
    }

    @CacheAuto(key = "#userDto.id", keyPrefix = "getById_", type = CacheType.DELETE)
    @Override
    public void update(UserDto userDto) {
        log.info("执行代码---update：{}", userDto);
    }

    @CacheAuto(key = "#id", keyPrefix = "getById_", type = CacheType.DELETE_REPLACE, timeOutSeconds = 60)
    @Override
    public UserDto updateReturn(Long id) {
        log.info("执行代码---update：{}", id);
        return UserDto.builder().id(id).age((int) (id % 100)).name("张三").build();
    }

    @CacheAuto(key = "#id", keyPrefix = "getById_", type = CacheType.DELETE)
    @Override
    public void delete(Long id) {
        log.info("执行代码---delete：{}", id);
    }
}
