package com.postgresql.ytdemo2.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Get the originally requested URL from the session
            String targetUrl = null;
            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                targetUrl = savedRequest.getRedirectUrl();
            }

            if (authorities.contains(new SimpleGrantedAuthority("ROLE_SENIOR"))) {

                if (targetUrl != null && targetUrl.contains("/deleteById")) {
                    response.sendRedirect("/deleteById");
                }
                else if (targetUrl != null && targetUrl.contains("/signUp")) {
                    response.sendRedirect("/signUp");
                }
                else if (targetUrl != null && targetUrl.contains("/deleteUser")) {
                    response.sendRedirect("/deleteUser");
                }
                else if (targetUrl != null && (targetUrl.contains("/addPet") || targetUrl.contains("/updateById"))) {
                    response.sendRedirect(targetUrl);
                }
                else {
                    response.sendRedirect("/homePage");

                }


            }
            else if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                if (targetUrl != null && (targetUrl.contains("/addPet") || targetUrl.contains("/updateById"))) {
                    response.sendRedirect(targetUrl);
                } else if (targetUrl != null && targetUrl.contains("/deleteById")) {
                    response.sendRedirect("/login?denied");
                }
                else if (targetUrl != null && targetUrl.contains("/signUp")) {
                    response.sendRedirect("/login?denied");
                }
                else if (targetUrl != null && targetUrl.contains("/deleteUser")) {
                    response.sendRedirect("/login?denied");
                }
                else {
                    response.sendRedirect("/homePage");
                }
            }

            else {
                response.sendRedirect("/login?denied");
            }
        }
    }







