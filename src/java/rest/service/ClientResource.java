package rest.service;

import db.ClientDb;
import db.JobSheetDb;
import entity.Client;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import rest.dto.RestClient;
import rest.filter.Authenticate;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Authenticate
@Path("/client")
public class ClientResource {
    @Inject
    ClientDb clientDb;
    @Inject
    JobSheetDb jobSheetDb;
    
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
    
    @POST
    @Path("/regist")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registClient(RestClient restClient) {
        Client client = clientDb.search(restClient.getId());
        if (client == null) {
            // 新規登録
            clientDb.add(new Client(restClient.getId(), restClient.getName()));
        } else {
            // 更新
            client.setName(restClient.getName());
            clientDb.update(client);
        }
    }
    
    /**
     * 業務削除
     * @param id
     * @return 0:正常終了、1:使用されているため削除不可
     */
    @DELETE
    @Path("/delete/{id}")
    public Integer deleteClient(@PathParam("id") Integer id) {
        Integer result;
        Client target = clientDb.search(id);
        if (target != null) {
            // 業務日誌に紐づいているか確認
            if (jobSheetDb.checkClient(id)) {
                // 使用されている場合は削除しない。
                result = 1;
            } else {
                // 使用されていない場合は削除する。
                clientDb.delete(target);
                result = 0;
            }
        } else {
            result = 0;
        }
        
        return result;
    }
}
