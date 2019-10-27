package me.ele.demo.pay.dto.alipay;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCallbackDto;

/**
 * 支付宝支付回调参数对象
 */
@Builder
@Data
public class AlipayCallbackDto implements BaseCallbackDto {

    private String errorCode;

}
