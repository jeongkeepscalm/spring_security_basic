package com.easybyte.springSecurityBasic.controller;

import com.easybyte.springSecurityBasic.model.Loans;
import com.easybyte.springSecurityBasic.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {

  private final LoanRepository loanRepository;

  @GetMapping("/myLoans")
  public List<Loans> getLoanDetails(@RequestParam long id) {
    List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
    if (loans != null) {
      return loans;
    } else {
      return null;
    }
  }

}
