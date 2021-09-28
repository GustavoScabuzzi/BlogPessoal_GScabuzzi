package org.generation.blogGScabuzzi.repository;

import java.util.List;

import org.generation.blogGScabuzzi.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> { // É Long pois a nosso chave primaria é Long

	/**
	 * Metodo utilizado para realizar pesquisa pela coluna titulo da tabela Postagem
	 * 
	 * @param titulo
	 * @return lista com Postagem
	 * @author Gustavo Scabuzzi
	 * @since 1.0
	 */
	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);

	/**
	 * Metodo utilizado para realizar pesquisa pela coluna texto da tabela Postagem
	 * 
	 * @param titulo
	 * @return lista com Postagem
	 * @author Gustavo Scabuzzi
	 * @since 1.0
	 */
	public List<Postagem> findAllByTextoContainingIgnoreCase(String texto);
}
