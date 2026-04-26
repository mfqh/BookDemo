package cn.com.mfqh.service.user.infrastructure.mediatr.spring;

import cn.com.mfqh.service.user.infrastructure.mediatr.core.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SpringMediator implements Mediator {
    private Registry registry;
    private Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public SpringMediator(Registry registry) {
        this.registry = registry;
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> TResponse dispatch(TRequest request) {
        RequestHandler<TRequest, TResponse> handler = (RequestHandler<TRequest, TResponse>) registry.getRequestHandler(request.getClass());
        return handler.handle(request);
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> dispatchAsync(TRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            RequestHandler<TRequest, TResponse> handler = (RequestHandler<TRequest, TResponse>) registry.getRequestHandler(request.getClass());
            return handler.handle(request);
        }, executor);
    }

    @Override
    public void dispatch(Command command) {
        CommandHandler handler = registry.getCommandHandler(command.getClass());
        handler.handle(command);
    }

    @Override
    public CompletableFuture<Void> dispatchAsync(Command command) {
        return CompletableFuture.runAsync(() -> {
            CommandHandler handler = registry.getCommandHandler(command.getClass());
            handler.handle(command);
        }, executor);
    }
}
