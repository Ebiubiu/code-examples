package me.ele.demo.pay;

import me.ele.demo.pay.dto.BasePayDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 支付基础类，主要用于自动绑定参数和对应的Service
 *
 * @param <T>
 * @param <R>
 * @author daiderong
 */
public abstract class BasePayBindService<T extends BasePayDto, R extends BasePayDto> implements IPayService<T, R> {


    @Autowired
    private IPayStrategyService payStrategyService;

    /**
     * 指定绑定的参数类
     * @return
     */
    public abstract Class<T> getBindParamClass();

    @PostConstruct
    public void bindService() {
        payStrategyService.bindPayService(getBindParamClass(), this);
    }
}
