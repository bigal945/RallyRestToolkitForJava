package com.rallydev.rest.matchers;

import com.squareup.okhttp.Request;
import org.mockito.ArgumentMatcher;

public class HttpRequestUrlMatcher extends ArgumentMatcher<Request> {
    private String url;
    private String value;

    public HttpRequestUrlMatcher(String url) {
        this.url = url;
    }

    public boolean matches(Object o) {
        if (o instanceof Request) {
            Request h = (Request) o;
            return h.url().toString().equals(url);
        }
        return false;
    }
}
