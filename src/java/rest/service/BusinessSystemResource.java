package rest.service;

import db.BusinessDb;
import db.BusinessSystemDb;
import db.JobSheetDb;
import entity.Business;
import entity.BusinessSystem;
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
import rest.dto.RestBusinessSystem;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/system")
public class BusinessSystemResource {
    @Inject
    BusinessSystemDb businessSystemDb;
    @Inject
    BusinessDb businessDb;
    @Inject
    JobSheetDb jobSheetDb;
    
    @GET
    @Path("/getList/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestBusinessSystem> getList(@PathParam("id") Integer businessId) {
        List<BusinessSystem> systemList = businessSystemDb.getAll();
        List<RestBusinessSystem> restSystemList = systemList
                                                    .stream()
                                                    .filter(sys -> sys.getBusiness().getId().equals(businessId))
                                                    .map(sys -> new RestBusinessSystem(sys))
                                                    .collect(Collectors.toList());
        restSystemList.sort(Comparator.comparing(RestBusinessSystem::getId));
        return restSystemList;
    }
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestBusinessSystem> getListAll() {
        List<BusinessSystem> systemList = businessSystemDb.getAll();
        List<RestBusinessSystem> restSystemList = systemList
                                                    .stream()
                                                    .map(system -> new RestBusinessSystem(system))
                                                    .collect(Collectors.toList());
        restSystemList.sort(Comparator.comparing(RestBusinessSystem::getId));
        return restSystemList;
    }
    
    @POST
    @Path("/regist")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registSystem(RestBusinessSystem restBusinessSystem) {
        BusinessSystem businessSystem = businessSystemDb.search(restBusinessSystem.getId());
        if (businessSystem == null) {
            // 新規登録
            Business business = businessDb.search(restBusinessSystem.getBusiness().getId());
            BusinessSystem targetBusinessSystem = new BusinessSystem(restBusinessSystem.getId(), restBusinessSystem.getName(), business);
            businessSystemDb.add(targetBusinessSystem);
        } else {
            // 更新
            Business business = businessDb.search(restBusinessSystem.getBusiness().getId());
            businessSystem.setName(restBusinessSystem.getName());
            businessSystem.setBusiness(business);
            businessSystemDb.update(businessSystem);
        }
    }
    
    /**
     * システム削除
     * @param id
     * @return 0:正常終了、1:使用されているため削除不可
     */
    @DELETE
    @Path("/delete/{id}")
    public Integer deleteSystem(@PathParam("id") Integer id) {
        Integer result;
        BusinessSystem target = businessSystemDb.search(id);
        if (target != null) {
            if (jobSheetDb.checkSystem(id)) {
                // 使用されているので削除不可
                result = 1;
            } else {
                // 使用されているので削除実施
                businessSystemDb.delete(target);
                result = 0;
            }
        } else {
            result = 0;
        }
        return result;
    }
}
