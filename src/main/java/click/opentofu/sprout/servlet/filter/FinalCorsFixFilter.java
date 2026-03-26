package click.opentofu.sprout.servlet.filter;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class FinalCorsFixFilter implements Filter {

    private static final String ALLOWED_ORIGIN = "https://studio.opentofu.click";

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        chain.doFilter(request, response);

        String origin = httpRequest.getHeader("Origin");

        if (ALLOWED_ORIGIN.equals(origin)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);

            String allowCredentials = httpResponse.getHeader("Access-Control-Allow-Credentials");
            if (allowCredentials != null) {
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            }
        }
    }
}
