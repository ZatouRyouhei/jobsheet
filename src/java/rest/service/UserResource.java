package rest.service;

import db.JobSheetDb;
import db.UserDb;
import entity.User;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import rest.dto.RestUser;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/user")
public class UserResource {
    
    @Inject
    UserDb userdb;
    
    @Inject
    JobSheetDb jobSheetDb;
    
    @GET
    @Path("/login/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestUser getUser(@PathParam("id") String id) {
        User searchUser = (User)userdb.search(id);
        if (searchUser != null) {
            RestUser restUser = new RestUser(searchUser);
            return restUser;
        } else {
            throw new NotFoundException();
        }
    }
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestUser> getList() {
        List<User> userList = userdb.getAll();
        List<RestUser> restUserList = userList
                                        .stream()
                                        .map(user -> new RestUser(user.getId(), user.getName()))
                                        .collect(Collectors.toList());
        restUserList.sort(Comparator.comparing(RestUser::getId));
                                        
        return restUserList;
    }
    
    @POST
    @Path("/regist")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registUser(RestUser restUser) {
        User user = userdb.search(restUser.getId());
        if (user == null) {
            // 新規登録
            User registUser = new User(restUser.getId(), restUser.getPassword(), restUser.getName());
            userdb.add(registUser);
        } else {
            // 更新
            if (StringUtils.isNotEmpty(restUser.getPassword())) {
                // パスワードが入力されている場合はセットする。
                user.setPassword(restUser.getPassword());
            }
            user.setName(restUser.getName());
            userdb.update(user);
        }
    }
    
    /**
     * ユーザ削除
     * @param id
     * @return 0:正常終了、1:使用されているため削除不可
     */
    @DELETE
    @Path("/delete/{id}")
    public Integer deleteUser(@PathParam("id") String id) {
        Integer result;
        User user = userdb.search(id);
        if (user != null) {
            // 業務日誌で使用されているか確認
            if (jobSheetDb.checkUser(id)) {
                // 使用されている場合は削除しない。
                result = 1;
            } else {
                // 使用されていない場合は削除する。
                userdb.delete(user);
                result = 0;
            }
        } else {
            result = 0;
        }
        
        return result;
    }
    
}
