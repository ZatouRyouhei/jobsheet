package rest.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestUserList {
    private List<String> userList;

    public RestUserList() {
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
    
}
