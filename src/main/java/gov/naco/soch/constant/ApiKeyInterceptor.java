package gov.naco.soch.constant;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApiKeyInterceptor implements HandlerInterceptor{

	private static final String SECRET_KEY = "9e4d1334-b8d9-11ed-afa1-0242ac120002";

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String secretKey = request.getHeader("X-Secret");
        if (secretKey != null && secretKey.equals(SECRET_KEY)) {
        	logger.warn("Authorized request: " + request.getRequestURI());
            return true;
        } else {
            logger.warn("Unauthorized request: " + request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid secret key");
            return false;
        }
    }
}