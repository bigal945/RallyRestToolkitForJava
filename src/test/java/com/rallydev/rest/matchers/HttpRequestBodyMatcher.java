package com.rallydev.rest.matchers;

import com.squareup.okhttp.Request;
import org.mockito.ArgumentMatcher;

import java.nio.charset.StandardCharsets;

public class HttpRequestBodyMatcher extends ArgumentMatcher<Request> {
    private String url;
    private String body;

    public HttpRequestBodyMatcher(String url, String body) {
        this.url = url;
        this.body = body;
    }

    public boolean matches(Object o) {
        if (o instanceof Request) {
            Request h = (Request) o;
            return h.body().contentType().charset().equals(StandardCharsets.UTF_8) &&
                    h.url().toString().equals(url) &&
                    h.body().toString().equals(body);
        }
        return false;
    }
}