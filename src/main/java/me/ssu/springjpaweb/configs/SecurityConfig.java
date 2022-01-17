package me.ssu.springjpaweb.configs;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO Spring Security(권한과 인증)-1
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO 요청에 대한 권한(authorizeRequests)
        http.authorizeRequests()
                // TODO 전체 열람 권한(mvcMatchers)
                .mvcMatchers("/", "/login", "/sign-up", "/check-email-token",
                             "/email-login", "/login-by-email", "/search/study").permitAll()
                // TODO Get 를요청 허용
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                // TODO 다른 요청(anyRequest) & 인증을 필요로 함(authenticated)
                .anyRequest().authenticated();
    }

    // TODO Favicon File(시큐리티 타게할 필요가 없음)-2
    // TODO Spring-Boot가 제공하는 ignoring() 처리
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // TODO PathRequest이후 다양한 메소드로 처리할 수도 있음.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
