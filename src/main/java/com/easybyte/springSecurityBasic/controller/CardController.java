package com.easybyte.springSecurityBasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

  @GetMapping("/myCards")
  public String getCardDetail() {
    return "cards";
  }
}
