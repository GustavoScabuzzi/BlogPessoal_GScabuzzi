package org.generation.blogGScabuzzi.repository;

import java.util.List;

import org.generation.blogGScabuzzi.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemaRepository extends JpaRepository<Tema, Long> {

	public List<Tema> findAllByTemaContainingIgnoreCase(String tema);
}