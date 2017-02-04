package net.eulerframework.web.core.exception.web;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class PageNotFoundException extends WebException {

    public PageNotFoundException(HttpServletRequest request) {
        super("Page not found: "+request.getRequestURI(), 404);
    }

}
