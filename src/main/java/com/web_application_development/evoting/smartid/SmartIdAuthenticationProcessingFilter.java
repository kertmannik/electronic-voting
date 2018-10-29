package com.web_application_development.evoting.smartid;

import ee.sk.smartid.SmartIdAuthenticationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class SmartIdAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public SmartIdAuthenticationProcessingFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SmartIdAuthenticationResult smartIdAuthenticationResult =
                (SmartIdAuthenticationResult) request.getSession().getAttribute(SmartIdController.AUTHENTICATION_RESULT_KEY);
        if (smartIdAuthenticationResult != null) {
            request.getSession().removeAttribute(SmartIdController.AUTHENTICATION_RESULT_KEY);
            return new SmartIdAuthenticationToken(
                    smartIdAuthenticationResult.getAuthenticationIdentity(),
                    null,
                    null
            );
        }
        return null;
    }
}