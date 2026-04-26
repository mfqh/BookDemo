package cn.com.mfqh.service.user.infrastructure.mediatr.core;

import java.util.concurrent.CompletableFuture;

/**
 * Defines a mediator to encapsulate dispatching and publishing interaction patterns
 *
 **/
public interface Mediator {
    /**
     * Dispatches a [Request] to a single [RequestHandler] synchronously
     *
     * @param <TRequest> the request to be executed
     * @return
     **/
    <TRequest extends Request<TResponse>, TResponse> TResponse dispatch(TRequest request);

    /**
     * Dispatches a [Request] to a single [RequestHandler] to execute asynchronously on another thread
     *
     * @param request The request to be executed
     * @return
     ***/
    <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> dispatchAsync(TRequest request);

    /***
     * Dispatch a [Command] to a single [CommandHandler] synchronously
     * @param command Commandto dispatch for execution
     **/
    void dispatch(Command command);

    /**
     * Dispatch a [Command] to a single [CommandHandler] to execute asynchronously on another thread
     **/
    CompletableFuture<Void> dispatchAsync(Command command);
}
