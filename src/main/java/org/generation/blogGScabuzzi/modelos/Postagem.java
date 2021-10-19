package org.generation.blogGScabuzzi.modelos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe espelho da tabela Postagem no banco blogGScabuzzi
 * 
 * @author Gustavo Scabuzzi
 * 
 *
 */
@Entity
@Table(name = "postagem")
public class Postagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPostagem;

	@NotBlank
	@Size(min = 5, max = 100)
	private String titulo;

	@NotBlank
	@Size(min = 10, max = 500)
	private String texto;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new java.sql.Date(System.currentTimeMillis());

	@ManyToOne
	@JoinColumn(name = "tema_id")
	@JsonIgnoreProperties({ "postagens" })
	@NotNull
	public Tema temaRelacionado;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({ "postagensUsuario" })
	@NotNull
	public Usuario usuarioRelacionado;

	public long getIdPostagem() {
		return idPostagem;
	}

	public void setIdPostagem(long idPostagem) {
		this.idPostagem = idPostagem;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Tema getTemaRelacionado() {
		return temaRelacionado;
	}

	public void setTemaRelacionado(Tema temaRelacionado) {
		this.temaRelacionado = temaRelacionado;
	}

	public Usuario getUsuarioRelacionado() {
		return usuarioRelacionado;
	}

	public void setUsuarioRelacionado(Usuario usuarioRelacionado) {
		this.usuarioRelacionado = usuarioRelacionado;
	}

	

}
