package org.generation.blogGScabuzzi.servicos;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.blogGScabuzzi.modelos.Usuario;
import org.generation.blogGScabuzzi.modelos.dtos.LoginDTO;
import org.generation.blogGScabuzzi.modelos.dtos.UsuarioLoginDTO;
import org.generation.blogGScabuzzi.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	private static String encriptadorDeSenha(String senha) {		// Criptografa a senha para armazenar no banco
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();	// Instancia a biblioteca "BCryptPasswordEncoder"
		return encoder.encode(senha);								// Encripta a senha que será cadastrada no novo usuario

	}
	
	public Optional<Object> cadastrarUsuario(Usuario usuarioParaCadastrar) {
		return repository.findByEmail(usuarioParaCadastrar.getEmail()).map(usuarioExistente -> { // Vai buscar o email para verificar se já existe
			return Optional.empty();												// Caso existir retorna um Optional Vazio
		}).orElseGet(() -> {										// Caso nao existir
			usuarioParaCadastrar.setSenha(encriptadorDeSenha(usuarioParaCadastrar.getSenha()));	// Encripta a senha no metodo "encriptadorDeSenha"
			return Optional.ofNullable(repository.save(usuarioParaCadastrar));			// E salva o novo usuario
		});
	}
	
	public ResponseEntity<LoginDTO> pegarCredenciais(UsuarioLoginDTO usuarioParaAutenticar) { // Pega como parametro os atributos da classe UsuarioLoginDTO
		return repository.findByEmail(usuarioParaAutenticar.getEmail()).map(resp -> {	// Buscará no banco se o email existe para fazer Login
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();		// Instancia a biblioteca "BCryptPasswordEncoder"

			if (encoder.matches(usuarioParaAutenticar.getSenha(), resp.getSenha())) {		// Comparara a senha informada com a senha correspondente do email
																							
				LoginDTO objetoCredenciaisDTO = new LoginDTO();					// Instancia a classe "LoginDTO" com seus atributos

				objetoCredenciaisDTO.setToken(gerarToken(usuarioParaAutenticar.getEmail(), usuarioParaAutenticar.getSenha())); // Passará as informaçoes para gerar um token com estrutura "Basic" ou Base64
				objetoCredenciaisDTO.setIdUsuario(resp.getIdUsuario());				// Seta o Id do usuario que está logado
				objetoCredenciaisDTO.setNome(resp.getNome());						// Seta o nome do usuario que está logado
				objetoCredenciaisDTO.setEmail(resp.getEmail());						// Seta o email do usuario que está logado
				objetoCredenciaisDTO.setSenha(resp.getSenha());						// Seta a senha do usuario que está logado

				return ResponseEntity.status(201).body(objetoCredenciaisDTO); // Usuario Credenciado - Usuario foi criado e retorná uma resposta CREATED
			} else {																		// A senha informada nao for correta enviara uma resposta BAD_REQUEST
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha Incorreta!"); // Senha incorreta
			}
		}).orElseGet(() -> {													// Se o email nao existir entrará nessa condiçao Lambda
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email não existe!"); // Email não existe
		});

	}

	private String gerarToken(String email, String senha) {				// Quem chamar esse metodo deve passar email e senha para formar o token "BASIC"
		String estrutura = email + ":" + senha;						// Monta a estrutrua - email + ":" + senha - do token 
		byte[] estruturaBase64 = Base64.encodeBase64(estrutura.getBytes(Charset.forName("US-ASCII"))); // Codifica essa estrutura em Base64
		return "Basic " + new String(estruturaBase64);			// Retorna a estrutura contatenado com a Palavra "Basic " no inicio
	}


}
