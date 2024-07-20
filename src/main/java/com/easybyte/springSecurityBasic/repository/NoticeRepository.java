package com.easybyte.springSecurityBasic.repository;


import com.easybyte.springSecurityBasic.model.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {

	/**
	 * @Query: 쿼리를 직접 호출하지 않는다.
	 * 쿼리 안 JPQL 사용
	 * 		JPQL: SQL 포맷으로, 엔터티 클래스와 함께 쿼리 작성을 도와준다.
	 *
	 */
	@Query(value = "from Notice n where CURDATE() BETWEEN noticBegDt AND noticEndDt")
	List<Notice> findAllActiveNotices();

}
