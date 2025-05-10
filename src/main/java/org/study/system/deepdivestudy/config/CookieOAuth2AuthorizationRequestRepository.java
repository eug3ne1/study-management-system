package org.study.system.deepdivestudy.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;


@Component
public class CookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String COOKIE_NAME = "OAUTH2_AUTH_REQUEST";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        try {
            return CookieUtils.deserialize(request, COOKIE_NAME, OAuth2AuthorizationRequest.class);
        } catch (IllegalArgumentException e) {
            // cookie not present or failed to deserialize
            return null;
        }
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.delete(request, response, COOKIE_NAME);
        } else {
            CookieUtils.serialize(response, COOKIE_NAME, authorizationRequest, COOKIE_EXPIRE_SECONDS, true, true);
        }
    }


    /**
     * OAuth2 flow will invoke this variant, so we delete the cookie here.
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        CookieUtils.delete(request, response, COOKIE_NAME);
        return authRequest;
    }
}

