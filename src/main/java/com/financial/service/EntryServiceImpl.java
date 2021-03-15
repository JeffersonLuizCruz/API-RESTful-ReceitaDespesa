package com.financial.service;


import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.financial.entity.Entry;
import com.financial.entity.Person;

import com.financial.repository.EntryRepository;
import com.financial.repository.PersonRepository;
import com.financial.repository.filter.EntryRequestDto;
import com.financial.repository.projection.ResultEntry;
import com.financial.serviceinterfaces.EntryServiceInterfaces;
import com.financial.exceptions.BadRequestException;
import com.financial.exceptions.NotFoundException;

@Service
public class EntryServiceImpl implements EntryServiceInterfaces{

	
	@Autowired private PersonRepository personRepository;
	@Autowired private EntryRepository entryRepository;
	
	
	@Override
	public Entry save(Entry entry) {
		Optional<Person> person = personRepository.findById(entry.getPerson().getId());
		
		if(person == null || !person.get().getActive()) {
			throw new BadRequestException("Usuário já existe ou inativo na base de dados!");
		}
		
		return entryRepository.save(entry);
	}

	@Override
	public Entry update(Long id, Entry entry) {
		Entry saveByEntry = getById(id);
		try {
			
			if(!entry.getPerson().equals(saveByEntry.getPerson())) {
				isActivePerson(saveByEntry);
			}	
			
		} catch (NullPointerException e) {
			
		}
		BeanUtils.copyProperties(entry, saveByEntry, "id", "paymentDate", "expirationDate");
		return entryRepository.save(saveByEntry);
	}

	@Override
	public Entry getById(Long id) {
		Optional<Entry> result = entryRepository.findById(id);
		return result.orElseThrow(() -> new NotFoundException("Não existe lançamento com esse id: " + id));
	}

	@Override
	public Page<Entry> listAllByOnLazyModel(EntryRequestDto entryRequestDto, Pageable pageable) {
		
		return entryRepository.filter(entryRequestDto, pageable);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<ResultEntry> result(EntryRequestDto entryRequestDto, Pageable pageable) {

		return entryRepository.result(entryRequestDto, pageable);
	}
	
	
	private void isActivePerson(Entry entry) {
		Person person = null;
		
		if(entry.getPerson().getId() != null) {
			person = (personRepository.findById(entry.getPerson().getId())
					.orElseThrow(() -> new NotFoundException("Não há esse usuário: " + entry.getPerson().getId())));
		}
		
		if(person == null || !person.getActive()) {
			throw new NotFoundException("Esse usuário está fora do sistema - [OFF]: " + person.getActive());
		}
	}


}
