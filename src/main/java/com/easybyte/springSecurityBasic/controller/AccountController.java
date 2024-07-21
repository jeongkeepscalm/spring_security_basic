package com.easybyte.springSecurityBasic.controller;

import com.easybyte.springSecurityBasic.model.Accounts;
import com.easybyte.springSecurityBasic.model.Customer;
import com.easybyte.springSecurityBasic.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private final AccountsRepository accountsRepository;

  @GetMapping("/myAccount")
  public Accounts getAccountDetails(@RequestParam long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    authentication.getAuthorities().stream().forEach(v -> {
      log.info("v.getAuthority(): {}", v.getAuthority());
    });
    Accounts accounts = accountsRepository.findByCustomerId(id);
    if (accounts != null) {
      return accounts;
    } else {
      return null;
    }
  }

}
