package com.nousinfo.mexsintr;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.common.base.Predicate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MexsIntegrationApplication {

	private static final String API_PREFIX = "/api.*";

	public static void main(String[] args) {
		SpringApplication.run(MexsIntegrationApplication.class, args);
	}

	@Bean
	public Docket swaggerApi() {

		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(paths())
				.build().forCodeGeneration(true);

	}

	@SuppressWarnings({ "unchecked" })
	private Predicate<String> paths() {
		return or(regex(API_PREFIX));
	}

	@Bean(name = "multipartResolver")
	public MultipartResolver commonsMultipartResolver() {
		return new CommonsMultipartResolver();
	}

}
