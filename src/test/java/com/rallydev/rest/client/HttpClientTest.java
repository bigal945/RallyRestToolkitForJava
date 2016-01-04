package com.rallydev.rest.client;

import com.rallydev.rest.matchers.HttpRequestBodyMatcher;
import com.rallydev.rest.matchers.HttpRequestHeaderMatcher;
import com.rallydev.rest.matchers.HttpRequestUrlMatcher;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpClientTest {

    private String server = "https://rally1.rallydev.com";
    private RallyHttpClient client;

    @BeforeMethod
    public void setUp() throws URISyntaxException {
        RallyHttpClient client = new RallyHttpClient(new URI(server));
        this.client = spy(client);
    }

    @Test
    public void shouldIntialize() {
        Assert.assertEquals(client.getServer(), server);
    }

    /*@Test
    public void shouldSetProxy() throws Exception {
        URI proxy = new URI("http://my.proxy.com:8000");
        client.setProxy(proxy);
        Assert.assertEquals(client.get_params().getParameter(ConnRoutePNames.DEFAULT_PROXY),
                new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getScheme()));
    }*/

    /*@Test
    public void shouldSetProxyWithCredentials() throws Exception {
        URI proxy = new URI("http://my.proxy.com:8000");
        client.setProxy(proxy, "username", "password");
        Assert.assertEquals(client.get_params().getParameter(ConnRoutePNames.DEFAULT_PROXY),
                new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getScheme()));
        verify(client).setClientCredentials(proxy, "username", "password");
    }*/

    @Test
    public void shouldSetApplicationVendor() throws Exception {
        doReturn("").when(client).executeRequest(any(Request.class));
        client.setApplicationVendor("FooVendor");
        client.doRequest(new Request.Builder());
        verify(client).doRequest(argThat(new HttpRequestHeaderMatcher("X-RallyIntegrationVendor", "FooVendor")).newBuilder());
    }

    @Test
    public void shouldSetApplicationName() throws Exception {
        doReturn("").when(client).executeRequest(any(Request.class));
        client.setApplicationName("FooName");
        client.doRequest(new Request.Builder());
        verify(client).doRequest(argThat(new HttpRequestHeaderMatcher("X-RallyIntegrationName", "FooName")).newBuilder());
    }

    @Test
    public void shouldSetApplicationVersion() throws Exception {
        doReturn("").when(client).executeRequest(any(Request.class));
        client.setApplicationVersion("FooVersion");
        client.doRequest(new Request.Builder());
        verify(client).doRequest(argThat(new HttpRequestHeaderMatcher("X-RallyIntegrationVersion", "FooVersion")).newBuilder());
    }

    @Test
    public void shouldUseDefaultWsapiVersion() {
        Assert.assertEquals(client.getWsapiVersion(), "v2.0");
        Assert.assertEquals(client.getWsapiUrl(), server + "/slm/webservice/v2.0");
    }

    @Test
    public void shouldSetWsapiVersion() {
        client.setWsapiVersion("v3.0");
        Assert.assertEquals(client.getWsapiVersion(), "v3.0");
        Assert.assertEquals(client.getWsapiUrl(), server + "/slm/webservice/v3.0");
    }

    @Test
    public void shouldPost() throws Exception {
        String url = "/defect/12345";
        String body = "{}";
        doReturn("").when(client).doRequest(any(Request.Builder.class));
        client.doPost(url, body);
        verify(client).doRequest(argThat(new HttpRequestBodyMatcher(client.getWsapiUrl() + url, body)).newBuilder());
    }

    @Test
    public void shouldPut() throws Exception {
        String url = "/defect/12345";
        String body = "{}";
        doReturn("").when(client).doRequest(any(Request.Builder.class));
        client.doPut(url, body);
        verify(client).doRequest(argThat(new HttpRequestBodyMatcher(client.getWsapiUrl() + url, body)).newBuilder());
    }

    @Test
    public void shouldDelete() throws Exception {
        String url = "/defect/12345";
        doReturn("").when(client).doRequest(any(Request.Builder.class));
        client.doDelete(url);
        verify(client).doRequest(argThat(new HttpRequestUrlMatcher(client.getWsapiUrl() + url)).newBuilder());
    }

    @Test
    public void shouldGet() throws Exception {
        String url = "/defect/12345";
        doReturn("").when(client).doRequest(any(Request.Builder.class));
        client.doGet(url);
        verify(client).doRequest(argThat(new HttpRequestUrlMatcher(client.getWsapiUrl() + url)).newBuilder());
    }

    @Test
    public void shouldReturnValidResponse() throws Exception {
        client.client = spy(client.client);
        doReturn(createMockResponse("{}")).when(client.client).newCall(any(Request.class)).execute();
        String response = client.doGet("/defect/1234");
        Assert.assertEquals("{}", response);
    }

    @Test(expectedExceptions = IOException.class)
    public void shouldExplodeWithInvalidResponse() throws Exception {
        client.client = spy(client.client);
        doReturn(createMockResponse("")).when(client.client).newCall(any(Request.class)).execute();
        client.doGet("/defect/1234");
    }

    private Response createMockResponse(String responseText) throws Exception {
        Response response = mock(Response.class);

        when(response.isSuccessful()).thenReturn(true);
        when(response.code()).thenReturn(responseText.length() == 0 ? 500 : 200);
        //when(response.body()).thenReturn(new Response.Builder().body(ResponseBody.create(MediaType.parse(responseText),responseText)).build()));

        return response;
    }
}