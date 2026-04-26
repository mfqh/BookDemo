package cn.com.mfqh.service.user.infrastructure.mediatr.core;

/**
 * A handler for a request
 *
 **/
public interface RequestHandler<TRequest extends Request<TResponse>, TResponse> {

    /**
     * Handles the request
     *
     * @param request request to be handle
     * @return the response of request
     **/
    TResponse handle(TRequest request);
}
