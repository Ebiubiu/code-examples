package me.ele.demo.pay.dto.wechat;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCallbackDto;

@Builder
@Data
public class WechatPayCallbackDto implements BaseCallbackDto {

    private String errorCode;

    private String orderNo;

}
