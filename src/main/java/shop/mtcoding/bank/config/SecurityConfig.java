package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import shop.mtcoding.bank.config.jwt.JwtAuthenticationFilter;
import shop.mtcoding.bank.config.jwt.JwtAuthorizationFilter;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.util.CustomResponseUtil;

/*
 * 시큐리티 컨피그 설정 공식 문서
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : passwordEncoder Bean 등록됨");
        return new BCryptPasswordEncoder();
    }

    // 모든 필터 등록은 여기서!! (AuthenticationManager 순환 의존 문제로 내부 클래스로 만들어진 듯, 추측임)
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            log.debug("디버그 : SecurityConfig의 configure");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager));
            http.addFilter(new JwtAuthorizationFilter(authenticationManager));
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : SecurityConfig의 filterChain");
        http.headers().frameOptions().disable(); // iframe 허용안함
        http.csrf().disable(); // csrf 허용안함
        http.cors().configurationSource(configurationSource()); // cors 재정의

        // ExcpetionTranslationFilter (인증 권한 확인 필터)
        http.exceptionHandling().authenticationEntryPoint(
                (request, response, authException) -> {
                    CustomResponseUtil.forbidden(response, "로그인 해주세요");
                });

        /*
         * SessionCreationPolicy.STATELESS
         * 클라이언트가 로그인 request
         * 서버는 User 세션 저장
         * 서버가 response
         * 세션값 사라짐. (즉 유지하지 않음)
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        // httpBasic()은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();

        // 필터 적용
        http.apply(new CustomSecurityFilterManager());

        http.authorizeHttpRequests()
                .antMatchers("/api/transaction/**").authenticated()
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/account/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN) // ROLE_ 안붙여도 됨
                .anyRequest().permitAll();
        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : SecurityConfig의 configurationSource");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        // configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));

        configuration.addAllowedOriginPattern("*"); // 프론트 서버의 주소 등록
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
