package com.easybyte.springSecurityBasic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.util.NetMask;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "authorities")
@Getter
@Setter
public class Authority {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private Long id;

  private String name;

  @ManyToOne // 여러 권한이 단일 유저에게 매핑될 수 있음을 알려준다.
  @JoinColumn(name = "customer_id")
  private Customer customer;

}
