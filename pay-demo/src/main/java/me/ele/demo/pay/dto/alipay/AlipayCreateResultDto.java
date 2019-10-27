package me.ele.demo.pay.dto.alipay;


import lombok.Builder;
import lombok.Data;
import me.ele.demo.pay.dto.BaseCreateOrderResultDto;

@Builder
@Data
public class AlipayCreateResultDto implements BaseCreateOrderResultDto {

    private String errorCode;

}
