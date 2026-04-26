package cn.com.mfqh.service.user.infrastructure.mediatr.core;

/**
 * A handler for give command
 *
 **/
public interface CommandHandler<C extends Command> {
    void handle(C t);
}
