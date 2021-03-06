package com.m5a.autenticacion.autenticador.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.m5a.autenticacion.autenticador.util.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int expirationTime = 200;
    public static final String OAUTH2_AUTHORIZATION_NAME = "oauth2_auth_request";
    public static final String OAUTH2_REDIRECT_URI = "redirect_uri";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // TODO Auto-generated method stub

        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_NAME)
                .map(cookie -> CookieUtils.desearilize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);

    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
            HttpServletResponse response) {


        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_NAME);
            CookieUtils.deleteCookie(request, response, OAUTH2_REDIRECT_URI);
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_NAME, CookieUtils.serialize(authorizationRequest),
                expirationTime);

  String dd = request.getParameter("type");

        CustomOAuth2UserService.type = dd;
        String redirectUriAfterLogin = request.getParameter(OAUTH2_REDIRECT_URI);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, OAUTH2_REDIRECT_URI, redirectUriAfterLogin, expirationTime);
        }
       
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);

    }

    public void removeAuthorizationCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_NAME);
        CookieUtils.deleteCookie(request, response, OAUTH2_REDIRECT_URI);
    }

}
