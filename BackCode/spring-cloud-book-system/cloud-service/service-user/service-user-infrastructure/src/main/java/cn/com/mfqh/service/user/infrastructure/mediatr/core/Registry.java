package cn.com.mfqh.service.user.infrastructure.mediatr.core;


import cn.com.mfqh.service.user.infrastructure.mediatr.core.exception.NoCommandHandlerException;
import cn.com.mfqh.service.user.infrastructure.mediatr.core.exception.NoRequestHandlerException;

/**
 * A registry for handlers for messages that can be dispatched in Spring MediatR
 *
 */
public interface Registry {

    /**
     * Retrieves the RequestHandler for the providded type. If not RequestHandler is registered to handle, the
     * type provided [NoRequestHandlerException] will be throw.
     *
     * @param requestClass the type of request.
     * @return The RequestHandler for the request.
     * @throws NoRequestHandlerException when there is no available RequestHandler for the request.
     **/
    <C extends Request<R>, R> RequestHandler<C, R> getRequestHandler(Class<C> requestClass);

    /**
     * Retrieve a CommandHander for provide type. If no CommandHandler is registered for the command type,
     * [NoCommandHandlerException] will be throw.
     *
     * @param commandClass The type of the command.
     * @return The CommandHandler for the command.
     * @throws NoCommandHandlerException when there is no available CommandHandler to the request.
     **/
    <C extends Command> CommandHandler<C> getCommandHandler(Class<C> commandClass);

}
