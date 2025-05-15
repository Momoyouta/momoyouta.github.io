package com.momoyouta.web_ani_common.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WebUtil {
    public static void renderString(HttpServletResponse response, String result) throws IOException {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
