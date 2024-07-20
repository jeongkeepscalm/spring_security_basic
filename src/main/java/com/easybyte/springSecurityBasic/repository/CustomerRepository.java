package com.easybyte.springSecurityBasic.repository;

import com.easybyte.springSecurityBasic.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
  CrudRepository: crud 연산을 위한 코드 자동 생성
  <A,B>
    A: 어느 표에 소속되는지
    B: 기본키 데이터 유형
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
  /*
    JPA 명명규칙
      findBy___
   */
  Optional<Customer> findByEmail(String email);
}
