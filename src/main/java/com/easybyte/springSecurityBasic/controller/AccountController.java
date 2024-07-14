package com.easybyte.springSecurityBasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  @GetMapping("/myAccounts")
  public String getAccountDetail() {
    return "accounts";
  }
}
