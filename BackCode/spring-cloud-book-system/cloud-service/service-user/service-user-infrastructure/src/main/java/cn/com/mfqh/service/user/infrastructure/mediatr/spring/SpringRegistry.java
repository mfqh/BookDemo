package cn.com.mfqh.service.user.infrastructure.mediatr.spring;

import cn.com.mfqh.service.user.infrastructure.mediatr.core.*;
import cn.com.mfqh.service.user.infrastructure.mediatr.core.exception.DuplicateRegistrationException;
import cn.com.mfqh.service.user.infrastructure.mediatr.core.exception.NoCommandHandlerException;
import cn.com.mfqh.service.user.infrastructure.mediatr.core.exception.NoRequestHandlerException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is responsible for registering beans from the Spring ApplicationContext.
 *
 **/
public class SpringRegistry implements Registry {
    private final ApplicationContext applicationContext;

    private Map<Class<Request<?>>, RequestHandlerProvider<?>> requestRegistry = new ConcurrentHashMap<>();
    private Map<Class<Command>, CommandHandlerProvider<?>> commandRegistry = new ConcurrentHashMap<>();

    private boolean initialized = false;

    public SpringRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <C extends Request<R>, R> RequestHandler<C, R> getRequestHandler(Class<C> requestClass) {
        if (!this.initialized) {
            this.initializeHandlers();
        }
        RequestHandlerProvider<?> requestHandlerProvider = requestRegistry.get(requestClass);
        if (requestHandlerProvider == null) {
            throw new NoRequestHandlerException("No RequestHandler is registered to handle request of type" + requestClass.getCanonicalName());
        }

        return (RequestHandler<C, R>) requestHandlerProvider.handler();
    }

    @Override
    public <C extends Command> CommandHandler<C> getCommandHandler(Class<C> commandClass) {
        if (!this.initialized) {
            this.initializeHandlers();
        }

        CommandHandlerProvider<?> commandHandlerProvider = commandRegistry.get(commandClass);
        if (commandHandlerProvider == null) {
            throw new NoCommandHandlerException("No CommandHandler is registered to handle request of type" + commandClass.getCanonicalName());
        }

        return (CommandHandler<C>) commandHandlerProvider.handler();
    }

    private void initializeHandlers() {
        synchronized (this) {
            if (!initialized) {
                Arrays.stream(applicationContext.getBeanNamesForType(CommandHandler.class)).forEach(this::registerCommandHandler);
                Arrays.stream(applicationContext.getBeanNamesForType(RequestHandler.class)).forEach(this::registerRequestHandler);
                initialized = true;
            }
        }
    }

    /**
     * Register a RequestHandler from Spring Context by name.
     *
     * @param name Name of bean to register as a RequestHandler.
     **/
    private void registerRequestHandler(String name) {
        RequestHandler<?, ?> requestHandler = (RequestHandler<?, ?>) this.applicationContext.getBean(name);
        Class<?>[] clazzes = GenericTypeResolver.resolveTypeArguments(requestHandler.getClass(), RequestHandler.class);
        Class<Request<?>> requestHandlerType = (Class<Request<?>>) clazzes[0];
        if (requestRegistry.containsKey(requestHandlerType)) {
            throw new DuplicateRegistrationException(String.format("%s already has a register handler. Each request must have a single request handler.", requestHandler));
        }

        RequestHandlerProvider<RequestHandler<?, ?>> requestHandlerProvider = new RequestHandlerProvider<RequestHandler<?, ?>>(applicationContext, (Class<RequestHandler<?, ?>>) requestHandler.getClass());
        requestRegistry.put(requestHandlerType, requestHandlerProvider);
    }

    /**
     * Register a CommandHandler from Spring Context by name
     *
     * @param name Name of bean to register as a CommandHandler
     **/
    private void registerCommandHandler(String name) {
        CommandHandler<?> commandHandler = (CommandHandler<?>) this.applicationContext.getBean(name);
        Class<?>[] clazzes = GenericTypeResolver.resolveTypeArguments(commandHandler.getClass(), CommandHandler.class);
        Class<Command> commandType = (Class<Command>) clazzes[0];
        if (commandRegistry.containsKey(commandType)) {
            throw new DuplicateRegistrationException(String.format("%s already has a register handler. Each command must have a single command handler.", commandType));
        }

        CommandHandlerProvider<CommandHandler<?>> commandHandlerProvider = new CommandHandlerProvider<CommandHandler<?>>(applicationContext, (Class<CommandHandler<?>>) commandHandler.getClass());
        commandRegistry.put(commandType, commandHandlerProvider);
    }
}
