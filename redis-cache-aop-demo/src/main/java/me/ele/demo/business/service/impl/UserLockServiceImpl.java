package me.ele.demo.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ele.demo.aop.annotition.LockAuto;
import me.ele.demo.business.dto.UserDto;
import me.ele.demo.business.service.IUserLockService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLockServiceImpl implements IUserLockService {

    /**
     * 防止多个用户同时更新一个用户的并发问题
     * @param userDto
     * @return
     */
    @LockAuto(key = "'lock_'+#userDto.id", timeOutSeconds = 5)
    @Override
    public Long updateUser(UserDto userDto) {
        log.info("正在添加用户。。。。{}", userDto.getId());
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDto.getId();
    }

}
