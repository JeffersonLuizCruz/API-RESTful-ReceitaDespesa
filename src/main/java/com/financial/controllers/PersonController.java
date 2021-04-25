package com.financial.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financial.dto.ActivePersonRequestDto;
import com.financial.entities.Person;
import com.financial.services.crudservice.PersonServiceInterfaces;
import com.financial.services.pagemodel.PageModel;
import com.financial.services.pagemodel.PageRequestModel;

import event.EventLocationHeader;

@RestController
@RequestMapping(value = "persons")
public class PersonController {
	
	@Autowired private PersonServiceInterfaces personService;
	@Autowired private ApplicationEventPublisher eventPublisher;
	
	
	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person, HttpServletResponse response){
		Person savePerson = personService.save(person);
		
		//Adiciona o Location do recurso criado
		eventPublisher.publishEvent(new EventLocationHeader(this, response, savePerson.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(savePerson);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody Person person) {
		Person updatePerson = personService.update(id, person);
		
		return ResponseEntity.ok(updatePerson);
		
	}
	
	@PatchMapping(value = "/{id}/active")
	public void updateActivePerson(@PathVariable Long id,@Valid @RequestBody ActivePersonRequestDto active) {
		Person person = personService.getById(id);
		person.setActive(active.transformToActive());
		
		personService.save(person);
		
	}
	
	@GetMapping(value = "/{id}")
	public  ResponseEntity<Person> getById(@PathVariable Long id) {
		Person person = personService.getById(id);
		
		return ResponseEntity.ok(person);
		
	}
	
	@GetMapping
	public ResponseEntity<PageModel<Person>> listAll(
			@RequestParam Map<String, String> params){
			PageRequestModel pr = new PageRequestModel(params);
			PageModel<Person> pm = personService.listAllByOnLazyModel(pr);
			
			return ResponseEntity.ok(pm);
		}
	
	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable Long id) {
		personService.delete(id);
		
	}
	
	
	

}