package com.easybyte.springSecurityBasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

  @GetMapping("/myBalances")
  public String getBalanceDetail() {
    return "balances";
  }
}
