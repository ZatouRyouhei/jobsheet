package rest.service;

import db.ClientDb;
import entity.Client;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import rest.dto.RestClient;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/client")
public class ClientResource {
    @Inject
    ClientDb clientDb;
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestClient> getList() {
        List<Client> clientList = clientDb.getAll();
        List<RestClient> restClientList = clientList
                                            .stream()
                                            .map(client -> new RestClient(client))
                                            .collect(Collectors.toList());
        restClientList.sort(Comparator.comparing(RestClient::getId));
        return restClientList;
    }
}
