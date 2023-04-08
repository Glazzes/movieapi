package com.glaze.movieapi.utils;

import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;

public final class ExceptionHandlerUtils {
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

    public static Locale getLocaleFromRequest(HttpServletRequest request) {
        String language = request.getHeader(ACCEPT_LANGUAGE_HEADER);
        if(language != null) {
            return Locale.forLanguageTag(language);
        }

        return Locale.US;
    }

}
