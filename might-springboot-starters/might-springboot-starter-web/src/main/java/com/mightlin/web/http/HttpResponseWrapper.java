package com.mightlin.web.http;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpResponseWrapper extends HttpServletResponseWrapper {
    public HttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }
}
