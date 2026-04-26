package cn.com.mfqh.service.user.infrastructure.mediatr.core.exception;

public class DuplicateRegistrationException extends RuntimeException {
    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
