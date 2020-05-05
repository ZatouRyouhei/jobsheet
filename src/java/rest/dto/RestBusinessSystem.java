package rest.dto;

import entity.Business;
import entity.BusinessSystem;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestBusinessSystem {
    private Integer id;
    private String name;
    private Business business;

    public RestBusinessSystem() {
    }

    public RestBusinessSystem(BusinessSystem sys) {
        this.id = sys.getId();
        this.name = sys.getName();
        this.business = sys.getBusiness();
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

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
