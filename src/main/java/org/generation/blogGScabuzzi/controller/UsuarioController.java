package org.generation.blogGScabuzzi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.model.Usuario;
import org.generation.blogGScabuzzi.repository.UsuarioRepository;
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
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> GetAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("{id_usuario}")
	public ResponseEntity<Usuario> getById(@PathVariable(value = "id_usuario") Long idUsuario){
		return repository.findById(idUsuario).map(resp -> ResponseEntity.status(200).body(resp))
				.orElseThrow(() -> {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND,
							"ID inexistente, passe um ID valido para pesquisa!");
				});	
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Usuario>> getAllByNome(@PathVariable(value = "nome") String nome){
		List<Usuario> Lista = repository.findAllByNomeContainingIgnoreCase(nome);
		
		if(Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(200).body(Lista);
		}
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<List<Usuario>> getAllByEmail(@PathVariable(value = "email") String email){
		List<Usuario> Lista = repository.findAllByEmailContainingIgnoreCase(email);
		
		if(Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(200).body(Lista);
		}
	}
	
	@PostMapping("/post")
	public ResponseEntity<Usuario> salvarUsuario(@Valid @RequestBody Usuario novoUsuario){
		return ResponseEntity.status(200).body(repository.save(novoUsuario));
	}
	
	@PutMapping("/put")
	public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody Usuario novoUsuario){
		return ResponseEntity.status(201).body(repository.save(novoUsuario));
	}
	
	@DeleteMapping("/delete/{id_usuario}")
	public ResponseEntity<Usuario> deleteUsuario(@PathVariable(value = "id_usuario") Long idUsuario){
		Optional<Usuario> objetoOptional = repository.findById(idUsuario);
		
		if(objetoOptional.isPresent()) {
			repository.deleteById(idUsuario);
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(400).build();
		}
	}
}
