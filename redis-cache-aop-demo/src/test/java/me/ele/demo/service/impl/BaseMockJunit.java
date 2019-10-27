package me.ele.demo.service.impl;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * 可参考
 * https://blog.csdn.net/u011236357/article/details/51197965
 */
public class BaseMockJunit {


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


}
