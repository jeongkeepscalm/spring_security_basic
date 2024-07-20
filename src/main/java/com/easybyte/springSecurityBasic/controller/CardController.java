package com.easybyte.springSecurityBasic.controller;

import com.easybyte.springSecurityBasic.model.Cards;
import com.easybyte.springSecurityBasic.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

  private final CardsRepository cardsRepository;

  @GetMapping("/myCards")
  public List<Cards> getCardDetails(@RequestParam long id) {
    List<Cards> cards = cardsRepository.findByCustomerId(id);
    if (cards != null ) {
      return cards;
    }else {
      return null;
    }
  }

}
