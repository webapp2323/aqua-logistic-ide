package ua.kiev.prog.oauth2.loginviagoogle.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SimpleCORSFilter> simpleCORSFilter() {
        FilterRegistrationBean<SimpleCORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SimpleCORSFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}