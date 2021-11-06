package org.generation.blogGScabuzzi.controladores;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.modelos.Postagem;
import org.generation.blogGScabuzzi.repositorios.PostagemRepository;
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
@RequestMapping("/postagens")
@Api(tags = "POSTAGEMS - Controlador", description = "Utilitario de Postagem")
@CrossOrigin("*")
public class PostagemController {

	private @Autowired PostagemRepository repository;

	@ApiOperation(value = "Busca lista de todos as Postagens no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna com todos as Postagens"),
			@ApiResponse(code = 204, message = "Retorno sem Postagens")
	})
	@GetMapping("/todos")
	public ResponseEntity<List<Postagem>> GetAll() {
		return ResponseEntity.ok(repository.findAll()); // findAllPostagem = um endPoint com a capacidade de trazer
														// todas as postagens
	}

	@ApiOperation(value = "Busca postagem por Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna postagem existente ou inexistente"),
			@ApiResponse(code = 400, message = "Retorno inexistente")
	})
	@GetMapping("{id_postagem}")
	public ResponseEntity<Postagem> getById(@PathVariable(value = "id_postagem") Long idPostagem) {
		return repository.findById(idPostagem).map(resp -> ResponseEntity.status(200).body(resp)).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"ID inexistente, passe um ID valido para pesquisa!");
		});
	}

	@ApiOperation(value = "Busca tema pelo titulo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna postagens existentes ou inexistentes"),
			@ApiResponse(code = 204, message = "Retorno inexistente")
	})
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable(value = "titulo") String titulo) {
		List<Postagem> Lista = repository.findAllByTituloContainingIgnoreCase(titulo);

		if (Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@ApiOperation(value = "Busca tema pelo corpo de texto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna postagens existentes ou inexistentes"),
			@ApiResponse(code = 204, message = "Retorno inexistente")
	})
	@GetMapping("/texto/{texto}")
	public ResponseEntity<List<Postagem>> getAllByTexto(@PathVariable(value = "texto") String texto) {
		List<Postagem> Lista = repository.findAllByTextoContainingIgnoreCase(texto);

		if (Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@ApiOperation(value = "Salva nova postagem no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna postagem cadastrada"),
			@ApiResponse(code = 400, message = "Erro na requisição")
	})
	@PostMapping("/post")
	public ResponseEntity<Postagem> salvarPostagem(@Valid @RequestBody Postagem novaPostagem) { // @Valid = Valida as
																								// informaçoes passadas
																								// no Model
		// @RequestBody = Pega as informaçoes passadas no corpo do Postman
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@ApiOperation(value = "Atualizar postagem existente")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna postagem atualizada"),
			@ApiResponse(code = 400, message = "Id de postagem invalido")
	})
	@PutMapping("/put") // METODO PUT ENSINADO EM AULA
	public ResponseEntity<Postagem> atualizarPostagem(@Valid @RequestBody Postagem novaPostagem) {
		return ResponseEntity.status(201).body(repository.save(novaPostagem));
	}

	@ApiOperation(value = "Deletar postagem existente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Postagem deletada!"),
			@ApiResponse(code = 400, message = "Id de postagem invalido")
	})
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
