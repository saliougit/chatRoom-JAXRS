package config;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class JaxRsConfig extends ResourceConfig {
    public JaxRsConfig() {
        packages("resources"); // Scan du package resources pour trouver les endpoints REST
    }
}
