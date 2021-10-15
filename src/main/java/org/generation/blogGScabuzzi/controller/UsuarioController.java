package org.generation.blogGScabuzzi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.model.Usuario;
import org.generation.blogGScabuzzi.model.dtos.LoginDTO;
import org.generation.blogGScabuzzi.model.dtos.UsuarioLoginDTO;
import org.generation.blogGScabuzzi.repository.UsuarioRepository;
import org.generation.blogGScabuzzi.service.UsuarioService;
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
	private UsuarioService service;

	@Autowired
	private UsuarioRepository repository;

	@PostMapping("/logar")
	public ResponseEntity<LoginDTO> logar(@Valid @RequestBody UsuarioLoginDTO userParaAutenticar) {
		return service.pegarCredenciais(userParaAutenticar);
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<Object> cadastrar(@Valid @RequestBody Usuario novoUsuario) {
		return service.cadastrarUsuario(novoUsuario).map(resp -> ResponseEntity.status(201).body(resp)) // metodo service.cadastrarUsuario vai verificar
																										// se o email já existe e dar um retorno
																										// CREATED ou Null
				.orElseThrow(() -> {											// Caso for Null entrará no "orElseThrow"
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,	// E retornará um BAD_REQUEST dizendo que o email já existe no banco de dados
							"Email existente, cadastre outro email!.");
				});

	}

	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("{id_usuario}")
	public ResponseEntity<Usuario> getById(@PathVariable(value = "id_usuario") Long idUsuario) {
		return repository.findById(idUsuario).map(resp -> ResponseEntity.status(200).body(resp)).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"ID inexistente, passe um ID valido para pesquisa!");
		});
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Usuario>> getAllByNome(@PathVariable(value = "nome") String nome) {
		List<Usuario> Lista = repository.findAllByNomeContainingIgnoreCase(nome);

		if (Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@PutMapping("/put")
	public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody Usuario novoUsuario) {
		return ResponseEntity.status(201).body(repository.save(novoUsuario));
	}

	@DeleteMapping("/delete/{id_usuario}")
	public ResponseEntity<Usuario> deleteUsuario(@PathVariable(value = "id_usuario") Long idUsuario) {
		Optional<Usuario> objetoOptional = repository.findById(idUsuario);

		if (objetoOptional.isPresent()) {
			repository.deleteById(idUsuario);
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(400).build();
		}
	}
}
