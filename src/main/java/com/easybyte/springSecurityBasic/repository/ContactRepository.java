package com.easybyte.springSecurityBasic.repository;

import com.easybyte.springSecurityBasic.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}
