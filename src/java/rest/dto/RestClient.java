package rest.dto;

import entity.Client;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestClient {
    private Integer id;
    private String name;

    public RestClient() {
    }
    
    public RestClient(Client client) {
        id = client.getId();
        name = client.getName();
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
