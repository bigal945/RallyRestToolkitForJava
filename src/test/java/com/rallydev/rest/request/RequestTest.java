package com.rallydev.rest.request;

import com.rallydev.rest.util.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class RequestTest {
    
    private Request createRequest() {
        return new Request() {
            @Override
            public String toUrl() {
                return "";
            }
        };    
    }
    
    @Test
    public void shouldBeAbleToAddParams() {
        Request r = createRequest();
        Assert.assertEquals(r.get_params().size(), 0);
        
        r.addParam("Name", "Value");
        Assert.assertEquals(r.get_params().size(), 1);
        
        r.addParam("Name2", "Value2");
        Assert.assertEquals(r.get_params().size(), 2);
        
        r.get_params().clear();
        Assert.assertEquals(r.get_params().size(), 0);
    }

    @Test
    public void shouldBeAbleToSetParams() {
        Request r = createRequest();
        Assert.assertEquals(r.get_params().size(), 0);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("Name", "Value"));
        r.set_params(params);
        Assert.assertSame(params, r.get_params());
        Assert.assertEquals(r.get_params().size(), 1);
    }
}
