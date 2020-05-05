package rest.dto;

import entity.Business;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestBusiness {
    private Integer id;
    private String name;

    public RestBusiness() {
    }

    public RestBusiness(Business business) {
        this.id = business.getId();
        this.name = business.getName();
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
