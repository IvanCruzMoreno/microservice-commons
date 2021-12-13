package com.ivanmoreno.commons.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ivanmoreno.commons.services.CommonService;

public class CommonController<E, S extends CommonService<E>> {

	@Autowired
	protected S service;
	
	@GetMapping
	public ResponseEntity<?> showAll() {
		List<E> entities = (List<E>) service.findAll();
		return ResponseEntity.ok().body(entities);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> showAlumno(@PathVariable Long id) {
		Optional<E> entity = service.findById(id);
		
		if(!entity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(entity.get());
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody E entity, BindingResult result) {
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		E entityDB = service.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDB);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	protected ResponseEntity<?> validar(BindingResult result) {
		Map<String, Object> errores = new HashMap<>();
		
		result.getFieldErrors().forEach(error -> {
			errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(errores);
	}
}
