package com.example.springemployee.utility;

import jakarta.servlet.http.HttpServletRequest;

public class Local {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
