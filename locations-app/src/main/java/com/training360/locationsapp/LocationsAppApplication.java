package com.training360.locationsapp;

import com.training360.locationsapp.ws.LocationsEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@SpringBootApplication
@Configuration
public class LocationsAppApplication
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
}
