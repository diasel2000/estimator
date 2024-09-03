package com.estimator.services;

import com.estimator.model.Role;
import com.estimator.model.User;
import com.estimator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            DefaultOidcUser principal = (DefaultOidcUser) oauthToken.getPrincipal();
            String googleId = principal.getAttribute("sub");

            User user = userRepository.findByGoogleID(googleId);

            if (user == null) {
                //customOAuth2UserService.loadUser();
                String redirectUri = "http://localhost:4200/register";

                String redirectUrlWithToken = UriComponentsBuilder.fromUriString(redirectUri).build().toUriString();
                response.sendRedirect(redirectUrlWithToken);
                return;
            }

            List<Role> roleList = userRepository.findRolesByUserID(user.getUserID());
            String token = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<>(roleList));

            String redirectUri = "http://localhost:4200/login";

            String redirectUrlWithToken = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam(token,"token")
                    .build().toUriString();

        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(redirectUrlWithToken);
    }
}