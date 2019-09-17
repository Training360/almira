package crystalball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@SpringBootApplication
@Configuration
public class CrystalBallApplication implements WebMvcConfigurer
{

	public static void main(String[] args) {
		SpringApplication.run(CrystalBallApplication.class, args);
	}

	@Bean
    public LocaleResolver localeResolver() {
	    return new AcceptHeaderLocaleResolver();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
