package com.devflow.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static void sendJsonResponse(HttpServletResponse response, Object data) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(toJson(data));
            out.flush();
        } catch (Exception e) {
            // response already handled
        }
    }

    public static void sendErrorJson(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(String.format("{\"success\":false,\"error\":\"%s\"}", message.replace("\"", "\\\"")));
            out.flush();
        } catch (Exception e) {
            // response already handled
        }
    }
}
