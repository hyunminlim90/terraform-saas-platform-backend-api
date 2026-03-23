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

                        /**
                         * 
                         * sync-infra 는 현재 안쓰는 API 같음. (GitLab Pipeline 에서 쓰이던 것 같음)
                         * 
                         */

                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/aws_not_used"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/opentofu-module/aws_not_used"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/sync-infra/gitlab-ci-scripts/aws_not_used"),

                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/reset/aws_not_used"),

                        /**
                         * 
                         * 실제 사용하는 API (모듈 추가하는 곳)
                         * 
                         */

                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/stream-emitter"),
                        
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/load-resource/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/save-resource/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/load-draft-version/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/issue-unique-id/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-plan/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-apply/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/delete-draft/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/exec-tofu-destroy/aws_vpc"),
                        (request) -> request.getRequestURI().equals("/api/v1/request/aws-resources/duplicate-draft/aws_vpc")

                    );
            })
            .addFilterAfter(new CustomCsrfFilter(csrfTokenRepository), CsrfFilter.class);
        return http.build();
    }
}
