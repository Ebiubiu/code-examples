package me.ele.demo.pay.exception;

public class PayServiceException extends Exception {

    public PayServiceException() {
    }

    public PayServiceException(String message) {
        super(message);
    }

    public PayServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayServiceException(PayServiceErrorMessage payServiceErrorMessage) {
        super(payServiceErrorMessage.getMessage());
    }

    public PayServiceException(PayServiceErrorMessage payServiceErrorMessage, Throwable cause) {
        super(payServiceErrorMessage.getMessage(), cause);
    }


}
