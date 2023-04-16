package com.glaze.movieapi.utils;

import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerUtilsTest {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @Mock private HttpServletRequest request;

    @Test
    @DisplayName("Given a request with Accept-Language header returns proper locale")
    void testGetLocaleHeaderFromRequest() {
        String language = "es";

        when(request.getHeader(ACCEPT_LANGUAGE)).thenReturn(language);
        Locale locale = ExceptionHandlerUtils.getLocaleFromRequest(request);

        assertThat(locale.getLanguage()).isEqualTo(language);
    }

    @Test
    @DisplayName("Given a request without Accept-Language header returns Locale.US")
    void testGetDefaultLocaleFromRequest() {
        String acceptLanguageHeader = null;

        when(request.getHeader(ACCEPT_LANGUAGE)).thenReturn(acceptLanguageHeader);
        Locale locale = ExceptionHandlerUtils.getLocaleFromRequest(request);

        assertThat(locale).isEqualTo(Locale.US);
    }

}
