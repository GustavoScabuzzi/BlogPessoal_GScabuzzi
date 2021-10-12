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
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping("{id_postagem}")
	public ResponseEntity<Postagem> getById(@PathVariable(value = "id_postagem") Long idPostagem) {
		return repository.findById(idPostagem).map(resp -> ResponseEntity.status(200).body(resp))
				.orElseThrow(() -> {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND,
							"ID inexistente, passe um ID valido para pesquisa!");
				});
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable(value = "titulo") String titulo){
		List<Postagem> Lista = repository.findAllByTituloContainingIgnoreCase(titulo);
		
		if(Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@GetMapping("/texto/{texto}")
	public ResponseEntity<List<Postagem>> getAllByTexto(@PathVariable(value = "texto") String texto){
		List<Postagem> Lista = repository.findAllByTextoContainingIgnoreCase(texto);
		
		if(Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@PostMapping("/post")
	public ResponseEntity<Postagem> salvarPostagem(@Valid @RequestBody Postagem novaPostagem) { // @Valid = Valida as informaçoes passadas no Model
																						// @RequestBody = Pega as informaçoes passadas no corpo do Postman
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@PutMapping("/put") // METODO PUT ENSINADO EM AULA
	public ResponseEntity<Postagem> atualizarPostagem(@Valid @RequestBody Postagem novaPostagem) {
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@DeleteMapping("/delete/{id_postagem}") // METODO DELETE ENSINADO EM AULA
	public ResponseEntity<Postagem> deletarUsuario(@PathVariable(value = "id_postagem") Long idPostagem) {
		Optional<Postagem> objetoOptional = repository.findById(idPostagem);

		if (objetoOptional.isPresent()) {
			repository.deleteById(idPostagem);
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(400).build();
		}
	}

}
