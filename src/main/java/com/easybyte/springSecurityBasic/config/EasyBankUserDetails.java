package com.easybyte.springSecurityBasic.config;

import com.easybyte.springSecurityBasic.model.Customer;
import com.easybyte.springSecurityBasic.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyBankUsernamePwdAuthenticationProvider( AuthenticationProvider 구현체 )
 *    해당 구현체를 커스텀하기 전에는 UserDetailsService 를 통해 사용자 정보를 로드하고,
 *    DaoAuthenticationProvider 가 비밀번호를 비교하여 인증을 수행하였지만,
 *    AuthenticationProvider 를 직접 구현하여 사용자 정보를 로드하고, 비밀번호를 비교하여 인증을 직접 수행하니
 *    EasyBankUserDetails 는 필요없다.
 *
 *    즉, UserDetailsService 를 구현하는 이유는 기본으로 구현되어 있는 DaoAuthenticationProvider 를 사용하기 위함이고,
 *    AuthenticationProvider 구현체를 직접 만들어서 사용하면 UserDetailsService 를 구현할 필요 없다.
 */
//@Service
@RequiredArgsConstructor
public class EasyBankUserDetails implements UserDetailsService {

  private final CustomerRepository customerRepository;

  /**
   * 유저를 조회해서 유저 정보를 가진 인스턴스를 반환한다.
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    String userName, password = null;
    List<GrantedAuthority> authorities = null;
    List<Customer> customer = customerRepository.findByEmail(email);

    if (customer.size() == 0) {
      throw new UsernameNotFoundException("User details not found for the user: " + email);
    } else {
      userName = customer.get(0).getEmail();
      password = customer.get(0).getPwd();
      authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
    }

    return new User(userName, password, authorities);

  }

}
