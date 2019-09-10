package locationsapp;

import locationsapp.ws.LocationsEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.xml.ws.Endpoint;

@SpringBootApplication
@Configuration
public class LocationsAppApplication implements WebMvcConfigurer
{

	public static void main(String[] args) {
		SpringApplication.run(LocationsAppApplication.class, args);
	}

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint endpoint(LocationsEndpoint locationsEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, locationsEndpoint);
        endpoint.publish("/locations");
        return endpoint;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
