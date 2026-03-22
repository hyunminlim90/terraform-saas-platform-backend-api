package click.opentofu.sprout.controller.csrf;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/auth")
public class CsrfRequest {

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/csrf-token")
    public ResponseEntity<Void> csrfToken(HttpServletRequest request, HttpServletResponse response) {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);
        log.warn("------------------------------------------");
        log.warn("Generated CSRF Token: " + csrfToken.getToken());
        log.warn("------------------------------------------");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CSRF-TOKEN", csrfToken.getToken());
        response.addHeader("Access-Control-Expose-Headers", "X-CSRF-TOKEN");
        return ResponseEntity.ok().headers(headers).build();
    }
}
