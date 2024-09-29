package com.example.actions.auth;


import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    final private RedisTemplate<String, Object> redisTemplate;
    final private Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    public CustomAuthenticationProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = String.valueOf(authentication.getPrincipal());
        String credentials = String.valueOf(authentication.getCredentials());
        if (!validatePrincipalCredential(principal, credentials)) {
            throw new BadCredentialsException("Bad Request Header Credentials");
        }
        return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());
    }

    private boolean validatePrincipalCredential(String principal, String credentials) {
        Object cachedValue = redisTemplate.opsForValue().get(principal);
        logger.info("Checking with redis. key for principal exists:" + (cachedValue != null));
        if (cachedValue != null && cachedValue instanceof String) {
            return String.valueOf(cachedValue).equals(credentials);
        }
        logger.info("No key for principal exists on redis. Executing default check");
        return "super-user".equals(principal) && "super-secret".equals(credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}