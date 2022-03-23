package poc.mongo.mongoapp.controllers.filters;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

import static poc.mongo.mongoapp.commons.HeadersConstants.TRACE_ID;

@Component
public class LogRequestFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(final HttpServletRequest request, final String message) {

        MDC.put(TRACE_ID, request.getHeader(TRACE_ID));
    }

    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {}
}
