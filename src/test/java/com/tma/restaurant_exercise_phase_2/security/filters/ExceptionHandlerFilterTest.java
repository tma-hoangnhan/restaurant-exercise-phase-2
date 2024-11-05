package com.tma.restaurant_exercise_phase_2.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExceptionHandlerFilterTest {
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @Mock private HandlerExceptionResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.exceptionHandlerFilter = new ExceptionHandlerFilter(resolver);
    }

    @Test
    void doFilterInternal_notAnyException() throws ServletException, IOException {
        // when
        exceptionHandlerFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_throwException() throws ServletException, IOException {
        // given
        doThrow(new RuntimeException("Exception")).when(filterChain).doFilter(request, response);

        // when
        exceptionHandlerFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(resolver, times(1)).resolveException(eq(request), eq(response), eq(null), Mockito.any(RuntimeException.class));
    }

    @Test
    void doFilterInternal_requestParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         exceptionHandlerFilter.doFilterInternal(null, response, filterChain)
        );

        // then
        assertEquals("request is marked non-null but is null", result.getMessage());
    }

    @Test
    void doFilterInternal_responseParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         exceptionHandlerFilter.doFilterInternal(request, null, filterChain)
        );

        // then
        assertEquals("response is marked non-null but is null", result.getMessage());
    }

    @Test
    void doFilterInternal_filterChainParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         exceptionHandlerFilter.doFilterInternal(request, response, null)
        );

        // then
        assertEquals("filterChain is marked non-null but is null", result.getMessage());
    }
}
