package io.monster.learn.reactive.webflux101.exception;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        // Do not include the "trace" property in JSON Error response that has the stacktrace
        return super.getErrorAttributes(request, false);
    }
}
