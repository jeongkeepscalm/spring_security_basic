package com.easybyte.springSecurityBasic.config;

import com.easybyte.springSecurityBasic.Filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    CsrfTokenRequestAttributeHandler ctrah = new CsrfTokenRequestAttributeHandler();
    ctrah.setCsrfRequestAttributeName("_csrf"); // 구현체 내부적으로 해당 필드를  "_csrf" 로 set 을 해주나 가독성을 위해 적어준 코드

    http
            .securityContext(context -> context.requireExplicitSave(false))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                    .csrfTokenRequestHandler(ctrah)
                    .ignoringRequestMatchers( "/register", "/contact")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            .authorizeHttpRequests((res) -> res
                    /*.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                    .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                    .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                    .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")*/
                    .requestMatchers("/myAccount").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/myLoans").hasRole("USER")
                    .requestMatchers("/myCards").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/user").authenticated()
                    .requestMatchers("/notices", "/register", "/contact").permitAll());
    http.formLogin(withDefaults());
    http.httpBasic(withDefaults());
    return http.build();

    /*
      http.formLogin(withDefaults());
      http.httpBasic(withDefaults());
        요청이 HTML 형태 또는 UI 애플리케이션을 통해 올 수 있게 허용한다.

      csrf.disable():
        csrf 보안 공격을 비활성화
        유저 등록 api 호출 시, 시큐리티가 csrf 공격으로 받아들여 403 에러 발생시켰다.

      csrf.ignoringRequestMatchers("/contact", "/register"))
        공공 api 일 경우에만 설정해준다.
        민감한 정보가 없는 공공의 엔드포인트는 csrf 보안이 필요 없다.
        "/notices" 을 추가하지 않은 이유는 csrf 공격은 post 방식일 경우에만 적용되기에 get 방식인 "/notices" 는 적용할 필요가 없다.

      csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        CSRF 토큰을 쿠키에 저장할 때, 해당 쿠키의 HttpOnly 속성을 false 로 설정
        HttpOnly 속성이 false 로 설정되면, JavaScript 에서 해당 쿠키에 접근할 수 있게 된다.

      http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        1. 스프링 시큐리티 자체 필터인 BasicAuthenticationFilter.class 가 실행
        2. csrf 토큰이 발급
        3. 이후에 CsrfCookieFilter 필터 실행 (첫 로그인 후, 헤더와 쿠키 값을 보낸다.)

      http.securityContext(context -> context.requireExplicitSave(false))
        보안 컨텍스트 변경시 자동으로 저장하도록 설정

      http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        프론트 서버를 따로 두지 않았을 경우 필요한 코드
        첫 로그인시, 항상 JSESSIONID 를 생성하게 한다.
        이 때, 프론트 서버에도 이에 대한 설정이 필요하다.

     */

    /**
     * 모든 요청 거부
     * 인증, 인가 후 HTTP ERROR 403 반환
     */
    /*
      http.authorizeHttpRequests((res) -> res.anyRequest().denyAll());
      http.formLogin(withDefaults());
      http.httpBasic(withDefaults());
      return http.build();
    */

    /**
     * 모든 요청 허용
     * 개발 및 테스트 환경에서 보안없이 접근하게 허용
     */
    /*
      http.authorizeHttpRequests(res -> res.anyRequest().permitAll());
      http.formLogin(withDefaults());
      http.httpBasic(withDefaults());
      return http.build();
    */

  }

  /**
   * InMemory 에 유저를 생성하여 저장한다.
   *  1. InMemoryUserDetailsManager 인스턴스 생성하여 반환
   *     InMemoryUserDetailsManager: UserDetailsManager 의 구현체
   *  2. PasswordEncoder 를 리턴하는 메소드에 빈 주입
   */
//  @Bean
  public InMemoryUserDetailsManager userDetailsManager() {
    /*
      1.
      UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("123").authorities("admin").build();
      UserDetails user = User.withDefaultPasswordEncoder().username("user").password("123").authorities("read").build();
     */
    // 2. 빈 주입된 PasswordEncoder 사용
    UserDetails admin = User.withUsername("admin").password("123").authorities("admin").build();
    UserDetails user = User.withUsername("user").password("123").authorities("read").build();
    return new InMemoryUserDetailsManager(admin, user);
  }

  /**
   * InMemory 방식을 사용하지 않고 JDBC 인증 방식을 사용한다.
   * new JdbcUserDetailsManager(dataSource):
   *    데이터베이스 설정한 값을 읽어 데이터 소스 객체를 자동으로 웹 애플리케이션 내부에 생성한다.
   * JdbcUserDetailsManager 에 정의된 table, column, query 만 사용 가능.
   */
//  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  /*
     해당 프로젝트에서 PasswordEncoder 에서 지정한 방식을 사용하라고 명시

     PasswordEncoder Interface
        encode(): PasswordEncoder 에 설정한 그에 상응하는 암호화 적차가 이뤄진다.
        matches(): 입력받은 비밀번호화 암호화 된 비밀번호를 비교한다.

     NoOpPasswordEncoder:
        시큐리티에서 제공하는 가장 간단한 패스워드 인코더
        비밀번호를 일반 텍스트 처리(비밀번호 암호화 및 해싱을 수행하지 않는다.)
        프로덕션 환경에서 권장되지 않는다.(유저의 자격증명을 코드 내부와 웹 어플리케이션의 메모리 내부에 저장하기 때문이다.)

     운영계에 고려해야할 passwordEncoder
        BCryptPasswordEncoder: BCrypt 해싱 알고리즘 사용
        SCryptPasswordEncoder: BCryptPasswordEncoder 고급 버전
        Argon2PasswordEncoder: 가장 안전하지만 많은 자원 요구
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    /*return NoOpPasswordEncoder.getInstance();*/
    return new BCryptPasswordEncoder();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}

