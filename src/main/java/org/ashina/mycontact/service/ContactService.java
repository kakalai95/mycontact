package org.ashina.mycontact.service;

import org.ashina.mycontact.entity.Contact;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {

    Iterable<Contact> findAll();

    public Page<Contact> search(String term, String email, String sdt, int page, int size);

    Contact findOne(Integer id);

    void save(Contact contact);

    void delete(Integer id);
    int totalPage();

}
