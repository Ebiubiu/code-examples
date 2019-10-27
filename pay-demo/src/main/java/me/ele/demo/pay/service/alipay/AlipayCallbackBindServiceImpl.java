package me.ele.demo.pay.service.alipay;

import lombok.extern.slf4j.Slf4j;
import me.ele.demo.pay.BasePayBindService;
import me.ele.demo.pay.dto.alipay.AlipayCallbackDto;
import me.ele.demo.pay.dto.alipay.AlipayCallbackResultDto;
import org.springframework.stereotype.Service;

/**
 * 支付宝回调通知处理service
 */
@Service
@Slf4j
public class AlipayCallbackBindServiceImpl extends BasePayBindService<AlipayCallbackDto, AlipayCallbackResultDto> {

    @Override
    public Class getBindParamClass() {
        return AlipayCallbackDto.class;
    }

    @Override
    public AlipayCallbackResultDto doProcess(AlipayCallbackDto basePayDto) {
        //省略业务逻辑
        log.info("处理支付宝回调通知业务逻辑.....");
        return AlipayCallbackResultDto.builder().build();
    }

    @Override
    public boolean checkSign(AlipayCallbackDto payDto) {
        return true;
    }
}
