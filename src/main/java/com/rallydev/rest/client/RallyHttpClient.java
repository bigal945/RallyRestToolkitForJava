package com.rallydev.rest.client;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A OkHttp implementation providing connectivity to Rally.  This class does not
 * provide any authentication on its own but instead relies on a concrete subclass to do so.
 */
public class RallyHttpClient {

    protected final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected URI server;
    protected String wsapiVersion = "v2.0";
    protected OkHttpClient client;

    private enum Header {
        Library,
        Name,
        Vendor,
        Version
    }

    private Map<Header, String> headers = new HashMap<Header, String>() {
        {
            put(Header.Library, "Rally Rest API for Java v2.1.1");
            put(Header.Name, "Rally Rest API for Java");
            put(Header.Vendor, "Rally Software, Inc.");
            put(Header.Version, "2.1.1");
        }
    };

    protected RallyHttpClient(URI server) {
        this.server = server;
        client = new OkHttpClient();
    }

    /**
     * Set the unauthenticated proxy server to use.  By default no proxy is configured.
     *
     * @param proxy The proxy server, e.g. {@code new URI("http://my.proxy.com:8000")}
     */
    public void setProxy(URI proxy) {
        client.setProxy(new Proxy(Proxy.Type.DIRECT, new InetSocketAddress(proxy.getHost(), proxy.getPort())));
    }

    /**
     * Set the authenticated proxy server to use.  By default no proxy is configured.
     *
     * @param proxy    The proxy server, e.g. {@code new URI("http://my.proxy.com:8000")}
     * @param userName The username to be used for authentication.
     * @param password The password to be used for authentication.
     */
    public void setProxy(URI proxy, String userName, String password) {
        setProxy(proxy);
        setClientCredentials(proxy, userName, password);
    }

    /**
     * Set the value of the X-RallyIntegrationVendor header included on all requests.
     * This should be set to your company name.
     *
     * @param value The vendor header to be included on all requests.
     */
    public void setApplicationVendor(String value) {
        headers.put(Header.Vendor, value);
    }

    /**
     * Set the value of the X-RallyIntegrationVersion header included on all requests.
     * This should be set to the version of your application.
     *
     * @param value The vendor header to be included on all requests.
     */
    public void setApplicationVersion(String value) {
        headers.put(Header.Version, value);
    }

    /**
     * Set the value of the X-RallyIntegrationName header included on all requests.
     * This should be set to the name of your application.
     *
     * @param value The vendor header to be included on all requests.
     */
    public void setApplicationName(String value) {
        headers.put(Header.Name, value);
    }

    /**
     * Get the current server being targeted.
     *
     * @return the current server.
     */
    public String getServer() {
        return server.toString();
    }

    /**
     * Get the current version of the WSAPI being targeted.
     *
     * @return the current WSAPI version.
     */
    public String getWsapiVersion() {
        return wsapiVersion;
    }

    /**
     * Set the current version of the WSAPI being targeted.
     *
     * @param wsapiVersion the new version, e.g. {@code "1.30"}
     */
    public void setWsapiVersion(String wsapiVersion) {
        this.wsapiVersion = wsapiVersion;
    }

    /**
     * Execute a request against the WSAPI
     *
     * @param requestBuilder the requestBuilder to be executed
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    protected String doRequest(Request.Builder requestBuilder) throws IOException {
        for (Map.Entry<Header, String> header : headers.entrySet()) {
            requestBuilder.addHeader("X-RallyIntegration" + header.getKey().name(), header.getValue());
        }

        return this.executeRequest(requestBuilder.build());
    }

    /**
     * Execute a request against the WSAPI
     *
     * @param request the request to be executed
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    protected String executeRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException(response.networkResponse().toString());
        }
    }

    /**
     * Perform a post against the WSAPI
     *
     * @param url  the request url
     * @param body the body of the post
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    public String doPost(String url, String body) throws IOException {
        Request.Builder builder = new Request.Builder()
            .url(getWsapiUrl() + url)
            .post(RequestBody.create(JSON, body));
        return doRequest(builder);
    }


    /**
     * Perform a put against the WSAPI
     *
     * @param url  the request url
     * @param body the body of the put
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    public String doPut(String url, String body) throws IOException {
        Request.Builder builder = new Request.Builder()
            .url(getWsapiUrl() + url)
            .put(RequestBody.create(JSON, body));
        return doRequest(builder);
    }

    /**
     * Perform a delete against the WSAPI
     *
     * @param url the request url
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    public String doDelete(String url) throws IOException {
        Request.Builder builder = new Request.Builder()
            .url(getWsapiUrl() + url).delete();
        return doRequest(builder);
    }

    /**
     * Perform a get against the WSAPI
     *
     * @param url the request url
     * @return the JSON encoded string response
     * @throws IOException if a non-200 response code is returned or if some other
     *                     problem occurs while executing the request
     */
    public String doGet(String url) throws IOException {
        Request.Builder builder = new Request.Builder()
            .url(getWsapiUrl() + url)
            .get();
        return doRequest(builder);
    }

    /**
     * Release all resources associated with this instance.
     *
     * @throws IOException if an error occurs releasing resources
     */
    public void close() throws IOException {
        //client.getConnectionManager().shutdown();

    }

    protected String setClientCredentials(URI server, String userName, String password) {
        return Credentials.basic(userName, password);
    }

    /**
     * Get the WSAPI base url based on the current server and WSAPI version
     *
     * @return the fully qualified WSAPI base url, e.g. https://rally1.rallydev.com/slm/webservice/1.33
     */
    public String getWsapiUrl() {
        return getServer() + "/slm/webservice/" + getWsapiVersion();
    }
}
