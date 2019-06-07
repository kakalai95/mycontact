package org.ashina.mycontact.service;

import java.util.Collection;
import java.util.List;

import org.ashina.mycontact.entity.Contact;
import org.ashina.mycontact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Iterable<Contact> findAll() {
		return contactRepository.findAll();
	}

	@Override
	public Page<Contact> search(String term, String email, String sdt, int page, int size) {
		Sort.Order order = new Sort.Order( Direction.ASC , "name").ignoreCase();
		return contactRepository.getAndPaging(term, email, sdt, new PageRequest(page, size, new Sort(order)));
		
	}

	@Override
	public Contact findOne(Integer id) {
		return contactRepository.findOne(id);
	}

	@Override
	public void save(Contact contact) {
		contactRepository.save(contact);
	}

	@Override
	public void delete(Integer id) {
		contactRepository.delete(id);
	}

	public int totalPage() {
		Iterable<Contact> contacts = contactRepository.findAll();

		if (contacts instanceof Collection) {
			return ((Collection<?>) contacts).size();
		}
		int counter = 0;
		for (Object i : contacts) {
			counter++;			
		}
		int x = counter/7+1;
		return x;
	}
}
