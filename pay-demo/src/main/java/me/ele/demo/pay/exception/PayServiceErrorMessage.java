package me.ele.demo.pay.exception;

import lombok.AllArgsConstructor;


/**
 * @author daiderong
 */
@AllArgsConstructor
public enum PayServiceErrorMessage {
    /**
     * 支付service的错误信息
     */
    SIGN_ERROR("签名失败"),

    CREATE_ORDER_ERROR("创建订单失败"),

    CALLBACK_ORDER_ERROR("回调失败");

    String message;

    public String getMessage() {
        return message;
    }


}
