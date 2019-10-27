package me.ele.demo.pay.dto.alipay;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCreateOrderDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 支付宝生成支付订单参数
 */
@Builder
@Data
public class AlipayCreateOrderDto implements BaseCreateOrderDto {

    @NotNull
    private String orderId;

    @NotNull
    private String cid;

    @NotNull
    @Min(1)
    private Long amount;

    private String orderDate;

}
