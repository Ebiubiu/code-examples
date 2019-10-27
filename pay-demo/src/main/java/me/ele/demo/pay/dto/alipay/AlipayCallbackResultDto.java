package me.ele.demo.pay.dto.alipay;

import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCallbackResultDto;

/**
 * 支付宝支付回调处理结果
 */
@Data
@Builder
public class AlipayCallbackResultDto implements BaseCallbackResultDto {
}
