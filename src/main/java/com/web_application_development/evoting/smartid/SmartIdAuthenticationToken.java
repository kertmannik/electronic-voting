package com.web_application_development.evoting.smartid;

import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
public class SmartIdAuthenticationToken extends AbstractAuthenticationToken {

    private AuthenticationIdentity identity;
    private Object credentials;


    public SmartIdAuthenticationToken(AuthenticationIdentity identity, Object credentials,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.identity = identity;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public AuthenticationIdentity getPrincipal() {
        return identity;
    }
}