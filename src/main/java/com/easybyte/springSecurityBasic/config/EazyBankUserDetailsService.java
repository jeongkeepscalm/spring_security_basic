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

import java.util.List;

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;
  /**
   * 유저를 조회해서 유저 정보를 가진 인스턴스를 반환한다.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new
            UsernameNotFoundException("User details not found for the user: " + username));
    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
    return new User(customer.getEmail(), customer.getPwd(), authorities);
  }
}