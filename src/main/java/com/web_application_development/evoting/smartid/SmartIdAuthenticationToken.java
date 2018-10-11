package com.web_application_development.evoting.smartid;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
public class SmartIdAuthenticationToken extends AbstractAuthenticationToken {
    private Object principal;
    private Object credentials;
    public SmartIdAuthenticationToken(Object principal, Object credentials,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }
    @Override
    public Object getCredentials() {
        return credentials;
    }
    @Override
    public Object getPrincipal() {
        return principal;
    }
}