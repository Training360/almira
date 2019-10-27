package locationsapp;

import locationsapp.controller.CreateLocationCommand;
import locationsapp.controller.UpdateLocationCommand;
import locationsapp.entities.Location;
import locationsapp.ws.AuthEndpoint;
import locationsapp.ws.FilesEndpoint;
import locationsapp.ws.LocationsEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.xml.ws.Endpoint;
import java.util.Locale;

@SpringBootApplication
@Configuration
public class LocationsAppApplication implements WebMvcConfigurer
{

	public static void main(String[] args) {
		SpringApplication.run(LocationsAppApplication.class, args);
	}

    @Autowired
    private Bus bus;

    @Autowired
    private Environment environment;

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Location.class, UpdateLocationCommand.class)
                .setConverter(new Converter<Location, UpdateLocationCommand>() {
                    @Override
                    public UpdateLocationCommand convert(MappingContext<Location, UpdateLocationCommand> context) {
                        var source = context.getSource();
                        var command = new UpdateLocationCommand();
                        command.setId(source.getId());
                        command.setName(source.getName());
                        command.setCoords(source.getLat() + "," + source.getLon());
                        command.setInterestingAt(source.getInterestingAt());
                        command.setTags(source.getTags().toString());
                        return command;
                    }
                });
        return modelMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.ENGLISH);
    }

    @Bean
    public Endpoint publishedLocationsEndpoint(LocationsEndpoint locationsEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, locationsEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/locations");
        endpoint.publish("/locations");
        return endpoint;
    }

    @Bean
    public Endpoint publishedFilesEndpoint(FilesEndpoint filesEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, filesEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/files");
        endpoint.publish("/files");
        return endpoint;
    }

    @Bean
    public Endpoint publishedAuthEndpoint(AuthEndpoint authEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, authEndpoint);
        endpoint.setPublishedEndpointUrl(environment.getProperty("publish.url.prefix") + "/services/auth");
        endpoint.publish("/auth");
        return endpoint;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }
}
