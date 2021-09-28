package org.generation.blogGScabuzzi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.model.Postagem;
import org.generation.blogGScabuzzi.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagemController {

	@Autowired
	private PostagemRepository repository;

	@GetMapping
	public ResponseEntity<List<Postagem>> GetAll() {
		return ResponseEntity.ok(repository.findAll()); // findAllPostagem = um endPoint com a capacidade de trazer
														// todas as postagens
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<Postagem> GetById(@PathVariable long id) {
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp)).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
	}

	@GetMapping("/texto/{texto}")
	public ResponseEntity<List<Postagem>> GetByTexto(@PathVariable String texto) {
		return ResponseEntity.ok(repository.findAllByTextoContainingIgnoreCase(texto));
	}

	@PostMapping("/salvar")
	public ResponseEntity<Postagem> salvar(@Valid @RequestBody Postagem novaPostagem) {
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@PutMapping("/atualizar") // METODO PUT ENSINADO EM AULA
	public ResponseEntity<Postagem> atualizar(@Valid @RequestBody Postagem novaPostagem) {
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@PutMapping // METODO PUT ENSINADO EM VIDEO
	public ResponseEntity<Postagem> put(@RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
	}

	@DeleteMapping("/deletar/{id}") // METODO DELETE ENSINADO EM AULA
	public ResponseEntity<Postagem> deletar(@PathVariable(value = "id") Long idPostagem) {
		Optional<Postagem> objetoOptional = repository.findById(idPostagem);

		if (objetoOptional.isPresent()) {
			repository.deleteById(idPostagem);
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(400).build();
		}
	}

	/**
	 * Method delete -> Usado em nas video aulas
	 * 
	 * @author Gustavo Scabuzzi
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

}
