package me.ele.demo.pay;


import me.ele.demo.pay.dto.BaseCallbackDto;
import me.ele.demo.pay.dto.BaseCallbackResultDto;
import me.ele.demo.pay.dto.BaseCreateOrderDto;
import me.ele.demo.pay.dto.BaseCreateOrderResultDto;
import me.ele.demo.pay.dto.BasePayDto;
import me.ele.demo.pay.exception.PayServiceException;

/**
 * 支付和回调策略
 */
public interface IPayStrategyService {


    /**
     * 创建支付订单
     *
     * @param createOrderDto
     * @return
     */
    BaseCreateOrderResultDto createOrder(BaseCreateOrderDto createOrderDto) throws PayServiceException;

    /**
     * 接收回调
     *
     * @param callBackDto
     * @return
     */
    BaseCallbackResultDto reciveCallback(BaseCallbackDto callBackDto) throws PayServiceException;


    /**
     * 绑定dto和service
     *
     * @param payDtoClass
     * @param payService
     */
    void bindPayService(Class<? extends BasePayDto> payDtoClass, IPayService payService);


}
