package org.generation.blogGScabuzzi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class BlogGScabuzziApplication {

	@GetMapping
	public ModelAndView swaggerUi() {						// Fará o redirecionamento para a pagina correta do swagger caso só insira o localHost:8080
		return new ModelAndView("redirect:/swagger-ui/");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BlogGScabuzziApplication.class, args);
	}

}
