package com.csfw.jexx.digest.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * @USER wh
 * @DATE 2020/6/10
 * @Description
 */
public class RestTempleteConfig {

    public RestTemplate getRestTemplate() {
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider())
                .useSystemProperties().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactoryDigestAuth(
                client);

        return new RestTemplate(requestFactory);
    }

    private CredentialsProvider provider() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("root", "pass");
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }
}
