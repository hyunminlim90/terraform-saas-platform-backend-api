package click.opentofu.sprout.servlet.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

@Slf4j
public final class CustomCsrfFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository tokenRepository;
    private RequestMatcher requireCsrfProtectionMatcher = DEFAULT_CSRF_MATCHER;
    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
    private XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
    public static final RequestMatcher DEFAULT_CSRF_MATCHER = new DefaultRequiresCsrfMatcher();
    private static final String SHOULD_NOT_FILTER = "SHOULD_NOT_FILTER" + CsrfFilter.class.getName();

    public CustomCsrfFilter(CsrfTokenRepository tokenRepository) {
        Assert.notNull(tokenRepository, "tokenRepository cannot be null");
        this.tokenRepository = tokenRepository;
    }

    private static final List<RequestMatcher> skipMatchers = List.of(
        (request) -> request.getRequestURI().equals("/api/v1/auth/csrf-token"),
        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/stream-emitter")
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return
            Boolean.TRUE.equals(request.getAttribute(SHOULD_NOT_FILTER)) ||
            skipMatchers.stream().anyMatch((matcher) -> { return matcher.matches(request); }); 
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.warn("------------------------------------------");
            log.debug("Request Header: {} = {}", headerName, request.getHeader(headerName));
            log.warn("------------------------------------------");
        }
        DeferredCsrfToken deferredCsrfToken = this.tokenRepository.loadDeferredToken(request, response);
        request.setAttribute(DeferredCsrfToken.class.getName(), deferredCsrfToken);
        this.requestHandler.handle(request, response, deferredCsrfToken::get);
        if (!this.requireCsrfProtectionMatcher.matches(request)) {
            if (log.isTraceEnabled()) {
                log.warn("------------------------------------------");
                log.trace(
                    "Did not protect against CSRF since request did not match " +
                    this.requireCsrfProtectionMatcher
                );
                log.warn("------------------------------------------");
            }
            filterChain.doFilter(request, response);
            return;
        }
        CsrfToken csrfToken = deferredCsrfToken.get();
        String actualToken = request.getHeader("X-XSRF-TOKEN");

        log.warn("------------------------------------------");
        log.debug("CSRF Token from request header: {}", actualToken);
        log.debug("CSRF Token from repository: {}", csrfToken != null ? csrfToken.getToken() : "null");
        log.warn("------------------------------------------");

        if (csrfToken == null || csrfToken.getToken() == null) {
            log.warn("CSRF Token is missing from repository");
            AccessDeniedException exception = new AccessDeniedException("Missing CSRF token");
            this.accessDeniedHandler.handle(request, response, exception);
            return;
        }

        if (!equalsConstantTime(csrfToken.getToken(), actualToken)) {
            boolean missingToken = deferredCsrfToken.isGenerated();
            log.warn("------------------------------------------");
            log.debug("Invalid CSRF token found for {}", UrlUtils.buildFullRequestUrl(request));
            log.warn("------------------------------------------");
            AccessDeniedException exception = (!missingToken) ?
                new AccessDeniedException("Invalid CSRF token", new InvalidCsrfTokenException(csrfToken, actualToken)) :
                new AccessDeniedException("Missing CSRF token", new MissingCsrfTokenException(actualToken));
            this.accessDeniedHandler.handle(request, response, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public static void skipRequest(HttpServletRequest request) {
        request.setAttribute(SHOULD_NOT_FILTER, Boolean.TRUE);
    }

    public void setRequireCsrfProtectionMatcher(RequestMatcher requireCsrfProtectionMatcher) {
        Assert.notNull(requireCsrfProtectionMatcher, "requireCsrfProtectionMatcher cannot be null");
        this.requireCsrfProtectionMatcher = requireCsrfProtectionMatcher;
    }

    public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        Assert.notNull(accessDeniedHandler, "accessDeniedHandler cannot be null");
        this.accessDeniedHandler = accessDeniedHandler;
    }

    public void setRequestHandler(XorCsrfTokenRequestAttributeHandler requestHandler) {
        Assert.notNull(requestHandler, "requestHandler cannot be null");
        this.requestHandler = requestHandler;
    }

    private static boolean equalsConstantTime(String expected, String actual) {
        if (expected == actual) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        byte[] expectedBytes = Utf8.encode(expected);
        byte[] actualBytes = Utf8.encode(actual);
        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }

    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final HashSet<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }

        @Override
        public String toString() {
            return "CsrfNotRequired " + this.allowedMethods;
        }
    }
}
