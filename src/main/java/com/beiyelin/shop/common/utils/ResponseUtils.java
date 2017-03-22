package com.beiyelin.shop.common.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by newmann on 2017/3/22.
 */
public class ResponseUtils {
    /**
     * set response for json for html
     *
     * @param response
     */
    public static void setJsonResponse(HttpServletResponse response) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }
    /**
     *
     * @param response
     */
    public static void setHtmlResponse(HttpServletResponse response) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
    }

}
