package me.ele.demo.pay.service.wechat;

import me.ele.demo.pay.BasePayBindService;
import me.ele.demo.pay.dto.wechat.WechatPayCallbackDto;
import me.ele.demo.pay.dto.wechat.WechatCallbackResultDto;
import org.springframework.stereotype.Service;

/**
 * @author daiderong
 */
@Service
public class WechatPayCallbackBindServiceImpl extends BasePayBindService<WechatPayCallbackDto, WechatCallbackResultDto> {

    @Override
    public Class<WechatPayCallbackDto> getBindParamClass() {
        return WechatPayCallbackDto.class;
    }

    @Override
    public WechatCallbackResultDto doProcess(WechatPayCallbackDto payDto) {
        return WechatCallbackResultDto.builder().build();
    }

    @Override
    public boolean checkSign(WechatPayCallbackDto payDto) {
        return true;
    }
}
