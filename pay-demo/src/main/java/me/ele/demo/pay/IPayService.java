package me.ele.demo.pay;

import me.ele.demo.pay.dto.BasePayDto;


/**
 * 支付统一处理接口
 * @param <T>
 * @param <R>
 */
public interface IPayService<T extends BasePayDto, R extends BasePayDto> {

    /**
     * 处理逻辑
     * @param payDto
     * @return
     */
    R doProcess(T payDto);

    /**
     * 验证签名
     * @param payDto
     * @return
     */
    boolean checkSign(T payDto);

}
