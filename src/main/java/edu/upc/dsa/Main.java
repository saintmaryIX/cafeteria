//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.upc.dsa;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/dsaApp/";

    public static HttpServer startServer() {
        ResourceConfig rc = (new ResourceConfig()).packages(new String[]{"edu.upc.dsa.services"});
        rc.register(ApiListingResource.class);
        rc.register(SwaggerSerializers.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/dsaApp");
        beanConfig.setContact("support@example.com");
        beanConfig.setDescription("REST API for Products Manager");
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
        beanConfig.setResourcePackage("edu.upc.dsa.services");
        beanConfig.setTermsOfServiceUrl("http://www.example.com/resources/eula");
        beanConfig.setTitle("REST API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        StaticHttpHandler staticHttpHandler = new StaticHttpHandler(new String[]{"./public/"});
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, new String[]{"/"});
        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}
