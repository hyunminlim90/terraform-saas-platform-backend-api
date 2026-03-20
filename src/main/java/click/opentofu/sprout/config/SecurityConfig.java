package click.opentofu.sprout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import click.opentofu.sprout.servlet.filter.CustomCsrfFilter;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

        http
        .cors(Customizer.withDefaults())
            .csrf(csrf -> {
                csrf
                    .csrfTokenRepository(csrfTokenRepository)
                    .ignoringRequestMatchers(
                        (request) -> request.getRequestURI().equals("/api/v1/auth/csrf-token"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/unique-id"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/emitter-object"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/opentofu-module/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/gitlab-ci-scripts/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/reset/aws_sprout"),

                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/load-resource/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/save-resource/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/load-draft-version/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/issue-unique-id/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/stream-emitter"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-plan/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-apply/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/delete-draft/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_sprout"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-destroy/aws_sprout"),

                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/duplicate-draft/aws_sprout")
                    );
            })
            .addFilterAfter(new CustomCsrfFilter(csrfTokenRepository), CsrfFilter.class);
        return http.build();
    }
}
