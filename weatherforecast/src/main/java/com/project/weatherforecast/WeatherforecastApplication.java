package com.project.weatherforecast;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration
@OpenAPIDefinition(info = @Info(title = "WeatherForecast API", version = "1.0", extensions = {
		@Extension(properties = @ExtensionProperty(name = "x-api-repo",
				value = "https://github.com/Anjana-MN/weather-app")),
		@Extension(properties = @ExtensionProperty(name = "x-owner", parseValue = true,
				value = "{\"name\":\"Anjana\"," + "\"email\" : \"anjana.mn@publicissapient.com\","
						+ "\"url\":\"https://github.com/Anjana-MN\"}")) },
		contact = @Contact(name = "Anjana", email = "anjana.mn@publicissapient.com",
				url = "https://github.com/Anjana-MN"), description = "WeatherForecast API"),
		servers = { @Server(description = "Dev Server", url = "https://api-dev.dev.dev-cglcloud.com/"),
				@Server(description = "Stg Server", url = "https://api-stage.stage.cglcloud.in"),
				@Server(description = "Prod Server", url = "https://api.cglcloud.com/")},
		security = { @SecurityRequirement(name = "OAuth2", scopes = "write: read"),
				@SecurityRequirement(name = "APIKEY", scopes = "write: read") })
public class WeatherforecastApplication {

	@Value("${timeout}")
	private int connectTimeout;

	public static void main(String[] args) {
		SpringApplication.run(WeatherforecastApplication.class, args);
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(Duration.ofMillis(connectTimeout)).build();
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/weather/**")
						.allowedOrigins("http://localhost:3000");
			}
		};
	}

}
