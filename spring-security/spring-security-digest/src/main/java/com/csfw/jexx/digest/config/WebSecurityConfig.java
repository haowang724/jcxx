package com.csfw.jexx.digest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

/**
 * @USER wh
 * @DATE 2020/6/10
 * @Description
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String REALM = "realm-wh";

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("admin").password("admin").roles("ADMIN")
                .and().withUser("test").password("admin").roles("USER");
    }


    private PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(getDigestEntryPoint())
                .and().addFilter(getDigestAuthenticationFilter(getDigestEntryPoint()));

    }

    @Bean
    public WhDigestAuthenticationEntryPoint getDigestEntryPoint() {
        WhDigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new WhDigestAuthenticationEntryPoint();
        digestAuthenticationEntryPoint.setKey("mykey");
        digestAuthenticationEntryPoint.setNonceValiditySeconds(120);
        digestAuthenticationEntryPoint.setRealmName(REALM);
        return digestAuthenticationEntryPoint;
    }

    public DigestAuthenticationFilter getDigestAuthenticationFilter(
            WhDigestAuthenticationEntryPoint digestAuthenticationEntryPoint) throws Exception {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint);
        digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
        return digestAuthenticationFilter;
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

}
