package rest.service;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import rest.filter.CORSFilter;

/**
 *
 * @author ryouhei
 */
@ApplicationPath("/webresources")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
     packages("rest").register(MultiPartFeature.class);
   }
}
