package com.easybyte.springSecurityBasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((res) -> res
                    .requestMatchers("/myAccounts", "/myBalances", "/myLoans", "/myCards").authenticated()
                    .requestMatchers("/notices", "/contact", "/register").permitAll());
    http.formLogin(withDefaults());
    http.httpBasic(withDefaults());
    return http.build();

    /*
      http.formLogin(withDefaults());
      http.httpBasic(withDefaults());
          요청이 HTML 형태 또는 UI 애플리케이션을 통해 올 수 있게 허용한다.
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

  /**
   * 해당 프로젝트에서 PasswordEncoder 에서 지정한 방식을 사용하라고 명시
   * NoOpPasswordEncoder:
   *  시큐리티에서 제공하는 가장 간단한 패스워드 인코더
   *  비밀번호를 일반 텍스트 처리(비밀번호 암호화 및 해싱을 수행하지 않는다.)
   *  프로덕션 환경에서 권장되지 않는다.(유저의 자격증명을 코드 내부와 웹 어플리케이션의 메모리 내부에 저장하기 때문이다.)
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

}
