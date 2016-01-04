package com.rallydev.rest.request;

import com.rallydev.rest.util.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Base class for all WSAPI requests.</p>  
 * Subclasses classes should provide an implementation of {@link com.rallydev.rest.request.Request#toUrl}
 */
public abstract class Request {
    
    private List<NameValuePair> _params = new ArrayList<NameValuePair>();

    /**
     * Create a new request.
     */
    public Request() {
    }

    /**
     * Get the list of additional parameters included in this request.
     * 
     * @return The list of additional parameters
     */
    public List<NameValuePair> get_params() {
        return _params;
    }

    /**
     * Gets an encoded string of parameters.
     *
     * @param params local modified params if different
     * @return string of encoded parameters
     */
    public String getParamsAsEncodedString(List<NameValuePair> params) {
        QueryString str = new QueryString();
        for (int i = 0; i < params.size(); i++) {
            str.add(params.get(i).getName(), params.get(i).getValue());
        }
        return str.getQuery();

        /*
        If abandon NameValuePair and use a map instead
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        */
    }

    /**
     * Gets a string of parameters with specified encoding.
     *
     * @return string of encoded parameters
     */
    public String getParamsAsEncodedString() {
        return getParamsAsEncodedString(_params);
    }

    /**
     * Set the list of additional parameters included in this request.
     * 
     * @param _params The list of additional parameters
     */
    public void set_params(List<NameValuePair> _params) {
        this._params = _params;
    }

    /**
     * Add the specified parameter to this request.
     * 
     * @param name the parameter name
     * @param value the parameter value
     */
    public void addParam(String name, String value) {
        get_params().add(new NameValuePair(name, value));
    }

    /**
     * <p>Convert this request into a url compatible with the WSAPI.</p>
     * Must be implemented by subclasses.
     * 
     * @return the url representing this request.
     */
    public abstract String toUrl();
}

class QueryString {

    private StringBuffer query = new StringBuffer( );

    public QueryString() {  }

    public QueryString(String name, String value) {
        encode(name, value);
    }

    public synchronized void add(String name, String value) {
        if (query.length() != 0)
            query.append('&');
        encode(name, value);
    }

    private synchronized void encode(String name, String value) {
        try {
            query.append(URLEncoder.encode(name, "UTF-8"));
            query.append('=');
            query.append(URLEncoder.encode(value, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Broken VM does not support UTF-8");
        }
    }

    public String getQuery( ) {
        return query.toString( );
    }

}
