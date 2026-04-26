package cn.com.mfqh.service.user.infrastructure.mediatr.core.exception;

public class NoCommandHandlerException extends RuntimeException {
    public NoCommandHandlerException(String message) {
        super(message);
    }
}
