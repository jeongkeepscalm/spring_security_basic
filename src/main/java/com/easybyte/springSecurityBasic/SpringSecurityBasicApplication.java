package com.easybyte.springSecurityBasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// main 패키지 안에 있으므로 추가할 필요 x
// @EnableJpaRepositories("com.easybyte.springSecurityBasic.repository")
// @EntityScan("com.easybyte.springSecurityBasic.model")
@SpringBootApplication
public class SpringSecurityBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityBasicApplication.class, args);
	}

}
