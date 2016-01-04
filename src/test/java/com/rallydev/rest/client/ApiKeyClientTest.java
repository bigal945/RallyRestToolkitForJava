package com.rallydev.rest.client;

import com.rallydev.rest.matchers.HttpRequestHeaderMatcher;
import com.squareup.okhttp.Request;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ApiKeyClientTest {

    private String server = "https://rally1.rallydev.com";
    private String apiKey = "foo";
    private ApiKeyClient client;

    @BeforeMethod
    public void setUp() throws URISyntaxException {
        ApiKeyClient client = new ApiKeyClient(new URI(server), apiKey);
        this.client = spy(client);
    }

    @Test
    public void shouldInitialize() {
        Assert.assertEquals(client.getServer(), server);
    }

    @Test
    public void shouldIncludeApiKeyOnRequest() throws Exception {
        doReturn("").when(client).executeRequest(any(Request.class));
        client.doRequest(new Request.Builder());
        verify(client).doRequest(argThat(new HttpRequestHeaderMatcher("zsessionid", apiKey)).newBuilder());
    }
}
