package me.ele.demo.business.service;

import me.ele.demo.business.dto.UserDto;

import java.util.List;

/**
 * @author daiderong
 */
public interface IUserCacheService {


    Long addUser(UserDto userDto);

    List<UserDto> getList();

    UserDto getById(Long id);

    void update(UserDto userDto);

    UserDto updateReturn(Long id);

    void delete(Long id);
}
