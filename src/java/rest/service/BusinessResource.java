package rest.service;

import db.BusinessDb;
import db.BusinessSystemDb;
import entity.Business;
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
import rest.dto.RestBusiness;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/business")
public class BusinessResource {
    @Inject
    BusinessDb businessDb;
    
    @Inject
    BusinessSystemDb businessSystemDb;
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestBusiness> getList() {
        List<Business> businessList = businessDb.getAll();
        List<RestBusiness> restBusinessList = businessList
                                                .stream()
                                                .map(business -> new RestBusiness(business))
                                                .collect(Collectors.toList());
        restBusinessList.sort(Comparator.comparing(RestBusiness::getId));
        return restBusinessList;
    }
    
    @POST
    @Path("/regist")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registBusiness(RestBusiness restBusiness) {
        Business business = businessDb.search(restBusiness.getId());
        if (business == null) {
            // 新規登録
            businessDb.add(new Business(restBusiness.getId(), restBusiness.getName()));
        } else {
            // 更新
            business.setName(restBusiness.getName());
            businessDb.update(business);
        }
    }
    
    /**
     * 業務削除
     * @param id
     * @return 0:正常終了、1:使用されているため削除不可
     */
    @DELETE
    @Path("/delete/{id}")
    public Integer deleteBusiness(@PathParam("id") Integer id) {
        Integer result;
        Business target = businessDb.search(id);
        if (target != null) {
            // システムに紐づいているか確認
            if (businessSystemDb.checkBusiness(id)) {
                // 使用されている場合は削除しない。
                result = 1;
            } else {
                // 使用されていない場合は削除する。
                businessDb.delete(target);
                result = 0;
            }
        } else {
            result = 0;
        }
        
        return result;
    }
}
