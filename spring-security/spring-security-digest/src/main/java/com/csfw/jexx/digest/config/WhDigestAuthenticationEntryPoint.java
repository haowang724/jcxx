package com.csfw.jexx.digest.config;

import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;

/**
 * @USER wh
 * @DATE 2020/6/10
 * @Description
 */
public class WhDigestAuthenticationEntryPoint extends DigestAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet(){
        super.afterPropertiesSet();
        setRealmName(WebSecurityConfig.REALM);
    }
}
