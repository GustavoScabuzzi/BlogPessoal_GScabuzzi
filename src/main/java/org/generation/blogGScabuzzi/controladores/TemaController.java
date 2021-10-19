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

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	@Autowired
	private TemaRepository  repository;
	
	@GetMapping("/todos")
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("{id_tema}")
	public ResponseEntity<Tema> getById(@PathVariable(value = "id_tema") Long idTema){
		return repository.findById(idTema).map(resp -> ResponseEntity.status(200).body(resp))
				.orElseThrow(() -> {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
							"ID inexistente, passe um ID valido para pesquisa!");
				});
	}
	
	
	@GetMapping("/tema/{nome_tema}")
	public ResponseEntity<List<Tema>> getAllByTema(@PathVariable(value = "nome_tema") String tema){
		List<Tema> Lista = repository.findAllByTemaContainingIgnoreCase(tema);
		
		if(Lista.isEmpty()) {
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(200).body(Lista);
		}
	}
	
	@PostMapping("/post")
	public ResponseEntity<Tema> adicionarTema(@Valid @RequestBody Tema novoTema){
		return ResponseEntity.status(201).body(repository.save(novoTema));
	}
	
	@PutMapping("/put")
	public ResponseEntity<Tema> atualizarTema(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(201).body(repository.save(tema));
	}
	
	@DeleteMapping("/delete/{id_tema}")
	public ResponseEntity<Tema> deletarTema(@PathVariable(value = "id_tema") Long idTema){
		Optional<Tema> objetoOptional = repository.findById(idTema);
		
		if(objetoOptional.isPresent()) {
			repository.deleteById(idTema);
			return ResponseEntity.status(204).build();
		}else {
			return ResponseEntity.status(400).build();
		}
	}
	
}
