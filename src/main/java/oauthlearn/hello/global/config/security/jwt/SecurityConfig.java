package oauthlearn.hello.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import oauthlearn.hello.global.common.LogFilter;
import oauthlearn.hello.domain.member.domain.Role;
import oauthlearn.hello.domain.member.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable() // 쿠키, 세션 사용하지 않음

                .authorizeRequests()// URL별 권한 권리
                    .antMatchers("/", "/login", "/v3/api-docs/**", "/swagger-ui/**", "/favicon.ico/**").permitAll() // 허용
                    .antMatchers("/uesrs/**").hasRole(Role.USER.name()) // USER 권한만 접근 가능
                    .anyRequest().authenticated() // 이외의 URL 모두 인증 필요

                .and()
                    .logout().logoutSuccessUrl("/")

                .and()
                    .addFilterBefore(new LogFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)

                .oauth2Login().loginPage("/login").userInfoEndpoint()
                .userService(customOAuth2UserService); // 리소스 서버(Google, Kakao)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시

        return httpSecurity.build();
    }
}
