package cn.com.mfqh.service.user.infrastructure.mediatr.core.exception;

public class NoRequestHandlerException extends RuntimeException {
    public NoRequestHandlerException(String message) {
        super(message);
    }
}
