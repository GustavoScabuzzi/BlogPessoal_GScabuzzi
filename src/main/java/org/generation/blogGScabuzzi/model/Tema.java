package org.generation.blogGScabuzzi.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

@Entity
@Table(name = "tema")
public class Tema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idTema;
	
	@NotBlank
	private String tema;
	
	@OneToMany(mappedBy = "temaRelacionado", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("temaRelacionado")
	private List<Postagem> postagens = new ArrayList<>();

	public long getIdTema() {
		return idTema;
	}

	public void setIdTema(long idTema) {
		this.idTema = idTema;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public List<Postagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}
	
}
