package com.csfw.jexx.digest.config;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * @USER wh
 * @DATE 2020/6/10
 * @Description
 */
public class HttpComponentsClientHttpRequestFactoryDigestAuth extends HttpComponentsClientHttpRequestFactory {
    public HttpComponentsClientHttpRequestFactoryDigestAuth(HttpClient client) {
        super(client);
    }

    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        return createHttpContext(uri);
    }

    private HttpContext createHttpContext(URI uri) {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local auth cache
        DigestScheme digestAuth = new DigestScheme();
        // If we already know the realm name
        digestAuth.overrideParamter("realm", "realm-wh");
        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort());
        authCache.put(targetHost, digestAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        return localcontext;
    }
}
