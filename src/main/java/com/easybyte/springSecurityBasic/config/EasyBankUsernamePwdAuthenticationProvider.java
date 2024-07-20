package com.easybyte.springSecurityBasic.config;

import com.easybyte.springSecurityBasic.model.Customer;
import com.easybyte.springSecurityBasic.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DaoAuthenticationProvider 를 사용하지 않고 커스텀하여 사용한다.
 * DaoAuthenticationProvider 에 영향을 미치지 않게 하기 위하여
 */
@Component
@RequiredArgsConstructor
public class EasyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 사용자가 입력한 정보
    String email = authentication.getName();
    String pwd = authentication.getCredentials().toString();
    // 해당 정보를 바탕으로 DB 조회
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
      // Fetch Age details and perform validation to check if age >18
      return new UsernamePasswordAuthenticationToken(email, pwd, userDetails.getAuthorities());
    }else {
      throw new BadCredentialsException("Invalid password!");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    // UsernamePasswordAuthenticationToken: 아이디, 비밀번호로 사용자를 인증한다.
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }

}
