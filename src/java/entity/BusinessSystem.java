package entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * システム
 * @author ryouhei
 */
@NamedQueries({
    @NamedQuery (
            name = BusinessSystem.BUSINESSSYSTEM_BUSINESSCHECK,
            query = "  SELECT bs"
                    + "  FROM BusinessSystem bs"
                    + " WHERE bs.business.id = :businessId"
    )
})
@Entity
@Table(name="t_business_system")
@Cacheable(false)
public class BusinessSystem implements Serializable {
    public static final String BUSINESSSYSTEM_BUSINESSCHECK = "BUSINESSSYSTEM_BUSINESSCHECK";
    
    public static final int SIZE_NAME = 20;
    
    @Id
    private Integer id;
    @Column(length=SIZE_NAME)
    private String name;
    private Business business;

    public BusinessSystem() {
    }

    public BusinessSystem(Integer id, String name, Business business) {
        this.id = id;
        this.name = name;
        this.business = business;
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
