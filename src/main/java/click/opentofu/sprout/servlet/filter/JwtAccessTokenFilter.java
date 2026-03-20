package click.opentofu.sprout.servlet.filter;

import java.io.IOException;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import click.opentofu.sprout.util.GeneralUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter implements Filter {

    private final GeneralUtils generalUtils;
    private final RestTemplate restTemplate;
    private PublicKey cachedPublicKey;

    @Value("${sso.publicKeyUrl}")
    private String publicKeyUrl;

    private PublicKey getPublicKey() {
        if (cachedPublicKey == null) {
            log.warn("------------------------------------------");
            log.warn("Fetching public key from SSO server");
            log.warn("------------------------------------------");
            String keyString = restTemplate.getForObject(publicKeyUrl, String.class);
            if (keyString == null) { throw new RuntimeException("get_public_key"); }
            try {
                byte[] keyBytes = Base64.getDecoder().decode(keyString);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                cachedPublicKey = keyFactory.generatePublic(spec);
            } catch (Exception error) {
                throw new RuntimeException(error);
            }
            log.warn("------------------------------------------");
            log.warn("Public key: ");
            if (cachedPublicKey != null) {
                log.warn("Algorithm: " + cachedPublicKey.getAlgorithm());
                log.warn("Format: " + cachedPublicKey.getFormat());
                log.warn("Encoded: \n" + Base64.getEncoder().encodeToString(cachedPublicKey.getEncoded()));
            } else {
                log.warn("Public key is null, cannot be used.");
            }
            log.warn("------------------------------------------");
        }
        return cachedPublicKey;
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        String jwtAccessToken = null;
        String jwtAccessTokenEmail = null;
        PublicKey publicKey = getPublicKey();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (
            path.equals("/api/v1/request/aws-resources/stream-emitter")
        ) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getCookies() != null) {
            Map<String, String> cookies = Arrays.stream(httpRequest.getCookies())
                .collect(Collectors.toMap(
                    (cookie) -> { return cookie.getName(); },
                    (cookie) -> { return cookie.getValue(); }
                ));

            jwtAccessToken = cookies.get("jwtAccessToken");
            log.warn("------------------------------------------");
            log.warn("JWT Access Token: ");
            log.warn(jwtAccessToken);
            log.warn("------------------------------------------");
        }
        if (jwtAccessToken != null) {
            try {
                Jws<Claims> jwtAccessTokenClaims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwtAccessToken);
                Claims jwtAccessTokenClaimsBody = jwtAccessTokenClaims.getBody();
                Date jwtAccessTokenIssuedAt = jwtAccessTokenClaimsBody.getIssuedAt();
                Date jwtAccessTokenExpiration = jwtAccessTokenClaimsBody.getExpiration();
                jwtAccessTokenEmail = jwtAccessTokenClaimsBody.getSubject();
                httpRequest.setAttribute("jwtAccessTokenEmail", jwtAccessTokenEmail);
                Object rolesObject = jwtAccessTokenClaimsBody.get("roles");

                List<String> roles = generalUtils.castToListOfString(rolesObject);
                
                httpRequest.setAttribute("roles", roles);
                log.warn("------------------------------------------");
                log.warn("JWT Access Token Email: " + jwtAccessTokenEmail.split("@")[0]);
                log.warn("JWT Access Token Roles: " + roles);
                log.warn("------------------------------------------");

                log.warn("------------------------------------------");
                log.warn("JWT Access Token Issued At: " + jwtAccessTokenIssuedAt);
                log.warn("JWT Access Token Expiration: " + jwtAccessTokenExpiration);
                log.warn("------------------------------------------");
            } catch (ExpiredJwtException expiredException) {
                log.warn("------------------------------------------");
                log.warn("JWT Access Token has expired");
                log.warn("------------------------------------------");
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "jwtAccessTokenExpired");
                return;
            } catch (JwtException error) { 
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "jwtAccessTokenInvalid - JwtException");
                return;
            } catch (Exception error) {
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "jwtAccessTokenInvalid - Exception");
                return;
            }
        } else {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "isJwtAccessTokenInvalid - jwtAccessToken is Null");
            return;
        }
        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }
}
