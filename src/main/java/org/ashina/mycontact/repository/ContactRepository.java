package org.ashina.mycontact.repository;

import java.util.List;

import org.ashina.mycontact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    List<Contact> findByNameContaining(String term);

String query = "select s from Contact s " + " where (s.name like lower('%' || :ten || '%') OR :ten is null) "
		
		+ " AND (lower(s.email) like lower('%' || :email || '%') OR :email is null) "
		+ " AND (lower(s.phone) like lower('%' || :sdt || '%') OR :sdt is null) "
		;
@Query(query)
public	Page<Contact> getAndPaging(@Param("ten") String ten,@Param("email") String email,
		@Param("sdt") String sdt, Pageable pageRequest);
}