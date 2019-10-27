package me.ele.demo.pay;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可参考
 * https://blog.csdn.net/u011236357/article/details/51197965
 */
public class BaseMockJunit {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


}
