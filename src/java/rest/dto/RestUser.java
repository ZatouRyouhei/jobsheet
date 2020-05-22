package rest.dto;

import entity.User;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestUser {
    private String id;
    private String password;
    private String name;
    private Integer seqNo;

    public RestUser() {
    }

    public RestUser(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.seqNo = user.getSeqNo();
    }
    
    public RestUser(String id, String name, Integer seqNo) {
        this.id = id;
        this.name = name;
        this.seqNo = seqNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
}
