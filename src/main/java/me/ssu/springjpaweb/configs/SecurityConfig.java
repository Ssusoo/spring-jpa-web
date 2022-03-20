package me.ssu.springjpaweb.configs;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.accounts.AccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    // TODO JPA를 사용하기 때문에 당연히 Datasource가 들어있음.
    private final DataSource dataSource;

    // TODO Spring Security(권한과 인증)-1
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO 요청에 대한 권한(authorizeRequests)
        http.authorizeRequests()
                // TODO 전체 열람 권한(mvcMatchers / check-email 빼주기)
                .mvcMatchers("/", "/login", "/sign-up", "/check-email-token",
                        "/email-login", "/check-email-login", "/login/link").permitAll()
                // TODO Get 요청 허용
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                // TODO 다른 요청(anyRequest) & 인증을 필요로 함(authenticated)
                .anyRequest().authenticated();

        // TODO 로그인 처리
        http.formLogin()
                .loginPage("/login").permitAll();

        // TODO 로그아웃 처리
        http.logout()
                .logoutSuccessUrl("/");
        // TODO 로그인 기억
        http.rememberMe()
                .userDetailsService(accountService)
                // TODO Username, 토큰(랜덤, 매번바뀜), 시리즈(랜덤, 고정)
                .tokenRepository(tokenRepository());
    }

    // TODO
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }

    // TODO Favicon(Spring-Boot에서 제공하는 ignoring 처리-2
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // TODO PathRequest이후 다양한 메소드로 처리할 수도 있음.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}

