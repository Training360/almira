package tr360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class IstqbTermTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(IstqbTermTestApplication.class, args);
    }
}
