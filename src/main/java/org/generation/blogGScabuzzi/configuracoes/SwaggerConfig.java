package org.generation.blogGScabuzzi.configuracoes;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	/**
	 * Define o local onde estao os Endpoints e fornece o package dos
	 * controladores @RestController
	 * 
	 * @return Docket com api documentada
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.generation.blogGScabuzzi.controller")) // Será passado o
																										// local dos
																										// controladores
				.paths(PathSelectors.any()).build().apiInfo(metadata()).useDefaultResponseMessages(false);
	}

	/**
	 * Método responsavel por passar dados do projeto e receber dados do metodo
	 * contact()
	 * 
	 * @return ApiInfo com dados do projeto
	 */
	public static ApiInfo metadata() {					// ApiInfo é uma criaçao de ArrayList
		return new ApiInfoBuilder().title("API - Blog GScabuzzi").description("Projeto API Spring - Blog GScabuzzi")
				.version("1.0.0")

				.license("Apache License Version 2.0").licenseUrl("https://github.com/GustavoScabuzzi")
				.contact(contact()).build();
	}

	/**
	 * Metodo responsavel por passar dados de contato
	 * 
	 * @return informaçoes com dados do criador
	 */
	private static Contact contact() {
		return new Contact("Gustavo Scabuzzi", "https://github.com/GustavoScabuzzi", "gustavo-gsg@hotmail.com");
	}

	/**
	 * Método estatico reponsavel por passar Lista com ResponseBuilder informando
	 * status possiveis e seus significados para todos os endpoints
	 * 
	 * Incluir codigo abaixo no builder do Docket caso necessario utilizar.
	 * .globalResponses(HttpMethod.GET, responseMessage())
	 * .globalResponses(HttpMethod.POST, responseMessage())
	 * .globalResponses(HttpMethod.PUT, responseMessage())
	 * .globalResponses(HttpMethod.DELETE, responseMessage());
	 * 
	 * @return Lista de Status possiveis da API
	 */
	@SuppressWarnings("unused")							// Desabilita o estado de erro do metodo nao usado
	private static List<Response> responseMessage() {
		return new ArrayList<Response>() {				// Criar uma ArrayList de Reponse
			private static final long serialVersionUID = 1L;
			{
				add(new ResponseBuilder().code("200").description("Sucesso!").build());			// Informa possiveis respostas da API
				add(new ResponseBuilder().code("201").description("Criado!").build());
				add(new ResponseBuilder().code("400").description("Erro na requisição!").build());
				add(new ResponseBuilder().code("401").description("Não Autorizado!").build());
				add(new ResponseBuilder().code("403").description("Proibido!").build());
				add(new ResponseBuilder().code("404").description("Não Encontrado!").build());
				add(new ResponseBuilder().code("500").description("Erro!").build());
			}
		};
	}
}
