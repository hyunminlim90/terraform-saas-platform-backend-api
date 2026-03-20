package click.opentofu.sprout.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import click.opentofu.sprout.servlet.filter.DomainFilter;
import click.opentofu.sprout.servlet.filter.JwtAccessTokenFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<DomainFilter> domainFilterRegistration() {
        FilterRegistrationBean<DomainFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new DomainFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtAccessTokenFilter> jwtAccessTokenFilterRegistration(JwtAccessTokenFilter jwtAccessTokenFilter) {
        FilterRegistrationBean<JwtAccessTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAccessTokenFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }
}
