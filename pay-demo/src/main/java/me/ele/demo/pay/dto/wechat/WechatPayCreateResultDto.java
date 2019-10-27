package me.ele.demo.pay.dto.wechat;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCreateOrderResultDto;

@Data
@Builder
public class WechatPayCreateResultDto implements BaseCreateOrderResultDto {

    private String code;

    private String orderNo;

    private String customerNo;


}
