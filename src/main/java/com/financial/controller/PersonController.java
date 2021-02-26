package com.financial.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financial.dto.PersonRequestDto;
import com.financial.enttry.Person;
import com.financial.service.PersonService;

import event.EventLocationHeader;

@RestController
@RequestMapping(value = "persons")
public class PersonController {
	
	@Autowired PersonService personService;
	@Autowired private ApplicationEventPublisher eventPublisher;
	
	
	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person, HttpServletResponse response){
		Person savePerson = personService.save(person);
		
		//Adiciona o Location do recurso criado
		eventPublisher.publishEvent(new EventLocationHeader(this, response, savePerson.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(savePerson);
	}
	
	@GetMapping
	public Page<Person> listar(PersonRequestDto personRequestDto, Pageable pageable) {
		
		return personService.listAll(personRequestDto, pageable);
		
	}

}