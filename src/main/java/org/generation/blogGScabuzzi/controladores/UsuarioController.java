package org.generation.blogGScabuzzi.controladores;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.modelos.Usuario;
import org.generation.blogGScabuzzi.modelos.dtos.LoginDTO;
import org.generation.blogGScabuzzi.modelos.dtos.UsuarioLoginDTO;
import org.generation.blogGScabuzzi.repositorios.UsuarioRepository;
import org.generation.blogGScabuzzi.servicos.UsuarioService;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/usuarios")
@Api(tags = "USUARIOS - Controlador", description = "Utilitario de Usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	private @Autowired UsuarioService service;
	private @Autowired UsuarioRepository repository;

	@ApiOperation(value = "Autentica usuario no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna credenciais de usuario"),
			@ApiResponse(code = 400, message = "Erro na requisição!")
	})
	@PostMapping("/logar")
	public ResponseEntity<LoginDTO> logar(@Valid @RequestBody UsuarioLoginDTO userParaAutenticar) {
		return service.pegarCredenciais(userParaAutenticar);
	}

	@ApiOperation(value = "Salva novo usuario no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna usuario cadastrado"),
			@ApiResponse(code = 400, message = "Erro na requisição")
	})
	@PostMapping("/cadastrar")
	public ResponseEntity<Object> cadastrar(@Valid @RequestBody Usuario novoUsuario) {
		return service.cadastrarUsuario(novoUsuario).map(resp -> ResponseEntity.status(201).body(resp)) // metodo
																										// service.cadastrarUsuario
																										// vai verificar
																										// se o email já
																										// existe e dar
																										// um retorno
																										// CREATED ou
																										// Null
				.orElseThrow(() -> { // Caso for Null entrará no "orElseThrow"
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, // E retornará um BAD_REQUEST dizendo que
																				// o email já existe no banco de dados
							"Email existente, cadastre outro email!.");
				});

	}

	@ApiOperation(value = "Busca lista de usuarios no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna com Usuarios"),
			@ApiResponse(code = 204, message = "Retorno sem Usuario")
	})
	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@ApiOperation(value = "Busca usuario por Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna usuario existente ou inexistente"),
			@ApiResponse(code = 400, message = "Retorno inexistente")
	})
	@GetMapping("{id_usuario}")
	public ResponseEntity<Usuario> getById(@PathVariable(value = "id_usuario") Long idUsuario) {
		return repository.findById(idUsuario).map(resp -> ResponseEntity.status(200).body(resp)).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"ID inexistente, passe um ID valido para pesquisa!");
		});
	}

	@ApiOperation(value = "Busca usuario por nome")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna usuario existente ou inexistente"),
			@ApiResponse(code = 204, message = "Retorno inexistente")
	})
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Usuario>> getAllByNome(@PathVariable(value = "nome") String nome) {
		List<Usuario> Lista = repository.findAllByNomeContainingIgnoreCase(nome);

		if (Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@ApiOperation(value = "Atualizar usuario existente")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna usuario atualizado"),
			@ApiResponse(code = 400, message = "Id de usuario invalido")
	})
	@PutMapping("/put")
	public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody Usuario novoUsuario) {
		return ResponseEntity.status(201).body(repository.save(novoUsuario));
	}

	@ApiOperation(value = "Deletar usuario existente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Caso deletado!"),
			@ApiResponse(code = 400, message = "Id de usuario invalido")
	})
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
