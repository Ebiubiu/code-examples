package me.ele.demo.pay.dto.wechat;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCreateOrderDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 生成银联订单参数
 */
@Builder
@Data
public class WechatCreateOrderDto implements BaseCreateOrderDto {

    @NotNull
    private String orderNo;

    @NotNull
    private String customerNo;

    @NotNull
    @Min(1)
    private Long totalAmount;

}
