package com.myhome.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .headers().frameOptions().disable()
                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은
                // 필요없으므로 생성안함.
//                .and()
                .cors()
                .and().authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/v1/signin", "/v1/signout", "/v1/social/**", "/v1/signup", "/v1/**", "/v1/tokenreissue", "/v1/user/check/*", "/h2-console/**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//				.antMatchers("/v1/*").hasRole("MASTER")
//				.antMatchers(HttpMethod.POST, "/v1/*").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//				.antMatchers(HttpMethod.GET, "/v1/*").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
//				.antMatchers(HttpMethod.PUT, "/v1/*").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
//				.antMatchers(HttpMethod.POST, "/*/board/**").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
				.anyRequest().hasRole("GUEST") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
//                .anyRequest().hasAnyRole( "MASTER")
                .and()
                    .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),  // jwt token 필터를 id/password 인증 필터 전에 넣는다
                    UsernamePasswordAuthenticationFilter.class);


    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
                "/swagger/**");

    }

}