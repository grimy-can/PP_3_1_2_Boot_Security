package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(SuccessUserHandler.class);
    private final Map<String, String> roleTargetUrlMap;

    public SuccessUserHandler(Map<String, String> roleTargetUrlMap) {
        this.roleTargetUrlMap = roleTargetUrlMap;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

        for (String role : roles) {
            logger.info("authenticated : " + auth.getName(), auth.getAuthorities());
            if(roleTargetUrlMap.containsKey(role)) {
                response.sendRedirect(roleTargetUrlMap.get(role));
            }
        }
    }
}