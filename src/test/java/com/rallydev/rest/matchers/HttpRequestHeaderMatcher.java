package com.rallydev.rest.matchers;

import com.squareup.okhttp.Request;
import org.mockito.ArgumentMatcher;

public class HttpRequestHeaderMatcher extends ArgumentMatcher<Request> {
    private String name;
    private String value;

    public HttpRequestHeaderMatcher(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean matches(Object o) {
        if (o instanceof Request) {
            Request h = (Request) o;
            String header = h.header(name);
            return header != null && header.equals(value);
        }
        return false;
    }
}
