package org.generation.blogGScabuzzi.controladores;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogGScabuzzi.modelos.Tema;
import org.generation.blogGScabuzzi.repositorios.TemaRepository;
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
@RequestMapping("/temas")
@Api(tags = "TEMAS - Controlador", description = "Utilitario de Temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	private @Autowired TemaRepository repository;

	@ApiOperation(value = "Busca lista de todos os Temas no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna com todos os Temas"),
			@ApiResponse(code = 204, message = "Retorno sem Temas")
	})
	@GetMapping("/todos")
	public ResponseEntity<List<Tema>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@ApiOperation(value = "Busca tema por Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna tema existente ou inexistente"),
			@ApiResponse(code = 400, message = "Retorno inexistente")
	})
	@GetMapping("{id_tema}")
	public ResponseEntity<Tema> getById(@PathVariable(value = "id_tema") Long idTema) {
		return repository.findById(idTema).map(resp -> ResponseEntity.status(200).body(resp)).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"ID inexistente, passe um ID valido para pesquisa!");
		});
	}

	@ApiOperation(value = "Busca tema por nome")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna tema existente ou inexistente"),
			@ApiResponse(code = 204, message = "Retorno inexistente")
	})
	@GetMapping("/tema/{nome_tema}")
	public ResponseEntity<List<Tema>> getAllByTema(@PathVariable(value = "nome_tema") String tema) {
		List<Tema> Lista = repository.findAllByTemaContainingIgnoreCase(tema);

		if (Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200).body(Lista);
		}
	}

	@ApiOperation(value = "Salva novo tema no sistema")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna tema cadastrado"),
			@ApiResponse(code = 400, message = "Erro na requisição")
	})
	@PostMapping("/post")
	public ResponseEntity<Tema> adicionarTema(@Valid @RequestBody Tema novoTema) {
		return ResponseEntity.status(201).body(repository.save(novoTema));
	}

	@ApiOperation(value = "Atualizar tema existente")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retorna tema atualizado"),
			@ApiResponse(code = 400, message = "Id de tema invalido")
	})
	@PutMapping("/put")
	public ResponseEntity<Tema> atualizarTema(@Valid @RequestBody Tema tema) {
		return ResponseEntity.status(201).body(repository.save(tema));
	}

	@ApiOperation(value = "Deletar tema existente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tema deletado!"),
			@ApiResponse(code = 400, message = "Id de tema invalido")
	})
	@DeleteMapping("/delete/{id_tema}")
	public ResponseEntity<Tema> deletarTema(@PathVariable(value = "id_tema") Long idTema) {
		Optional<Tema> objetoOptional = repository.findById(idTema);

		if (objetoOptional.isPresent()) {
			repository.deleteById(idTema);
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(400).build();
		}
	}

}
