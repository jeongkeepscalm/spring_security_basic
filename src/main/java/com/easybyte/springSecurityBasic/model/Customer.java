package com.easybyte.springSecurityBasic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Entity
@Getter @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long id;

    private String name;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    private String role;

    /*
    @OneToMany: 단일 유저는 여러 권한을 가질수 있게 매핑된다.
    mappedBy = "customer",: Authority 클래스의 필드와 맞춰준다.

    @JsonIgnore:
        해당 필드는 JSON 응답으로 보내지지 않는다.
        authorities 필드는 민감한 정보이므로 프론트서버로 보내지 않고, 백엔드 애플리케이션에서만 사용한다.
 */
    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @Column(name = "create_dt")
    @JsonIgnore
    private Date createDt;

}
