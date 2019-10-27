package me.ele.demo.pay.service;


import lombok.extern.slf4j.Slf4j;
import me.ele.demo.pay.IPayService;
import me.ele.demo.pay.IPayStrategyService;
import me.ele.demo.pay.dto.BaseCallbackDto;
import me.ele.demo.pay.dto.BaseCallbackResultDto;
import me.ele.demo.pay.dto.BaseCreateOrderDto;
import me.ele.demo.pay.dto.BaseCreateOrderResultDto;
import me.ele.demo.pay.dto.BasePayDto;
import me.ele.demo.pay.exception.PayServiceErrorMessage;
import me.ele.demo.pay.exception.PayServiceException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付和回调策略
 */
@Service
@Slf4j
public class PayStrategySerivceImpl implements IPayStrategyService {

    /**
     * 支付的dto class与Service对应关系Map
     */
    private Map<Class, IPayService> payServiceMap = new HashMap<>();

    /**
     * 创建支付订单
     *
     * @param createOrderDto
     * @return
     */
    @Override
    public BaseCreateOrderResultDto createOrder(BaseCreateOrderDto createOrderDto) throws PayServiceException {
        try {
            return (BaseCreateOrderResultDto) doProcess(createOrderDto);
        } catch (PayServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建订单异常,参数：{}", createOrderDto, e);
            throw new PayServiceException(PayServiceErrorMessage.CREATE_ORDER_ERROR, e);
        }
    }


    /**
     * 接收回调处理
     *
     * @param callBackDto
     * @return
     */
    @Override
    public BaseCallbackResultDto reciveCallback(BaseCallbackDto callBackDto) throws PayServiceException {
        try {
            return (BaseCallbackResultDto) doProcess(callBackDto);
        } catch (PayServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("回调异常,参数：{}", callBackDto, e);
            throw new PayServiceException(PayServiceErrorMessage.CALLBACK_ORDER_ERROR, e);
        }

    }

    /**
     * 统一封装
     * 签名-->处理
     * 业务异常封装
     * @param basePayDto
     * @return
     * @throws PayServiceException
     */
    private BasePayDto doProcess(BasePayDto basePayDto) throws PayServiceException {
        IPayService payService = payServiceMap.get(basePayDto.getClass());
        log.info("dto:{} -> Service:{}", basePayDto.getClass().getSimpleName(), payService.getClass().getSimpleName());
        if (!payService.checkSign(basePayDto)) {
            throw new PayServiceException(PayServiceErrorMessage.SIGN_ERROR);
        }
        payService.checkSign(basePayDto);
        return payService.doProcess(basePayDto);
    }


    /**
     * 将参数类型和service绑定
     *
     * @param payDtoClass
     * @param payService
     */
    @Override
    public void bindPayService(Class<? extends BasePayDto> payDtoClass, IPayService payService) {
        payServiceMap.putIfAbsent(payDtoClass, payService);
    }


}
